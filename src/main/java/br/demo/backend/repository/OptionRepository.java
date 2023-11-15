package br.demo.backend.repository;

import br.demo.backend.model.OptionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<OptionModel, Long> {
}
