
import managers.Managers;
import managers.TaskManager;
import taskCore.Epic;
import taskCore.Status;
import taskCore.Subtask;
import taskCore.Task;

public class Main {
    public static void main(String[] args) {

        Task task1 = new Task("title1", "1", Status.IN_PROGRESS);
        Task task2 = new Task("title2", "2", Status.IN_PROGRESS);

        Epic epic1 = new Epic("Epic1", "3");
        Epic epic2 = new Epic("Epic2", "4");

        Subtask subtask1 = new Subtask("Subtask1", "5", Status.DONE, 3);
        Subtask subtask2 = new Subtask("Subtask2", "6", Status.DONE, 3);
        Subtask subtask3 = new Subtask("Subtask3", "7", Status.DONE, 3);

        TaskManager taskManager = Managers.getDefault();

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);


        taskManager.getTask(2);
        taskManager.getTask(1);

        taskManager.getSubtask(6);
        taskManager.getSubtask(7);
        taskManager.getSubtask(5);

        System.out.println(taskManager.getHistory());

        taskManager.deleteSubtask(7);
        taskManager.deleteTask(2);

        System.out.println(taskManager.getHistory());

        taskManager.getTask(1);

        taskManager.getEpic(3);
        taskManager.getEpic(3);
        taskManager.getEpic(4);

        System.out.println(taskManager.getHistory());

        taskManager.deleteEpic(3);

        System.out.println(taskManager.getHistory());

    }
}
