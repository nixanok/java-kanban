package test.taskCoreTest;

import taskCore.Status;
import taskCore.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void shouldCalculateEndTimeOfTask() {
        Task task = new Task("title", "description", Status.NEW,
                LocalDateTime.of(2023, 6, 1, 0, 0), Duration.ofMinutes(120));
        assertEquals(LocalDateTime.of(2023, 6, 1, 2, 0), task.getEndTime()
                , "Ожидалось получить верное время конца задачи.");

        task.setStartTime(LocalDateTime.of(2023, 6, 1, 0, 0));
        task.setDuration(Duration.ofMinutes(0));
        assertEquals(LocalDateTime.of(2023, 6, 1, 0, 0), task.getEndTime()
                , "Ожидалось получить равное время начало и конца при 0 длительности задачи.");
    }
}
