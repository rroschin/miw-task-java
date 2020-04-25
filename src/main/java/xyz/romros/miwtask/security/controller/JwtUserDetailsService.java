package xyz.romros.miwtask.security.controller;

import static java.util.Collections.emptyList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.romros.miwtask.repository.CustomerRepository;
import xyz.romros.miwtask.repository.domain.Customer;

@Service
public class JwtUserDetailsService implements UserDetailsService {

  private final CustomerRepository customerRepository;

  public JwtUserDetailsService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    final Customer customer = customerRepository.findByUsername(username)//
                                                .orElseThrow(() -> new UsernameNotFoundException("Not found."));
    return new User(customer.getUsername(), customer.getPassword(), emptyList());
  }

}
