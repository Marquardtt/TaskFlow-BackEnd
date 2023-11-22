package br.demo.backend.repository.properties;

import br.demo.backend.model.properties.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends JpaRepository<Date, Long> {
}
