package br.demo.backend.repository;

import br.demo.backend.model.Project;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
@Repository

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByPropertiesContaining(Property p);
    Project findByPagesContaining(Page p);

    Collection<Project> findProjectsByOwner_Id(Long id);
}