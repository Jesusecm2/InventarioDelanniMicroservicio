package com.microservices.delanni.inventory.app.DAO.Pagos;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.microservices.delanni.inventory.app.entity.pagos.Moneda;
import com.microservices.delanni.inventory.app.entity.pagos.ValorMoneda;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

public interface ValorMonedaDAO extends CrudRepository<ValorMoneda,Long>{
	
	public List<ValorMoneda> findFirstByMonedaAndStatusAndRegistro(Moneda moneda,String status, Date registro);

}
