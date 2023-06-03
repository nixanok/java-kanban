package taskCore.test;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import taskCore.Epic;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    public void shouldReturnEmptyListIdsOfSubtasks() {
        Epic epic = new Epic("epic1", "description1");
        assertEquals(Collections.emptyList(), epic.getSubtasksId(), "Список подзадач в Epic непустой.");
    }

    @Test
    public void shouldGet2SubtasksAfterAdd() {
        Epic epic = new Epic("epic1", "description1");

        epic.addSubtask(2);
        epic.addSubtask(3);

        List<Integer> expectedSubtasks = List.of(2, 3);
        assertEquals(expectedSubtasks, epic.getSubtasksId(), "Id подзадач сохранены некорректно.");
    }

    @Test
    public void shouldGetSubtasksAfterAddAndRemove() {
        Epic epic = new Epic("epic1", "description1");

        epic.addSubtask(1);
        epic.addSubtask(2);
        epic.addSubtask(3);
        epic.addSubtask(4);

        epic.removeSubtask(1);
        epic.removeSubtask(2);

        List<Integer> expectedSubtasks = List.of(3, 4);
        assertEquals(expectedSubtasks, epic.getSubtasksId(), "Подзадачи некорректно удаляются из эпика.");
    }

    @Test
    public void shouldReturnEmptyListOfSubtasksAfterDeleteAll() {
        Epic epic = new Epic("epic1", "description1");

        epic.addSubtask(1);
        epic.addSubtask(2);
        epic.addSubtask(3);
        epic.addSubtask(4);

        epic.removeAllSubtasks();

        assertEquals(Collections.emptyList(), epic.getSubtasksId(), "Подзадачи некорректно удаляются из эпика.");
    }

}