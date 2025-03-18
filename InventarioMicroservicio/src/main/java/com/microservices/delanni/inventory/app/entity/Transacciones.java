package com.microservices.delanni.inventory.app.entity;

import java.util.Date;

import com.microservices.delanni.inventory.app.entity.pagos.Moneda;
import com.microservices.delanni.inventory.app.entity.pagos.Pago;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
public class Transacciones {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	private Cuenta cuenta;
	
	private String accion;

	private String user;

	private Date fecha;

	private String ref;

	@OneToOne
	private Pago pago;
	
	
	@OneToOne
	@PrimaryKeyJoinColumn(name = "id_cliente")
	private Cliente cliente;

	@JoinColumn(name = "id_producto", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.LAZY)
	private Producto id_producto;
	
	
	@JoinColumn(name = "id_factura", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.LAZY)
	private Factura id_factura;
	
	@OneToOne
	private TpIngreso tpIngreso;

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}



	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


	public Producto getProducto() {
		return id_producto;
	}

	public void setProducto(Producto producto) {
		this.id_producto = producto;
	}

	public Factura getFactura() {
		return id_factura;
	}

	public void setFactura(Factura factura) {
		this.id_factura = factura;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public Pago getPago() {
		return pago;
	}

	public void setPago(Pago pago) {
		this.pago = pago;
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

	public TpIngreso getTpIngreso() {
		return tpIngreso;
	}

	public void setTpIngreso(TpIngreso tpIngreso) {
		this.tpIngreso = tpIngreso;
	}


	
	
	
	

}
