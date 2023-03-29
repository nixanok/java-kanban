package managers;

import java.util.ArrayList;
import java.util.HashMap;

import taskCore.*;

public class Manager {
    private int nextId = 1;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;
    private HashMap<Integer, Task> tasks;

    public Manager() {
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        tasks = new HashMap<>();
    }

    public int createTask(Task task) {

        task.setId(nextId);
        nextId++;
        tasks.put(task.getId(), task);
        return task.getId();

    }

    public void updateTask(Task task) { tasks.put(task.getId(), task); }

    public Task getTask(int id) { return tasks.get(id); }

    public HashMap<Integer, Task> getTasks() { return tasks; }

    public void deleteTask(Integer taskId) {

        if (!tasks.containsKey(taskId))
            return;
        tasks.remove(taskId);

    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public int createSubtask(Subtask subtask) {

        subtask.setId(nextId);
        nextId++;
        subtasks.put(subtask.getId(), subtask);

        if (subtask.getEpicId() != 0) {
            int epicId = subtask.getEpicId();
            Epic epic = epics.get(epicId);
            epic.addSubtask(subtask.getId());
        }

        return subtask.getId();

    }

    public void updateSubtask(Subtask subtask) { subtasks.put(subtask.getId(), subtask); }

    public Subtask getSubtask(int id) { return subtasks.get(id); }

    public HashMap<Integer, Subtask> getSubtasks() { return subtasks; }

    public void deleteSubtask(int subtaskId) {

        if (!subtasks.containsKey(subtaskId))
            return;

        Subtask subtask = subtasks.get(subtaskId);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);

        if(epic != null) {
            epic.removeSubtask(subtaskId);
            subtasks.remove(subtaskId);
        }

        updateEpicsStatus();
    }

    public void deleteAllSubtasks() {

        for(Integer id: subtasks.keySet()) {
            deleteSubtask(id);
        }

        updateEpicsStatus();
    }

    public int createEpic(Epic epic) {

        epic.setId(nextId);
        nextId++;
        epics.put(epic.getId(), epic);
        return epic.getId();

    }

    public void updateEpic(Epic epic) { epics.put(epic.getId(), epic); }


    public Epic getEpic(int id) { return epics.get(id); }

    public HashMap<Integer, Epic> getEpics() { return epics; }

    public void deleteEpic(int epicId) {
        Epic epic = epics.get(epicId);
        epic.removeAllSubtasks();

        if (!epics.containsKey(epicId))
            return;
        epics.remove(epicId);

    }

    public void deleteAllEpics() {
        for (int epicId : epics.keySet()) {
            deleteEpic(epicId);
        }
    }

    public void addSubtaskInEpic(int epicId, int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        subtask.setEpicId(epicId);

        Epic epic = epics.get(epicId);
        epic.addSubtask(subtaskId);
        updateEpicsStatus();
    }

    public ArrayList<Subtask> getSubtasksFromEpic(int epicId) {
        if (!epics.containsKey(epicId)) {
            return null;
        }

        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtasksId = epic.getSubtasksId();

        ArrayList<Subtask> subtasksFromEpic = new ArrayList<>();
        for (Integer id : subtasksId) {
            subtasksFromEpic.add(subtasks.get(id));
        }

        return subtasksFromEpic;
    }

    public String getEpicStatus(int epicId) {

        boolean isNew = true;
        boolean isDone = true;

        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtasksId = epic.getSubtasksId();

        if(subtasksId.isEmpty())
            return "NEW";

        for (Integer id : subtasksId) {
            Subtask subtask = subtasks.get(id);
            switch (subtask.getStatus()) {
                case "NEW":
                    isDone = false;
                    break;
                case "DONE":
                    isNew = false;
                    break;
                default:
                    isNew = false;
                    isDone = false;
            }
        }

        if (isNew) {
            return "NEW";
        } else if (isDone) {
            return "DONE";
        } else {
            return "IN_PROGRESS";
        }
    }

    public void updateEpicsStatus() {
        for(Integer id: epics.keySet()) {
            String status = getEpicStatus(id);
            Epic epic = epics.get(id);
            epic.setStatus(status);
        }
    }
}
