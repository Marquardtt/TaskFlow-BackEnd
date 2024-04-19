package br.demo.backend.security.utils;

import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.service.ProjectService;
import br.demo.backend.utils.ModelToGetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class GetHisProjects {
    private static ProjectService projectService;

    @Autowired
    public void setUserRepository(ProjectService repo) {
        GetHisProjects.projectService = repo;
    }

    public static Collection<Project> getHisProjects(User user){
        return projectService.findProjectsByUser(user);
    }



}
