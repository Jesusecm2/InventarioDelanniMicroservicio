package com.microservices.delanni.inventory.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TipoOperacion {

	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Integer ingreso_egreso;
	
	private String nombre;
	
	private String prefix;

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

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Integer getIngreso_egreso() {
		return ingreso_egreso;
	}

	public void setIngreso_egreso(Integer ingreso_egreso) {
		this.ingreso_egreso = ingreso_egreso;
	}
	
	
	
	
	
}
