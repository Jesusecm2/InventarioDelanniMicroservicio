package com.microservices.delanni.inventory.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class ImagenProducto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String imagen;

	private Integer sequence;

	@JsonIgnore
	@JoinColumn(name = "producto", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.LAZY)
	private Producto producto;

	@PrePersist
	private void prePersist() {
		if (sequence == null) {
			sequence = 1;
		}

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public ImagenProducto(Long id, String imagen, Integer sequence) {
		super();
		this.id = id;
		this.imagen = imagen;
		this.sequence = sequence;
	}

	public ImagenProducto() {
		super();
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

}
