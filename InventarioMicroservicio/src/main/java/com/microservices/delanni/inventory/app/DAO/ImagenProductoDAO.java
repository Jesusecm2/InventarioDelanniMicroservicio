package com.microservices.delanni.inventory.app.DAO;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.microservices.delanni.inventory.app.entity.ImagenProducto;

public interface ImagenProductoDAO extends CrudRepository<ImagenProducto, Long>{

	
	public List<ImagenProducto> findByimagen(String imagen);
	
	
}
