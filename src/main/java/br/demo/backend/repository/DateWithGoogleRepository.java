package br.demo.backend.repository;

import br.demo.backend.model.values.DateWithGoogle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DateWithGoogleRepository extends JpaRepository<DateWithGoogle, Long> {
}
