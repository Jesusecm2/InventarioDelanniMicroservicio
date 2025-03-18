package com.microservices.delanni.inventory.app.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservices.delanni.inventory.app.DAO.ComprobantePagoDAO;
import com.microservices.delanni.inventory.app.DAO.TpIngresoDAO;
import com.microservices.delanni.inventory.app.DAO.Pagos.MonedaDAO;
import com.microservices.delanni.inventory.app.DAO.Pagos.PagoDAO;
import com.microservices.delanni.inventory.app.DAO.Pagos.TipodePagoDAO;
import com.microservices.delanni.inventory.app.DAO.Pagos.ValorMonedaDAO;
import com.microservices.delanni.inventory.app.entity.TpIngreso;
import com.microservices.delanni.inventory.app.entity.pagos.ComprobantePago;
import com.microservices.delanni.inventory.app.entity.pagos.Moneda;
import com.microservices.delanni.inventory.app.entity.pagos.Pago;
import com.microservices.delanni.inventory.app.entity.pagos.TipodePago;
import com.microservices.delanni.inventory.app.entity.pagos.ValorMoneda;

@Service
public class PagoServiceImpl implements IPagosService {

	@Autowired
	private TipodePagoDAO tipopagoDAO;

	@Autowired
	private MonedaDAO monedaDAO;

	@Autowired
	private ValorMonedaDAO valorMonedaDAO;
	
	@Autowired
	private PagoDAO pagoDao;
	
	@Autowired
	private TpIngresoDAO ingresoDAO;
	
	@Autowired
	private ComprobantePagoDAO comprobantePagoDAO;

	@Override
	public ValorMoneda guardarValorMoneda(ValorMoneda param) {
		// TODO Auto-generated method stub
		return valorMonedaDAO.save(param);
	}

	@Override
	public TipodePago guardarTipoDePago(TipodePago pago) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pago guardarPago(Pago pago) {
		// TODO Auto-generated method stub
		return pagoDao.save(pago);
	}

	@Override
	public List<ValorMoneda> ObtenerValorMoneda(String status, Moneda moneda, Date registro) {
		// TODO Auto-generated method stub
		return (List<ValorMoneda>) valorMonedaDAO.findFirstByMonedaAndStatusAndRegistro(moneda, status, registro);
	}

	@Override
	public List<TipodePago> ObtenerTiposdePago() {
		// TODO Auto-generated method stub
		return (List<TipodePago>) tipopagoDAO.findAll();
	}

	@Override
	public List<Moneda> ObtenerMonedas() {
		// TODO Auto-generated method stub
		return (List<Moneda>) monedaDAO.findAll();
	}

	@Override
	public ValorMoneda actualizarValorMoneda(ValorMoneda valor) {
		// TODO Auto-generated method stub
		return valorMonedaDAO.save(valor);
	}

	@Override
	public List<TpIngreso> obtenerIngreso(String tipo) {
		// TODO Auto-generated method stub
		return ingresoDAO.findBytipo(tipo);
	}

	@Override
	public ComprobantePago guardarComprobante(ComprobantePago pago) {
		// TODO Auto-generated method stub
		return comprobantePagoDAO.save(pago);
	}

}
