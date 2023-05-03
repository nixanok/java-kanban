package additionalStructures;
import java.util.ArrayList;
import java.util.List;

public class CustomLinkedList<T> {

    private Node<T> head;

    private Node<T> tail;

    private int size = 0;

    public Node<T> linkLast(T element) {

        final Node<T> oldTail = tail;
        final Node<T> newNode;

        newNode = new Node<>(oldTail, element, null);

        if (size == 0) {
            head = newNode;
        }
        else {
            oldTail.next = newNode;
        }

        tail = newNode;
        size++;

        return newNode;
    }

    public void delete(Node<T> element) {

        if (element == null)
            return;

        if (size == 1) {
            head = null;
            tail = null;
        }

        Node<T> prevNode = element.prev;
        Node<T> nextNode = element.next;

        if (prevNode != null) {
            prevNode.next = nextNode;

        }
        else{
            head = nextNode;
        }

        if (nextNode != null) {
            nextNode.prev = prevNode;
        }
        else{
            tail = prevNode;
        }

        size--;

    }

    public List<T> getList(){
        List<T> list = new ArrayList<>();

        Node<T> iterator = head;
        while (iterator != null) {
            list.add(iterator.data);
            iterator = iterator.next;
        }

        return list;
    }

    public int size() {
        return this.size;
    }

}
