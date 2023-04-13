package managers;

import taskCore.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> historyTasks;

    InMemoryHistoryManager() {
        historyTasks = new ArrayList<>();
    }

    private void limitHistory(int limit) {
        while (historyTasks.size() > limit) {
            historyTasks.remove(0);
        }
    }

    @Override
    public void add(Task task) {
        historyTasks.add(task);
        limitHistory(10);
    }

    @Override
    public ArrayList<Task> getHistory(){

        return historyTasks;
    }
}
