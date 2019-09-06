package io.pivotal.pal.tracker;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.StatementContext;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {
    private Jdbi jdbi;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        jdbi = Jdbi.create(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        Long id = jdbi.withHandle(handle ->
                handle.createUpdate("INSERT INTO time_entries (project_id, user_id, entry_date, hours) VALUES (:projectId, :userId, :date, :hours)")
                        .bindBean(timeEntry)
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Long.class).first());

        return find(id);
    }

    @Override
    public TimeEntry find(long id) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT id, project_id, user_id, entry_date, hours FROM time_entries WHERE id = ?")
                        .bind(0, id)
                        .map(this::timeEntryMapper)
                        .findFirst()
                        .orElse(null)
        );
    }

    private TimeEntry timeEntryMapper(ResultSet rs, StatementContext ctx) throws SQLException {
        return new TimeEntry(
                rs.getLong("id"),
                rs.getLong("project_id"),
                rs.getLong("user_id"),
                rs.getDate("entry_date").toLocalDate(),
                rs.getInt("hours")
        );
    }

    @Override
    public List<TimeEntry> list() {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM time_entries")
                        .map(this::timeEntryMapper)
                        .list()
        );
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry updated = new TimeEntry(id, timeEntry);
        int updateCount = jdbi.withHandle(handle ->
                handle.createUpdate("UPDATE time_entries SET project_id = :projectId, user_id = :userId, entry_date = :date , hours = :hours WHERE id = :id")
                        .bindBean(updated)
                        .execute());

        return find(id);
    }

    @Override
    public void delete(long id) {
        jdbi.withHandle(handle ->
                handle.execute("DELETE FROM time_entries WHERE id = ?", id));
    }
}
