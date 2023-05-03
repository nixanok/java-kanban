package managers;

import additionalStructures.CustomLinkedList;
import additionalStructures.Node;

import taskCore.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {
    final private CustomLinkedList<Task> historyTasks;

    final private Map<Integer, Node<Task>> indicesOfNodes;

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

        final Node<Task> node = historyTasks.linkLast(task);
        indicesOfNodes.put(id, node);
    }

    @Override
    public void delete(int id) {
        final Node<Task> node = indicesOfNodes.remove(id);

        if(node != null)
            historyTasks.delete(node);
    }

    @Override
    public List<Task> getHistory(){
        return historyTasks.getList();
    }
}
