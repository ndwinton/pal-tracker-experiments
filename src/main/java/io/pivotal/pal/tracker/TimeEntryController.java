package io.pivotal.pal.tracker;

import org.springframework.http.ResponseEntity;

import java.util.List;

public class TimeEntryController {
    public TimeEntryController(TimeEntryRepository timeEntryRepository) {

    }

    public ResponseEntity<TimeEntry> create(TimeEntry timeEntryToCreate) {
        return null;
    }

    public ResponseEntity<TimeEntry> read(long timeEntryId) {
        return null;
    }

    public ResponseEntity<List<TimeEntry>> list() {
        return null;
    }

    public ResponseEntity<TimeEntry> update(long timeEntryId, TimeEntry expected) {
        return null;
    }

    public ResponseEntity delete(long timeEntryId) {
        return null;
    }
}
