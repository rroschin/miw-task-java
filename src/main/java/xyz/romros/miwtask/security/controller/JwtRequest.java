package xyz.romros.miwtask.security.controller;

import lombok.Data;

@Data
public class JwtRequest {

  private String username;
  private String password;

}
