package br.demo.backend.service.pages;


import br.demo.backend.model.pages.CommonPage;
import br.demo.backend.model.properties.Option;
import br.demo.backend.model.relations.TaskPage;
import br.demo.backend.model.relations.TaskValue;
import br.demo.backend.repository.pages.CommonPageRepository;
import br.demo.backend.globalfunctions.AutoMapper;
import br.demo.backend.globalfunctions.ResolveStackOverflow;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class CommonPageService {

    private CommonPageRepository commonPageRepository;
    private AutoMapper<CommonPage> autoMapper;

    public Collection<CommonPage> findAll() {
        Collection<CommonPage> commonPages = commonPageRepository.findAll();
        return commonPages.stream().map(c -> (CommonPage) ResolveStackOverflow.resolveStackOverflow(c)).toList();

    }

    public CommonPage findOne(Long id) {
        CommonPage commonPage = commonPageRepository.findById(id).get();
        return (CommonPage) ResolveStackOverflow.resolveStackOverflow(commonPage);
    }

    public CommonPage updateIndexes(CommonPage page, Long taskId, Integer index, Integer columnChaged) {
        TaskPage taskOld = page.getTasks().stream().filter(task ->
                task.getTask().getId().equals(taskId)).findFirst().get();
        Option columnOption = (Option) taskOld.getTask().getProperties().stream().filter(p ->
                p.getProperty().equals(page.getPropertyOrdering())).findFirst().get().getValue().getValue();

        page.setTasks(page.getTasks().stream().map(t -> {
            TaskValue prop = t.getTask().getProperties().stream().filter(p ->
                    p.getProperty().equals(page.getPropertyOrdering()) &&
                            (p.getValue().getValue() == null && columnOption == null ||
                                    ((Option) p.getValue().getValue()).getId().equals(columnOption.getId()))
            ).findFirst().orElse(null);
            if (prop == null) return t;
            boolean verification = taskOld.getIndexAtColumn() > index || columnChaged == 1;
            if (t.equals(taskOld)) {
                t.setIndexAtColumn(index + (verification ? 0 : 1));
            } else {
                if ((t.getIndexAtColumn() >= index && verification) || t.getIndexAtColumn() > index) {
                    t.setIndexAtColumn(t.getIndexAtColumn() + 1);
                }
            }
            return t;
        }).toList());
       return (CommonPage)ResolveStackOverflow.resolveStackOverflow(commonPageRepository.save(page));
    }

    public CommonPage updateIndexes(CommonPage page, Long taskId, Integer index) {
        TaskPage taskOld = page.getTasks().stream().filter(task ->
                task.getTask().getId().equals(taskId)).findFirst().get();
        page.setTasks(page.getTasks().stream().map(t -> {
            boolean verification = taskOld.getIndexAtColumn() > index;
            if (t.equals(taskOld)) {
                t.setIndexAtColumn(index + (verification ? 0 : 1));
            } else {
                if ((t.getIndexAtColumn() >= index && verification) || t.getIndexAtColumn() > index) {
                    t.setIndexAtColumn(t.getIndexAtColumn() + 1);
                }
            }
            return t;
        }).toList());
        return (CommonPage) ResolveStackOverflow.resolveStackOverflow(commonPageRepository.save(page));
    }


    public void update(CommonPage commonPageDto, Boolean patching) {
        CommonPage commonPage = patching ? commonPageRepository.findById(commonPageDto.getId()).get() : new CommonPage();
        autoMapper.map(commonPageDto, commonPage, patching);
        commonPageRepository.save(commonPage);
    }

    public void save(CommonPage canvasModel) {
        commonPageRepository.save(canvasModel);
    }

    public void delete(Long id) {
        commonPageRepository.deleteById(id);
    }
}
