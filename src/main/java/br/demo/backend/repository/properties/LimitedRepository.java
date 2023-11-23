package br.demo.backend.repository.properties;

import br.demo.backend.model.properties.Limited;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LimitedRepository extends JpaRepository<Limited, Long> {
}
