package com.byrybdyk.lb1.service;

import com.byrybdyk.lb1.model.AdminRequest;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Value("${keycloak.admin.password}")
    private String AdminPassword;

    @Autowired
    AdminRequestService adminRequestRepository;

    public void registerUser(String username, String password, String role) throws Exception {
        try {

            if ("admin".equalsIgnoreCase(role)) {
                adminRequestRepository.createRequest(username, password);
                System.out.println("Admin request created");
            } else {
                Keycloak keycloak = KeycloakBuilder.builder()
                        .serverUrl(keycloakServerUrl)
                        .realm("IS-realm")
                        .username("client_admin-cli")
                        .password(AdminPassword)
                        .clientId("IS-admin")
                        .build();

                UserRepresentation user = new UserRepresentation();
                user.setUsername(username);
                user.setEnabled(false);
                user.setAttributes(Map.of("requested_role", Collections.singletonList(role)));

                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(password);
                credential.setTemporary(false);

                user.setCredentials(Collections.singletonList(credential));

                keycloak.realm(realm).users().create(user);
            }

        } catch (Exception e) {
            System.out.println(e + " ERROR");
            throw new Exception("User already exists");
        }

    }
}
