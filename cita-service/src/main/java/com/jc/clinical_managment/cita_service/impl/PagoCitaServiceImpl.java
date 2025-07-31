package com.jc.clinical_managment.cita_service.impl;

import com.jc.clinical_managment.cita_service.domain.Cita;
import com.jc.clinical_managment.cita_service.domain.PagoCita;
import com.jc.clinical_managment.cita_service.domain.StatusCobro;
import com.jc.clinical_managment.cita_service.dto.PagoCitaRequestDTO;
import com.jc.clinical_managment.cita_service.dto.PagoCitaResponseDTO;
import com.jc.clinical_managment.cita_service.repository.CitaRepository;
import com.jc.clinical_managment.cita_service.repository.PagoCitaRepository;
import com.jc.clinical_managment.cita_service.service.PagoCitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PagoCitaServiceImpl implements PagoCitaService {

    @Autowired
    private PagoCitaRepository pagoCitaRepository;

    @Autowired
    private CitaRepository citaRepository;

    private PagoCita toEntity(PagoCitaRequestDTO dto) {
        return PagoCita.builder()
                .citaId(dto.getCitaId())
                .monto(dto.getMonto())
                .metodoPago(dto.getMetodoPago())
                .referencia(dto.getReferencia())
                .observaciones(dto.getObservaciones())
                .build();
    }

    private PagoCitaResponseDTO toResponseDTO(PagoCita pagoCita) {
        return PagoCitaResponseDTO.builder()
                .id(pagoCita.getId())
                .citaId(pagoCita.getCitaId())
                .monto(pagoCita.getMonto())
                .fechaPago(pagoCita.getFechaPago())
                .metodoPago(pagoCita.getMetodoPago())
                .referencia(pagoCita.getReferencia())
                .observaciones(pagoCita.getObservaciones())
                .build();
    }

    @Override
    public PagoCitaResponseDTO registrarPago(PagoCitaRequestDTO dto) {
        PagoCita pagoCita = toEntity(dto);
        pagoCita.setFechaPago(new Date());
        PagoCita pagoGuardado = pagoCitaRepository.save(pagoCita);
        actualizarStatusCobro(pagoCita.getCitaId());
        return toResponseDTO(pagoGuardado);
    }

    @Override
    public List<PagoCitaResponseDTO> obtenerPagosPorCitaId(Long citaId) {
        return pagoCitaRepository.findByCitaId(citaId).stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public double obtenerTotalPagadoPorCita(Long citaId) {
        return pagoCitaRepository.findByCitaId(citaId)
                .stream()
                .mapToDouble(PagoCita::getMonto)
                .sum();
    }

    @Override
    public Optional<PagoCitaResponseDTO> obtenerPagoPorId(Long id) {
        return pagoCitaRepository.findById(id).map(this::toResponseDTO);
    }

    @Override
    public List<PagoCitaResponseDTO> listarTodosLosPagos() {
        return pagoCitaRepository.findAll().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<PagoCitaResponseDTO> listarPagosPorFecha(Date fecha) {
        return pagoCitaRepository.findByFechaPago(fecha).stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<PagoCitaResponseDTO> listarPagosPorRangoFechas(Date inicio, Date fin) {
        return pagoCitaRepository.findByFechaPagoBetween(inicio, fin).stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public List<PagoCitaResponseDTO> listarPagosPorMetodo(String metodoPago) {
        return pagoCitaRepository.findByMetodoPago(metodoPago).stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public void eliminarPago(Long id) {
        pagoCitaRepository.deleteById(id);
    }

    @Override
    public PagoCitaResponseDTO actualizarPago(Long id, PagoCitaRequestDTO dto) {
        return pagoCitaRepository.findById(id).map(existing -> {
            PagoCita updated = toEntity(dto);
            updated.setId(id);
            updated.setFechaPago(existing.getFechaPago());
            updated.setCitaId(existing.getCitaId());
            PagoCita saved = pagoCitaRepository.save(updated);
            actualizarStatusCobro(saved.getCitaId());
            return toResponseDTO(saved);
        }).orElseThrow(() -> new RuntimeException("Pago no encontrado"));
    }

    private void actualizarStatusCobro(Long citaId) {
        Optional<Cita> optionalCita = citaRepository.findById(citaId);
        if (optionalCita.isPresent()) {
            Cita cita = optionalCita.get();
            double totalPagado = obtenerTotalPagadoPorCita(citaId);
            StatusCobro nuevoStatus;
            if (totalPagado >= cita.getCostoCita()) {
                nuevoStatus = StatusCobro.PAGADO;
            } else if (totalPagado > 0) {
                nuevoStatus = StatusCobro.PAGO_PARCIAL;
            } else {
                nuevoStatus = StatusCobro.PENDIENTE;
            }
            cita.setStatusCobro(nuevoStatus);
            cita.setFechaActualizacion(new Date());
            citaRepository.save(cita);
        }
    }
}