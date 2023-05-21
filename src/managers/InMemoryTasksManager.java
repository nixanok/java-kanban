package managers;

import java.util.*;

import taskCore.*;

public class InMemoryTasksManager implements TasksManager {
    private int nextId = 1;
    final protected Map<Integer, Epic> epics;
    final protected Map<Integer, Subtask> subtasks;
    final protected Map<Integer, Task> tasks;

    final protected Set<Integer> busyIds;

    final protected HistoryManager historyManager;

    public InMemoryTasksManager() {
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        tasks = new HashMap<>();
        busyIds = new HashSet<>();
        historyManager = Managers.getDefaultHistory();
    }

    protected void updateBusyIds() {
        while (busyIds.contains(nextId)) {
            nextId++;
        }
    }

    @Override
    public void createTask(Task task) {
        updateBusyIds();

        if (task == null)
            return;

        if (task.getId() == 0) {
            task.setId(nextId);
            nextId++;
        }

        busyIds.add(task.getId());
        tasks.put(task.getId(), task);

        updateBusyIds();

    }

    @Override
    public void createSubtask(Subtask subtask) {
        updateBusyIds();

        if (subtask == null)
            return;

        if (subtask.getId() == 0) {
            subtask.setId(nextId);
            nextId++;
        }

        busyIds.add(subtask.getId());
        subtasks.put(subtask.getId(), subtask);

        int epicId = subtask.getEpicId();

        Epic epic = epics.get(epicId);
        epic.addSubtask(subtask.getId());

        updateEpicStatus(epicId);

        updateBusyIds();
    }

    @Override
    public void createEpic(Epic epic) {
        updateBusyIds();

        if (epic == null)
            return;

        if (epic.getId() == 0) {
            epic.setId(nextId);
            nextId++;
        }

        busyIds.add(epic.getId());
        epics.put(epic.getId(), epic);

        updateBusyIds();

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
    public void deleteTask(int taskId) {

        if (!tasks.containsKey(taskId))
            return;
        tasks.remove(taskId);
        busyIds.remove(taskId);
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
        busyIds.remove(subtaskId);

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

        busyIds.remove(epicId);
        historyManager.delete(epicId);
    }

    @Override
    public void deleteAllTasks() {
        for (int taskId : tasks.keySet()) {
            historyManager.delete(taskId);
        }
        busyIds.clear();
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {

        for(Epic epic : epics.values()) {
            epic.removeAllSubtasks();
            epic.setStatus(Status.NEW);
        }

        for (int subtaskId : subtasks.keySet()) {
            historyManager.delete(subtaskId);
            busyIds.remove(subtaskId);
        }

        subtasks.clear();
    }

    @Override
    public void deleteAllEpics() {

        for (int subtaskId : subtasks.keySet()) {
            historyManager.delete(subtaskId);
            busyIds.remove(subtaskId);
        }

        for (int epicId : epics.keySet()) {
            historyManager.delete(epicId);
            busyIds.remove(epicId);
        }

        epics.clear();
        subtasks.clear();
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

    protected Status calculateEpicStatus(int epicId) {

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

    protected void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        Status status = calculateEpicStatus(epicId);
        epic.setStatus(status);
    }

}
