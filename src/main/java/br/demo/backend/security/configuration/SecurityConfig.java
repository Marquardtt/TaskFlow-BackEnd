package br.demo.backend.security.configuration;

import br.demo.backend.security.AuthorizationRequestsRoutes;
import br.demo.backend.security.IsOwnerAuthorization;
import br.demo.backend.security.IsOwnerOrMemberAuthorization;
import br.demo.backend.security.ProjectOrGroupAuthorization;
import br.demo.backend.security.filter.FilterAuthentication;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final SecurityContextRepository repo;
    private final FilterAuthentication filterAuthentication;
    private final AuthorizationRequestsRoutes authorizationRequestsRoutes;
    private final IsOwnerAuthorization isOwnerAuthorization;
    private final IsOwnerOrMemberAuthorization isOwnerOrMemberAuthorization;
    private final ProjectOrGroupAuthorization projectOrGroupAuthorization;

    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        // Prevenção ao ataque CSRF (Cross-Site Request Forgery)
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(authz -> authz

                //USER
                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/user").permitAll()
                .requestMatchers(HttpMethod.PUT, "/user/**").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/user/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/user").authenticated()
                .requestMatchers(HttpMethod.GET, "/user/name/{name}").authenticated()
                .requestMatchers(HttpMethod.GET, "/user").authenticated()
                .requestMatchers(HttpMethod.GET, "/user/logged").authenticated()
                .requestMatchers(HttpMethod.GET,"/user/{username}/project/{projectId}").access(isOwnerOrMemberAuthorization)

                //PROJECT
                .requestMatchers(HttpMethod.POST, "/project").authenticated()
                .requestMatchers(HttpMethod.PATCH, "/project/{projectId}/{picture}").access(isOwnerAuthorization)
                .requestMatchers(HttpMethod.PATCH, "/project/{projectId}/set-now").access(isOwnerOrMemberAuthorization)
                .requestMatchers(HttpMethod.PATCH, "/project/{projectId}").access(isOwnerAuthorization)
                .requestMatchers(HttpMethod.PATCH, "/project/{projectId}/change-owner").access(isOwnerAuthorization)
                .requestMatchers(HttpMethod.PUT, "/project/{projectId}").access(isOwnerAuthorization)
                .requestMatchers(HttpMethod.GET, "/project/{projectId}").access(isOwnerOrMemberAuthorization)
                .requestMatchers(HttpMethod.GET, "/project/me-owner").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/project/{projectId}").access(isOwnerAuthorization)

                //TASK
                .requestMatchers(HttpMethod.POST, "/task/project/{projectId}/{pageId}").access(authorizationRequestsRoutes)
                .requestMatchers(HttpMethod.PUT, "/task/project/{projectId}").access(authorizationRequestsRoutes)
                .requestMatchers(HttpMethod.PUT, "task/project/{projectId}/redo/{id}").access(authorizationRequestsRoutes)
                .requestMatchers(HttpMethod.PATCH, "/task/project/{projectId}").access(authorizationRequestsRoutes)
                .requestMatchers(HttpMethod.GET, "/task/today/{id}").authenticated()
                .requestMatchers(HttpMethod.GET, "/task/project/{projectId}").access(isOwnerOrMemberAuthorization)
                .requestMatchers(HttpMethod.PATCH, "/task/{id}/project/{projectId}/complete").access(isOwnerAuthorization)
                .requestMatchers(HttpMethod.DELETE, "task/project/{projectId}/{id}/permanent").access(isOwnerAuthorization)
                .requestMatchers(HttpMethod.DELETE, "/task/project/{projectId}/{id}").access(authorizationRequestsRoutes)

                //PROPERTY
                .requestMatchers(HttpMethod.POST, "/property/project/{projectId}/limited").access(authorizationRequestsRoutes)
                .requestMatchers(HttpMethod.POST, "/property/project/{projectId}/select").access(authorizationRequestsRoutes)
                .requestMatchers(HttpMethod.POST, "/property/project/{projectId}/date").access(authorizationRequestsRoutes)
                .requestMatchers(HttpMethod.PUT, "/property/project/{projectId}/limited").access(authorizationRequestsRoutes)
                .requestMatchers(HttpMethod.PUT, "/property/project/{projectId}/select").access(authorizationRequestsRoutes)
                .requestMatchers(HttpMethod.PUT, "/property/project/{projectId}/date").access(authorizationRequestsRoutes)
                .requestMatchers(HttpMethod.PATCH, "/property/project/{projectId}/limited").access(authorizationRequestsRoutes)
                .requestMatchers(HttpMethod.PATCH, "/property/project/{projectId}/select").access(authorizationRequestsRoutes)
                .requestMatchers(HttpMethod.PATCH, "/property/project/{projectId}/date").access(authorizationRequestsRoutes)
                .requestMatchers(HttpMethod.DELETE, "/property/project/{projectId}/{id}").access(authorizationRequestsRoutes)

                //PAGE
                .requestMatchers(HttpMethod.POST, "/page/project/{projectId}").access(authorizationRequestsRoutes)
//                .requestMatchers(HttpMethod.PATCH, "/page/{taskId}/{index}/{columnChanged}project/{projectId}").access(authorizationRequestsRoutes)
                .requestMatchers(HttpMethod.PATCH, "/page/{id}/project/{projectId}").access(authorizationRequestsRoutes)
//                .requestMatchers(HttpMethod.PATCH, "/page/{taskId}/{index}/project/{projectId}").access(authorizationRequestsRoutes)
                .requestMatchers(HttpMethod.PATCH, "/page/x-and-y/project/{projectId}").access(authorizationRequestsRoutes)
                .requestMatchers(HttpMethod.PATCH, "/page/draw/{id}/project/{projectId}").access(authorizationRequestsRoutes)
                .requestMatchers(HttpMethod.PATCH, "/page/project/{projectId}/prop-ordering/{id}").access(authorizationRequestsRoutes)
                .requestMatchers(HttpMethod.DELETE, "/page/{id}/project/{projectId}").access(isOwnerAuthorization)
                .requestMatchers(HttpMethod.PATCH, "/page/merge/{id}/project/{projectId}").access(authorizationRequestsRoutes)

                //PERMISSION
                .requestMatchers(HttpMethod.POST, "/permission/project/{projectId}").access(isOwnerAuthorization)
                .requestMatchers(HttpMethod.PUT, "/permission/project/{projectId}").access(isOwnerAuthorization)
                .requestMatchers(HttpMethod.PATCH, "/permission/project/{projectId}").access(isOwnerAuthorization)
                .requestMatchers(HttpMethod.GET, "/permission/{id}/project/{projectId}").access(isOwnerAuthorization)
                .requestMatchers(HttpMethod.GET, "/permission/project/{projectId}").access(isOwnerAuthorization)
                .requestMatchers(HttpMethod.DELETE, "/permission/{id}/project/{projectId}").access(isOwnerAuthorization)

                //GROUP
                .requestMatchers(HttpMethod.POST, "/group").authenticated()
                .requestMatchers(HttpMethod.PUT, "/group/{groupId}").access(isOwnerAuthorization)
                .requestMatchers(HttpMethod.PATCH, "/group/{groupId}").access(isOwnerAuthorization)
                .requestMatchers(HttpMethod.GET, "/group/{groupId}").access(isOwnerOrMemberAuthorization)
                .requestMatchers(HttpMethod.GET, "/group").authenticated()
                .requestMatchers(HttpMethod.GET, "/group/project/{projectId}").access(isOwnerOrMemberAuthorization)
                .requestMatchers(HttpMethod.GET, "/group/{groupId}").authenticated()
                .requestMatchers(HttpMethod.GET, "/group/{groupId}/permissions/{projectId}").access(isOwnerOrMemberAuthorization)
                .requestMatchers(HttpMethod.DELETE, "/group/{groupId}").access(isOwnerAuthorization)
                .requestMatchers(HttpMethod.PATCH, "/group/{groupId}/picture").access(isOwnerAuthorization)
                .requestMatchers(HttpMethod.PATCH, "/group/{groupId}/change-owner").access(isOwnerAuthorization)


                .requestMatchers(HttpMethod.POST, "/forgotPassword").permitAll()// vai ser o esqueceu sua senha
                .requestMatchers(HttpMethod.POST, "/projects").authenticated()
                .anyRequest().authenticated());

        // Manter a sessão do usuário na requisição ativa
        http.securityContext((context) -> context.securityContextRepository(repo));
        http.sessionManagement(config -> {
            config.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });
        http.addFilterBefore(filterAuthentication, UsernamePasswordAuthenticationFilter.class);

        http.formLogin(AbstractHttpConfigurer::disable); // este metodo desabilita o formulario de login do spring security
        http.logout(AbstractHttpConfigurer::disable); // este metodo desabilita o logout do spring security


        return http.build();
    }
}