package br.demo.backend.security;

import br.demo.backend.model.Notification;
import br.demo.backend.repository.NotificationRepository;
import br.demo.backend.security.entity.UserDatailEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@AllArgsConstructor
@Component
public class NotificationsOwnerAuthorization implements AuthorizationManager<RequestAuthorizationContext> {

    private final NotificationRepository notificationRepository;
    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }


    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        UserDatailEntity userDatailEntity = (UserDatailEntity) authentication.get().getPrincipal();
        Notification notification = notificationRepository.findById(Long.parseLong(object.getVariables().get("id"))).get();
       return new AuthorizationDecision(notification.getUser().getUserDetailsEntity().getUsername().equals(userDatailEntity.getUsername()));
    }
}
