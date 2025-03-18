package com.microservices.delanni.inventory.app.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Producto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	
	private String codigo;

	@NotBlank()
	@Size(min = 2, max = 255)
	private String nombre;

	@NotBlank
	private String descripcion;

	@OneToMany(mappedBy = "producto")
	private List<ImagenProducto> imagenes;
	
	private Double stock_min;
	
	private Double cant_actual;
	
	private Double cant_anterior;
	
	@NotNull
	private Double precio_unit;

	private Double precio_vent;

	private Date create_at;

	private Date last_mod;
	
	private String status;

	@OneToOne
	private Categoria cat;

	@PrePersist
	private void prePersist() {
		if (create_at == null) {
			create_at = new Date(System.currentTimeMillis());
			last_mod = new Date(System.currentTimeMillis());
		}
		if (cant_actual == null) {
			cant_actual = 0.0;
			cant_anterior = 0.0;
		}
		if(status == null) {
			status=" ";
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<ImagenProducto> getImagenes() {
		return imagenes;
	}

	public void setImagenes(List<ImagenProducto> imagenes) {
		this.imagenes = imagenes;
	}

	public Double getCant_actual() {
		return cant_actual;
	}

	public void setCant_actual(Double cant_actual) {
		this.cant_actual = cant_actual;
	}

	public Double getCant_anterior() {
		return cant_anterior;
	}

	public void setCant_anterior(Double cant_anterior) {
		this.cant_anterior = cant_anterior;
	}

	public Double getPrecio_unit() {
		return precio_unit;
	}

	public void setPrecio_unit(Double precio_unit) {
		this.precio_unit = precio_unit;
	}

	public Double getPrecio_vent() {
		return precio_vent;
	}

	public void setPrecio_vent(Double precio_vent) {
		this.precio_vent = precio_vent;
	}

	public Categoria getCat() {
		return cat;
	}

	public void setCat(Categoria cat) {
		this.cat = cat;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Date getCreate_at() {
		return create_at;
	}

	public void setCreate_at(Date create_at) {
		this.create_at = create_at;
	}

	public Date getLast_mod() {
		return last_mod;
	}

	public void setLast_mod(Date last_mod) {
		this.last_mod = last_mod;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getStock_min() {
		return stock_min;
	}

	public void setStock_min(Double stock_min) {
		this.stock_min = stock_min;
	}



	
}
