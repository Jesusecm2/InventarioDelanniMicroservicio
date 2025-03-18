package com.microservices.delanni.inventory.app.entity.reporte;

import com.microservices.delanni.inventory.app.entity.LineaFactura;

public class LineaFacturaR {

	private String tccod;
	private String tcnom;
	private Double tcgarant;
	private Double tccant;
	private Double tcmonto;
	private Double tctotal;
	
	
	
	public LineaFacturaR(LineaFactura linea) {
		super();
		this.tccod = linea.getId_producto().getCodigo();
		this.tcnom = linea.getId_producto().getNombre();
		this.tccant = linea.getCantidad();
		this.tcgarant = 0.0;
		this.tcmonto = linea.getPrecio_unit();
		this.tctotal = linea.getPrecio_unit()*linea.getCantidad();
		
	}



	public String getTccod() {
		return tccod;
	}



	public void setTccod(String tccod) {
		this.tccod = tccod;
	}



	public String getTcnom() {
		return tcnom;
	}



	public void setTcnom(String tcnom) {
		this.tcnom = tcnom;
	}



	public Double getTcgarant() {
		return tcgarant;
	}



	public void setTcgarant(Double tcgarant) {
		this.tcgarant = tcgarant;
	}



	public Double getTccant() {
		return tccant;
	}



	public void setTccant(Double tccant) {
		this.tccant = tccant;
	}

	public Double getTcmonto() {
		return tcmonto;
	}



	public void setTcmonto(Double tcmonto) {
		this.tcmonto = tcmonto;
	}



	public Double getTctotal() {
		return tctotal;
	}



	public void setTctotal(Double tctotal) {
		this.tctotal = tctotal;
	}
	
	
	
	

}
