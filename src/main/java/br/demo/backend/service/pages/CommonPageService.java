package br.demo.backend.service.pages;


import br.demo.backend.model.pages.CommonPage;
import br.demo.backend.model.properties.Option;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.model.relations.TaskValue;
import br.demo.backend.repository.pages.CommonPageRepository;
import br.demo.backend.service.ResolveStackOverflow;
import br.demo.backend.service.tasks.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class CommonPageService {

    private CommonPageRepository commonPageRepository;
    private TaskService taskService;

    public Collection<CommonPage> findAll() {
        Collection<CommonPage> commonPages = commonPageRepository.findAll();
        for (CommonPage commonPage : commonPages) {
            ResolveStackOverflow.resolveStackOverflow(commonPage);
        }
        return commonPages;
    }

    public CommonPage findOne(Long id) {
        CommonPage commonPage = commonPageRepository.findById(id).get();
        ResolveStackOverflow.resolveStackOverflow(commonPage);
        return commonPage;
    }

    public CommonPage updateIndexes(CommonPage page, Long taskId, Integer index) {
        Integer oldIndex = 0;
        Option columnOption = null;
        for (TaskPage task : page.getTasks()) {
            if (task.getTask().getId().equals(taskId)) {
                oldIndex = task.getIndexAtColumn();
                for (TaskValue taskVl : task.getTask().getProperties()) {
                    if (taskVl.getProperty().getId().equals(page.getPropertyOrdering().getId())) {
                        columnOption = (Option) taskVl.getValue().getValue();
                    }
                }
                task.setIndexAtColumn(index);
            }
        }
        for (TaskPage task : page.getTasks()) {
            for (TaskValue taskVl : task.getTask().getProperties()) {
                if (taskVl.getProperty().getId().equals(page.getPropertyOrdering().getId())) {
                    if (((Option)taskVl.getValue().getValue()).getId().equals(columnOption.getId())) {
                         if (oldIndex < index && task.getIndexAtColumn() <= index && task.getIndexAtColumn() > oldIndex &&  !task.getTask().getId().equals(taskId)) {
                            task.setIndexAtColumn(index - 1);
                        } else if (oldIndex > index && task.getIndexAtColumn() >= index && !task.getTask().getId().equals(taskId)) {
                            task.setIndexAtColumn(index + 1);
                        }
                    }
                }
            }
        }
        CommonPage updatedPage = commonPageRepository.save(page);
        ResolveStackOverflow.resolveStackOverflow(updatedPage);
        return updatedPage;
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
