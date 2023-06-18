package test.managersTest;

import main.managers.HttpTaskManager;
import main.managers.Managers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;

public class HttpTaskManagerTest extends TasksManagerTest<HttpTaskManager> {
    @BeforeEach
    public void initManager() {
        taskManager = Managers.getHttpTaskManager(8078);
    }

    @AfterAll
    static public void stopServer() {

    }
}
