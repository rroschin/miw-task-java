package xyz.romros.miwtask.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.romros.miwtask.security.config.JwtTokenService;

@RestController
@RequestMapping("/authentication")
public class JwtAuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenService jwtTokenService;
  private final JwtUserDetailsService jwtUserDetailsService;

  @Autowired
  public JwtAuthenticationController(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService,
                                     JwtUserDetailsService jwtUserDetailsService) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenService = jwtTokenService;
    this.jwtUserDetailsService = jwtUserDetailsService;
  }

  @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
    authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
    final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
    final String token = jwtTokenService.generateToken(userDetails);
    return ResponseEntity.ok(new JwtResponse(token));
  }

  private void authenticate(String username, String password) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
      throw new RuntimeException("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new RuntimeException("INVALID_CREDENTIALS", e);
    }
  }

}
