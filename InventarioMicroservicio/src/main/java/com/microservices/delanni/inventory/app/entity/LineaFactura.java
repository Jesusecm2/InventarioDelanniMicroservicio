package com.microservices.delanni.inventory.app.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class LineaFactura implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private Double cantidad;
	@NotNull
	private Double precio_unit;
	
	
	@JoinColumn(name = "id_producto", referencedColumnName = "ID")
	@OneToOne
	private Producto id_producto;
	
	@JoinColumn(name = "id_factura", referencedColumnName = "ID")
	@OneToOne
	@JsonIgnore
	private Factura id_factura;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public Double getPrecio_unit() {
		return precio_unit;
	}

	public void setPrecio_unit(Double precio_unit) {
		this.precio_unit = precio_unit;
	}

	public Producto getId_producto() {
		return id_producto;
	}

	public void setId_producto(Producto id_producto) {
		this.id_producto = id_producto;
	}

	public Factura getId_factura() {
		return id_factura;
	}

	public void setId_factura(Factura id_factura) {
		this.id_factura = id_factura;
	}
	
	
	

}
