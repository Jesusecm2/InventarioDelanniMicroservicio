package com.microservices.delanni.inventory.app.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.delanni.inventory.app.clients.AuditoriaClients;
import com.microservices.delanni.inventory.app.service.ICalculoService;
import com.microservices.delanni.inventory.app.service.TransaccionesService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/graficos")
public class DinamicoController {

	@Autowired
	private ICalculoService calcService;
	
	@Autowired
	private AuditoriaClients clienteAuditoria;

	@GetMapping("/trans/resumen")
	public ResponseEntity<?> obtenerValoresSemana(HttpServletRequest request,@RequestParam int tipo) {
		enviarsecurity(request, "Path");
		switch(tipo) {
		case 1:
			return new ResponseEntity<>(calcService.obtenerValoresSemana(), HttpStatus.OK);
			
		case 2:	
			return new ResponseEntity<>(calcService.obtenerValoresMes(), HttpStatus.OK);
			
		case 3:
			return new ResponseEntity<>(calcService.obtenerValoresAnual(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		
	}
	
	private void enviarsecurity(HttpServletRequest request,String obj) {
		Map<String, String> values = new HashMap<>();
		values.put("ip", request.getHeader("ip"));
		values.put("route", request.getHeader("path"));
		values.put("system", request.getHeader("system"));
		values.put("entity", obj);
		values.put("provider", request.getHeader("provider"));
		try {
			clienteAuditoria.guardarAudit(values);	
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
