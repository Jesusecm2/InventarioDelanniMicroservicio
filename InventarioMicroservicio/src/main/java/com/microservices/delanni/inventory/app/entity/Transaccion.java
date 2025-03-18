package com.microservices.delanni.inventory.app.entity;

import java.io.Serializable;
import java.util.Date;

//import com.formacionbdi.springboot.app.commons.usuarios.models.entity.Usuario;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Transaccion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String tipo;

	// 5 entrada 0 salida
	private Integer trnin;

	private String descripcion;

	private String usuario;

	private Date create_at;

	private Double monto;

	private Long trnNumber;

	// private Cuenta cuenta;

	@JoinColumn(name = "cliente", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.LAZY)
	private Cliente cliente;

	@JoinColumn(name = "proveedor", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.LAZY)
	private Proveedor proveedor;

	@JoinColumn(name = "producto", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.LAZY)
	private Producto producto;
	@JoinColumn(name = "factura", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.LAZY)
	private Factura factura;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getTrnin() {
		return trnin;
	}

	public void setTrnin(Integer trnin) {
		this.trnin = trnin;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Date getCreate_at() {
		return create_at;
	}

	public void setCreate_at(Date create_at) {
		this.create_at = create_at;
	}

	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}

	public Long getTrnNumber() {
		return trnNumber;
	}

	public void setTrnNumber(Long trnNumber) {
		this.trnNumber = trnNumber;
	}

	/*
	 * public Cuenta getCuenta() { return cuenta; }
	 * 
	 * public void setCuenta(Cuenta cuenta) { this.cuenta = cuenta; }
	 */
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Factura getFactura() {
		return factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}
	
	

}
