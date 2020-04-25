package xyz.romros.miwtask.service;

import static java.util.Collections.nCopies;
import static java.util.Objects.isNull;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.romros.miwtask.controller.request.CustomerRegistrationRequest;
import xyz.romros.miwtask.controller.response.CustomerRegistrationResponse;
import xyz.romros.miwtask.repository.CustomerRepository;
import xyz.romros.miwtask.repository.domain.Customer;

@Slf4j
@Service
public class CustomerRegistrationService {

  private final CustomerRepository customerRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public CustomerRegistrationService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
    this.customerRepository = customerRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional(propagation = REQUIRED)
  public ResponseEntity<CustomerRegistrationResponse> registerCustomer(CustomerRegistrationRequest request) {

    if (isNull(request.getUsername()) || request.getUsername().isEmpty()//
        || isNull(request.getPassword()) || request.getPassword().isEmpty()) {
      log.warn("Tried register new customer with empty username/password: {}", request);
      return ResponseEntity.badRequest().build();
    }

    final Optional<Customer> byUsername = customerRepository.findByUsername(request.getUsername());
    if (byUsername.isPresent()) {
      log.warn("Tried register new customer with existing username: {}", request.getUsername());
      return ResponseEntity.badRequest().build();
    }

    final String encodedPassword = passwordEncoder.encode(request.getPassword());
    Customer customer = new Customer(null, request.getUsername(), encodedPassword);
    customerRepository.save(customer);

    final String masked = String.join("", nCopies(request.getPassword().length(), "*"));
    return ResponseEntity.ok(new CustomerRegistrationResponse(customer.getUsername(), masked));
  }

}
