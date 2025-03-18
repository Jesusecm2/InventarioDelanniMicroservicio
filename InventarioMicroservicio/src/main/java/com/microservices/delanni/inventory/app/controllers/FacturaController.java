package com.microservices.delanni.inventory.app.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.delanni.inventory.app.clients.AuditoriaClients;
import com.microservices.delanni.inventory.app.clients.FileClients;
import com.microservices.delanni.inventory.app.entity.Cliente;
import com.microservices.delanni.inventory.app.entity.Cuenta;
import com.microservices.delanni.inventory.app.entity.Factura;
import com.microservices.delanni.inventory.app.entity.LineaFactura;
import com.microservices.delanni.inventory.app.entity.Producto;
import com.microservices.delanni.inventory.app.entity.Proveedor;
import com.microservices.delanni.inventory.app.entity.Transacciones;
import com.microservices.delanni.inventory.app.entity.pagos.Pago;
import com.microservices.delanni.inventory.app.entity.pagos.ValorMoneda;
import com.microservices.delanni.inventory.app.service.IFacturaService;
import com.microservices.delanni.inventory.app.service.IInventarioService;
import com.microservices.delanni.inventory.app.service.IPagosService;
import com.microservices.delanni.inventory.app.service.TransaccionesService;
import com.netflix.discovery.converters.Auto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/inventario")
public class FacturaController {

	@Autowired
	IInventarioService servicioInventario;

	@Autowired
	IFacturaService facturaServicio;

	@Autowired
	FileClients clienteArchivo;

	@Autowired
	private AuditoriaClients clienteAuditoria;

	@Autowired
	IPagosService ServicioPago;

	@Autowired
	private TransaccionesService serviceTrans;

	@Autowired
	private PagoController pagoController;

	@PostMapping("/factura/proveedor/guardar")
	public ResponseEntity<?> guardarProveedor(@Valid @RequestBody Proveedor proveedor,BindingResult result, HttpServletRequest request) {
		Map<String, Object> response = new HashMap<>();
		if (result.hasErrors()) {
			return responseErrors(response, result);
		}
		Proveedor guardado = facturaServicio.guardarProveedor(proveedor);
		enviarsecurity(request, "Guardar proveedor".concat(String.valueOf(guardado.getId())));
		return new ResponseEntity<>(facturaServicio.guardarProveedor(proveedor),HttpStatus.OK);
	}

	@PostMapping("/factura/cuenta/guardar")
	public Cuenta guardarCuenta(@RequestBody Cuenta cuenta) {
		return facturaServicio.guardarCuenta(cuenta);
	}

	@GetMapping("/factura/proveedor/listado")
	public ResponseEntity<?> listadoProveedor(HttpServletRequest request) {
		List<Proveedor> listado = facturaServicio.listadoProveedor();
		if (listado == null) {

		} else {
			enviarsecurity(request, "Listado de proveedor");
			return new ResponseEntity<>(listado, HttpStatus.OK);
		}
		return null;
	}

	@GetMapping("/factura/cliente")
	public ResponseEntity<?> obtenerVentasClientes(HttpServletRequest request,
			@RequestParam(required = false) Integer cliente, @RequestParam(required = false) String sts) {
		enviarsecurity(request, "Busqueda facturas clientes");
		if (cliente != null) {
			Cliente cl = new Cliente(cliente);
			if (sts == null)
				return new ResponseEntity<>(facturaServicio.listadoVentas(cl), HttpStatus.OK);
			else
				;
			return new ResponseEntity<>(facturaServicio.listadoVentas(cl, sts), HttpStatus.OK);
		}
		return new ResponseEntity<>(facturaServicio.listadoVentas(), HttpStatus.OK);
	}

	@GetMapping("/factura/listado")
	public ResponseEntity<?> listadoFactura(HttpServletRequest request) {
		List<Factura> listado = facturaServicio.listadoFacturas();
		if (listado == null) {

		} else {
			enviarsecurity(request, "Listado de Facturas");
			return new ResponseEntity<>(listado, HttpStatus.OK);
		}
		return null;
	}

	@GetMapping("/factura/listado/nonulo")
	public ResponseEntity<?> listadoFacturaNonull(HttpServletRequest request) {
		List<Factura> listado = facturaServicio.listadoFacturasNotNull();
		if (listado == null) {

		} else {
			enviarsecurity(request, "Listado factura NotNull");
			return new ResponseEntity<>(listado, HttpStatus.OK);
		}
		return null;
	}

	@PostMapping("/factura/guardar")
	public ResponseEntity<?> guardarFactura(@RequestBody Map<String, String> valores, HttpServletRequest request)
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper maper = new ObjectMapper();
		Factura factura = maper.readValue(valores.get("factura"), Factura.class);
		// ******************************Proceso Factura
		Double montoP = 0.0;
		factura.setSaldo_pagado(montoP);
		Factura saving = facturaServicio.guardarFactura(factura);
		enviarsecurity(request, "Guardar factura: ".concat(String.valueOf(saving.getId())));

