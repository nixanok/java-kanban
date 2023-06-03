package managers.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import managers.FileBackedTasksManager;
import managers.InMemoryTasksManager;
import managers.Managers;
import managers.TasksManager;

import taskCore.Subtask;
import taskCore.Task;
import taskCore.Epic;

import java.io.*;
import java.util.Collections;
import java.util.List;

import exceptions.ManagerSaveException;

public class FileBackedTasksManagerTest extends TasksManagerTest<InMemoryTasksManager> {

    public static final  String PATH_TO_DATA_TEST = "src/managers/test/resources/dataTest.csv";

    @BeforeEach
    public void initManager() {
        taskManager = (FileBackedTasksManager) Managers.getFileBackedTasksManager(PATH_TO_DATA_TEST);
    }

    @AfterEach
    public void clearFile() {
        try {
            new FileWriter(PATH_TO_DATA_TEST, false).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldReturnEmptyListTasksAfterSerialization() {
        TasksManager tasksManagerFromFile = FileBackedTasksManager
                .loadFromFile(new File(PATH_TO_DATA_TEST));
        assertEquals(0, tasksManagerFromFile.getTasks().size());
    }

    @Test
    public void shouldReturnListTasksAfterSerialization() {
        TasksManager tasksManager = Managers.getFileBackedTasksManager(PATH_TO_DATA_TEST);

        tasksManager.createEpic(epic1);
        tasksManager.createEpic(epic2);

        tasksManager.createTask(task1);

        tasksManager.createSubtask(subtask1);
        tasksManager.createSubtask(subtask2);
        tasksManager.createSubtask(subtask3);

        TasksManager tasksManagerFromFile = FileBackedTasksManager
                .loadFromFile(new File(PATH_TO_DATA_TEST));
        assertEquals(2, tasksManagerFromFile.getEpics().size()
                , "Эпики загружены из файла некорректно.");
        assertEquals(1, tasksManagerFromFile.getTasks().size()
                , "Задачи загружены из файла некорректно.");
        assertEquals(3, tasksManagerFromFile.getSubtasks().size()
                , "Подзадачи загружены из файла некорректно.");

        List<Task> tasksFromFile = tasksManagerFromFile.getTasks();
        List<Subtask> subtasksFromFile = tasksManagerFromFile.getSubtasks();
        List<Epic> epicsFromFile = tasksManagerFromFile.getEpics();

        List<Task> tasks = List.of(task1);
        List<Subtask> subtasks = List.of(subtask1, subtask2, subtask3);
        List<Epic> epics = List.of(epic1, epic2);

        assertEquals(tasks, tasksFromFile, "Задачи загружены из файла некорректно.");
        assertEquals(subtasks, subtasksFromFile, "Подзадачи загружены из файла некорректно.");
        assertEquals(epics, epicsFromFile, "Эпики загружены из файла некорректно.");
    }

    @Test
    public void shouldReturnEpicWithoutSubtasksAfterSerialization() {
        TasksManager tasksManager = Managers.getFileBackedTasksManager(PATH_TO_DATA_TEST);

        tasksManager.createEpic(epic1);

        TasksManager tasksManagerFromFile = FileBackedTasksManager
                .loadFromFile(new File(PATH_TO_DATA_TEST));

        assertEquals(tasksManager.getEpic(1), tasksManagerFromFile.getEpic(1));
    }

    @Test
    public void shouldReturnHistory() {
        TasksManager tasksManager = Managers.getFileBackedTasksManager(PATH_TO_DATA_TEST);

        tasksManager.createEpic(epic1);
        tasksManager.createEpic(epic2);

        tasksManager.createTask(task1);

        tasksManager.createSubtask(subtask1);
        tasksManager.createSubtask(subtask2);
        tasksManager.createSubtask(subtask3);

        tasksManager.getEpic(1);
        tasksManager.getTask(3);
        tasksManager.getSubtask(4);
        tasksManager.getSubtask(5);

        TasksManager tasksManagerFromFile = FileBackedTasksManager
                .loadFromFile(new File(PATH_TO_DATA_TEST));

        assertEquals(List.of(1, 3, 4, 5), tasksManagerFromFile.getIdHistory());
    }

    @Test
    public void shouldReturnEmptyHistory() {
        TasksManager tasksManager = Managers.getFileBackedTasksManager(PATH_TO_DATA_TEST);

        tasksManager.createEpic(epic1);
        tasksManager.createEpic(epic2);

        tasksManager.createTask(task1);

        tasksManager.createSubtask(subtask1);
        tasksManager.createSubtask(subtask2);
        tasksManager.createSubtask(subtask3);

        TasksManager tasksManagerFromFile = FileBackedTasksManager
                .loadFromFile(new File(PATH_TO_DATA_TEST));

        assertEquals(Collections.emptyList(), tasksManagerFromFile.getIdHistory());
    }

    @Test
    public void shouldThrowSaveExceptionIfIOException() {
        FileBackedTasksManager tasksManager =
                (FileBackedTasksManager) Managers.getFileBackedTasksManager("resources");
        assertThrows(ManagerSaveException.class, tasksManager::save, "Ошибка записи.");
    }
}
