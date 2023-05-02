package managers;

import taskCore.Task;

import java.util.ArrayList;

public interface HistoryManager {
    void add(Task task);
    void delete(int id);
    ArrayList<Task> getHistory();
}
