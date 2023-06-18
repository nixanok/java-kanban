package test.managersTest;

import main.managers.InMemoryTasksManager;
import main.managers.Managers;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTasksManagerTest extends TasksManagerTest<InMemoryTasksManager> {

    @BeforeEach
    public void initManager() {
        taskManager = Managers.getInMemoryTasksManager();
    }
}
