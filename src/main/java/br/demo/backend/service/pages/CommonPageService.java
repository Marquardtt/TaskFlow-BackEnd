package br.demo.backend.service.pages;


import br.demo.backend.model.Project;
import br.demo.backend.model.pages.CommonPage;
import br.demo.backend.model.pages.CommonPage;
import br.demo.backend.model.pages.Page;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.relations.TaskCanvas;
import br.demo.backend.model.tasks.Task;
import br.demo.backend.repository.pages.CommonPageRepository;
import br.demo.backend.service.properties.PropertyService;
import br.demo.backend.service.tasks.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class CommonPageService {

    private CommonPageRepository commonPageRepository;
    private TaskService taskService;

    public void resolveStackOverflow(CommonPage commonPage) {
        for (Property property : commonPage.getProperties()) {
            property.setPages(null);
            property.setProject(null);
        }
        Project project = commonPage.getProject();
        for(Task task : commonPage.getTasks()) {
            taskService.resolveStackOverflow(task);
        }
        try{
            for (Page page : project.getPages()) {
                page.setProject(null);
            }
        }catch (NullPointerException ignored){
        }
    }
    public Collection<CommonPage> findAll() {
        Collection<CommonPage> commonPages =  commonPageRepository.findAll();
        for(CommonPage commonPage : commonPages) {
            resolveStackOverflow(commonPage);
        }
        return commonPages;
    }

    public CommonPage findOne(Long id) {
        CommonPage commonPage = commonPageRepository.findById(id).get();
        resolveStackOverflow(commonPage);
        return commonPage;
    }
    public void update(CommonPage commonPage) {
        commonPageRepository.save(commonPage);
    }
    public void save(CommonPage canvasModel) {
        commonPageRepository.save(canvasModel);
    }

    public void delete(Long id) {
        commonPageRepository.deleteById(id);
    }
}
