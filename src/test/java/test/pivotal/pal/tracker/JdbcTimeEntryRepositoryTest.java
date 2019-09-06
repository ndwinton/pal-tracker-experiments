package test.pivotal.pal.tracker;


import io.pivotal.pal.tracker.JdbcTimeEntryRepository;
import io.pivotal.pal.tracker.TimeEntry;
import io.pivotal.pal.tracker.TimeEntryRepository;
import oracle.jdbc.pool.OracleDataSource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

public class JdbcTimeEntryRepositoryTest {
    private TimeEntryRepository subject;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() throws SQLException {
        OracleDataSource dataSource = new OracleDataSource();
        dataSource.setURL(System.getenv("SPRING_DATASOURCE_URL"));

        subject = new JdbcTimeEntryRepository(dataSource);

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("DELETE FROM time_entries");

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    public void createInsertsATimeEntryRecord() {
        TimeEntry newTimeEntry = new TimeEntry(123, 321, LocalDate.parse("2017-01-09"), 8);
        TimeEntry entry = subject.create(newTimeEntry);

        Map<String, Object> foundEntry = jdbcTemplate.queryForMap("Select * from time_entries where id = ?", entry.getId());

        assertThat(longEntry(foundEntry, "id")).isEqualTo(entry.getId());
        assertThat(longEntry(foundEntry, "project_id")).isEqualTo(123L);
        assertThat(longEntry(foundEntry, "user_id")).isEqualTo(321L);
        assertThat(localDateEntry(foundEntry, "entry_date")).isEqualTo(LocalDate.parse("2017-01-09"));
        assertThat(longEntry(foundEntry, "hours")).isEqualTo(8);
    }

    private static Long longEntry(Map<String,Object> map, String key) {
        return ((BigDecimal)map.get(key)).longValue();
    }

    private static LocalDate localDateEntry(Map<String,Object> map, String key) {
        return ((Timestamp)map.get(key)).toLocalDateTime().toLocalDate();
    }

    @Test
    public void createReturnsTheCreatedTimeEntry() {
        TimeEntry newTimeEntry = new TimeEntry(123, 321, LocalDate.parse("2017-01-09"), 8);
        TimeEntry entry = subject.create(newTimeEntry);

        assertThat(entry.getId()).isNotNull();
        assertThat(entry.getProjectId()).isEqualTo(123);
        assertThat(entry.getUserId()).isEqualTo(321);
        assertThat(entry.getDate()).isEqualTo(LocalDate.parse("2017-01-09"));
        assertThat(entry.getHours()).isEqualTo(8);
    }

    @Test
    public void findFindsATimeEntry() {
        jdbcTemplate.execute(
                "INSERT INTO time_entries (id, project_id, user_id, entry_date, hours) " +
                        "VALUES (999, 123, 321, '09-JAN-2017', 8)"
        );

        TimeEntry timeEntry = subject.find(999L);

        assertThat(timeEntry.getId()).isEqualTo(999L);
        assertThat(timeEntry.getProjectId()).isEqualTo(123L);
        assertThat(timeEntry.getUserId()).isEqualTo(321L);
        assertThat(timeEntry.getDate()).isEqualTo(LocalDate.parse("2017-01-09"));
        assertThat(timeEntry.getHours()).isEqualTo(8);
    }

    @Test
    public void findReturnsNullWhenNotFound() {
        TimeEntry timeEntry = subject.find(999L);

        assertThat(timeEntry).isNull();
    }

    @Test
    public void listFindsAllTimeEntries() {
        jdbcTemplate.execute(
                "INSERT INTO time_entries (id, project_id, user_id, entry_date, hours) " +
                        "VALUES (999, 123, 321, '09-JAN-2017', 8)"
        );
        jdbcTemplate.execute(
                "INSERT INTO time_entries (id, project_id, user_id, entry_date, hours) " +
                        "VALUES (888, 456, 678, '08-JAN-2017', 9)"
        );

        List<TimeEntry> timeEntries = subject.list();
        assertThat(timeEntries.size()).isEqualTo(2);

        timeEntries.sort(Comparator.comparing(TimeEntry::getId));

        TimeEntry timeEntry = timeEntries.get(0);
        assertThat(timeEntry.getId()).isEqualTo(888L);
        assertThat(timeEntry.getProjectId()).isEqualTo(456L);
        assertThat(timeEntry.getUserId()).isEqualTo(678L);
        assertThat(timeEntry.getDate()).isEqualTo(LocalDate.parse("2017-01-08"));
        assertThat(timeEntry.getHours()).isEqualTo(9);

        timeEntry = timeEntries.get(1);
        assertThat(timeEntry.getId()).isEqualTo(999L);
        assertThat(timeEntry.getProjectId()).isEqualTo(123L);
        assertThat(timeEntry.getUserId()).isEqualTo(321L);
        assertThat(timeEntry.getDate()).isEqualTo(LocalDate.parse("2017-01-09"));
        assertThat(timeEntry.getHours()).isEqualTo(8);
    }

    @Test
    public void updateReturnsTheUpdatedRecord() {
        jdbcTemplate.execute(
                "INSERT INTO time_entries (id, project_id, user_id, entry_date, hours) " +
                        "VALUES (1000, 123, 321, '09-JAN-2017', 8)");

        TimeEntry timeEntryUpdates = new TimeEntry(456, 987, LocalDate.parse("2017-01-10"), 10);

        TimeEntry updatedTimeEntry = subject.update(1000L, timeEntryUpdates);

        assertThat(updatedTimeEntry.getId()).isEqualTo(1000L);
        assertThat(updatedTimeEntry.getProjectId()).isEqualTo(456L);
        assertThat(updatedTimeEntry.getUserId()).isEqualTo(987L);
        assertThat(updatedTimeEntry.getDate()).isEqualTo(LocalDate.parse("2017-01-10"));
        assertThat(updatedTimeEntry.getHours()).isEqualTo(10);
    }

    @Test
    public void updateUpdatesTheRecord() {
        jdbcTemplate.execute(
                "INSERT INTO time_entries (id, project_id, user_id, entry_date, hours) " +
                        "VALUES (1000, 123, 321, '09-JAN-2017', 8)");

        TimeEntry updatedTimeEntry = new TimeEntry(456, 322, LocalDate.parse("2017-01-10"), 10);

        TimeEntry timeEntry = subject.update(1000L, updatedTimeEntry);

        Map<String, Object> foundEntry = jdbcTemplate.queryForMap("Select * from time_entries where id = ?", timeEntry.getId());

        assertThat(longEntry(foundEntry, "id")).isEqualTo(timeEntry.getId());
        assertThat(longEntry(foundEntry, "project_id")).isEqualTo(456L);
        assertThat(longEntry(foundEntry, "user_id")).isEqualTo(322L);
        assertThat(localDateEntry(foundEntry, "entry_date")).isEqualTo(LocalDate.parse("2017-01-10"));
        assertThat(longEntry(foundEntry, "hours")).isEqualTo(10);
    }

    @Test
    public void deleteRemovesTheRecord() {
        jdbcTemplate.execute(
                "INSERT INTO time_entries (id, project_id, user_id, entry_date, hours) " +
                        "VALUES (999, 123, 321, '09-JAN-2017', 8)"
        );
        jdbcTemplate.execute(
                "INSERT INTO time_entries (id, project_id, user_id, entry_date, hours) " +
                        "VALUES (888, 456, 678, '08-JAN-2017', 9)"
        );

        subject.delete(999L);

        Map<String, Object> notFoundEntry = jdbcTemplate.queryForMap("Select count(*) count from time_entries where id = ?", 999);
        assertThat(longEntry(notFoundEntry, "count")).isEqualTo(0L);
        Map<String, Object> foundEntry = jdbcTemplate.queryForMap("Select count(*) count from time_entries where id = ?", 888);
        assertThat(longEntry(foundEntry, "count")).isEqualTo(1L);
    }
}
