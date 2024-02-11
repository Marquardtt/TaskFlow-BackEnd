package br.demo.backend.repository.chat;

import br.demo.backend.model.chat.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
}
