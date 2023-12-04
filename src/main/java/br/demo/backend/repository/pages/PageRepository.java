package br.demo.backend.repository.pages;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {

    public Page findByPropertiesContaining(Property p);
}
