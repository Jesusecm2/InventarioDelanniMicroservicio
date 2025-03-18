package com.microservices.delanni.inventory.app.service;

import java.util.LinkedHashMap;
import java.util.Map;

public interface ICalculoService {

	
	
	public LinkedHashMap<String, String> obtenerValoresSemana();
	
	public LinkedHashMap<String, String> obtenerValoresMes();
	
	public LinkedHashMap<String, String> obtenerValoresAnual();
}
