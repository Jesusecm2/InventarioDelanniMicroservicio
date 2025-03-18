package com.microservices.delanni.inventory.app.DAO.Pagos;

import org.springframework.data.repository.CrudRepository;

import com.microservices.delanni.inventory.app.entity.pagos.TipodePago;

public interface TipodePagoDAO extends CrudRepository<TipodePago, Long>{

}
