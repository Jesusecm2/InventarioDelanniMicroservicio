package com.microservices.delanni.inventory.app.service;

import java.util.Date;
import java.util.List;

import com.microservices.delanni.inventory.app.entity.Factura;
import com.microservices.delanni.inventory.app.entity.Producto;
import com.microservices.delanni.inventory.app.entity.Proveedor;
import com.microservices.delanni.inventory.app.entity.TpIngreso;
import com.microservices.delanni.inventory.app.entity.Transaccion;
import com.microservices.delanni.inventory.app.entity.Transacciones;
import com.microservices.delanni.inventory.app.entity.pagos.Pago;

public interface TransaccionesService {

	
	
	public Transacciones CrearTProducto(Producto producto,String accion,Double valor);
	
	public Transacciones CrearTFactura(Factura factura,Pago pago);
	
	public Transacciones CrearTEgreso(TpIngreso ingreso,Pago pago);
	
	
	
	public List<Transacciones> obtenerEgresos(Date inicio,Date fin,List<TpIngreso> ingreso);
	
	
	public List<Transacciones> obtenerTransacciones(Factura factura);
	
	public List<Transacciones> obtenerTransacciones(Proveedor factura);
	
	public List<Transacciones> obtenerTransacciones(TpIngreso tp);
	
	public List<Transacciones> obtenerIngresosEgresos(Date inicio,Date fin);
	
	
	
	public List<Transacciones> obtenerTransacciones(Date inicio,Date fin,TpIngreso tp);
	
	public List<Transacciones> obtenerTransacciones(Date start,Date end);
	
	public List<Transacciones> obtenerVentas(Date start,Date end);
	
	
	
	
}
