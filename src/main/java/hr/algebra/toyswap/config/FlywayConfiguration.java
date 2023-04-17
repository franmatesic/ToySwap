package hr.algebra.toyswap.config;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfiguration {

  public FlywayConfiguration(DataSource dataSource) {
    Flyway.configure().baselineOnMigrate(true).dataSource(dataSource).load().migrate();
  }
}
