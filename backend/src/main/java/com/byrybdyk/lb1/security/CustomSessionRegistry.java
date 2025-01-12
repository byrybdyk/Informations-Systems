package com.byrybdyk.lb1.security;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class CustomSessionRegistry implements SessionRegistry, HttpSessionListener {

    // Хранение сессий с временем их последнего обновления
    private final ConcurrentHashMap<String, SessionEntry> sessions = new ConcurrentHashMap<>();

    // Добавление сессии в реестр
    public void addSession(HttpSession session) {
        String sessionId = session.getId();
        sessions.put(sessionId, new SessionEntry(session, Instant.now()));
        System.out.println("Added session to registry: " + sessionId);
        removeExpiredSessions(15);
    }

    // Получение сессии по sessionId
    public HttpSession getSession(String sessionId) {
        SessionEntry entry = sessions.get(sessionId);
        if (entry != null) {
            entry.updateLastAccessedTime();
            return entry.getSession();
        }
        return null;
    }

    // Удаление сессии из реестра
    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
        System.out.println("Removed session from registry: " + sessionId);
    }

    // Очистка всех сессий
    public void clearSessions() {
        sessions.clear();
    }

    // Удаление просроченных сессий
    public void removeExpiredSessions(long maxInactiveIntervalSeconds) {
        Instant now = Instant.now();
        sessions.entrySet().removeIf(entry -> now.isAfter(entry.getValue().getLastAccessedTime().plusSeconds(maxInactiveIntervalSeconds)));
        System.out.println("Expired sessions removed.");
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        addSession(se.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        removeSession(se.getSession().getId());
    }

    // Реализация методов SessionRegistry


    @Override
    public void removeSessionInformation(String sessionId) {
        removeSession(sessionId);
    }

    @Override
    public List<Object> getAllPrincipals() {
        return List.of();
    }

    @Override
    public List<SessionInformation> getAllSessions(Object principal, boolean includeExpiredSessions) {
        return sessions.values().stream()
                .map(entry -> new SessionInformation(principal, entry.getSession().getId(),
                        new java.util.Date(entry.getLastAccessedTime().toEpochMilli())))
                .collect(Collectors.toList());
    }


    @Override
    public SessionInformation getSessionInformation(String sessionId) {
        SessionEntry entry = sessions.get(sessionId);
        if (entry != null) {
            return new SessionInformation(entry.getSession(), sessionId,
                    new java.util.Date(entry.getLastAccessedTime().toEpochMilli()));
        }
        return null;
    }


    @Override
    public void refreshLastRequest(String sessionId) {

    }

    @Override
    public void registerNewSession(String sessionId, Object principal) {

    }

    // Вспомогательный класс для хранения сессии и времени её последнего доступа
    private static class SessionEntry {
        private final HttpSession session;
        private Instant lastAccessedTime;

        public SessionEntry(HttpSession session, Instant lastAccessedTime) {
            this.session = session;
            this.lastAccessedTime = lastAccessedTime;
        }

        public HttpSession getSession() {
            return session;
        }

        public Instant getLastAccessedTime() {
            return lastAccessedTime;
        }

        public void updateLastAccessedTime() {
            this.lastAccessedTime = Instant.now();
        }
    }
}
