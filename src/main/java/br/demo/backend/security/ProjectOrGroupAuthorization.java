package br.demo.backend.security;

import br.demo.backend.model.Group;
import br.demo.backend.model.Permission;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.security.entity.UserDatailEntity;
import br.demo.backend.security.utils.GetHisProjects;
import br.demo.backend.service.ProjectService;
import br.demo.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Component
@AllArgsConstructor
public class ProjectOrGroupAuthorization implements AuthorizationManager<RequestAuthorizationContext> {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> supplier, RequestAuthorizationContext object) {
        User user = userRepository.findByUserDetailsEntity_Username(((UserDatailEntity)supplier.get().getPrincipal()).getUsername()).get();
        Map<String, String> variables = object.getVariables();
        Long otherUserId = Long.parseLong(variables.get("userId"));
        User otherUser = userRepository.findById(otherUserId).get();

        Collection<Group> groups = groupRepository.findGroupsByOwnerOrUsersContaining(user, user);
        //se ambos estao num mesmo grupo
        boolean decision = groups.stream().anyMatch(group -> groupTest(otherUser, group));

        if (!decision){
            Collection<Project> projects =  GetHisProjects.getHisProjects(user);
            Collection<Project> otherProjects = GetHisProjects.getHisProjects(otherUser);
            decision = projects.stream().anyMatch(otherProjects::contains);
        }


        return new AuthorizationDecision(decision);
    }

    private boolean groupTest(User user, Group group) {
        return group.getOwner().equals(user) || group.getUsers().contains(user);
    }
}