		// ********************************Evaluar Pagos
		if (valores.get("pagos") != null && !valores.get("pagos").isEmpty()) {
			List<Pago> pagos = (Arrays.asList(maper.readValue(valores.get("pagos"), Pago[].class)));
			if (pagos != null && !pagos.isEmpty()) {
				for (Pago p : pagos) {
					if (p.getComprobante() != null && p.getComprobante().getId() == null) {
						try {
							String rute = clienteArchivo.copy(p.getComprobante().getImagen(), "Pago");
							p.getComprobante().setImagen(rute);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							p.setComprobante(null);
							e.printStackTrace();
						}
					}
					montoP += p.getMonto();
					boolean mod = false;
					if(p.getId()!=null && p.getId()>0) {
						mod=true;
					}
					p = ServicioPago.guardarPago(p);
					if(!mod)serviceTrans.CrearTFactura(factura, p);
				}
				saving.setSaldo_pagado(montoP);
				
				facturaServicio.guardarFactura(saving);
				saving.getLineas().forEach( (e) ->{
					Producto auxP = servicioInventario.BuscarProductoId(e.getId_producto().getId());
					
					if(saving.getIdProveedor()!=null) {
						servicioInventario.guardarProducto(auxP, "actualizar", e.getCantidad());
					}else {
						servicioInventario.guardarProducto(auxP, "actualizar", e.getCantidad()*-1);
					}
					
				});
				
			}
		}

		return new ResponseEntity<>(saving, HttpStatus.OK);

	}

	private List<Pago> extracted(Map<String, Object> valores) {
		return (List<Pago>) valores.get("pagos");
	}

	@GetMapping("/factura/proveedor/buscar")
	public ResponseEntity<?> buscarFProveedor(@RequestParam Integer id, HttpServletRequest request) {
		Proveedor prov = new Proveedor();
		prov.setId(id);
		List<Factura> listado = facturaServicio.listadoFacturas(prov);
		if (listado == null) {

		} else {
			enviarsecurity(request, "Buscar proveedor:".concat(String.valueOf(id)));
			return new ResponseEntity<>(listado, HttpStatus.OK);
		}
		return null;
	}

	@GetMapping("/factura/buscar/estatusProveedor")
	public ResponseEntity<?> buscarProveedorStatus(@RequestParam Integer id, @RequestParam String status,
			HttpServletRequest request) {
		Proveedor prov = new Proveedor();
		prov.setId(id);
		List<Factura> listado = facturaServicio.listadoFacturas(prov, status);
		if (listado == null) {

		} else {
			enviarsecurity(request, "Buscar proveedor:".concat(String.valueOf(id)));
			return new ResponseEntity<>(listado, HttpStatus.OK);
		}
		return null;
	}

	@GetMapping("/factura/buscar/estatus")
	public ResponseEntity<?> buscarFacturaStatus(@RequestParam String status, HttpServletRequest request) {
		List<Factura> listado = facturaServicio.listadoFacturas(status);
		if (listado == null) {

		} else {
			enviarsecurity(request, "Buscar Facturas:".concat(String.valueOf(status)));
			return new ResponseEntity<>(listado, HttpStatus.OK);
		}
		return null;
	}

	@GetMapping("/factura/buscar")
	public ResponseEntity<?> listadoFacturaFecha(@RequestParam String date, HttpServletRequest request)
			throws ParseException {
		String formato = "dd-MM-yyyy";
		SimpleDateFormat format = new SimpleDateFormat(formato);
		Date find = format.parse(date);
		Calendar start = Calendar.getInstance();
		start.setTime(find);
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		Calendar end = (Calendar) start.clone();
		end.add(Calendar.DATE, 1);

		Date busq1 = start.getTime();
		Date busq2 = end.getTime();
		List<Factura> listado = facturaServicio.listadoFacturas(busq1, busq2);
		if (listado == null) {

		} else {
			enviarsecurity(request, "Buscar Facturas:".concat(String.valueOf(date.toString())));
			return new ResponseEntity<>(listado, HttpStatus.OK);
		}
		return null;
	}

	@GetMapping("/factura/buscar/cliente")
	public ResponseEntity<?> listadoFacturaCliente(@RequestParam String date,
			@RequestParam(required = false) Integer id, HttpServletRequest request) throws ParseException {
		String formato = "dd-MM-yyyy";
		SimpleDateFormat format = new SimpleDateFormat(formato);
		Date find = format.parse(date);
		Calendar start = Calendar.getInstance();
		start.setTime(find);
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		Calendar end = (Calendar) start.clone();
		end.add(Calendar.DATE, 1);

		Date busq1 = start.getTime();
		Date busq2 = end.getTime();
		Cliente cliente = new Cliente();
		cliente.setId(id);
		List<Factura> listado = null;
		if (id != null) {
			listado = facturaServicio.listadoVentas(cliente, busq1, busq2);
		} else {
			listado = facturaServicio.listadoVentas(busq1, busq2);
		}

		if (listado == null) {

		} else {
			enviarsecurity(request, "Buscar Facturas:".concat(String.valueOf(date.toString())));
			return new ResponseEntity<>(listado, HttpStatus.OK);
		}
		return null;
	}
	
	
	private ResponseEntity<?> responseErrors(Map<String, Object> response, BindingResult param) {
		List<String> error = param.getFieldErrors().stream().map(err -> {
			return err.getDefaultMessage();
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
