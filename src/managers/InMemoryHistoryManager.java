package managers;

import taskCore.Task;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    final private LinkedList<Task> historyTasks;

    public InMemoryHistoryManager() {
        historyTasks = new LinkedList<>();
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
    public LinkedList<Task> getHistory(){

        return historyTasks;
    }
}
