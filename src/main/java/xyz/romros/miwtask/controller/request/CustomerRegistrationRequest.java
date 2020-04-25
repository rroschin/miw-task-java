package xyz.romros.miwtask.controller.request;

import static java.util.Collections.nCopies;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerRegistrationRequest {

  String username;
  String password;

  @Override
  public String toString() {
    final String masked = String.join("", nCopies(password.length(), "*"));
    return "CustomerRegistrationRequest{" + "username='" + username + '\'' + ", password='" + masked + '\'' + '}';
  }

}
