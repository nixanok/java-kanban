package managers;

import taskCore.Epic;
import taskCore.Subtask;
import taskCore.Task;

import java.util.List;

public interface TaskManager {
    int createTask(Task task);
    int createSubtask(Subtask subtask);
    int createEpic(Epic epic);

    void updateTask(Task task);
    void updateSubtask(Subtask subtask);
    void updateEpic(Epic epic);

    Task getTask(int id);
    Subtask getSubtask(int id);
    Epic getEpic(int id);

    List<Task> getTasks();
    List<Subtask> getSubtasks();
    List<Epic> getEpics();

    void deleteTask(Integer taskId);
    void deleteSubtask(int subtaskId);
    void deleteEpic(int epicId);

    void deleteAllTasks();
    void deleteAllSubtasks();
    void deleteAllEpics();

    List<Subtask> getSubtasksFromEpic(int epicId);

    List<Task> getHistory();



}
