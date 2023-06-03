package managers;

import exceptions.ManagerSaveException;
import taskCore.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static taskCore.TaskType.*;

public class FileBackedTasksManager extends InMemoryTasksManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    private TaskType getTypeTask(Task task) {
        return TaskType.valueOf(task.getClass().getSimpleName().toUpperCase());
    }

    public void save() throws ManagerSaveException {
        try (Writer fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write(this.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Save error");
        }
    }

    static public FileBackedTasksManager loadFromFile(File file) {

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

        try (Reader fileReader = new FileReader(file)) {
            Map<Integer, TaskType> idToType = new HashMap<>();
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String firstLine = bufferedReader.readLine();
            if (firstLine == null) {
                return fileBackedTasksManager;
            }

            if (!bufferedReader.ready() || firstLine.equals("\n")) {
                return fileBackedTasksManager;
            }

            while (bufferedReader.ready()) {

                String line = bufferedReader.readLine();

                if (line.equals("")) {
                    break;
                }

                Task task = fromString(line);
                fileBackedTasksManager.loadTask(task);

                idToType.put(task.getId(), fileBackedTasksManager.getTypeTask(task));
            }

            String lastLine = bufferedReader.readLine();

            if (lastLine == null) {
                return fileBackedTasksManager;
            }

            if (lastLine.equals("")){
                return fileBackedTasksManager;
            }

            List<Integer> historyTasksId = historyFromString(lastLine);

            for (Integer id : historyTasksId) {

                Task task;
                TaskType taskType = idToType.get(id);

                switch (taskType){
                    case TASK:
                        task = fileBackedTasksManager.getTask(id);
                        break;
                    case EPIC:
                        task = fileBackedTasksManager.getEpic(id);
                        break;
                    case SUBTASK:
                        task = fileBackedTasksManager.getSubtask(id);
                        break;
                    default:
                        throw new RuntimeException();
                }
                fileBackedTasksManager.historyManager.add(task);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileBackedTasksManager;
    }

    protected void loadTask(Task task) {

        TaskType taskType = getTypeTask(task);

        switch (taskType) {
            case TASK:
                super.updateId(task.getId());
                tasks.put(task.getId(), task);
                break;
            case EPIC:
                super.updateId(task.getId());
                epics.put(task.getId(), (Epic) task);
                break;
            case SUBTASK:
                super.updateId(task.getId());
                subtasks.put(task.getId(), (Subtask) task);
                int epicId = ((Subtask)task).getEpicId();

                Epic epic = epics.get(epicId);
                epic.addSubtask(task.getId());

                updateEpicStatus(epicId);
                break;
            default:
                throw new RuntimeException();
        }

    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        try {
            save();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
            save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public List<Task> getTasks() {
        List<Task> tasks = super.getTasks();
        save();
        return tasks;
    }

    @Override
    public List<Subtask> getSubtasks() {
        List<Subtask> subtasks = super.getSubtasks();
        save();
        return subtasks;
    }

    @Override
    public List<Epic> getEpics() {
        List<Epic> epics = super.getEpics();
        save();
        return epics;
    }

    @Override
    public void deleteTask(int taskId) {
        super.deleteTask(taskId);
        save();
    }

    @Override
    public void deleteSubtask(int subtaskId) {
        super.deleteSubtask(subtaskId);
        save();
    }

    @Override
    public void deleteEpic(int epicId) {
        super.deleteEpic(epicId);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
            save();
    }

    @Override
    public List<Subtask> getSubtasksFromEpic(int epicId) {
        List<Subtask> subtasks = super.getSubtasksFromEpic(epicId);
        save();
        return subtasks;
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public String toString() {

        StringBuilder total = new StringBuilder("id,type,title,status,description,epicId\n");

        for (Task task : super.getTasks()) {
            total.append(String.format("%s,%s,%s,%s,%s\n", task.getId(), TASK, task.getTitle(),
                    task.getStatus(), task.getDescription()));
        }

        for (Epic task : super.getEpics()) {
            total.append(String.format("%s,%s,%s,%s,%s\n", task.getId(), EPIC, task.getTitle(),
                    task.getStatus(), task.getDescription()));
        }

        for (Subtask task : super.getSubtasks()) {
            total.append(String.format("%s,%s,%s,%s,%s,%s\n", task.getId(), SUBTASK, task.getTitle(),
                    task.getStatus(), task.getDescription(), task.getEpicId()));
        }

        total.append("\n");
        total.append(historyToString(historyManager));

        return total.toString();
    }

    static public Task fromString(String taskAttributes) throws RuntimeException {

        String[] attributes = taskAttributes.split(",");

        int id = Integer.parseInt(attributes[0]);
        TaskType taskType = TaskType.valueOf(attributes[1]);
        String title = attributes[2];
        Status status = Status.valueOf(attributes[3]);
        String description = attributes[4];

        switch (taskType) {
            case TASK:
                return new Task(id, title, description,status);
            case SUBTASK:
                return new Subtask(id, title, description, status, Integer.parseInt(attributes[5]));
            case EPIC:
                return new Epic(id, title, description);
            default:
                throw new RuntimeException("Некорректно определен тип задачи");
        }
    }

    static public List<Integer> historyFromString(String value) {

        List<Integer> tasksHistory = new ArrayList<>();

        String[] tasksId = value.split(",");

        for (String id : tasksId) {
            tasksHistory.add(Integer.parseInt(id));
        }

        return tasksHistory;
    }

    static public String historyToString(HistoryManager historyManager) {

        final List<Task> tasksHistory = historyManager.getHistory();
        StringBuilder idHistory = new StringBuilder();

        for (Task task : tasksHistory) {
            idHistory.append(task.getId()).append(",");
        }

        if (idHistory.length() != 0) {
            idHistory.deleteCharAt(idHistory.length() - 1);
        }

        return idHistory.toString();
    }
}
