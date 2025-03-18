package com.microservices.delanni.inventory.app.clients;

import java.io.IOException;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "servicio-archivos")
public interface FileClients {

	
	@GetMapping("/archivos/consultar/imagen")
	public String obtenerImagen(@RequestParam String path) ;
	
	@PostMapping("/archivos/guardar/imagen")
	public String copy(String file,@RequestParam String servicename) throws IOException;
	
	
	@PostMapping("/archivos/eliminar/imagen")
	public ResponseEntity<?> deleteImage(@RequestBody String imagen) ;
	
	
	@GetMapping(value = "/archivos/consultar/imagen/stream")
	public byte[] obtenerImagenStream(@RequestParam String path);
}
