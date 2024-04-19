package br.demo.backend.utils;

import br.demo.backend.exception.IdGroupDontMatchException;
import br.demo.backend.interfaces.IValidationId;
import br.demo.backend.model.Group;
import jdk.jfr.Category;
import org.springframework.stereotype.Component;

@Component
public class IdGroupValidation implements IValidationId<Group,Long> {
    public void ofObject(Long idGroup, Group project){
        if(!idGroup.equals(project.getId())){
            throw new IdGroupDontMatchException();
        }
    }

    public void of(Long idGroup, Long projectId){
        if(!idGroup.equals(projectId)){
            throw new IdGroupDontMatchException();
        }
    }
}
