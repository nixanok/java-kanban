package managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import taskCore.*;

public class InMemoryTaskManager implements TaskManager {
    private int nextId = 1;
    final private HashMap<Integer, Epic> epics;
    final private HashMap<Integer, Subtask> subtasks;
    final private HashMap<Integer, Task> tasks;

    final HistoryManager historyManager;

    public InMemoryTaskManager() {
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        tasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public int createTask(Task task) {
        task.setId(nextId);
        nextId++;
        tasks.put(task.getId(), task);
        return task.getId();

    }
    @Override
    public int createSubtask(Subtask subtask) {

        subtask.setId(nextId);
        nextId++;
        subtasks.put(subtask.getId(), subtask);

        int epicId = subtask.getEpicId();

        Epic epic = epics.get(epicId);
        epic.addSubtask(subtask.getId());

        updateEpicStatus(epicId);

        return subtask.getId();

    }
    @Override
    public int createEpic(Epic epic) {

        epic.setId(nextId);
        nextId++;
        epics.put(epic.getId(), epic);
        return epic.getId();

    }

    @Override
    public void updateTask(Task task) { tasks.put(task.getId(), task); }
    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);

        int epicId = subtask.getEpicId();
        updateEpicStatus(epicId);
    }
    @Override
    public void updateEpic(Epic epic) { epics.put(epic.getId(), epic); }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }
    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }
    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public ArrayList<Task> getTasks() { return  new ArrayList<>(tasks.values()); }
    @Override
    public ArrayList<Subtask> getSubtasks() { return new ArrayList<>(subtasks.values()); }
    @Override
    public ArrayList<Epic> getEpics() { return new ArrayList<>(epics.values()); }

    @Override
    public void deleteTask(Integer taskId) {

        if (!tasks.containsKey(taskId))
            return;
        tasks.remove(taskId);

    }
    @Override
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
    @Override
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

    @Override
    public void deleteAllTasks() { tasks.clear(); }
    @Override
    public void deleteAllSubtasks() {

        for(Epic epic : epics.values()) {
            epic.removeAllSubtasks();
            epic.setStatus(Status.NEW);
        }

        subtasks.clear();
    }
    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
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

    @Override
    public LinkedList<Task> getHistory() {
        return historyManager.getHistory();
    }

    private Status calculateEpicStatus(int epicId) {

        boolean isNew = true;
        boolean isDone = true;
        boolean isInProgress = false;
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtasksId = epic.getSubtasksId();

        if(subtasksId.isEmpty())
            return Status.NEW;

        for (Integer id : subtasksId) {

            if (isInProgress) {
                break;
            }

            Subtask subtask = subtasks.get(id);
            switch (subtask.getStatus()) {
                case NEW:
                    isDone = false;
                    break;
                case DONE:
                    isNew = false;
                    break;
                default:
                    isNew = false;
                    isDone = false;
                    isInProgress = true;
            }
        }

        if (isNew) {
            return Status.NEW;
        } else if (isDone) {
            return Status.DONE;
        } else {
            return Status.IN_PROGRESS;
        }
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        Status status = calculateEpicStatus(epicId);
        epic.setStatus(status);
    }

}
