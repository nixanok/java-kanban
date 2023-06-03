package taskCore.test;

import org.junit.jupiter.api.Test;
import taskCore.Status;
import taskCore.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    public void shouldReturnRightEpicId() {
        Subtask subtask = new Subtask("title", "disc", Status.IN_PROGRESS
                , LocalDateTime.parse("2023-06-03T13:54"), Duration.ofMinutes(10), 3);
        int id = subtask.getEpicId();
        assertEquals(3, id);
    }

}