package io.pivotal.pal.tracker;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class JpaTimeEntryRepository implements TimeEntryRepository {
    private TimeEntryCrudRepository crudRepository;

    public JpaTimeEntryRepository(TimeEntryCrudRepository repository) {
        this.crudRepository = repository;
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        return crudRepository.save(timeEntry);
    }

    @Override
    public TimeEntry find(long id) {
        Optional<TimeEntry> result = crudRepository.findById(id);
        return result.orElse(null);
    }

    @Override
    public List<TimeEntry> list() {
        return StreamSupport
                .stream(crudRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {

        TimeEntry record = new TimeEntry(id, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
        return crudRepository.save(record);
    }

    @Override
    public void delete(long id) {
        crudRepository.deleteById(id);
    }
}
