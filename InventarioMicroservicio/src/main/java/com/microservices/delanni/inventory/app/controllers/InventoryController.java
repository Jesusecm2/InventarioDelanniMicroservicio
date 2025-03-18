package com.microservices.delanni.inventory.app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.delanni.inventory.app.clients.AuditoriaClients;
import com.microservices.delanni.inventory.app.clients.FileClients;
import com.microservices.delanni.inventory.app.entity.Categoria;
import com.microservices.delanni.inventory.app.entity.Cliente;
import com.microservices.delanni.inventory.app.entity.ImagenProducto;
import com.microservices.delanni.inventory.app.entity.Producto;
import com.microservices.delanni.inventory.app.entity.Transaccion;
import com.microservices.delanni.inventory.app.entity.Transacciones;
import com.microservices.delanni.inventory.app.service.IInventarioService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.val;

@RestController
@RequestMapping("/inventario")
public class InventoryController {

	@Autowired
	IInventarioService servicioInventario;
	@Autowired
	FileClients clienteArchivo;
	@Autowired
	AuditoriaClients clienteAuditoria;

	@PostMapping("/categoria/guardar")
	private ResponseEntity<?> GuardarCategoria(@Valid @RequestBody Categoria categoria, BindingResult result,
			HttpServletRequest request) {
		Map<String, Object> response = new HashMap<>();
		if (result.hasErrors()) {
			return responseErrors(response, result);
		}
		boolean guardar_img = false;
		Categoria comparar = null;
		if (categoria.getId() != null) {
			comparar = servicioInventario.ObtenerCategoriaId(categoria);
		}

		// **************Validacion 1
		if (categoria.getImage() != null) {
			if (categoria.getId() == null) {
				guardar_img = true;
			} else {
				if (comparar.getImage() == null) {
					guardar_img = true;
				} else {
					if (!categoria.getImage().equals(comparar.getImage())) {
						guardar_img = true;
					}
				}
			}

			if (guardar_img) {
				try {
					String ruta = clienteArchivo.copy(categoria.getImage(), "ct");
					if (ruta != null) {
						categoria.setImage(ruta);
					} else {
						categoria.setImage(null);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					List<String> error = new ArrayList<>();
					error.add("No se pudo guardar la imagen");
					response.put("errors", error);
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
			}

		}
		Categoria cat = servicioInventario.GuardarCategoria(categoria);
		enviarsecurity(request, "Categoria ID:".concat(String.valueOf(cat.getId())));
		return new ResponseEntity<>(cat, HttpStatus.OK);
	}

	@GetMapping("/categoria/listado")
	private ResponseEntity<?> ObtenerCategorias(@RequestHeader Map<String, String> headers,
			HttpServletRequest request) {
		List<Categoria> list = servicioInventario.ListadoCategoria();
		enviarsecurity(request, "Listado Categorias");
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@PostMapping("/producto/guardar")
	private ResponseEntity<?> GuardarProducto(@Valid @RequestBody Producto producto, BindingResult result,
			@RequestParam String action, @RequestParam Double value, HttpServletRequest request) {
		Map<String, Object> response = new HashMap<>();
		if (result.hasErrors()) {
			return responseErrors(response, result);
		}
		if (producto.getId() != null) {
			Producto db = servicioInventario.BuscarProductoId(producto.getId());
		}

		Producto aux = new Producto();
		aux.setImagenes(producto.getImagenes());
		producto.setImagenes(null);
		Producto requested = servicioInventario.guardarProducto(producto, action, value);
		enviarsecurity(request, "guardar Producto:".concat(String.valueOf(requested.getId())));
		if (aux.getImagenes() != null) {
			aux.getImagenes().forEach((e) -> {
				try {
					if (e.getId() == null) {
						String ruta = clienteArchivo.copy(e.getImagen(), "prod");
						e.setProducto(requested);
						if (ruta != null) {
							e.setImagen(ruta);
							servicioInventario.GuardarImagen(e);
						} else {
							e.setImagen(null);
						}
					} else {
						ImagenProducto imagenReemplazo = servicioInventario.BuscarImagen(e.getId());
						if (imagenReemplazo != null && !imagenReemplazo.getImagen().equals(e.getImagen())) {
							try {
								String ruta = clienteArchivo.copy(e.getImagen(), "prod");
								imagenReemplazo.setImagen(ruta);
								servicioInventario.GuardarImagen(imagenReemplazo);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					e.setImagen(null);
				}
			});
		}

		Transaccion trans = new Transaccion();
		if (producto.getId() != null) {
			trans.setDescripcion("Actualización");
			trans.setUsuario(action);
		}

		return new ResponseEntity<>(requested, HttpStatus.OK);
	}

	@GetMapping("/producto/lista")
	private ResponseEntity<?> ObtenerProductos(HttpServletRequest request) {
		enviarsecurity(request, Producto.class.getName());
		return new ResponseEntity<>(servicioInventario.ListadoProducto(), HttpStatus.OK);
	}

	@GetMapping("/producto/catnombre")
	private ResponseEntity<?> ObtenerProductosNombrecat(HttpServletRequest request, @RequestParam Integer id,
			@RequestParam String nombre) {
		Categoria cat = new Categoria();
		cat.setId(id);
		enviarsecurity(request, "Buscar categoría y Nombre".concat(nombre));
		return new ResponseEntity<>(servicioInventario.buscarCategoriaYStartNombre(cat, nombre), HttpStatus.OK);
	}

	@PostMapping("/cliente/guardar")
	private ResponseEntity<?> guardarCliente(@Valid @RequestBody Cliente body, BindingResult result,
			HttpServletRequest request) {

		Map<String, Object> response = new HashMap<>();
		if (result.hasErrors()) {
			return responseErrors(response, result);
		}

		Cliente cl = servicioInventario.guardarCliente(body);
		enviarsecurity(request, "Crear cliente".concat(String.valueOf(cl.getId())));
		return new ResponseEntity<>(servicioInventario.guardarCliente(body), HttpStatus.OK);

	}

	@GetMapping("/cliente/cedula")
	private ResponseEntity<?> buscarClienteCedula(@RequestParam String cd, HttpServletRequest request) {
		enviarsecurity(request, "Cliente por Cedula: ".concat(cd));
		List<Cliente> listado = servicioInventario.BuscarCedulaCliente(cd);
		if (!listado.isEmpty()) {
			return new ResponseEntity<>(listado.get(0), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/cliente/todos")
	private ResponseEntity<?> listadoClientes(HttpServletRequest request) {
		enviarsecurity(request, "Consulta Listado Cliente");
		return new ResponseEntity<>(servicioInventario.listadoCliente(), HttpStatus.OK);
	}

	@GetMapping("/producto/nombre")
	private ResponseEntity<?> ObtenerProductos(@RequestParam String nombre, HttpServletRequest request) {
		enviarsecurity(request, "Obtener Productos nombre: ".concat(nombre));
		return new ResponseEntity<>(servicioInventario.BuscarProductoNombre(nombre), HttpStatus.OK);
	}

	@GetMapping("/producto/listacat")
	private ResponseEntity<?> ObtenerProductos(@RequestParam Integer id, HttpServletRequest request) {
		if (id != null) {
			Categoria param = new Categoria();
			param.setId(id);
			enviarsecurity(request, "Obtener Productos catID: ".concat(String.valueOf(id)));
			return new ResponseEntity<>(servicioInventario.ListadoProducto(param), HttpStatus.OK);
		} else {
			Map<String, Object> response = new HashMap<>();
			response.put("errors", "No se encontro la categoría");
			enviarsecurity(request, "Error Obtener Productos catID: ".concat(String.valueOf(id)));
			return new ResponseEntity<>(servicioInventario.ListadoProducto(), HttpStatus.OK);
		}
	}

	@GetMapping("/producto/codigo")
	private ResponseEntity<?> ObtenerProducto(@RequestParam String cod, HttpServletRequest request) {
		if (cod != null) {
			enviarsecurity(request, "Obtener Productos codigo: ".concat(cod));
			return new ResponseEntity<>(servicioInventario.BuscarProductoCodigo(cod), HttpStatus.OK);
		} else {
			Map<String, Object> response = new HashMap<>();
			response.put("error", "No se encontro la categoría");
			enviarsecurity(request, "Error Obtener Productos codigo: ".concat(cod));
			return new ResponseEntity<>(servicioInventario.ListadoProducto(), HttpStatus.OK);
		}
	}

	@PostMapping("/imagen/eliminar")
	private ResponseEntity<?> EliminarImagen(@RequestParam Long obj, HttpServletRequest request) {
		if (obj > 0) {
			ImagenProducto pr = servicioInventario.BuscarImagen(obj);
			if (pr != null) {

				ResponseEntity<?> borrar = clienteArchivo.deleteImage(pr.getImagen());
				if (borrar.getStatusCode() == HttpStatus.OK) {
					servicioInventario.EliminarImagen(pr);
					enviarsecurity(request, "Eliminar archivo: ".concat(String.valueOf(obj)));
					return new ResponseEntity<>("Completed", HttpStatus.OK);
				}
			}

		} else {
			enviarsecurity(request, "Error eliminar archivo: ".concat(String.valueOf(obj)));
			return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
		}
		return null;

	}

	private ResponseEntity<?> responseErrors(Map<String, Object> response, BindingResult param) {
		List<String> error = param.getFieldErrors().stream().map(err -> {
			System.out.println(err.getField() + "-" + err.getDefaultMessage());
			return "El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage());
		}).collect(Collectors.toList());
		response.put("errors", error);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
	}

	private void enviarsecurity(HttpServletRequest request, String obj) {
		Map<String, String> values = new HashMap<>();
		values.put("ip", request.getHeader("ip"));
		values.put("route", request.getHeader("path"));
		values.put("system", request.getHeader("system"));
		values.put("entity", obj);
		values.put("provider", request.getHeader("provider"));
		try {
			clienteAuditoria.guardarAudit(values);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
