package com.microservices.delanni.inventory.app.DAO;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.microservices.delanni.inventory.app.entity.Categoria;
import com.microservices.delanni.inventory.app.entity.Producto;

public interface ProductoDAO extends CrudRepository<Producto, Long> {

	public List<Producto> findByCodigo(String codigo);
	
	public List<Producto> findByNombreStartingWith(String nombre);
	
	public List<Producto> findByCat(Categoria cat);
	
	public List<Producto> findByCatAndNombreStartingWith(Categoria cat, String nombre);
}
