package sharingcalender.calender.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sharingcalender.calender.dto.AuthenticatedUser;
import sharingcalender.calender.jwt.JwtUtil;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {


        log.debug("request : URI = {}, METHOD = {} ", request.getRequestURI(), request.getMethod());

        if (request.getHeader(HttpHeaders.AUTHORIZATION) == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);

        if (token.equals("ANONYMOUS")) {
            filterChain.doFilter(request, response);
            return;
        }

        Map<String, String> claims = JwtUtil.decodePayload(token);
        String username = claims.get("username");
        String role = claims.get("role");

        Authentication authentication = new UsernamePasswordAuthenticationToken(new AuthenticatedUser(username,role), null,
            Collections.singletonList(new SimpleGrantedAuthority(role)));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
