package io.pivotal.pal.tracker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    Map<Long,TimeEntry> entries = new HashMap<>();
    long nextId = 1;

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        TimeEntry entryWithId = cloneEntryWithId(nextId, timeEntry);
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
        if (find(id) != null) {
            entries.put(id, cloneEntryWithId(id, timeEntry));
        }
        return find(id);
    }

    private TimeEntry cloneEntryWithId(long id, TimeEntry timeEntry) {
        return new TimeEntry(id, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
    }

    @Override
    public void delete(long id) {
        entries.remove(id);
    }
}
