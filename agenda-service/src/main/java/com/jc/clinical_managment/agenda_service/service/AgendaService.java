package com.jc.clinical_managment.agenda_service.service;

import com.jc.clinical_managment.agenda_service.domain.Agenda;
import com.jc.clinical_managment.agenda_service.repository.AgendaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;

    public AgendaService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    public Agenda crearAgenda(Agenda agenda) {
        return agendaRepository.save(agenda);
    }

    public List<Agenda> listarAgendas() {
        return agendaRepository.findAll();
    }

    public Optional<Agenda> obtenerAgendaPorId(Long id) {
        return agendaRepository.findById(id);
    }

    public List<Agenda> listarAgendasPorUsuario(Long usuarioId) {
        return agendaRepository.findByUsuarioId(usuarioId);
    }

    public void eliminarAgenda(Long id) {
        agendaRepository.deleteById(id);
    }

    public Optional<Agenda> modificarAgenda(Long id, Agenda nuevaAgenda) {
        return agendaRepository.findById(id).map(agendaExistente -> {
            // Actualiza los campos según tu modelo Agenda
            agendaExistente.setNombre(nuevaAgenda.getNombre());
            agendaExistente.setDescripcion(nuevaAgenda.getDescripcion());
            agendaExistente.setUsuarioId(nuevaAgenda.getUsuarioId());
            // Agrega aquí otros campos que quieras actualizar

            Agenda agendaActualizada = agendaRepository.save(agendaExistente);
            return agendaActualizada;
        });
    }

}