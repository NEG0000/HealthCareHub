package nimblix.in.HealthCareHub.service;

import jakarta.servlet.http.HttpServletRequest;
import nimblix.in.HealthCareHub.response.LoginRequest;

public interface AuthService {
    String login(LoginRequest request);
    void logout(HttpServletRequest request);
}
