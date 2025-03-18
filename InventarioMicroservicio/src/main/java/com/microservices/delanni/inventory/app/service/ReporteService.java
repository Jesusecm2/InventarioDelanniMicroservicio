package com.microservices.delanni.inventory.app.service;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.net.HttpHeaders;
import com.microservices.delanni.inventory.app.clients.FileClients;
import com.microservices.delanni.inventory.app.entity.Categoria;
import com.microservices.delanni.inventory.app.entity.Factura;
import com.microservices.delanni.inventory.app.entity.Producto;
import com.microservices.delanni.inventory.app.entity.Transacciones;
import com.microservices.delanni.inventory.app.entity.reporte.LineaFacturaR;
import com.microservices.delanni.inventory.app.entity.reporte.TTRansReporte;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

@Service
public class ReporteService implements IReporteService {

	@Autowired
	private FileClients clienteArchivo;

	@Override
	public byte[] ReporteCategoria(List<Producto> productos, Categoria cat) throws JRException {
		// TODO Auto-generated method stub
		/*
		 * Controlador debe ser el siguiten byte[]arreglo = generar Reporte Headers
		 * return ResponseEntity.ok() .header(HttpHeaders.CONTENT_DISPOSITION,
		 * "attachment; filename=\"" + path + "\"") .body(mapeo);
		 * 
		 */
		JRBeanArrayDataSource ds = new JRBeanArrayDataSource(productos.toArray());
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("ds", ds);
		// parameters.put("total_v", "2.0");
		// parameters.put("logo", img);
		parameters.put("cat_title", cat.getNombre());
		byte[] reporte = clienteArchivo.obtenerImagenStream("producto.jasper");
		byte[] logo = clienteArchivo.obtenerImagenStream("LOGO.png");
		try {
			Image img = ImageIO.read(new ByteArrayInputStream(logo));
			parameters.put("img_logo", img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream stream = new ByteArrayInputStream(reporte);
		JasperPrint printer = JasperFillManager.fillReport(stream, parameters);

		return JasperExportManager.exportReportToPdf(printer);
	}

	@Override
	public byte[] ReporteFactura(Factura factura) throws JRException {
		// TODO Auto-generated method stub
		List<LineaFacturaR> descripcion = factura.getLineas().stream().map(e -> new LineaFacturaR(e))
				.collect(Collectors.toList());
		descripcion.forEach((e -> System.out.println(e)));
		JRBeanArrayDataSource ds = new JRBeanArrayDataSource(descripcion.toArray());
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("ds", ds);
		if (factura.getIdCliente() != null) {
			parameters.put("tipo_cl", "Cliente:");
			parameters.put("nombre_cl", factura.getIdCliente().getNombre());
			parameters.put("rif_cl", factura.getIdCliente().getRif());
			parameters.put("cel_report", factura.getIdCliente().getNumero());
		} else {
			parameters.put("tipo_cl", "Proveedor:");
			parameters.put("nombre_cl", factura.getIdProveedor().getNombre());
			parameters.put("rif_cl", factura.getIdProveedor().getRif());
			parameters.put("cel_report", factura.getIdProveedor().getNumero());
		}
		parameters.put("saldo", factura.getSub_total());
		if (factura.getIVA() != null) {
			parameters.put("iva", String.valueOf(factura.getIVA()).concat("% ")
					.concat(String.valueOf(factura.getSub_total() * (factura.getIVA() / 100))));
		} else {
			parameters.put("iva", "0% ".concat("0.0"));
		}

		parameters.put("excento", factura.getExento());
		parameters.put("total", factura.getSaldo());
		parameters.put("id_factura", String.valueOf(factura.getId()));
		// parameters.put("logo", img);
		parameters.put("cat_title", "Productos Belleza");
		byte[] reporte = clienteArchivo.obtenerImagenStream("factura.jasper");
		byte[] logo = clienteArchivo.obtenerImagenStream("LOGO.png");
		try {
			Image img = ImageIO.read(new ByteArrayInputStream(logo));
			parameters.put("img_logo", img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream stream = new ByteArrayInputStream(reporte);

		JasperPrint printer = JasperFillManager.fillReport(stream, parameters);

		return JasperExportManager.exportReportToPdf(printer);
	}

	@Override
	public byte[] ReporteTransacciones(List<Transacciones> listado, String titulo) throws JRException {
		// TODO Auto-generated method stub
		Double opent = 0.0;
		Double opsal = 0.0;
		Double totalop = 0.0;
		List<TTRansReporte> descripcion = new ArrayList<>();
		for (Transacciones e : listado) {
			TTRansReporte rp = new TTRansReporte(e);
			totalop += e.getPago().getMonto();
			if (rp.isIngreso()) {
				opent += e.getPago().getMonto();
			} else {
				opsal += e.getPago().getMonto();
				
			}
			descripcion.add(rp);
		}

		JRBeanArrayDataSource ds = new JRBeanArrayDataSource(descripcion.toArray());
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("ds", ds);

		parameters.put("cat_title", titulo);
		parameters.put("opent", String.format("%.2f", opent));
		parameters.put("opsal",  String.format("%.2f", opsal));
		parameters.put("totalop",  String.format("%.2f", totalop));
		byte[] reporte = clienteArchivo.obtenerImagenStream("ventas.jasper");
		byte[] logo = clienteArchivo.obtenerImagenStream("LOGO.png");
		try {
			Image img = ImageIO.read(new ByteArrayInputStream(logo));
			parameters.put("img_logo", img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream stream = new ByteArrayInputStream(reporte);
		JasperPrint printer = JasperFillManager.fillReport(stream, parameters);

		return JasperExportManager.exportReportToPdf(printer);
	}

}
