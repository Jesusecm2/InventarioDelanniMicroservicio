package com.microservices.delanni.inventory.app.DAO.Pagos;

import org.springframework.data.repository.CrudRepository;

import com.microservices.delanni.inventory.app.entity.pagos.Moneda;

public interface MonedaDAO extends CrudRepository<Moneda,Long> {

}
