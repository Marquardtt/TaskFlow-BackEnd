package br.demo.backend.repository;

import br.demo.backend.model.PropertyModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRespository extends JpaRepository<PropertyModel, Long> {
}
