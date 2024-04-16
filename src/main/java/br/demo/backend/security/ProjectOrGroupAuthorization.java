package br.demo.backend.security;

import br.demo.backend.model.Group;
import br.demo.backend.model.Permission;
import br.demo.backend.model.Project;
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


        Collection<Project> projects =  user.getPermissions().stream().map(Permission::getProject).toList();
        Collection<Group> groups = groupRepository.findGroupsByOwnerOrUsersContaining(user, user);


        //se ambos estao num mesmo grupo
        boolean decision = groups.stream().anyMatch(group -> groupTest(otherUser, group));

        if (!decision){
            //se o logado esta no projeto do outro
                decision = user.getPermissions().stream().anyMatch(permission -> permission.getProject().getOwner().equals(otherUser));
                if(!decision){
                    //se o outro esta no projeto do logado
                    decision = otherUser.getPermissions().stream().anyMatch(permission -> permission.getProject().getOwner().equals(user));
                    if(!decision){
                        //se ambos estao em um projeto de terceiros
                        Collection<Group> otherGroups = groupRepository.findGroupsByOwnerOrUsersContaining(otherUser, otherUser);
                        decision = groups.stream().anyMatch(group ->
                                group.getPermissions().stream().anyMatch(permission ->
                                        otherGroups.stream().anyMatch(otherGroup ->
                                                otherGroup.getPermissions().stream().anyMatch(otherPermission ->
                                                        otherPermission.getProject().equals(permission.getProject())))));
                    }
                }
        }


        return new AuthorizationDecision(decision);
    }

    private boolean groupTest(User user, Group group) {
        return group.getOwner().equals(user) || group.getUsers().contains(user);
    }
}
