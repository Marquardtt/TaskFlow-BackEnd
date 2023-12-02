package br.demo.backend.service.properties;

import br.demo.backend.model.Project;
import br.demo.backend.model.enums.TypeOfProperty;
import br.demo.backend.model.properties.Property;
import br.demo.backend.model.properties.Select;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.properties.SelectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class SelectService {

    SelectRepository selectRepository;
    ProjectRepository projectRepository;

    public Collection<Select> findAll() {
        return selectRepository.findAll();
    }

    public Select findOne(Long id) {
        return selectRepository.findById(id).get();
    }

    public void save(Select select) {
        selectRepository.save(select);
    }

    public void delete(Long id) {
        Select select = selectRepository.findById(id).get();
        Integer count  = select.getPage() == null ?
                countSelectsOnProject(projectRepository.findByPropertiesContaining(select)) :
                selectRepository.countByPage(select.getPage());
        if(count > 1){
            selectRepository.deleteById(id);
        }else{
            return;
//            Erro de apagar select
        }
    }

    private Integer countSelectsOnProject(Project p){
        Integer count = 0;
        for(Property prop : p.getProperties()){
            if(prop.getType() == TypeOfProperty.SELECT){
                count++;
            }
        }
        return count;
    }
}
