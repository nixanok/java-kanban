package managers;

import additionalStructures.CustomLinkedList;
import additionalStructures.Node;

import taskCore.Task;

import java.util.ArrayList;
import java.util.HashMap;


public class InMemoryHistoryManager implements HistoryManager {
    final private CustomLinkedList<Task> historyTasks;

    final private HashMap<Integer, Node> indicesOfNodes;

    public InMemoryHistoryManager() {
        historyTasks = new CustomLinkedList<>();
        indicesOfNodes = new HashMap<>();
    }

    @Override
    public void add(Task task) {

        final int id = task.getId();

        if (indicesOfNodes.containsKey(id)) {
            delete(id);
        }

        final Node node = historyTasks.linkLast(task);
        indicesOfNodes.put(id, node);
    }

    @Override
    public void delete(int id) {
        final Node node = indicesOfNodes.get(id);
        historyTasks.delete(node);
        indicesOfNodes.remove(id);
    }

    @Override
    public ArrayList<Task> getHistory(){
        return historyTasks.getList();
    }
}
