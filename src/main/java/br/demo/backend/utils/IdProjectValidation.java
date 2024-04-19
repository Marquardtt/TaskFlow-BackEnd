package br.demo.backend.utils;

import br.demo.backend.exception.IdProjectDontMatchException;
import br.demo.backend.interfaces.IValidationId;
import br.demo.backend.model.Project;
import org.springframework.stereotype.Component;

@Component
public class IdProjectValidation  implements IValidationId<Project,Long> {

    public void ofObject(Long idProject, Project project){
        if(!idProject.equals(project.getId())){
            throw new IdProjectDontMatchException();
        }
    }

    public void of(Long idProject, Long projectId){
        if(!idProject.equals(projectId)){
            throw new IdProjectDontMatchException();
        }
    }
}
