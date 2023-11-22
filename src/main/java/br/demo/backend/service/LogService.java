package br.demo.backend.service;


import br.demo.backend.model.Log;
import br.demo.backend.repository.LogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class LogService {

    LogRepository logRepository;

    public Collection<Log> findAll() {
        return logRepository.findAll();
    }

    public Log findOne(Long id) {
        return logRepository.findById(id).get();
    }

    public void save(Log logModel) {
        logRepository.save(logModel);
    }

    public void delete(Long id) {
        logRepository.deleteById(id);
    }
}
