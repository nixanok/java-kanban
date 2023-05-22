package managers;

import java.io.File;

public class Managers {
    private Managers(){}
    public static TasksManager getDefault() { return new InMemoryTasksManager(); }

    public static TasksManager getFileBackedTasksManager() {
        return new FileBackedTasksManager(new File("resources/data.csv"));
    }

    public static HistoryManager getDefaultHistory() { return new InMemoryHistoryManager(); }

}
