package com.microservices.delanni.inventory.app.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservices.delanni.inventory.app.entity.TpIngreso;
import com.microservices.delanni.inventory.app.entity.Transacciones;

@Service
public class CalculoServiceImpl implements ICalculoService {

	@Autowired
	private TransaccionesService transService;

	@Autowired
	private IPagosService pagoService;

	@Override
	public LinkedHashMap<String, String> obtenerValoresSemana() {
		LinkedHashMap<String, String> valores = new LinkedHashMap<>();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -6);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		//calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Calendar next = (Calendar) calendar.clone();
		next.add(Calendar.DATE, 1);
		for (int i = 0; i < 7; i++) {
			Double obtenido = 0.0;
			Date iter = calendar.getTime();
			
			Date sig = next.getTime();
			// ****************************Realizar busqueda
			List<Transacciones> dia = transService.obtenerTransacciones(iter, sig);
			// ****************************Calcular
			obtenido = calcular(dia);
			System.out.println("Día:"+calendar.get(Calendar.DAY_OF_WEEK)+"/ Y Dia del mes: "+calendar.get(Calendar.DATE)+"/ Obtenido:"+obtenido);
			//valores.put(obtenerDiaSemana(calendar.get(calendar.DAY_OF_WEEK)), String.format("%.2f", obtenido));
			valores.put(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()), String.format("%.2f", obtenido));
			calendar.add(Calendar.DATE, 1);
			next.add(Calendar.DATE, 1);
		}

		return valores;
	}

	private Double calcular(List<Transacciones> listado) {
		Double up = 0.0;
		Double down = 0.0;
		if (!listado.isEmpty()) {
			for (Transacciones e : listado) {
				// *******************************Ingreso
				//System.out.println("Procesando:"+e.getId()+"/" +e.getAccion());
				if (e.getPago() != null) {
						System.out.println(e.getAccion());
					if (e.getTpIngreso() != null) {
						if (e.getTpIngreso().getTipo().equals("E")) {
							down += e.getPago().getMonto();
						} else {
							up += e.getPago().getMonto();
						}
					}
					// *******************************FIN Ingreso
					if(e.getFactura()!=null) {
						if(e.getFactura().getIdProveedor()!=null) {
							down += e.getPago().getMonto();
						}else {
							up += e.getPago().getMonto();
						}
					}

				}
				
				
			}

		}
		return up-down;

	}	
	 

	@Override
	public LinkedHashMap<String, String> obtenerValoresMes() {
		// TODO Auto-generated method stub
		LinkedHashMap<String, String> valores = new LinkedHashMap<>();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -30);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		//calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Calendar next = (Calendar) calendar.clone();
		next.add(Calendar.DATE, 1);
		for (int i = 0; i < 30; i++) {
			Double obtenido = 0.0;
			Date iter = calendar.getTime();
			
			Date sig = next.getTime();
			// ****************************Realizar busqueda
			List<Transacciones> dia = transService.obtenerTransacciones(iter, sig);
			// ****************************Calcular
			obtenido = calcular(dia);
			System.out.println("Día:"+calendar.getDisplayName(Calendar.DAY_OF_MONTH, Calendar.LONG, Locale.getDefault())+"/ Y Dia del mes: "+calendar.get(Calendar.DATE)+"/ Obtenido:"+obtenido);
			//valores.put(obtenerDiaSemana(calendar.get(calendar.DAY_OF_WEEK)), String.format("%.2f", obtenido));
			
			valores.put(String.valueOf(calendar.get(Calendar.DATE)+"/"+String.valueOf(calendar.get(Calendar.MONTH)+1)), String.format("%.2f", obtenido));
			calendar.add(Calendar.DATE, 1);
			next.add(Calendar.DATE, 1);
		}

		return valores;
	}

	@Override
	public LinkedHashMap<String, String> obtenerValoresAnual() {
			// TODO Auto-generated method stub
			LinkedHashMap<String, String> valores = new LinkedHashMap<>();
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -365);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			//calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			Calendar next = (Calendar) calendar.clone();
			
			next.add(Calendar.DATE, 1);
			for(int i=0;i<=12;i++) {
				Double obtenido = 0.0;
				int mes = calendar.get(Calendar.MONTH);
			while(mes == calendar.get(Calendar.MONTH)) {
				Date iter = calendar.getTime();
				Date sig = next.getTime();
				// ****************************Realizar busqueda
				List<Transacciones> dia = transService.obtenerTransacciones(iter, sig);
				// ****************************Calcular
				obtenido += calcular(dia);
				System.out.println("Procesando:".concat(iter.toString().concat("valor dia: ")+obtenido));
				calendar.add(Calendar.DATE, 1);
				next.add(Calendar.DATE, 1);
			}
			valores.put(String.valueOf(calendar.get(Calendar.MONTH)+1).concat("/").concat(String.valueOf(calendar.get(Calendar.YEAR))), String.format("%.2f", obtenido));
			
			}
			return valores;
		}
	}

