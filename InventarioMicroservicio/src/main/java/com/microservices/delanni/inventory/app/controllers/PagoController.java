package com.microservices.delanni.inventory.app.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
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
import com.microservices.delanni.inventory.app.entity.Factura;
import com.microservices.delanni.inventory.app.entity.Proveedor;
import com.microservices.delanni.inventory.app.entity.TpIngreso;
import com.microservices.delanni.inventory.app.entity.Transacciones;
import com.microservices.delanni.inventory.app.entity.pagos.ComprobantePago;
import com.microservices.delanni.inventory.app.entity.pagos.Moneda;
import com.microservices.delanni.inventory.app.entity.pagos.Pago;
import com.microservices.delanni.inventory.app.entity.pagos.TipodePago;
import com.microservices.delanni.inventory.app.entity.pagos.ValorMoneda;
import com.microservices.delanni.inventory.app.service.IPagosService;
import com.microservices.delanni.inventory.app.service.TransaccionesService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;

@RestController
@RequestMapping("/pago")
public class PagoController {

	@Autowired
	private IPagosService servicePago;

	@Autowired
	private FileClients clienteArchivo;

	@Autowired
	private TransaccionesService transService;

	@Autowired
	private AuditoriaClients clienteAuditoria;

	@GetMapping("/tipospago")
	public ResponseEntity<?> obtenerTiposPagos() {
		List<TipodePago> pagos = servicePago.ObtenerTiposdePago();
		return new ResponseEntity<>(pagos, HttpStatus.OK);
	}

	@GetMapping("/moneda")
	public ResponseEntity<?> obtenerMonedas() {
		List<Moneda> monedas = servicePago.ObtenerMonedas();
		return new ResponseEntity<>(monedas, HttpStatus.OK);
	}

