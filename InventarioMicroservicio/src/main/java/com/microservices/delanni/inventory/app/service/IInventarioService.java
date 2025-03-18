package com.microservices.delanni.inventory.app.service;

import java.util.List;

import com.microservices.delanni.inventory.app.entity.Categoria;
import com.microservices.delanni.inventory.app.entity.Cliente;
import com.microservices.delanni.inventory.app.entity.ImagenProducto;
import com.microservices.delanni.inventory.app.entity.Producto;

public interface IInventarioService {

	
	//*****************Funciones Categoría
	public Categoria GuardarCategoria(Categoria categoria);
	
	public Categoria ObtenerCategoriaId(Categoria categoria);
	
	public List<Categoria> ListadoCategoria();
	
	//**************
	
	public Producto guardarProducto(Producto producto,String accion,Double valor);
	
	public List<Producto> BuscarProductoCodigo(String codigo);
	
	public List<Producto> ListadoProducto();
	
	public List<Producto> ListadoProducto(Categoria cat);
	
	public Producto BuscarProductoId(Long id);
	
	public List<Producto> BuscarProductoNombre(String nombre);
	
	public List<Producto> buscarCategoriaYStartNombre(Categoria cat,String nombre);
	
	//****************
	
	public ImagenProducto BuscarImagen(Long id);
	
	public ImagenProducto GuardarImagen(ImagenProducto imagen);
	
	public void EliminarImagen(ImagenProducto imagen);
	
	public List<ImagenProducto> BuscarImagen(String imagen);
	
	
	//**********************
	
	public Cliente guardarCliente(Cliente save);
	
	public List<Cliente> BuscarCedulaCliente(String cedula);
	
	public List<Cliente> listadoCliente();
	
	
}
