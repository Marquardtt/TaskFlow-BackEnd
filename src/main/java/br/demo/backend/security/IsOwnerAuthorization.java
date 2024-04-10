package br.demo.backend.security;

import br.demo.backend.model.Group;
import br.demo.backend.model.Project;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.security.entity.UserDatailEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@AllArgsConstructor
public class IsOwnerAuthorization implements AuthorizationManager<RequestAuthorizationContext> {
    private final ProjectRepository projectRepository;
    private final GroupRepository groupRepository;

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> suplier, RequestAuthorizationContext object) {
        UserDatailEntity userDatailEntity = (UserDatailEntity) suplier.get().getPrincipal();
        boolean decision = false;

        if (object.getRequest().getRequestURI().contains("/group")) {
            Group group = groupRepository.findById(Long.parseLong(object.getVariables().get("groupId"))).get();
            if (group.getOwner().equals(userDatailEntity)){
                decision = true;
            }
        } else {
            String projectId =  object.getVariables().get("projectId");
            decision = isOwner(projectId, userDatailEntity);
        }
        return new AuthorizationDecision(decision);
    }

    public boolean isOwner(String projectId, UserDatailEntity userDatailEntity) {
        Project project = projectRepository.findById(Long.parseLong(projectId)).get();
        boolean decision = false;
        if (project.getOwner().equals(userDatailEntity.getUser())) {
            decision = true;
        }
        return decision;
    }
}
