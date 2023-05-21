package managers;

import java.nio.file.Paths;

public class Managers {
    private Managers(){}
    public static TasksManager getDefault() { return new InMemoryTasksManager(); }

    public static TasksManager getFileBackedTasksManager() {
        return new FileBackedTasksManager(Paths.get("resources/data.csv")); }

    public static HistoryManager getDefaultHistory() { return new InMemoryHistoryManager(); }

}
