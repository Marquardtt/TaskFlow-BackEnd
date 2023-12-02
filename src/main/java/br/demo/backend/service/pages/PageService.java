package br.demo.backend.service.pages;


import br.demo.backend.model.Project;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.pages.PagePostDTO;
import br.demo.backend.model.properties.Property;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.pages.PageRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

@Service
@AllArgsConstructor
public class PageService {

    PageRepository pageRepository;
    ProjectRepository projectRepository;

    public Collection<Page> findAll() {
        return pageRepository.findAll();
    }

    public Page findOne(Long id) {
        return pageRepository.findById(id).get();
    }

    public void save(PagePostDTO pagePostDTO) {
        Project project = projectRepository.findById(pagePostDTO.getProject().getId()).get();
        ArrayList<Property> properties = new ArrayList<>(project.getProperties());
        Page page = new Page();
        BeanUtils.copyProperties(pagePostDTO, page);
        page.setPropertyOrdering(properties.get(0));
        pageRepository.save(page);
    }
    public void update(Page page) {
        pageRepository.save(page);
    }

    public void delete(Long id) {
        pageRepository.deleteById(id);
    }
}
