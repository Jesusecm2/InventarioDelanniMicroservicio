package com.microservices.delanni.inventory.app.entity.pagos;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class ValorMoneda {

	
	@PrePersist
	private void prePersist() {
		if(registro==null) {
			registro = new Date(System.currentTimeMillis());
		}
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Moneda moneda;
	
	private Double valor;
	
	@Temporal(TemporalType.DATE)
	private Date registro;
	//***********************A pago, B Desactivado, H historico
	private String status;
	
	private String casa_cambio;
	
	private String uso;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Moneda getMoneda() {
		return moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Date getRegistro() {
		return registro;
	}

	public void setRegistro(Date registro) {
		this.registro = registro;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCasa_cambio() {
		return casa_cambio;
	}

	public void setCasa_cambio(String casa_cambio) {
		this.casa_cambio = casa_cambio;
	}

	public String getUso() {
		return uso;
	}

	public void setUso(String uso) {
		this.uso = uso;
	}
	
	
	
	
	
	
}
