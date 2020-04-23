package xyz.romros.miwtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJdbcRepositories
@EnableAsync
public class MiwTaskStarter {

  public static void main(String[] args) {
    SpringApplication.run(MiwTaskStarter.class, args);
  }

}
