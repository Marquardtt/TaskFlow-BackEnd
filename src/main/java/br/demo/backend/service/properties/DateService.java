package br.demo.backend.service.properties;

import br.demo.backend.model.properties.Date;
import br.demo.backend.repository.properties.DataRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class DateService {

    DataRepository dateRepository;

    public Collection<Date> findAll() {
        return dateRepository.findAll();
    }

    public Date findOne(Long id) {
        return dateRepository.findById(id).get();
    }

    public void save(Date date) {
        dateRepository.save(date);
    }

    public void delete(Long id) {
        dateRepository.deleteById(id);
    }
}
