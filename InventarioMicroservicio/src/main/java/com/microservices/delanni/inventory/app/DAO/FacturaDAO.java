package com.microservices.delanni.inventory.app.DAO;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.microservices.delanni.inventory.app.entity.Cliente;
import com.microservices.delanni.inventory.app.entity.Factura;
import com.microservices.delanni.inventory.app.entity.Proveedor;

public interface FacturaDAO extends CrudRepository<Factura, Long> {

	List<Factura> findByIdProveedor(Proveedor idProveedor);

	List<Factura> findByIdProveedorIsNotNull();

	List<Factura> findByIdProveedorAndStatus(Proveedor idProveedor, String status);

	List<Factura> findByStatus(String status);

	List<Factura> findByIdClienteIsNotNull();

	List<Factura> findByIdClienteAndStatus(Cliente idCliente, String status);

	List<Factura> findByIdCliente(Cliente idCliente);

	List<Factura> findByIdClienteAndCreateatBetween(Cliente idCliente, Date start, Date end);

	List<Factura> findBycreateatBetween(Date start, Date end);

	List<Factura> findByIdProveedorAndCreateatBetween(Proveedor idProveedor, Date start, Date end);

	List<Factura> findBycreateatBetweenAndIdProveedorIsNotNull(Date start, Date end);

	List<Factura> findBycreateatBetweenAndIdClienteIsNotNull(Date start, Date end);

}