	@PostMapping("/valorMoneda/hoy")
	public ResponseEntity<?> obtenerValorMonedaHoy(@RequestBody Moneda mon) {
		List<ValorMoneda> moneda = null;
		Date actual = new Date(System.currentTimeMillis());
		String status = "A";
		if (mon != null) {
			moneda = servicePago.ObtenerValorMoneda(status, mon, actual);
		}
		if (moneda != null && !moneda.isEmpty()) {
			return new ResponseEntity<>(moneda.get(0), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

	}

	@PostMapping("/valorMoneda/fecha")
	public ResponseEntity<?> obtenerValorMoneda(@RequestBody Moneda mon, @RequestParam String date)
			throws ParseException {
		List<ValorMoneda> moneda = null;
		//DateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
		String formato = "dd-MM-yyyy";
		SimpleDateFormat format = new SimpleDateFormat(formato);
		Date find = format.parse(date);
		String status = "A";
		if (mon != null) {
			moneda = servicePago.ObtenerValorMoneda(status, mon, find);
		}
		if (moneda != null && !moneda.isEmpty()) {
			return new ResponseEntity<>(moneda.get(0), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

	}

	@PostMapping("/valorMoneda/guardar")
	public ResponseEntity<?> guardarValorMoneda(@RequestBody ValorMoneda param,@RequestParam(required = false)String date, HttpServletRequest request) throws ParseException {
		String value = "A";
		Date today = new Date(System.currentTimeMillis());
		if(date!=null) {
			today = new SimpleDateFormat("dd-MM-yyyy").parse(date);
		}
		if (param.getMoneda() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<ValorMoneda> list = servicePago.ObtenerValorMoneda(value, param.getMoneda(), today);
		if (list != null && !list.isEmpty()) {
			ValorMoneda chg = list.get(0);
			chg.setStatus("H");
			chg.setMoneda(param.getMoneda());
			servicePago.guardarValorMoneda(chg);
		}
		param.setStatus("A");
		enviarsecurity(request, "guardar tasa: ".concat(param.getMoneda().getMoneda()));
		servicePago.guardarValorMoneda(param);

		return new ResponseEntity<>(servicePago.guardarValorMoneda(param), HttpStatus.OK);
	}

	@PostMapping("/guardar")
	public ResponseEntity<?> guardarPago(@RequestBody Pago pago, HttpServletRequest request) {
		Pago response = servicePago.guardarPago(pago);
		enviarsecurity(request, "Registrar Pago:".concat(String.valueOf(response.getId())));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/guardar/pagofactura")
	public ResponseEntity<?> guardarPagoFactura(@RequestBody Map<String, String> request, HttpServletRequest servlet)
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Pago pago = mapper.readValue(request.get("pago"), Pago.class);
		Factura factura = mapper.readValue(request.get("factura"), Factura.class);
		pago = servicePago.guardarPago(pago);
		transService.CrearTFactura(factura, pago);
		enviarsecurity(servlet, "Registrar Pago Factura:".concat(String.valueOf(pago.getId())));
		return new ResponseEntity<>(pago, HttpStatus.OK);
	}

	@PostMapping("/guardar/pagoingreso")
	public ResponseEntity<?> guardarIngresoPago(@RequestBody Map<String, String> request, HttpServletRequest servlet)
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Pago pago = mapper.readValue(request.get("pago"), Pago.class);
		TpIngreso ingreso = mapper.readValue(request.get("ingreso"), TpIngreso.class);
		if (pago.getComprobante() != null && pago.getComprobante().getId() == null) {
			try {
				String rute = clienteArchivo.copy(pago.getComprobante().getImagen(), "Pago");
				pago.getComprobante().setImagen(rute);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				pago.setComprobante(null);
				e.printStackTrace();
			}
			if (pago.getComprobante() != null) {
				ComprobantePago comp = servicePago.guardarComprobante(pago.getComprobante());
				pago.setComprobante(comp);
			}

		}
		pago = servicePago.guardarPago(pago);
		enviarsecurity(servlet, "Registrar Pago Ingreso:".concat(String.valueOf(pago.getId())));
		transService.CrearTEgreso(ingreso, pago);
		return new ResponseEntity<>(pago, HttpStatus.OK);
	}

	// ***************************************************************************************CONSULTAS
	@GetMapping("/obtener/tingreso")
	public ResponseEntity<?> obtenerTipoIngreso(@RequestParam String tipo) {
		List<TpIngreso> tpIn = servicePago.obtenerIngreso(tipo);
		return new ResponseEntity<>(tpIn, HttpStatus.OK);
	}

	@GetMapping("/obtener/egresos/dia") // *******************************************Del dia
	public ResponseEntity<?> obtenerEgresos(HttpServletRequest servlet) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
		calendar1.add(Calendar.DATE, 1);
		Date start = calendar.getTime();
		Date end = calendar1.getTime();
		List<TpIngreso> listado = servicePago.obtenerIngreso("E");
		List<Transacciones> listado1 = transService.obtenerEgresos(start, end, listado);
		enviarsecurity(servlet, "Consulta Egresos:".concat(start.toString() + "/" + end.toString()));
		return new ResponseEntity<>(listado1, HttpStatus.OK);
	}

	@GetMapping("/obtener/ingresos/dia") // ****************************************Del dia
	public ResponseEntity<?> obtenerIngresos(HttpServletRequest servlet) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
		calendar1.add(Calendar.DATE, 1);
		Date start = calendar.getTime();
		Date end = calendar1.getTime();
		List<TpIngreso> listado = servicePago.obtenerIngreso("I");
		List<Transacciones> listado1 = transService.obtenerEgresos(start, end, listado);
		enviarsecurity(servlet, "Consulta Ingresos:".concat(start.toString() + "/" + end.toString()));
		return new ResponseEntity<>(listado1, HttpStatus.OK);
	}

	// **********************************************Pagos por factura
	@GetMapping("/obtener/pagos/factura")
	public ResponseEntity<?> Obtenerpagosfactura(@RequestParam long id_factura) {

		List<Transacciones> transacciones = transService.obtenerTransacciones(new Factura(id_factura));
		if (transacciones != null) {
			return new ResponseEntity<>(transacciones, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	// ******************************************Pagos clientes del día
	@GetMapping("/obtener/ventas/dia")
	public ResponseEntity<?> obtenerVentas(HttpServletRequest servlet) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
		calendar1.add(Calendar.DATE, 1);
		Date start = calendar.getTime();
		Date end = calendar1.getTime();
		List<Transacciones> listado1 = transService.obtenerVentas(start, end);
		enviarsecurity(servlet, "Consulta Ventas:".concat(start.toString() + "/" + end.toString()));
		return new ResponseEntity<>(listado1, HttpStatus.OK);
	}

	// *****************************************************
	@GetMapping("/obtener/pagos/tpPago")
	private ResponseEntity<?> ObtenerPagosTppago(@RequestParam long id, @RequestParam(required = false) String dt1,
			@RequestParam(required = false) String dt2, HttpServletRequest servlet) {
		enviarsecurity(servlet, "Consulta Pagos");
		TpIngreso tpIngreso = new TpIngreso();
		List<Transacciones> listado = null;
		tpIngreso.setId(id);
		if (dt1 == null || dt2 == null) {
			listado = transService.obtenerTransacciones(tpIngreso);
		} else {
			DateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
			try {
				Date f1 = fmt.parse(dt1);
				Date f2 = fmt.parse(dt2);
				listado = transService.obtenerTransacciones(f1, f2, tpIngreso);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

		}

		if (listado != null) {
			return new ResponseEntity<>(listado, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/obtener/pagos/tpPago/date")
	private ResponseEntity<?> ObtenerPagosTppago(@RequestParam(required = false) String dt1,
			@RequestParam(required = false) String dt2, HttpServletRequest servlet) {
		enviarsecurity(servlet, "Consulta Pagos");
		List<Transacciones> listado = null;
		if (dt1 != null && dt2 != null) {
			DateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
			try {
				Date f1 = fmt.parse(dt1);
				Date f2 = fmt.parse(dt2);
				listado = transService.obtenerIngresosEgresos(f1, f2);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			if (listado != null) {
				return new ResponseEntity<>(listado, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

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
