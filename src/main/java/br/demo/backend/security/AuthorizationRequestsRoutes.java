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
        boolean decision = false;
        if (!object.getRequest().getRequestURI().contains("picture")) {
            if (userDatailEntity.getAuthorities() != null) {
                for (GrantedAuthority simple :
                        userDatailEntity.getAuthorities()) {
                    if (!(object.getRequest().getRequestURI().contains("redo"))) {  // verifica se for uma task e se for o método redo, pois o mesmo precisa da permissão DELETE para realizar o redo
                        if ((simple.getAuthority().contains(("Project_" + projectId + "_")) && (simple.getAuthority()).contains(object.getRequest().getMethod()))) {
                            decision = true;
                            break;
                        }
                    } else if (simple.getAuthority().contains("Project_" + projectId) && simple.getAuthority().contains("DELETE")) {
                        decision = true;
                        break;
                    }
                }
            }
        }
        return new AuthorizationDecision(decision);
    }
}
