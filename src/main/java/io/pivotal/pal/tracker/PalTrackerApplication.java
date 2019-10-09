package io.pivotal.pal.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@SpringBootApplication
public class PalTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PalTrackerApplication.class, args);
    }

    @Bean
    public TimeEntryRepository inMemoryTimeEntryRepository() {
        return new InMemoryTimeEntryRepository();
    }

    @Bean
    public TimeEntryRepository jdbcTimeEntryRepository(DataSource dataSource) {
        return new JdbcTimeEntryRepository(dataSource);
    }

    @Primary
    @Bean
    public TimeEntryRepository jpaTimeEntryRepository(TimeEntryCrudRepository crudRepository) {
        return new JpaTimeEntryRepository(crudRepository);
    }
}
