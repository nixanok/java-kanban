import managers.FileBackedTasksManager;
import managers.Managers;
import managers.TasksManager;
import taskCore.*;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        Task task1 = new Task("title1", "1", Status.IN_PROGRESS);
        Task task2 = new Task("title2", "2", Status.IN_PROGRESS);

        Epic epic1 = new Epic("Epic1", "3");
        Epic epic2 = new Epic("Epic2", "4");

        Subtask subtask1 = new Subtask("Subtask1", "5", Status.DONE, 3);
        Subtask subtask2 = new Subtask("Subtask2", "6", Status.DONE, 3);
        Subtask subtask3 = new Subtask("Subtask3", "7", Status.DONE, 3);

        TasksManager tasksManager = Managers.getFileBackedTasksManager();

        tasksManager.createTask(task1);
        tasksManager.createTask(task2);

        tasksManager.createEpic(epic1);
        tasksManager.createEpic(epic2);

        tasksManager.createSubtask(subtask1);
        tasksManager.createSubtask(subtask2);
        tasksManager.createSubtask(subtask3);


        tasksManager.getTask(2);
        tasksManager.getTask(1);

        tasksManager.getSubtask(6);
        tasksManager.getSubtask(7);
        tasksManager.getSubtask(5);

        tasksManager.getTask(1);

        tasksManager.getEpic(3);
        tasksManager.getEpic(3);
        tasksManager.getEpic(4);

        TasksManager tasksManagerFromFile = FileBackedTasksManager.loadFromFile(new File("resources/data.csv"));
    }
}
