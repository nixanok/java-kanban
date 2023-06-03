package managers.test;

import managers.InMemoryTasksManager;
import managers.Managers;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTasksManagerTest extends TasksManagerTest<InMemoryTasksManager> {

    @BeforeEach
    public void initManager() {
        taskManager = (InMemoryTasksManager) Managers.getDefault();
    }
}
