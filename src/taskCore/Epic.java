package taskCore;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    final private List<Integer> subtasks;
    public Epic(String title, String description) {
        super(title, description, Status.NEW);
        subtasks = new ArrayList<>();
    }

    public Epic(int id, String title, String description) {
        super(id, title, description, Status.NEW);
        subtasks = new ArrayList<>();
    }

    public void addSubtask(int id) {
        subtasks.add(id);
    }

    public List<Integer> getSubtasksId() {
        return subtasks;
    }

    public void removeSubtask(Integer id) {
        subtasks.remove(id);
    }

    public void removeAllSubtasks() {
        subtasks.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasks +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
