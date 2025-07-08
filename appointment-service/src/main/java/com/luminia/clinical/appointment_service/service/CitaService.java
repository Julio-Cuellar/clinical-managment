package com.luminia.clinical.appointment_service.service;

import com.luminia.clinical.appointment_service.model.Cita;
import com.luminia.clinical.appointment_service.model.EstadoCita;
import com.luminia.clinical.appointment_service.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepo;

    public Cita crearCita(Cita cita) {
        cita.setEstado(EstadoCita.SOLICITADA);
        return citaRepo.save(cita);
    }

    public void actualizarEstado(UUID id, EstadoCita estado) {
        var cita = citaRepo.findById(id).orElseThrow();
        cita.setEstado(estado);
        citaRepo.save(cita);
    }

    public void registrarPago(UUID id, BigDecimal pago) {
        var cita = citaRepo.findById(id).orElseThrow();
        cita.setMontoPagado(pago);
        if (pago.compareTo(cita.getMontoTotal().multiply(BigDecimal.valueOf(0.5))) >= 0) {
            cita.setEstado(EstadoCita.CONFIRMADA);
        } else {
            cita.setEstado(EstadoCita.PENDIENTE_PAGO);
        }
        citaRepo.save(cita);
    }

    public List<Cita> obtenerCitasPorMedico(String medicoId) {
        return citaRepo.findByMedicoId(medicoId);
    }
}
