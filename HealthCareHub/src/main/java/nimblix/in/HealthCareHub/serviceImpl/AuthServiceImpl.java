package nimblix.in.HealthCareHub.serviceImpl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nimblix.in.HealthCareHub.response.LoginRequest;
import nimblix.in.HealthCareHub.security.JwtUtil;
import nimblix.in.HealthCareHub.service.AuthService;
import nimblix.in.HealthCareHub.service.TokenBlacklistService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final TokenBlacklistService tokenBlacklistService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public String login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return jwtUtil.generateToken(userDetails);
    }

    @Override
    public void logout(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
           tokenBlacklistService.blacklistToken(token);
        }

        SecurityContextHolder.clearContext();
    }
}
