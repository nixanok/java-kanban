package managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import taskCore.*;

public class InMemoryTaskManager implements TaskManager {
    private int nextId = 1;
    final private Map<Integer, Epic> epics;
    final private Map<Integer, Subtask> subtasks;
    final private Map<Integer, Task> tasks;

    final private HistoryManager historyManager;

    public InMemoryTaskManager() {
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        tasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public int createTask(Task task) {
        if (task == null)
            return -1;

        task.setId(nextId);
        nextId++;
        tasks.put(task.getId(), task);
        return task.getId();

    }

    @Override
    public int createSubtask(Subtask subtask) {
        if (subtask == null)
            return -1;

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
        if (epic == null)
            return -1;

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
    public List<Task> getTasks() { return  new ArrayList<>(tasks.values()); }
    @Override
    public List<Subtask> getSubtasks() { return new ArrayList<>(subtasks.values()); }
    @Override
    public List<Epic> getEpics() { return new ArrayList<>(epics.values()); }

    @Override
    public void deleteTask(Integer taskId) {

        if (!tasks.containsKey(taskId))
            return;
        tasks.remove(taskId);

        historyManager.delete(taskId);

    }
    @Override
    public void deleteSubtask(int subtaskId) {

        if (!subtasks.containsKey(subtaskId))
            return;

        Subtask subtask = subtasks.remove(subtaskId);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);

        epic.removeSubtask(subtaskId);

        updateEpicStatus(epicId);

        historyManager.delete(subtaskId);
    }
    @Override
    public void deleteEpic(int epicId) {

        if (!epics.containsKey(epicId))
            return;

        Epic epic = epics.remove(epicId);
        List<Integer> subtasksId = epic.getSubtasksId();
        for (Integer subtaskId : subtasksId) {
            historyManager.delete(subtaskId);
            subtasks.remove(subtaskId);
        }

        historyManager.delete(epicId);
    }

    @Override
    public void deleteAllTasks() {
        for (int taskId : tasks.keySet()) {
            historyManager.delete(taskId);
        }
        tasks.clear();
    }
    @Override
    public void deleteAllSubtasks() {

        for(Epic epic : epics.values()) {
            epic.removeAllSubtasks();
            epic.setStatus(Status.NEW);
            historyManager.delete(epic.getId());
        }

        subtasks.clear();
    }
    @Override
    public void deleteAllEpics() {
        for (int epicId : epics.keySet()) {
            deleteEpic(epicId);
        }
    }

    @Override
    public List<Subtask> getSubtasksFromEpic(int epicId) {

        if (!epics.containsKey(epicId)) {
            return null;
        }

        Epic epic = epics.get(epicId);
        List<Integer> subtasksId = epic.getSubtasksId();

        List<Subtask> subtasksFromEpic = new ArrayList<>();
        for (Integer id : subtasksId) {
            subtasksFromEpic.add(subtasks.get(id));
        }

        return subtasksFromEpic;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private Status calculateEpicStatus(int epicId) {

        boolean isNew = true;
        boolean isDone = true;
        boolean isInProgress = false;
        Epic epic = epics.get(epicId);
        List<Integer> subtasksId = epic.getSubtasksId();

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
