package com.microservices.delanni.inventory.app.controllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.net.HttpHeaders;
import com.microservices.delanni.inventory.app.clients.FileClients;
import com.microservices.delanni.inventory.app.entity.Categoria;
import com.microservices.delanni.inventory.app.entity.Factura;
import com.microservices.delanni.inventory.app.entity.Producto;
import com.microservices.delanni.inventory.app.entity.Transacciones;
import com.microservices.delanni.inventory.app.service.IFacturaService;
import com.microservices.delanni.inventory.app.service.IInventarioService;
import com.microservices.delanni.inventory.app.service.IReporteService;
import com.microservices.delanni.inventory.app.service.TransaccionesService;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/reporte")
public class ReporteController {

	
	
	@Autowired
	private FileClients clienteArchivo;
	
	@Autowired
	private IInventarioService serviceInventario;
	
	@Autowired
	private IFacturaService serviceFactura;
	
	@Autowired
	private TransaccionesService serviceTrans;
	
	
	@Autowired
	private IReporteService reportService;
	
	
	@GetMapping("/producto/categoria")
	public ResponseEntity<?> obtenerReporteCategoria(@RequestParam Integer id_Categoria){
		Categoria ct = new Categoria();
		ct.setId(id_Categoria);
		Categoria busq = serviceInventario.ObtenerCategoriaId(ct);
		List<Producto> listado = serviceInventario.ListadoProducto(busq);
		
		try {
			byte[] file = reportService.ReporteCategoria(listado,ct);
			if(file!=null) {
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
						.body(file);
			}
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	
	@GetMapping("/producto/factura")
	public ResponseEntity<?> obtenerReporteFactura(@RequestParam Long id_factura){
		Factura listado = serviceFactura.facturaById(id_factura);
		
		try {
			byte[] file = reportService.ReporteFactura(listado);
			if(file!=null) {
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
						.body(file);
			}
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	@GetMapping("/transacciones/reporte")
	public ResponseEntity<?> obtenerReporteTrans(@RequestParam String start,@RequestParam String end) throws ParseException{
		SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
		Date st = fmt.parse(start);
		Date ed = fmt.parse(end);
		List<Transacciones> listado = serviceTrans.obtenerTransacciones(st, ed);
		String titulo = start.concat(" a ").concat(end);
		try {
			byte[] file = reportService.ReporteTransacciones(listado,titulo);
			if(file!=null) {
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
						.body(file);
			}
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
}
