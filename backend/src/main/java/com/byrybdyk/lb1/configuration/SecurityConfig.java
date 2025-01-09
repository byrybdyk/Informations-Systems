package com.byrybdyk.lb1.configuration;

import com.byrybdyk.lb1.security.CustomSessionRegistry;
import com.byrybdyk.lb1.security.SessionTrackingListener;
import com.byrybdyk.lb1.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableWebSocketMessageBroker
public class SecurityConfig implements WebSocketMessageBrokerConfigurer {

    private final CustomSessionRegistry sessionRegistry;
    private final SessionTrackingListener sessionTrackingListener;

    @Autowired
    public SecurityConfig(CustomSessionRegistry sessionRegistry, SessionTrackingListener sessionTrackingListener) {
        this.sessionRegistry = sessionRegistry;
        this.sessionTrackingListener = sessionTrackingListener;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests()
                .requestMatchers("/auth/login", "/auth/logout/backchannel", "/auth/register", "/login", "/login*", "/auth/realms/master/protocol/openid-connect/token").permitAll()
                .requestMatchers("/auth/login/oauth2/**", "/oauth2/authorization/keycloak", "/oauth2/code/keycloak", "/error").permitAll()
                .requestMatchers("/logout/connect/back-channel").permitAll()
                .requestMatchers("/admin/**").hasAnyAuthority("SCOPE_Admin_scope")
                .requestMatchers("/user/**").authenticated()
                .requestMatchers("/labworks/**").authenticated()
                .requestMatchers("/ws/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .successHandler((request, response, authentication) -> {
                            if (authentication != null) {
                                OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

                                HttpSession session = request.getSession();
                                sessionRegistry.addSession(session);

                                String keycloakSid = (String) token.getPrincipal().getAttributes().get("sid");
                                sessionTrackingListener.onUserLogin(session, keycloakSid);

                                Map<String, Object> attributes = token.getPrincipal().getAttributes();

                                List<String> roles = (List<String>) attributes.get("roles");
                                boolean isAdmin = roles != null && roles.contains("ROLE_ADMIN");
                                System.out.println("!!!!!Is admin: " + isAdmin);

                                String redirectUrl = isAdmin ? "/admin/home" : "/user/home";
                                response.sendRedirect(redirectUrl);
                            } else {
                                response.sendRedirect("/login?error");
                            }
                        })
                        .failureHandler((request, response, exception) -> {
                            response.sendRedirect("/login?error");
                        })
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessHandler((request, response, authentication) -> {
                            request.getSession().invalidate();
                            sessionRegistry.removeSession(request.getSession().getId());
                            response.sendRedirect("http://localhost:8180/realms/IS-realm/protocol/openid-connect/logout?redirect_uri=http://localhost:8080/login");
                        })
                )
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .expiredSessionStrategy(event -> event.getResponse().sendRedirect("/login?expired"))
                        .sessionRegistry(sessionRegistry)
                );
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (accessor != null && accessor.getUser() == null) {
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    if (authentication != null) {
                        accessor.setUser(authentication);
                    }
                }
                return message;
            }
        });
    }
}
