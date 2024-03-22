package br.demo.backend.security.filter;

import br.demo.backend.model.Permission;
import br.demo.backend.model.User;
import br.demo.backend.model.enums.TypePermission;
import br.demo.backend.repository.ProjectRepository;
import br.demo.backend.security.entity.UserDatailEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@AllArgsConstructor
public class FilterAuthorization extends OncePerRequestFilter {

    private final ProjectRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (!publicRoute(request)) {
            UserDatailEntity userDatailEntity = (UserDatailEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // Neste trecho o usuário logado é pego
            User user = userDatailEntity.getUser(); // obtém o usuário que possui referência dentro de userDatailEntity

            String projectIdString = request.getParameter("projectId");
            if (projectIdString != null) {
                Long projectId = Long.parseLong(projectIdString);
                Optional<Permission> permission = user.getPermissions().stream().filter(
                        p -> p.getProject().getId().equals(projectId)).findFirst();
                if (permission.isEmpty()) {
                    throw new RuntimeException("Não tem permissão");
                } else {
                    Permission per = permission.get();
                    String method = request.getMethod();

                    if (Objects.equals(method, "POST")) {
                        if (!List.of(TypePermission.CREATE, TypePermission.DELETE_CREATE, TypePermission.UPDATE_DELETE_CREATE, TypePermission.UPDATE_CREATE).
                                contains(per)) {
                            throw new RuntimeException("Não tem permissão");
                        }

                    } else if (method.equals("PUT") || method.equals("PATCH")) {
                        if (!List.of(TypePermission.UPDATE, TypePermission.UPDATE_CREATE, TypePermission.UPDATE_DELETE, TypePermission.UPDATE_DELETE_CREATE).contains(per)) {
                            throw new RuntimeException("Não possui permissão");
                        }
                    } else if (method.equals("DELETE")) {
                        if (!List.of(TypePermission.DELETE, TypePermission.UPDATE_DELETE, TypePermission.UPDATE_DELETE_CREATE, TypePermission.UPDATE_CREATE).contains(per)) {
                            throw new RuntimeException("Não possui permissão");
                        }
                    }
                }


            }


        }
        filterChain.doFilter(request, response);
    }

    private boolean publicRoute(HttpServletRequest request) {
        return   (request.getRequestURI().equals("/login") || request.getRequestURI().equals("/user")) && request.getMethod().equals("POST");
    }


}

