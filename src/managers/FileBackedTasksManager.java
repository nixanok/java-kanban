package managers;

import exceptions.ManagerSaveException;
import taskCore.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static taskCore.TaskType.*;

public class FileBackedTasksManager extends InMemoryTasksManager {
    private final Path path;

    public FileBackedTasksManager(Path path) {
        super();
        this.path = path;
        load();
        updateBusyIds();
    }

    private TaskType getTypeTask(Task task) {
        return TaskType.valueOf(task.getClass().getSimpleName().toUpperCase());
    }

    public void save() throws ManagerSaveException {
        try (Writer fileWriter = new FileWriter(path.toFile(), StandardCharsets.UTF_8)) {
            fileWriter.write(this.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Save error");
        }
    }

    public void load() {

        try (Reader fileReader = new FileReader(path.toFile())) {
            Map<Integer, TaskType> idToType = new HashMap<>();
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String firstLine = bufferedReader.readLine();
            if (firstLine == null) {
                return;
            }

            if (!bufferedReader.ready() || firstLine.equals("\n")) {
                return;
            }

            while (bufferedReader.ready()) {

                String line = bufferedReader.readLine();

                if (line.equals("")) {
                    break;
                }

                Task task = fromString(line);
                loadTask(task);

                idToType.put(task.getId(), getTypeTask(task));
            }

            String lastLine = bufferedReader.readLine();

            if (lastLine == null) {
                return;
            }

            if (lastLine.equals("")){
                return;
            }

            List<Integer> historyTasksId = historyFromString(lastLine);

            for (Integer id : historyTasksId) {

                Task task;
                TaskType taskType = idToType.get(id);

                switch (taskType){
                    case TASK:
                        task = getTask(id);
                        break;
                    case EPIC:
                        task = getEpic(id);
                        break;
                    case SUBTASK:
                        task = getSubtask(id);
                        break;
                    default:
                        throw new RuntimeException();
                }
                historyManager.add(task);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void loadTask(Task task) {

        TaskType taskType = getTypeTask(task);

        switch (taskType){
            case TASK:
                super.createTask(task);
                break;
            case EPIC:
                super.createEpic((Epic) task);
                break;
            case SUBTASK:
                super.createSubtask((Subtask) task);
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
        try {
            save();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        try {
            save();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        try {
            save();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        try {
            save();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        try {
            save();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        try {
            save();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        try {
            save();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        try {
            save();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return epic;
    }

    @Override
    public List<Task> getTasks() {
        List<Task> tasks = super.getTasks();
        try {
            save();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }

    @Override
    public List<Subtask> getSubtasks() {
        List<Subtask> subtasks = super.getSubtasks();
        try {
            save();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return subtasks;
    }

    @Override
    public List<Epic> getEpics() {
        List<Epic> epics = super.getEpics();
        try {
            save();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return epics;
    }

    @Override
    public void deleteTask(int taskId) {
        super.deleteTask(taskId);
        try {
            save();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteSubtask(int subtaskId) {
        super.deleteSubtask(subtaskId);
        try {
            save();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteEpic(int epicId) {
        super.deleteEpic(epicId);
        try {
            save();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        try {
            save();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        try {
            save();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        try {
            save();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Subtask> getSubtasksFromEpic(int epicId) {
        List<Subtask> subtasks = super.getSubtasksFromEpic(epicId);
        try {
            save();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
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
