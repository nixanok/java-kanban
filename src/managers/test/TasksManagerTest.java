package managers.test;

import static org.junit.jupiter.api.Assertions.*;

import managers.TasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import taskCore.Epic;
import taskCore.Status;
import taskCore.Subtask;
import taskCore.Task;

import java.util.List;

abstract class TasksManagerTest<T extends TasksManager> {

    T taskManager;

    Task task1;
    Task task2;
    Task task3;

    Epic epic1;
    Epic epic2;
    Epic epic3;

    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;

    @BeforeEach
    public void initTasks() {
        task1 = new Task("task1", "taskDescription1", Status.NEW);
        task2 = new Task("task2", "taskDescription2", Status.NEW);
        task3 = new Task("task3", "taskDescription3", Status.NEW);

        subtask1 = new Subtask("subtask1", "subtaskDescription1", Status.NEW, 1);
        subtask2 = new Subtask("subtask2", "subtaskDescription2", Status.NEW, 1);
        subtask3 = new Subtask("subtask3", "subtaskDescription3", Status.NEW, 1);

        epic1 = new Epic("epic1", "epicDescription1");
        epic2 = new Epic("epic2", "epicDescription2");
        epic3 = new Epic("epic3", "taskDescription3");
    }

    @Test
    public void shouldCreateTask() {

        taskManager.createTask(task1);

        final Task savedTask = taskManager.getTask(1);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void shouldCreateSomeTasksWithId123() {

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        final List<Task> savedTasks = taskManager.getTasks();

        assertEquals(3, savedTasks.size(), "Количество задач не совпадает.");
        assertNotNull(savedTasks.get(0), "Задача не найдена.");
        assertNotNull(savedTasks.get(1), "Задача не найдена.");
        assertNotNull(savedTasks.get(2), "Задача не найдена.");

        assertEquals(task1, savedTasks.get(0), "Задачи не совпадают");
        assertEquals(task2, savedTasks.get(1), "Задачи не совпадают");
        assertEquals(task3, savedTasks.get(2) , "Задачи не совпадают");

        assertEquals(1, task1.getId(), "Индентификаторы не совпадают.");
        assertEquals(2, task2.getId(), "Индентификаторы не совпадают.");
        assertEquals(3, task3.getId(), "Индентификаторы не совпадают.");
    }

    @Test
    public void shouldCreateEpic() {

        taskManager.createEpic(epic1);

        final Epic savedEpic = taskManager.getEpic(1);
        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic1, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic1, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    public void shouldCreateSomeEpicsWithId123() {

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);

        final List<Epic> savedEpics = taskManager.getEpics();

        assertNotNull(savedEpics.get(0), "Эпик не найден.");
        assertNotNull(savedEpics.get(1), "Эпик не найден.");
        assertNotNull(savedEpics.get(2), "Эпик не найден.");

        assertEquals(epic1, savedEpics.get(0), "Эпики не совпадают.");
        assertEquals(epic2, savedEpics.get(1), "Эпики не совпадают.");
        assertEquals(epic3, savedEpics.get(2) , "Эпики не совпадают.");

        assertEquals(1, epic1.getId(), "Индентификаторы не совпадают.");
        assertEquals(2, epic2.getId(), "Индентификаторы не совпадают.");
        assertEquals(3, epic3.getId(), "Индентификаторы не совпадают.");
    }

    @Test
    public void shouldCreateSubtask() {

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);

        final Subtask savedSubtask = taskManager.getSubtask(2);
        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask1, savedSubtask, "Подзадачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество Подзадач.");
        assertEquals(subtask1, subtasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    public void shouldCreateSomeSubtasksWithId234() {

        taskManager.createEpic(epic1);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        final List<Subtask> savedSubtasks = taskManager.getSubtasks();

        assertNotNull(savedSubtasks.get(0), "Подзадача не найдена.");
        assertNotNull(savedSubtasks.get(1), "Подзадача не найдена.");
        assertNotNull(savedSubtasks.get(2), "Подзадача не найдена.");

        assertEquals(subtask1, savedSubtasks.get(0), "Подзадачи не совпадают.");
        assertEquals(subtask2, savedSubtasks.get(1), "Подзадачи не совпадают.");
        assertEquals(subtask3, savedSubtasks.get(2) , "Подзадачи не совпадают.");

        assertEquals(2, subtask1.getId(), "Индентификаторы не совпадают.");
        assertEquals(3, subtask2.getId(), "Индентификаторы не совпадают.");
        assertEquals(4, subtask3.getId(), "Индентификаторы не совпадают.");
    }

    @Test
    public void shouldUpdateTask() {
        taskManager.createTask(task1);

        task1.setTitle("newTask");
        task1.setDescription("newDescription");
        task1.setStatus(Status.DONE);

        taskManager.updateTask(task1);

        Task savedTask = taskManager.getTask(1);
        assertEquals(task1, savedTask, "Задачи не совпадают.");
    }

    @Test
    public void shouldUpdateSubtask() {

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);

        subtask1.setTitle("newTask");
        subtask1.setDescription("newDescription");
        subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);

