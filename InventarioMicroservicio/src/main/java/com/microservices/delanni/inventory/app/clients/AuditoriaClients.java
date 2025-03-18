package com.microservices.delanni.inventory.app.clients;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "springboot-servicio-audit")
public interface AuditoriaClients {

	@PostMapping("/auditoria/guardar")
	public ResponseEntity<?> guardarAudit(@RequestBody Map<String, String> values);
}
