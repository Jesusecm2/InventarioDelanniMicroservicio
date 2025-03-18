package com.microservices.delanni.inventory.app.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservices.delanni.inventory.app.DAO.CategoriaDAO;
import com.microservices.delanni.inventory.app.DAO.ClienteDAO;
import com.microservices.delanni.inventory.app.DAO.ImagenProductoDAO;
import com.microservices.delanni.inventory.app.DAO.ProductoDAO;
import com.microservices.delanni.inventory.app.entity.Categoria;
import com.microservices.delanni.inventory.app.entity.Cliente;
import com.microservices.delanni.inventory.app.entity.ImagenProducto;
import com.microservices.delanni.inventory.app.entity.Producto;

@Service
public class InventarioServiceImpl implements IInventarioService {

	@Autowired
	CategoriaDAO categoriadao;

	@Autowired
	ProductoDAO productodao;

	@Autowired
	ImagenProductoDAO imagenDAO;
	
	@Autowired
	private ClienteDAO clienteDAO;

	@Override
	public Categoria GuardarCategoria(Categoria categoria) {
		return categoriadao.save(categoria);
	}

	@Override
	public Categoria ObtenerCategoriaId(Categoria categoria) {
		// TODO Auto-generated method stub
		return categoriadao.findById(categoria.getId()).orElse(null);
	}

	@Override
	public List<Categoria> ListadoCategoria() {
		// TODO Auto-generated method stub
		return (List<Categoria>) categoriadao.findAll();
	}

	@Override
	public Producto guardarProducto(Producto producto, String accion, Double valor) {
		// TODO Auto-generated method stub

		if (accion.equals("crear")) {
			producto.setCant_actual(valor);
			producto.setCant_anterior(valor);
		} else if (accion.equals("actualizar")) {
			producto.setCant_anterior(producto.getCant_anterior());
			producto.setCant_actual(producto.getCant_actual() + valor);

		}
		if (producto.getCant_actual() == 0) {
			producto.setStatus("I");
		} else {
			producto.setStatus("A");
		}
		if(producto.getCodigo()==null) {
			String codigoT = UUID.randomUUID().toString();
			while(productodao != null && !productodao.findByCodigo(codigoT).isEmpty()) {
				codigoT = UUID.randomUUID().toString();
			}
			producto.setCodigo(codigoT);
			
		}
		return productodao.save(producto);
	}

	@Override
	public ImagenProducto GuardarImagen(ImagenProducto imagen) {
		// TODO Auto-generated method stub
		return imagenDAO.save(imagen);
	}

	@Override
	public ImagenProducto BuscarImagen(Long id) {
		// TODO Auto-generated method stub
		return  imagenDAO.findById(id).orElse(null);
	}

	@Override
	public List<ImagenProducto> BuscarImagen(String imagen) {
		// TODO Auto-generated method stub
		return imagenDAO.findByimagen(imagen);
	}

	@Override
	public List<Producto> BuscarProductoCodigo(String codigo) {
		// TODO Auto-generated method stub
		return productodao.findByCodigo(codigo);
	}

	@Override
	public Producto BuscarProductoId(Long id) {
		// TODO Auto-generated method stub
		return productodao.findById(id).orElse(null);
	}

	@Override
	public List<Producto> ListadoProducto() {
		// TODO Auto-generated method stub
		return (List<Producto>) productodao.findAll();
	}

	@Override
	public List<Producto> ListadoProducto(Categoria cat) {
		// TODO Auto-generated method stub
		return (List<Producto>) productodao.findByCat(cat);
	}

	@Override
	public List<Producto> BuscarProductoNombre(String nombre) {
		// TODO Auto-generated method stub
		return (List<Producto>) productodao.findByNombreStartingWith(nombre);
	}

	@Override
	public void EliminarImagen(ImagenProducto imagen) {
		imagenDAO.delete(imagen);
		
	}

	@Override
	public Cliente guardarCliente(Cliente save) {
		// TODO Auto-generated method stub
		return clienteDAO.save(save);
	}

	@Override
	public List<Cliente> BuscarCedulaCliente(String cedula) {
		// TODO Auto-generated method stub
		return clienteDAO.findByRif(cedula);
	}

	@Override
	public List<Cliente> listadoCliente() {
		// TODO Auto-generated method stub
		return (List<Cliente>)clienteDAO.findAll();
	}

	@Override
	public List<Producto> buscarCategoriaYStartNombre(Categoria cat, String nombre) {
		// TODO Auto-generated method stub
		return productodao.findByCatAndNombreStartingWith(cat, nombre);
	}

}
