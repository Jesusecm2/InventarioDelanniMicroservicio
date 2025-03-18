package com.microservices.delanni.inventory.app.DAO;

import org.springframework.data.repository.CrudRepository;

import com.microservices.delanni.inventory.app.entity.Cuenta;

public interface CuentaDAO extends CrudRepository<Cuenta, Integer>{

}
