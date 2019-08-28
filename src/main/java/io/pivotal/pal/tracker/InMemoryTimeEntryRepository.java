package io.pivotal.pal.tracker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    private Map<Long,TimeEntry> entries = new HashMap<>();
    private long nextId = 1;

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        TimeEntry entryWithId = new TimeEntry(nextId, timeEntry);
        entries.put(nextId, entryWithId);
        nextId++;
        return entryWithId;
    }

    @Override
    public TimeEntry find(long id) {
        return entries.get(id);
    }

    @Override
    public List<TimeEntry> list() {
        return List.copyOf(entries.values());
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        entries.replace(id, new TimeEntry(id, timeEntry));
        return find(id);
    }

    @Override
    public void delete(long id) {
        entries.remove(id);
    }
}
