package br.demo.backend.security;

import br.demo.backend.model.Group;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;

@Component
@AllArgsConstructor
public class IsOwnerOrMemberAuthorization implements AuthorizationManager<RequestAuthorizationContext> {
    private final ProjectRepository projectRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> suplier, RequestAuthorizationContext object) {

        List<String> uriMemberWithoutPermission = List.of("/project/{projectId}/set-now");

        UserDatailEntity userDatailEntity = (UserDatailEntity) suplier.get().getPrincipal();
        boolean decision = false;
        //TODO: fazer para consguir pegar o grupo no projeto apenas quem é membro ou owner do projeto
        if(object.getRequest().getRequestURI().contains("/group")){
            String groupId = object.getRequest().getParameter("grouId");
            Group group = groupRepository.findById(Long.parseLong(groupId)).get();
            if (group.getOwner().equals(userDatailEntity.getUser()) || group.getUsers().contains(userDatailEntity.getUser())){
                decision = true;
            }

        } else  {
            String projectId =  object.getVariables().get("projectId");
            System.out.println("Owner?");

            Project project = projectRepository.findById(Long.parseLong(projectId)).get();

            if (!project.getOwner().equals(userDatailEntity.getUser())) {
                System.out.println("!Owner");
                for (GrantedAuthority simple :
                        userDatailEntity.getAuthorities()) {
                    if (("Project_" + projectId + "_").contains(simple.getAuthority())) {
                        decision = true;
                        break;
                    }
                }
//                if(uriMemberWithoutPermission.contains(object.getRequest().getRequestURI())){
//                    User user = userRepository.findByUserDetailsEntity_Username(object.getRequest().getUserPrincipal().getName()).get();
//                    if(user.getPermissions().stream().anyMatch(permission -> permission.getProject().getId().equals(project.getId()))){
//                        decision = true;
//                    }
//                }
            } else {
                System.out.println("Owner");
                decision = true;
            }
        }
        return new AuthorizationDecision(decision);
    }
}
