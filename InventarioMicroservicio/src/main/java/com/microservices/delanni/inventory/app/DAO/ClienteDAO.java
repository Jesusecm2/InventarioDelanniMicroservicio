package com.microservices.delanni.inventory.app.DAO;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.microservices.delanni.inventory.app.entity.Cliente;

public interface ClienteDAO extends CrudRepository<Cliente, Long> {

	
	public List<Cliente> findByRif(String rif);
}
