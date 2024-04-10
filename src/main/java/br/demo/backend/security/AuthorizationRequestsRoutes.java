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

import java.util.Map;
import java.util.function.Supplier;

@Component
@AllArgsConstructor
public class AuthorizationRequestsRoutes implements AuthorizationManager<RequestAuthorizationContext> {

    ProjectRepository projectRepository;

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> supplier, RequestAuthorizationContext object) {

        Authentication authentication = supplier.get();
        UserDatailEntity userDatailEntity = (UserDatailEntity) authentication.getPrincipal();
        Map<String, String> variables = object.getVariables();
        long projectId = Long.parseLong(variables.get("projectId"));
        Project project = projectRepository.findById(projectId).get();
        boolean decision = false;
        if (!project.getOwner().equals(((UserDatailEntity) authentication.getPrincipal()).getUser())) {
            if (!object.getRequest().getRequestURI().contains("picture")) {
                if (userDatailEntity.getAuthorities() != null) {
                    for (GrantedAuthority simple :
                            userDatailEntity.getAuthorities()) {
                        if (!(object.getRequest().getRequestURI().contains("redo") && simple.getAuthority().contains("Project_" + projectId + "_DELETE"))) {  // verifica se for uma task e se for o método redo, pois o mesmo precisa da permissão DELETE para realizar o redo
                            if ((("Project_" + projectId + "_").contains(simple.getAuthority()) && (object.getRequest().getMethod()).contains(simple.getAuthority()))) {
                                decision = true;
                                break;
                            }
                        }else {
                            decision = true;
                        }
                    }
                }
            }
        } else {
            System.out.println("OWNER");
            decision = true;
        }
        return new AuthorizationDecision(decision);
    }
}
