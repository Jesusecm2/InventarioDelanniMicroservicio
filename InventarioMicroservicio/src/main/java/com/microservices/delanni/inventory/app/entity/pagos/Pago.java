package com.microservices.delanni.inventory.app.entity.pagos;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Pago {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String narrativa;
	@NotBlank(message = "{nombre.noblanco}")
	@Size(min = 2, max = 255,message = "{nombre.tamano}")
	private String cod_ejecucion;
	
	private Date registro;
	
	private Date ejecucion;
	
	private Double monto;
	
	@OneToOne
	private ComprobantePago comprobante;
	
	
	@OneToOne
	private TipodePago tipo;
	
	@OneToOne
	private Moneda moneda;
	
	@OneToOne
	private ValorMoneda valor;
	
	@PrePersist
	private void PrePersist() {
		if(registro==null) {
			registro = new Date(System.currentTimeMillis());
		}
		if(ejecucion==null) {
			ejecucion = new Date(System.currentTimeMillis());
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNarrativa() {
		return narrativa;
	}

	public void setNarrativa(String narrativa) {
		this.narrativa = narrativa;
	}

	public String getCod_ejecucion() {
		return cod_ejecucion;
	}

	public void setCod_ejecucion(String cod_ejecucion) {
		this.cod_ejecucion = cod_ejecucion;
	}

	public Date getRegistro() {
		return registro;
	}

	public void setRegistro(Date registro) {
		this.registro = registro;
	}

	public Date getEjecucion() {
		return ejecucion;
	}

	public void setEjecucion(Date ejecucion) {
		this.ejecucion = ejecucion;
	}

	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public ComprobantePago getComprobante() {
		return comprobante;
	}

	public void setComprobante(ComprobantePago comprobante) {
		this.comprobante = comprobante;
	}

	public TipodePago getTipo() {
		return tipo;
	}

	public void setTipo(TipodePago tipo) {
		this.tipo = tipo;
	}

	public Moneda getMoneda() {
		return moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}

	public ValorMoneda getValor() {
		return valor;
	}

	public void setValor(ValorMoneda valor) {
		this.valor = valor;
	}
	
	
	
	
}
