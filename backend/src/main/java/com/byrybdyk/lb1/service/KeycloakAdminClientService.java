package com.byrybdyk.lb1.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.Map;

@Service
public class KeycloakAdminClientService {
    @Value("${keycloak.auth-server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    public void registerUser(String username, String password, String role) throws Exception {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakServerUrl)
                .realm("master")
                .username("admin") // Укажите данные администратора
                .password("admin-password")
                .clientId("admin-cli")
                .build();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEnabled(false); // Отключаем, если роль ADMIN требует подтверждения
        user.setAttributes(Map.of("requested_role", Collections.singletonList(role)));

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        user.setCredentials(Collections.singletonList(credential));

        keycloak.realm(realm).users().create(user);
    }
}
