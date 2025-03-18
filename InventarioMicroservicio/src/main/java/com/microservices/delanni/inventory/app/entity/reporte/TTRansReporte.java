package com.microservices.delanni.inventory.app.entity.reporte;

import com.microservices.delanni.inventory.app.entity.Transacciones;

public class TTRansReporte {

	
	private String trn;
	
	private String ccy;
	
	private String mnto;
	
	private Double aux;
	
	private boolean ingreso;
	
	

	public TTRansReporte() {
		super();
	}
	
	

	public TTRansReporte(Transacciones trn) {
		super();
		String base = trn.getPago().getTipo().getTipo().concat("-".concat(trn.getPago().getCod_ejecucion()).concat(": "));
		aux = trn.getPago().getMonto();
		ingreso = true;
		if(trn.getFactura()!=null) {
			base = base.concat("Factura ".concat(String.valueOf(trn.getFactura().getId())));
			if(trn.getFactura().getIdProveedor()==null) {
				ingreso = true;
				
			}else {
				ingreso = false;
				aux = aux*-1;
			}
		}else if(trn.getTpIngreso()!=null) {
			base = base.concat(trn.getTpIngreso().getNombre());
			
			if(trn.getTpIngreso().getTipo().equals("E")) {
				ingreso = false;
				aux = aux*-1;
			}
		}
		
		
		
		if(trn.getPago().getMoneda().getConverted().equals("1")) {
			try {
				this.mnto = String.format("%.2f",(aux*trn.getPago().getValor().getValor()));	
				base = base.concat(" Tasa:".concat(String.format("%.2f", trn.getPago().getValor().getValor())));
			}catch(Exception ex) {
				this.mnto = String.format("%.2f",aux);
			}
			
		}else {
			this.mnto = String.format("%.2f",aux);
		}
		
		this.ccy = trn.getPago().getMoneda().getCcy();
		this.trn = base;
	}


	

	public boolean isIngreso() {
		return ingreso;
	}



	public void setIngreso(boolean ingreso) {
		this.ingreso = ingreso;
	}



	public TTRansReporte(String trn, String ccy, String mnto) {
		super();
		this.trn = trn;
		this.ccy = ccy;
		this.mnto = mnto;
	}

	public String getTrn() {
		return trn;
	}

	public void setTrn(String trn) {
		this.trn = trn;
	}

	public String getCcy() {
		return ccy;
	}

	public void setCcy(String ccy) {
		this.ccy = ccy;
	}

	public String getMnto() {
		return mnto;
	}

	public void setMnto(String mnto) {
		this.mnto = mnto;
	}
	
	
}
