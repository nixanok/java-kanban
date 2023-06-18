package main.managers;

import main.additionalStructures.CustomLinkedList;
import main.additionalStructures.Node;

import main.taskCore.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

    @Override
    public List<Integer> getIdHistory(){
        return historyTasks.getList()
                .stream()
                .map(Task::getId)
                .collect(Collectors.toList());
    }
}
