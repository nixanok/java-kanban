package taskCore.test;

import org.junit.jupiter.api.Test;
import taskCore.Status;
import taskCore.Subtask;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    public void shouldReturnRightEpicId() {
        Subtask subtask = new Subtask("title", "disc", Status.IN_PROGRESS, 3);
        int id = subtask.getEpicId();
        assertEquals(3, id);
    }

}