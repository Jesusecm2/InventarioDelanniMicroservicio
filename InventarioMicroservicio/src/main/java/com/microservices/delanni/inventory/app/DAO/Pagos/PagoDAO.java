package com.microservices.delanni.inventory.app.DAO.Pagos;

import org.springframework.data.repository.CrudRepository;

import com.microservices.delanni.inventory.app.entity.pagos.Pago;

public interface PagoDAO extends CrudRepository<Pago,Long>{

}
