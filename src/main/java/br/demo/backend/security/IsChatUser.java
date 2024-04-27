package br.demo.backend.security;

import br.demo.backend.model.Group;
import br.demo.backend.model.Permission;
import br.demo.backend.model.Project;
import br.demo.backend.model.User;
import br.demo.backend.model.chat.Chat;
import br.demo.backend.repository.GroupRepository;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.repository.chat.ChatRepository;
import br.demo.backend.security.entity.UserDatailEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

@Component
@AllArgsConstructor
public class IsChatUser implements AuthorizationManager<RequestAuthorizationContext> {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> supplier, RequestAuthorizationContext object) {
        String username = ((UserDatailEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user  = userRepository.findByUserDetailsEntity_Username(username).get();
        Long chatId = Long.parseLong(object.getVariables().get("chatId"));
        Chat chat = chatRepository.findById(chatId).get();
        boolean decision = chat.finUsers().contains(user);
        return new AuthorizationDecision(decision);
    }

    private boolean groupTest(User user, Group group) {
        return group.getOwner().equals(user) || group.getUsers().contains(user);
    }
}
