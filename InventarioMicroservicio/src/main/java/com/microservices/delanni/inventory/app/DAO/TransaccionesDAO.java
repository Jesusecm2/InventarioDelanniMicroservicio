package com.microservices.delanni.inventory.app.DAO;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.microservices.delanni.inventory.app.entity.Cliente;
import com.microservices.delanni.inventory.app.entity.Factura;
import com.microservices.delanni.inventory.app.entity.Proveedor;
import com.microservices.delanni.inventory.app.entity.TpIngreso;
import com.microservices.delanni.inventory.app.entity.Transacciones;

public interface TransaccionesDAO extends CrudRepository<Transacciones, Long>{
	
	List<Transacciones> findByFechaBetweenAndTpIngresoIn(Date fecha,Date fin,List<TpIngreso> valores);
	
	List<Transacciones> findByFechaBetweenAndTpIngreso(Date fecha,Date fin,TpIngreso valores);

	List<Transacciones> findByFechaBetween(Date start,Date fin);
	
	List<Transacciones> findByTpIngreso(TpIngreso tpIngreso);
	
	List<Transacciones> findByFechaBetweenAndTpIngresoIsNotNull(Date fecha,Date fin);
	
	//List<Transacciones> findByFechaBetweenAndFacturaIn(Date start,Date fin,List<Factura> Factura);
	
	@Query("SELECT t, f, c FROM Transacciones t JOIN t.id_factura f JOIN f.idCliente c where t.fecha BETWEEN :f1 AND :f2")
	List<Transacciones> obtenerVentas(@Param("f1")Date f1,@Param("f2")Date f2);

	@Query("SELECT t, f, c FROM Transacciones t JOIN t.id_factura f JOIN f.idCliente c ")
	List<Transacciones> obtenerVentas();
	
	@Query("SELECT t, f, c FROM Transacciones t JOIN t.id_factura f JOIN f.idProveedor c where f.idProveedor=:f1 ")
	List<Transacciones> obtenerTransaccionesProveedor(@Param("f1") Proveedor prov);
	
	
	@Query("SELECT t, f, c FROM Transacciones t JOIN t.id_factura f JOIN f.idProveedor c where t.fecha BETWEEN :f1 AND :f2")
	List<Transacciones> obtenerTransaccionesProveedor(@Param("f1")Date f1,@Param("f2")Date f2);

	@Query("SELECT t, f FROM Transacciones t JOIN t.id_factura f where t.id_factura = :factura")
	List<Transacciones> obtenerTransacciones(@Param("factura")Factura proveedor);
	

}
