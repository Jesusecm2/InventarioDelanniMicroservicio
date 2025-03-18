package com.microservices.delanni.inventory.app.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class Factura implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String descripcion;

	private Double saldo;

	private Double saldo_pagado;

	private Date ultimaActualizacion;

	private String status;
	
	@Column(name = "create_at")
	private Date createat;
	
	private Double sub_total;
	
	private Double IVA;
	
	private Double exento;
	
	public Factura() {
		super();
	}
	
	public Factura(Long id) {
		super();
		this.id = id;
	}

	///***************************C cliente : L local
	@OneToOne
	private Cliente idCliente;

	@JoinColumn(name = "idProveedor", referencedColumnName = "ID")
	@OneToOne
	private Proveedor idProveedor;
	
	@NotEmpty
	@OneToMany(mappedBy = "id_factura")
	private List<LineaFactura> lineas;

	@PrePersist
	private void prePersist() {
		if (createat == null) {
			createat = new Date(System.currentTimeMillis());
			ultimaActualizacion = new Date(System.currentTimeMillis());
		}
		if(saldo_pagado==null) {
			saldo_pagado = 0.0;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public Double getSaldo_pagado() {
		return saldo_pagado;
	}

	public void setSaldo_pagado(Double saldo_pagado) {
		this.saldo_pagado = saldo_pagado;
	}

	public Date getUltimaActualizacion() {
		return ultimaActualizacion;
	}

	public void setUltimaActualizacion(Date ultimaActualizacion) {
		this.ultimaActualizacion = ultimaActualizacion;
	}


	public Date getCreate_at() {
		return createat;
	}

	public void setCreate_at(Date createat) {
		this.createat = createat;
	}

	public Proveedor getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(Proveedor idProveedor) {
		this.idProveedor = idProveedor;
	}

	public List<LineaFactura> getLineas() {
		return lineas;
	}

	public void setLineas(List<LineaFactura> lineas) {
		this.lineas = lineas;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getSub_total() {
		return sub_total;
	}

	public void setSub_total(Double sub_total) {
		this.sub_total = sub_total;
	}

	public Double getIVA() {
		return IVA;
	}

	public void setIVA(Double iVA) {
		IVA = iVA;
	}

	public Double getExento() {
		return exento;
	}

	public void setExento(Double exento) {
		this.exento = exento;
	}

	public Cliente getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Cliente idCliente) {
		this.idCliente = idCliente;
	}

	
	
	
	
	

	

	

	
	
}
