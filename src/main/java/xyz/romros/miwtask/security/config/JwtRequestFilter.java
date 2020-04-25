package xyz.romros.miwtask.security.config;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.romros.miwtask.security.controller.JwtUserDetailsService;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  private final JwtUserDetailsService jwtUserDetailsService;
  private final JwtTokenService jwtTokenService;

  @Autowired
  public JwtRequestFilter(JwtUserDetailsService jwtUserDetailsService, JwtTokenService jwtTokenService) {
    this.jwtUserDetailsService = jwtUserDetailsService;
    this.jwtTokenService = jwtTokenService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    final String requestTokenHeader = request.getHeader("Authorization");
    if (isNull(requestTokenHeader) || !requestTokenHeader.startsWith("Bearer ")) {
      chain.doFilter(request, response);
      return;
    }

    final String jwtToken = requestTokenHeader.substring(7);
    String username;
    try {
      username = jwtTokenService.getUsernameFromToken(jwtToken);
    } catch (Exception e) {
      throw new ServletException(e);
    }

    if (nonNull(username) && isNull(SecurityContextHolder.getContext().getAuthentication())) {
      final UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
      if (jwtTokenService.isTokenValid(jwtToken, userDetails)) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(token);
      }
    }
    chain.doFilter(request, response);
  }

}
