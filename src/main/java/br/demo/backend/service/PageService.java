package br.demo.backend.service;


import br.demo.backend.model.PageModel;
import br.demo.backend.repository.PageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class PageService {

    PageRepository pageRepository;

    public Collection<PageModel> findAll() {
        return pageRepository.findAll();
    }

    public PageModel findOne(Long id) {
        return pageRepository.findById(id).get();
    }

    public void save(PageModel pageModel) {
        pageRepository.save(pageModel);
    }

    public void delete(Long id) {
        pageRepository.deleteById(id);
    }
}
