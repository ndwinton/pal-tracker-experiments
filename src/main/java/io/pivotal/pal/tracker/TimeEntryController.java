package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class TimeEntryController {
    private TimeEntryRepository repository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.repository = timeEntryRepository;
    }

    public ResponseEntity<TimeEntry> create(TimeEntry timeEntryToCreate) {
        TimeEntry result = repository.create(timeEntryToCreate);
        return new ResponseEntity<TimeEntry>(result, HttpStatus.CREATED);
    }

    public ResponseEntity<TimeEntry> read(long timeEntryId) {
        TimeEntry foundEntry = repository.find(timeEntryId);
        if (foundEntry == null) {
            return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<TimeEntry>(foundEntry, HttpStatus.OK);
        }
    }

    public ResponseEntity<List<TimeEntry>> list() {
        return new ResponseEntity<List<TimeEntry>>(repository.list(), HttpStatus.OK);
    }

    public ResponseEntity<TimeEntry> update(long timeEntryId, TimeEntry timeEntry) {
        TimeEntry updatedEntry = repository.update(timeEntryId, timeEntry);
        if (updatedEntry == null) {
            return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<TimeEntry>(updatedEntry, HttpStatus.OK);
        }
    }

    public ResponseEntity delete(long timeEntryId) {
        repository.delete(timeEntryId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
