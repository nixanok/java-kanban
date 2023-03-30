package managers;

import java.util.ArrayList;
import java.util.Collection;
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

    public int createSubtask(Subtask subtask) {

        subtask.setId(nextId);
        nextId++;
        subtasks.put(subtask.getId(), subtask);

        int epicId = subtask.getEpicId();
        updateEpicStatus(epicId);

        return subtask.getId();

    }

    public int createEpic(Epic epic) {

        epic.setId(nextId);
        nextId++;
        epics.put(epic.getId(), epic);
        return epic.getId();

    }

    public void updateTask(Task task) { tasks.put(task.getId(), task); }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);

        int epicId = subtask.getEpicId();
        updateEpicStatus(epicId);
    }

    public void updateEpic(Epic epic) { epics.put(epic.getId(), epic); }

    public Task getTask(int id) { return tasks.get(id); }

    public Subtask getSubtask(int id) { return subtasks.get(id); }

    public Epic getEpic(int id) { return epics.get(id); }

    public Collection<Task> getTasks() { return  tasks.values(); }

    public Collection<Subtask> getSubtasks() { return subtasks.values(); }

    public Collection<Epic> getEpics() { return epics.values(); }

    public void deleteTask(Integer taskId) {

        if (!tasks.containsKey(taskId))
            return;
        tasks.remove(taskId);

    }

    public void deleteSubtask(int subtaskId) {

        if (!subtasks.containsKey(subtaskId))
            return;

        Subtask subtask = subtasks.get(subtaskId);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);

        epic.removeSubtask(subtaskId);
        subtasks.remove(subtaskId);

        updateEpicStatus(epicId);
    }

    public void deleteEpic(int epicId) {

        if (!epics.containsKey(epicId))
            return;

        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtasksId = epic.getSubtasksId();
        for (Integer subtaskId : subtasksId) {
            subtasks.remove(subtaskId);
        }

        epics.remove(epicId);

    }

    public void deleteAllTasks() { tasks.clear(); }

    public void deleteAllSubtasks() {

        for(Epic epic : epics.values()) {
            epic.removeAllSubtasks();
            epic.setStatus("NEW");
        }

        subtasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
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

    private String calculateEpicStatus(int epicId) {

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

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        String status = calculateEpicStatus(epicId);
        epic.setStatus(status);
    }


    private void updateEpicsStatus() {
        for(Integer id: epics.keySet()) {
            String status = calculateEpicStatus(id);
            Epic epic = epics.get(id);
            epic.setStatus(status);
        }
    }
}
