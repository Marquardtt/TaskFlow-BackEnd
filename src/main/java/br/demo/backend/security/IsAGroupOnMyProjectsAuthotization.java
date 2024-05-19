package br.demo.backend.security;

import br.demo.backend.model.Group;
import br.demo.backend.model.Project;
import br.demo.backend.model.dtos.project.SimpleProjectGetDTO;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.security.entity.UserDatailEntity;
import br.demo.backend.service.GroupService;
import br.demo.backend.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

@Component
@AllArgsConstructor
public class IsAGroupOnMyProjectsAuthotization implements AuthorizationManager<RequestAuthorizationContext> {
    private final GroupRepository groupRepository;
    private final ProjectService projectService;

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    @Override
    @Transactional
    public AuthorizationDecision check(Supplier<Authentication> suplier, RequestAuthorizationContext object) {

        Long groupId = Long.parseLong(object.getVariables().get("groupId"));
        Group group = groupRepository.findById(groupId).get();
        Collection<SimpleProjectGetDTO> projects = projectService.finAllOfAUser();
        return new AuthorizationDecision(group.getPermissions().stream().anyMatch(
                permission -> projects.stream().anyMatch(
                        project -> project.getId().equals(permission.getProject().getId())
                )
        ));
    }
}
