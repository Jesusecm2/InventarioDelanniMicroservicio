package com.microservices.delanni.inventory.app.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.microservices.delanni.inventory.app.entity.TpIngreso;
import com.microservices.delanni.inventory.app.entity.pagos.ComprobantePago;
import com.microservices.delanni.inventory.app.entity.pagos.Moneda;
import com.microservices.delanni.inventory.app.entity.pagos.Pago;
import com.microservices.delanni.inventory.app.entity.pagos.TipodePago;
import com.microservices.delanni.inventory.app.entity.pagos.ValorMoneda;

public interface IPagosService {

	//**********************Guardar
	public ValorMoneda guardarValorMoneda(ValorMoneda param);
	
	public TipodePago guardarTipoDePago(TipodePago pago);
	
	public Pago guardarPago(Pago pago);
	
	public ComprobantePago guardarComprobante(ComprobantePago pago);
	
	
	
	//********************
	
	public List<ValorMoneda> ObtenerValorMoneda(String status,Moneda moneda,Date registro);
	
	public ValorMoneda actualizarValorMoneda(ValorMoneda valor);
	
	public List<TipodePago> ObtenerTiposdePago();
	
	public List<Moneda> ObtenerMonedas();
		
	public List<TpIngreso> obtenerIngreso(String tipo);
	
	//public List<Pagos> ObtenerPagos()
	
	
	
}
