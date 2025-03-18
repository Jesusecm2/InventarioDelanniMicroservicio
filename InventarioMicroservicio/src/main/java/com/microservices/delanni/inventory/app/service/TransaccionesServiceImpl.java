package com.microservices.delanni.inventory.app.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservices.delanni.inventory.app.DAO.FacturaDAO;
import com.microservices.delanni.inventory.app.DAO.TransaccionesDAO;
import com.microservices.delanni.inventory.app.entity.Factura;
import com.microservices.delanni.inventory.app.entity.Producto;
import com.microservices.delanni.inventory.app.entity.Proveedor;
import com.microservices.delanni.inventory.app.entity.TpIngreso;
import com.microservices.delanni.inventory.app.entity.Transaccion;
import com.microservices.delanni.inventory.app.entity.Transacciones;
import com.microservices.delanni.inventory.app.entity.pagos.Pago;

@Service
public class TransaccionesServiceImpl implements TransaccionesService{

	@Autowired
	private TransaccionesDAO transDAO;
	
	@Autowired
	private IFacturaService serviceFactura;
	
	
	@Override
	public Transacciones CrearTProducto(Producto producto, String accion, Double valor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transacciones CrearTFactura(Factura factura,Pago pago) {
		// TODO Auto-generated method stub
		Transacciones trans = new Transacciones();
		trans.setPago(pago);
		trans.setFactura(factura);
		factura.setSaldo_pagado(factura.getSaldo_pagado()+pago.getMonto());
		serviceFactura.guardarFactura(factura);
		//trans.set(pago.getMonto());
		trans.setAccion(retornarAccion("PF", String.valueOf(factura.getId()), pago));
		if(pago.getEjecucion()!=null) {
			trans.setFecha(pago.getEjecucion());
		}
		if(pago.getNarrativa()!=null) {
			trans.setRef(pago.getNarrativa());
		}
		
		
		return transDAO.save(trans);
	}

	@Override
	public Transacciones CrearTEgreso(TpIngreso ingreso, Pago pago) {
		// TODO Auto-generated method stub
		Transacciones trans = new Transacciones();
		trans.setPago(pago);
		trans.setTpIngreso(ingreso);
		if(pago.getEjecucion()!=null) {
			trans.setFecha(pago.getEjecucion());
		}
		switch(ingreso.getTipo()) {
			case "E":
				trans.setAccion(retornarAccion("PE", String.valueOf(ingreso.getId()), pago));
				break;
			case "I":
				trans.setAccion(retornarAccion("PI", String.valueOf(ingreso.getId()), pago));
				break;
		}
		return transDAO.save(trans);
	}
	
	
	
	private String retornarAccion(String inicial,String id,Pago pago) {
		return (inicial.concat("-").concat(String.valueOf(id).concat("(").concat(pago.getTipo().getTipo().concat(")"))).concat("-").concat(pago.getCod_ejecucion()));
	}

	@Override
	public List<Transacciones> obtenerEgresos(Date inicio,Date fin,List<TpIngreso> ingreso) {
		// TODO Auto-generated method stub
	
		//return transDAO.findByfecha(date);
		return transDAO.findByFechaBetweenAndTpIngresoIn(inicio,fin,ingreso);
		
	}

	@Override
	public List<Transacciones> obtenerTransacciones(Date start, Date end) {
		// TODO Auto-generated method stub
		return transDAO.findByFechaBetween(start, end);
	}

	@Override
	public List<Transacciones> obtenerVentas(Date start, Date end) {
		// TODO Auto-generated method stub
		return transDAO.obtenerVentas(start, end);
	}

	@Override
	public List<Transacciones> obtenerTransacciones(Factura factura) {
		// TODO Auto-generated method stub
		return transDAO.obtenerTransacciones(factura);
	}

	@Override
	public List<Transacciones> obtenerTransacciones(TpIngreso tp) {
		// TODO Auto-generated method stub
		return transDAO.findByTpIngreso(tp);
	}

	@Override
	public List<Transacciones> obtenerTransacciones(Date inicio, Date fin, TpIngreso tp) {
		// TODO Auto-generated method stub
		return transDAO.findByFechaBetweenAndTpIngreso(inicio, fin, tp);
	}

	@Override
	public List<Transacciones> obtenerTransacciones(Proveedor proveedor) {
		// TODO Auto-generated method stub
		return transDAO.obtenerTransaccionesProveedor(proveedor);
	}

	@Override
	public List<Transacciones> obtenerIngresosEgresos(Date inicio, Date fin) {
		// TODO Auto-generated method stub
		return transDAO.findByFechaBetweenAndTpIngresoIsNotNull(inicio, fin);
	}

	
	
}
