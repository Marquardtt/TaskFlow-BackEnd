package br.demo.backend.security;

import br.demo.backend.model.Group;
import br.demo.backend.model.User;
import br.demo.backend.model.chat.Chat;
import br.demo.backend.model.dtos.group.GroupGetDTO;
import br.demo.backend.model.dtos.group.SimpleGroupGetDTO;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.repository.chat.ChatRepository;
import br.demo.backend.security.entity.UserDatailEntity;
import br.demo.backend.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Supplier;

@Component
@AllArgsConstructor
public class IsOwnerInThisProject implements AuthorizationManager<RequestAuthorizationContext> {
    private final UserRepository userRepository;
    private final GroupService groupService;

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> supplier, RequestAuthorizationContext object) {
        User user = userRepository.findByUserDetailsEntity_Username(((UserDatailEntity)supplier.get().getPrincipal()).getUsername()).get();
        String usernameOther = object.getVariables().get("username");
        Long projectId = Long.parseLong(object.getVariables().get("projectId"));
        User otherUser = userRepository.findByUserDetailsEntity_Username(usernameOther).get();
        Collection<SimpleGroupGetDTO> groups = groupService.findGroupsByAProject(projectId);
        for(SimpleGroupGetDTO group : groups){
            GroupGetDTO groupDataBase = groupService.findOne(group.getId());
            if( groupDataBase.getOwner().getId().equals(user.getId()) &&
                groupDataBase.getUsers().stream().anyMatch(u -> u.getId().equals(otherUser.getId()))){
                return new AuthorizationDecision(true);
            }
        }
        return new AuthorizationDecision(false);
    }

    private boolean groupTest(User user, Group group) {
        return group.getOwner().equals(user) || group.getUsers().contains(user);
    }
}
