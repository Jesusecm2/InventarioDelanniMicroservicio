package com.microservices.delanni.inventory.app.entity;

import java.io.Serializable;
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
public class Cliente implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message = "{nombre.noblanco}")
	@Size(min = 2, max = 255,message = "{nombre.tamano}")
	private String nombre;
	@NotBlank(message = "{nombre.noblanco}")
	@Size(min = 2, max = 255,message = "{nombre.tamano}")
	private String rif;
	
	private String apellido;
	
	private Date create_at;
	
	private String email;
	
	private String numero;
	
	@OneToOne
	private Cuenta cuenta;

	@PrePersist
	private void PrePersist() {
		if(create_at==null) {
			create_at = new Date(System.currentTimeMillis());
		}
	}
	
	public Cliente() {
		super();
	}

	public Cliente(Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRif() {
		return rif;
	}

	public void setRif(String rif) {
		this.rif = rif;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public Date getCreate_at() {
		return create_at;
	}

	public void setCreate_at(Date create_at) {
		this.create_at = create_at;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}
	
	
	
}
