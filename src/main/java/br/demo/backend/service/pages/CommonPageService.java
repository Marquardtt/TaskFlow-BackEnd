package br.demo.backend.service.pages;


import br.demo.backend.model.pages.CommonPage;
import br.demo.backend.model.pages.CommonPage;
import br.demo.backend.model.properties.Property;
import br.demo.backend.repository.pages.CommonPageRepository;
import br.demo.backend.service.properties.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class CommonPageService {

    private CommonPageRepository commonPageRepository;
    private PropertyService propertyService;

    public Collection<CommonPage> findAll() {
        return commonPageRepository.findAll();
    }

    public CommonPage findOne(Long id) {
        return commonPageRepository.findById(id).get();
    }
    public void update(CommonPage commonPage) {
        CommonPage oldCommonPage = commonPageRepository.findById(commonPage.getId()).get();

        for(Property propNew : commonPage.getProperties()){
            if(!testIfAlredyExistsProperty(propNew, oldCommonPage)) {
                propertyService.setRelationsBetweenPropAndTasks(commonPage, propNew);
            }
        }

        commonPageRepository.save(commonPage);
    }
    private Boolean testIfAlredyExistsProperty(Property property, CommonPage commonPage){
        for(Property prop : commonPage.getProperties()){
            if(prop.getId().equals(property.getId())){
                return true;
            }
        }
        return false;
    }
    public void save(CommonPage canvasModel) {
        commonPageRepository.save(canvasModel);
    }

    public void delete(Long id) {
        commonPageRepository.deleteById(id);
    }
}
