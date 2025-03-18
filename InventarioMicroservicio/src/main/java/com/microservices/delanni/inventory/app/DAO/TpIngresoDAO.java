package com.microservices.delanni.inventory.app.DAO;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.microservices.delanni.inventory.app.entity.TpIngreso;

public interface TpIngresoDAO extends CrudRepository<TpIngreso,Long>{

	public List<TpIngreso> findBytipo(String tipo);
}
