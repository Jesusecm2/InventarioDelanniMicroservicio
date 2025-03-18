package com.microservices.delanni.inventory.app.DAO;

import org.springframework.data.repository.CrudRepository;

import com.microservices.delanni.inventory.app.entity.pagos.ComprobantePago;

public interface ComprobantePagoDAO extends CrudRepository<ComprobantePago, Long>{

}
