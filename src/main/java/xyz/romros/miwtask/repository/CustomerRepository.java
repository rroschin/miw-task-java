package xyz.romros.miwtask.repository;

import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import xyz.romros.miwtask.repository.domain.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

  //language=SQL
  String SELECT_BY_USERNAME = "SELECT * FROM customer WHERE username = :username";

  @Query(SELECT_BY_USERNAME)
  Optional<Customer> findByUsername(@Param("username") String username);

}
