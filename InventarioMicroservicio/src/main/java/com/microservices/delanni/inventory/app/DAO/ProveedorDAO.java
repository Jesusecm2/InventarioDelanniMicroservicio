package com.microservices.delanni.inventory.app.DAO;

import org.springframework.data.repository.CrudRepository;

import com.microservices.delanni.inventory.app.entity.Proveedor;

public interface ProveedorDAO extends CrudRepository<Proveedor, Integer>{

}
