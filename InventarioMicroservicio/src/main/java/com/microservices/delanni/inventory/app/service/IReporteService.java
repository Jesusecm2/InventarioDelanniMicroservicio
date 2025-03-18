package com.microservices.delanni.inventory.app.service;

import java.io.InputStream;
import java.util.List;

import com.microservices.delanni.inventory.app.entity.Categoria;
import com.microservices.delanni.inventory.app.entity.Factura;
import com.microservices.delanni.inventory.app.entity.Producto;
import com.microservices.delanni.inventory.app.entity.Transacciones;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public interface IReporteService {

	
	
	
	public byte[] ReporteCategoria(List<Producto>productos,Categoria cat) throws JRException;
	
	public byte[] ReporteFactura(Factura factura) throws JRException;
	
	public byte[] ReporteTransacciones(List<Transacciones> listado,String fechas) throws JRException;
	
}
