package br.demo.backend.service.properties;

import br.demo.backend.model.properties.Time;
import br.demo.backend.repository.TimeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class TimeService {

    TimeRepository timeRepository;

    public Collection<Time> findAll() {
        return timeRepository.findAll();
    }

    public Time findOne(Long id) {
        return timeRepository.findById(id).get();
    }

    public void save(Time time) {
        timeRepository.save(time);
    }

    public void delete(Long id) {
        timeRepository.deleteById(id);
    }
}
