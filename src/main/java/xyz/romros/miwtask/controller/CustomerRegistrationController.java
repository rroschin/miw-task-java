package xyz.romros.miwtask.controller;

import static java.util.UUID.randomUUID;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.slf4j.MDC.MDCCloseable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.romros.miwtask.controller.request.CustomerRegistrationRequest;
import xyz.romros.miwtask.controller.response.CustomerRegistrationResponse;
import xyz.romros.miwtask.service.CustomerRegistrationService;

@Slf4j
@RestController
@RequestMapping("/registration")
public class CustomerRegistrationController {

  private final CustomerRegistrationService customerRegistrationService;

  @Autowired
  public CustomerRegistrationController(CustomerRegistrationService customerRegistrationService) {
    this.customerRegistrationService = customerRegistrationService;
  }

  @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<CustomerRegistrationResponse> registerCustomer(@RequestBody CustomerRegistrationRequest request) {
    try (MDCCloseable ignored = MDC.putCloseable("logEntry", randomUUID().toString())) {
      log.debug("POST Request to /registration with body: {}", request);
      return this.customerRegistrationService.registerCustomer(request);
    }
  }

}
