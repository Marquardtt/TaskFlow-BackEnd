package br.demo.backend.service.pages;


import br.demo.backend.model.pages.CommonPage;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.repository.pages.CommonPageRepository;
import br.demo.backend.service.ResolveStackOverflow;
import br.demo.backend.service.tasks.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class CommonPageService {

    private CommonPageRepository commonPageRepository;
    private TaskService taskService;

    public Collection<CommonPage> findAll() {
        Collection<CommonPage> commonPages =  commonPageRepository.findAll();
        for(CommonPage commonPage : commonPages) {
            ResolveStackOverflow.resolveStackOverflow(commonPage);
        }
        return commonPages;
    }

    public CommonPage findOne(Long id) {
        CommonPage commonPage = commonPageRepository.findById(id).get();
        ResolveStackOverflow.resolveStackOverflow(commonPage);
        return commonPage;
    }

    public void updateIndexes(CommonPage page, Long taskId, Integer index) {
        for(TaskPage task : page.getTasks()) {
            if(task.getTask().getId().equals(taskId)) {
                task.setIndexAtColumn(index);
            } else if (task.getIndexAtColumn() >= index) {
                task.setIndexAtColumn(task.getIndexAtColumn() + 1);
            }
        }
        commonPageRepository.save(page);
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
