package br.demo.backend.security;

import br.demo.backend.model.Group;
import br.demo.backend.model.User;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.security.entity.UserDatailEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

@Component
@AllArgsConstructor
public class ProjectOrGroupAuthorization implements AuthorizationManager<RequestAuthorizationContext> {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final ProjectRepository projectRepository;

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> supplier, RequestAuthorizationContext object) {
        String username = object.getRequest().getParameter("username");
        User userFind = userRepository.findByUserDetailsEntity_Username(username).get();
        User user = userRepository.findByUserDetailsEntity_Username(
                ((UserDatailEntity) supplier.get().getPrincipal()).getUsername()).get();
        List<Group> groupList = groupRepository.findAll();
        boolean decision = false;
        for (Group group : groupList) {
            if (group.getUsers().containsAll(List.of(user,userFind))){
                decision = true ;
            } else if () {
                
            }
        }


        return;
    }
}
