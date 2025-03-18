package com.microservices.delanni.inventory.app.service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.microservices.delanni.inventory.app.DAO.CuentaDAO;
import com.microservices.delanni.inventory.app.DAO.FacturaDAO;
import com.microservices.delanni.inventory.app.DAO.LineaFacturaDAO;
import com.microservices.delanni.inventory.app.DAO.ProveedorDAO;
import com.microservices.delanni.inventory.app.entity.Cliente;
import com.microservices.delanni.inventory.app.entity.Cuenta;
import com.microservices.delanni.inventory.app.entity.Factura;
import com.microservices.delanni.inventory.app.entity.LineaFactura;
import com.microservices.delanni.inventory.app.entity.Proveedor;
import com.microservices.delanni.inventory.app.entity.Transacciones;

@Service
public class FacturaServiceImpl implements IFacturaService {

	@Autowired
	private ProveedorDAO proveedorDao;

	@Autowired
	private CuentaDAO cuentaDao;

	@Autowired
	private FacturaDAO facturaDAO;

	@Autowired
	private LineaFacturaDAO lineaDAO;

	@Override
	public Proveedor guardarProveedor(Proveedor save) {
		// TODO Auto-generated method stub
		return proveedorDao.save(save);
	}

	@Override
	public Cuenta guardarCuenta(Cuenta save) {
		// TODO Auto-generated method stub
		return cuentaDao.save(save);
	}

	@Override
	public List<Proveedor> listadoProveedor() {
		// TODO Auto-generated method stub
		return (List<Proveedor>) proveedorDao.findAll();
	}

	@Override
	public Factura guardarFactura(Factura save) {
		NumberFormat f1 = NumberFormat.getInstance();
		f1.setMaximumFractionDigits(2);
		Double monto_total = 0.0;
		if (save.getLineas() != null && !save.getLineas().isEmpty()) {
			for (LineaFactura linea : save.getLineas()) {
			
					monto_total += linea.getCantidad() * linea.getPrecio_unit();
				

			}
		}
		if (save.getIdCliente() == null && save.getIdProveedor() == null) {
			save.setIdCliente(new Cliente(-1));
		}
		save.setSub_total(monto_total);
		if (monto_total > 0) {
			if (save.getExento() > 0) {
				monto_total = monto_total - save.getExento();
			}
			if (save.getIVA() > 0) {
				monto_total = monto_total + ((save.getIVA() / 100) * monto_total);
			}

			save.setSaldo(monto_total);
		}
		if (save.getSaldo() <= save.getSaldo_pagado()) {
			save.setStatus("C");
		} else {
			save.setStatus("A");
		}

		save = facturaDAO.save(save);

		if (save.getLineas() != null && !save.getLineas().isEmpty()) {
			for (LineaFactura linea : save.getLineas()) {
					linea.setId_factura(save);
					lineaDAO.save(linea);

			}
		}
		return facturaDAO.save(save);
	}

	@Override
	public List<Factura> listadoFacturas(Proveedor prov) {
		// TODO Auto-generated method stub
		return facturaDAO.findByIdProveedor(prov);
	}

	@Override
	public LineaFactura guardarLinea(LineaFactura linea) {
		// TODO Auto-generated method stub
		return lineaDAO.save(linea);
	}

	@Override
	public List<Factura> listadoFacturas() {
		// TODO Auto-generated method stub
		return (List<Factura>) facturaDAO.findAll();
	}

	@Override
	public List<Factura> listadoFacturasNotNull() {
		// TODO Auto-generated method stub
		return facturaDAO.findByIdProveedorIsNotNull();
	}

	@Override
	public List<Factura> listadoFacturas(Proveedor prov, String parametro) {
		// TODO Auto-generated method stub
		return facturaDAO.findByIdProveedorAndStatus(prov, parametro);
	}

	@Override
	public List<Factura> listadoFacturas(String parametro) {
		// TODO Auto-generated method stub
		return facturaDAO.findByStatus(parametro);
	}

	@Override
	public List<Factura> listadoVentas() {
		// TODO Auto-generated method stub
		return facturaDAO.findByIdClienteIsNotNull();
	}

	@Override
	public List<Factura> listadoVentas(Cliente cl) {
		// TODO Auto-generated method stub
		return facturaDAO.findByIdCliente(cl);
	}

	@Override
	public List<Factura> listadoVentas(Cliente cl, String status) {
		// TODO Auto-generated method stub
		return facturaDAO.findByIdClienteAndStatus(cl, status);
	}

	@Override
	public List<Factura> listadoFacturas(Date start, Date end) {
		// TODO Auto-generated method stub
		return facturaDAO.findBycreateatBetween(start, end);
	}

	@Override
	public List<Factura> listadoVentas(Date date, Date end) {
		// TODO Auto-generated method stub
		return facturaDAO.findBycreateatBetweenAndIdClienteIsNotNull(date, end);
	}

	@Override
	public List<Factura> listadoVentas(Cliente cl, Date date, Date end) {
		// TODO Auto-generated method stub
		return facturaDAO.findByIdClienteAndCreateatBetween(cl, date, end);
	}

	@Override
	public Factura facturaById(Long id) {
		// TODO Auto-generated method stub
		return facturaDAO.findById(id).orElse(null);
	}



}
