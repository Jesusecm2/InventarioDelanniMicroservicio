package com.microservices.delanni.inventory.app.DAO;

import org.springframework.data.repository.CrudRepository;

import com.microservices.delanni.inventory.app.entity.Categoria;

public interface CategoriaDAO extends CrudRepository<Categoria,Integer>{

}