        Task savedSubtask = taskManager.getSubtask(2);
        assertEquals(subtask1, savedSubtask, "Подзадачи не совпадают.");
    }

    @Test
    public void shouldUpdateEpic() {
        taskManager.createEpic(epic1);

        epic1.setTitle("newTask");
        epic1.setDescription("newDescription");

        taskManager.updateEpic(epic1);

        Epic savedEpic = taskManager.getEpic(1);
        assertEquals(epic1, savedEpic, "Эпики не совпадают.");
    }

    @Test
    public void shouldReturnSubtasksFromEpic() {

        taskManager.createEpic(epic1);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        final List<Subtask> savedSubtasks = taskManager.getSubtasksFromEpic(1);

        assertNotNull(savedSubtasks.get(0), "Подзадача не найдена.");
        assertNotNull(savedSubtasks.get(1), "Подзадача не найдена.");
        assertNotNull(savedSubtasks.get(2), "Подзадача не найдена.");

        assertEquals(subtask1, savedSubtasks.get(0), "Подзадачи не совпадают.");
        assertEquals(subtask2, savedSubtasks.get(1), "Подзадачи не совпадают.");
        assertEquals(subtask3, savedSubtasks.get(2) , "Подзадачи не совпадают.");

        assertEquals(2, subtask1.getId(), "Индентификаторы не совпадают.");
        assertEquals(3, subtask2.getId(), "Индентификаторы не совпадают.");
        assertEquals(4, subtask3.getId(), "Индентификаторы не совпадают.");
    }

    @Test
    public void shouldReturnNullGetSubtasksIfEpicDoesNotExist() {
        assertNull(taskManager.getSubtasksFromEpic(50));
    }

    @Test
    public void shouldRemoveTask() {
        taskManager.createTask(task1);
        taskManager.deleteTask(1);

        assertEquals(0, taskManager.getTasks().size());
        assertThrows(NullPointerException.class, () -> taskManager.getTask(1), "Задача удаляется некорректно.");
    }

    @Test
    public void shouldRemoveAllTasks() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        taskManager.deleteAllTasks();

        assertEquals(0, taskManager.getTasks().size(), "Задачи удаляются некорректно.");
        assertThrows(NullPointerException.class, () -> taskManager.getTask(1), "Задача удаляется некорректно.");
        assertThrows(NullPointerException.class, () -> taskManager.getTask(2), "Задача удаляется некорректно.");
        assertThrows(NullPointerException.class, () -> taskManager.getTask(3), "Задача удаляется некорректно.");
    }

    @Test
    public void shouldRemoveSubtask() {
        taskManager.createEpic(epic1);

        taskManager.createSubtask(subtask1);

        taskManager.deleteSubtask(2);
        assertEquals(0, taskManager.getSubtasks().size());
        assertThrows(NullPointerException.class, () -> taskManager.getTask(2), "Подзадача удаляется некорректно.");
    }

    @Test
    public void shouldRemoveAllSubtasks() {
        taskManager.createEpic(epic1);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        taskManager.deleteAllSubtasks();

        assertEquals(0, taskManager.getSubtasks().size(), "Подзадачи удаляются некорректно.");
        assertThrows(NullPointerException.class, () -> taskManager.getSubtask(1), "Подзадача удаляется некорректно.");
        assertThrows(NullPointerException.class, () -> taskManager.getSubtask(2), "Подзадача удаляется некорректно.");
        assertThrows(NullPointerException.class, () -> taskManager.getSubtask(3), "Подзадача удаляется некорректно.");
    }

    @Test
    public void shouldRemoveEpic() {
        taskManager.createEpic(epic1);

        taskManager.deleteEpic(1);
        assertEquals(0, taskManager.getEpics().size(), "Эпик удаляется некорректно.");
        assertThrows(NullPointerException.class, () -> taskManager.getEpic(1), "Эпик удаляется некорректно.");
    }

    @Test
    public void shouldRemoveEpicAndItsSubtasks() {
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        taskManager.deleteEpic(1);
        assertEquals(0, taskManager.getEpics().size(), "Эпик удаляется некорректно.");
        assertThrows(NullPointerException.class, () -> taskManager.getEpic(1),
                "Эпик удаляется некорректно.");
        assertEquals(0, taskManager.getSubtasks().size(), "Подзадачи из эпика удаляются некорректно.");
        assertThrows(NullPointerException.class, () -> taskManager.getSubtask(2), "Подзадача из эпика " +
                "удаляется некорректно.");
        assertThrows(NullPointerException.class, () -> taskManager.getSubtask(3), "Подзадача из эпика " +
                "удаляется некорректно.");
        assertThrows(NullPointerException.class, () -> taskManager.getSubtask(4), "Подзадача из эпика " +
                "удаляется некорректно.");
    }

    @Test
    public void shouldRemoveAllEpics() {
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);

        subtask1.setEpicId(1);
        subtask2.setEpicId(2);
        subtask3.setEpicId(3);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        taskManager.deleteAllEpics();

        assertEquals(0, taskManager.getSubtasks().size(), "Подзадачи удаляются некорректно.");
        assertEquals(0, taskManager.getEpics().size(), "Эпики удаляются некорректно.");

        assertThrows(NullPointerException.class, () -> taskManager.getEpic(1),
                "Подзадача удаляется некорректно.");
        assertThrows(NullPointerException.class, () -> taskManager.getEpic(2),
                "Подзадача удаляется некорректно.");
        assertThrows(NullPointerException.class, () -> taskManager.getEpic(3),
                "Подзадача удаляется некорректно.");

        assertThrows(NullPointerException.class, () -> taskManager.getSubtask(4),
                "Подзадача удаляется некорректно.");
        assertThrows(NullPointerException.class, () -> taskManager.getSubtask(5),
                "Подзадача удаляется некорректно.");
        assertThrows(NullPointerException.class, () -> taskManager.getSubtask(6),
                "Подзадача удаляется некорректно.");
    }

    @Test
    public void shouldReturnNewStatusWhenEpicIsEmpty() {
        taskManager.createEpic(epic1);
        assertEquals(Status.NEW, epic1.getStatus(), "Статус эпика рассчитывается некорректно.");

        taskManager.createSubtask(subtask1);
        assertEquals(Status.NEW, epic1.getStatus(), "Статус эпика рассчитывается некорректно.");

        taskManager.deleteSubtask(2);
        assertEquals(Status.NEW, epic1.getStatus(), "Статус эпика рассчитывается некорректно.");
    }

    @Test
    public void shouldReturnNewStatusIfAllSubtasksIsNew() {
        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.NEW);
        subtask3.setStatus(Status.NEW);

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        Status epicStatus = taskManager.getEpic(1).getStatus();
        assertEquals(Status.NEW, epicStatus, "Статус эпика рассчитывается некорректно.");
    }

    @Test
    public void shouldReturnDoneStatusIfAllSubtasksIsDone() {
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        subtask3.setStatus(Status.DONE);

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        Status epicStatus = taskManager.getEpic(1).getStatus();
        assertEquals(Status.DONE, epicStatus, "Статус эпика рассчитывается некорректно.");
    }

    @Test
    public void shouldInProgressNewStatusIfAllSubtasksIsNewAndDone() {
        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.DONE);
        subtask3.setStatus(Status.NEW);

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        Status epicStatus = taskManager.getEpic(1).getStatus();
        assertEquals(Status.IN_PROGRESS, epicStatus, "Статус эпика рассчитывается некорректно.");
    }

    @Test
    public void shouldInProgressNewStatusIfAllSubtasksIsInProgress() {
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        subtask3.setStatus(Status.IN_PROGRESS);

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        Status epicStatus = taskManager.getEpic(1).getStatus();
        assertEquals(Status.IN_PROGRESS, epicStatus, "Статус эпика рассчитывается некорректно.");
    }

    @Test
    public void shouldReturnEmptyHistory() {
        assertEquals(0, taskManager.getHistory().size(), "История не пуста при создании нового менеджера.");

        taskManager.createEpic(epic1);
        taskManager.createTask(task1);
        taskManager.createSubtask(subtask1);

        taskManager.getEpic(1);
        taskManager.getTask(2);
        taskManager.getSubtask(3);

        taskManager.deleteEpic(1);
        taskManager.deleteTask(2);
        taskManager.deleteSubtask(3);

        assertEquals(0, taskManager.getHistory().size(), "История не пуста при создании нового менеджера.");
    }

    @Test
    public void shouldReturnRightHistoryWhenSomeDeleted() {

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        taskManager.getEpic(1);
        taskManager.getEpic(2);
        taskManager.getEpic(3);
        taskManager.getTask(4);
        taskManager.getSubtask(7);
        taskManager.getSubtask(8);
        taskManager.getSubtask(9);

        assertEquals(List.of(1, 2, 3, 4, 7, 8, 9), taskManager.getIdHistory(), "История сохраняется некорректно.");

        taskManager.deleteEpic(1);

        assertEquals(List.of(2, 3, 4), taskManager.getIdHistory(), "История сохраняется некорректно.");

        taskManager.deleteEpic(3);

        assertEquals(List.of(2, 4), taskManager.getIdHistory(), "История сохраняется некорректно.");

        taskManager.deleteTask(4);

        assertEquals(List.of(2), taskManager.getIdHistory(), "История сохраняется некорректно.");
    }

    @Test
    public void shouldReturnRightHistoryWhenSomeDubbed() {

        taskManager.createEpic(epic1);
        taskManager.createTask(task1);
        taskManager.createSubtask(subtask1);

        taskManager.getEpic(1);
        taskManager.getTask(2);
        taskManager.getSubtask(3);
        taskManager.getTask(2);
        taskManager.getEpic(1);
        taskManager.getEpic(1);

        assertEquals(List.of(3, 2, 1), taskManager.getIdHistory()
                , "История сохраняется некорректно при дублировании просмотров.");
    }

}