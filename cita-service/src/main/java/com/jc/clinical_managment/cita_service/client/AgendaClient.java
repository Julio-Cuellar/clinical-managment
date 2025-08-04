package com.jc.clinical_managment.cita_service.client;


import com.jc.clinical_managment.cita_service.dto.AgendaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "agenda-service", url = "http://localhost:8083")
public interface AgendaClient {
    @GetMapping("/api/agendas/{id}")
    AgendaDTO obtenerAgendaPorId(@PathVariable("id") Long id);
}