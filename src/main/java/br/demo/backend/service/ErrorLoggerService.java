package br.demo.backend.service;

import br.demo.backend.model.ErrorLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ErrorLoggerService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void logException(Exception exception) {
        entityManager.persist(new ErrorLog(exception));
    }

}
