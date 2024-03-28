package br.demo.backend.security;

import br.demo.backend.model.Project;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.security.entity.UserDatailEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@AllArgsConstructor
public class IsOwnerOrMemberAuthorization implements AuthorizationManager<RequestAuthorizationContext> {
    private final ProjectRepository projectRepository;
    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> suplier, RequestAuthorizationContext object) {
        UserDatailEntity userDatailEntity = (UserDatailEntity) suplier.get().getPrincipal();
        String projectId = object.getRequest().getParameter("projectId");
        Project project = projectRepository.findById(Long.parseLong(projectId)).get();
        boolean decision = false;

            if (!project.getOwner().equals(userDatailEntity.getUser())) {
                for (GrantedAuthority simple :
                        userDatailEntity.getAuthorities()) {
                    if (("Project_" + projectId + "_").contains(simple.getAuthority())) {
                        decision = true;
                        break;
                    }
                }
            } else {
                decision = true;
            }
        return new AuthorizationDecision(decision);
    }
}
