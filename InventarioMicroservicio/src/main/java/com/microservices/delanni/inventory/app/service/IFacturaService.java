package com.microservices.delanni.inventory.app.service;

import java.util.Date;
import java.util.List;

import com.microservices.delanni.inventory.app.entity.Cliente;
import com.microservices.delanni.inventory.app.entity.Cuenta;
import com.microservices.delanni.inventory.app.entity.Factura;
import com.microservices.delanni.inventory.app.entity.LineaFactura;
import com.microservices.delanni.inventory.app.entity.Proveedor;

public interface IFacturaService {

	
	
	
	public Proveedor guardarProveedor(Proveedor save);
	
	public Cuenta guardarCuenta(Cuenta save);
	
	public Factura facturaById(Long id);
	
	public List<Proveedor> listadoProveedor();
	
	public List<Factura> listadoFacturas();

	
	public List<Factura> listadoFacturas(Proveedor prov);
	
	public List<Factura> listadoFacturas(Proveedor prov,String parametro);
	
	public List<Factura> listadoFacturas(Date start,Date end);
	
	public List<Factura> listadoFacturas(String status);
	
	public List<Factura> listadoFacturasNotNull();
	
	public List<Factura> listadoVentas();
	
	public List<Factura> listadoVentas(Cliente cl);
	
	public List<Factura> listadoVentas(Cliente cl,String status);
	
	public List<Factura> listadoVentas(Date date,Date end);
	
	public List<Factura> listadoVentas(Cliente cl,Date start,Date end);
	
	public Factura guardarFactura(Factura save);
	
	public LineaFactura guardarLinea(LineaFactura linea);
	
}
