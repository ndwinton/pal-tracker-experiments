package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {
    private TimeEntryRepository repository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.repository = timeEntryRepository;
    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry result = repository.create(timeEntryToCreate);
        return new ResponseEntity<TimeEntry>(result, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") long timeEntryId) {
        TimeEntry foundEntry = repository.find(timeEntryId);
        if (foundEntry == null) {
            return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<TimeEntry>(foundEntry, HttpStatus.OK);
        }
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        return new ResponseEntity<List<TimeEntry>>(repository.list(), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable("id") long timeEntryId, @RequestBody TimeEntry timeEntry) {
        TimeEntry updatedEntry = repository.update(timeEntryId, timeEntry);
        if (updatedEntry == null) {
            return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<TimeEntry>(updatedEntry, HttpStatus.OK);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") long timeEntryId) {
        repository.delete(timeEntryId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
