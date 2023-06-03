package managers;

import taskCore.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);
    void delete(int id);
    List<Task> getHistory();
    List<Integer> getIdHistory();
}
