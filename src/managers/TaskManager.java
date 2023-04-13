package managers;

import taskCore.Epic;
import taskCore.Subtask;
import taskCore.Task;

import java.util.ArrayList;

public interface TaskManager {
    public int createTask(Task task);
    public int createSubtask(Subtask subtask);
    public int createEpic(Epic epic);

    public void updateTask(Task task);
    public void updateSubtask(Subtask subtask);
    public void updateEpic(Epic epic);

    public Task getTask(int id);
    public Subtask getSubtask(int id);
    public Epic getEpic(int id);

    public ArrayList<Task> getTasks();
    public ArrayList<Subtask> getSubtasks();
    public ArrayList<Epic> getEpics();

    public void deleteTask(Integer taskId);
    public void deleteSubtask(int subtaskId);
    public void deleteEpic(int epicId);

    public void deleteAllTasks();
    public void deleteAllSubtasks();
    public void deleteAllEpics();

    public ArrayList<Subtask> getSubtasksFromEpic(int epicId);

    public ArrayList<Task> getHistory();



}
