package com.jc.clinical_managment.consultorio_service.client;

import com.jc.clinical_managment.consultorio_service.dto.AgendaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "agenda-service", configuration = FeignClientConfig.class)
public interface AgendaServiceClient {
    
    @PostMapping("/api/agendas")
    AgendaDTO crearAgenda(@RequestBody AgendaDTO agendaDTO, @RequestHeader("Authorization") String token);
}