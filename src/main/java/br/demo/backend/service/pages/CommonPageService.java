package br.demo.backend.service.pages;


import br.demo.backend.model.pages.CommonPage;
import br.demo.backend.repository.pages.CommonPageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class CommonPageService {

    private CommonPageRepository commonPageRepository;

    public Collection<CommonPage> findAll() {
        return commonPageRepository.findAll();
    }

    public CommonPage findOne(Long id) {
        return commonPageRepository.findById(id).get();
    }

    public void save(CommonPage canvasModel) {
        commonPageRepository.save(canvasModel);
    }

    public void delete(Long id) {
        commonPageRepository.deleteById(id);
    }
}
