package com.byrybdyk.lb1.service;

import com.byrybdyk.lb1.model.AdminRequest;
import com.byrybdyk.lb1.model.User;
import com.byrybdyk.lb1.model.enums.Role;
import com.byrybdyk.lb1.repository.AdminRequestRepository;
import com.byrybdyk.lb1.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;


import org.keycloak.representations.idm.UserRepresentation;


import java.util.Collections;
import java.util.List;

@Service
public class AdminRequestService {

    @Value("${keycloak.auth-server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${keycloak.admin.password}")
    private String AdminPassword;

    private final AdminRequestRepository adminRequestRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminRequestService(AdminRequestRepository adminRequestRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.adminRequestRepository = adminRequestRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createRequest(String username, String password) {
        AdminRequest request = new AdminRequest(username, password);
        adminRequestRepository.save(request);
    }

    public void approveRequest(Long requestId) {
        AdminRequest request = adminRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        try {
            // Создаем пользователя в Keycloak
            createUserInKeycloak(request.getUsername(), request.getPassword(), "admin");

            // Удаляем заявку после успешного создания пользователя
            adminRequestRepository.delete(request);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при добавлении пользователя в Keycloak: " + e.getMessage(), e);
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    private void createUserInKeycloak(String username, String password, String role) {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8180")
                .realm("IS-realm")
                .username("admin-user")
                .password(AdminPassword)
                .clientId("admin-cli")
                .build();
        System.out.println("Connected to Keycloak");

        String realm = "IS-realm";

        List<UserRepresentation> existingUsers = keycloak.realm(realm).users().search(username);
        if (!existingUsers.isEmpty()) {
            throw new RuntimeException("Пользователь с именем " + username + " уже существует");
        }

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEnabled(true);
        user.setAttributes(Map.of("requested_role", Collections.singletonList(role)));

        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(password);
        credentials.setTemporary(false);

        user.setCredentials(Collections.singletonList(credentials));

        Response response = keycloak.realm(realm).users().create(user);
        if (response.getStatus() != 201) {
            System.out.println("Ошибка при добавлении пользователя: " + response.getStatus() + " " + response.readEntity(String.class));
            throw new RuntimeException("Ошибка при добавлении пользователя в Keycloak: " + response.getStatus());
        }

        System.out.println("User created in Keycloak");

        List<RoleRepresentation> realmRoles = keycloak.realm(realm)
                .roles()
                .list();
        RoleRepresentation adminRole = realmRoles.stream()
                .filter(r -> r.getName().equals("ROLE_ADMIN"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN " + role + " не найдена в Keycloak"));

        System.out.println("Role found: " + adminRole.getName());

        List<UserRepresentation> users = keycloak.realm(realm).users().search(username);
        if (users.isEmpty()) {
            throw new RuntimeException("Пользователь " + username + " не найден в Keycloak");
        }
        String userId = users.get(0).getId();

        keycloak.realm(realm)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(Collections.singletonList(adminRole));

        keycloak.realm(realm)
                .users()
                .get(userId)
                .joinGroup("48075453-64ac-4f02-be02-64e3ae81b9f2");

        System.out.println("User added to ADMIN-group ");
    }

    public List<AdminRequest> getAllRequests() {
        return adminRequestRepository.findAll();
    }
}
