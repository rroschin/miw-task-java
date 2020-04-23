package xyz.romros.miwtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@EnableJdbcRepositories
public class MiwTaskStarter {

  public static void main(String[] args) {
    SpringApplication.run(MiwTaskStarter.class, args);
  }

}
