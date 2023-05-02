package additionalStructures;
import java.util.ArrayList;

public class CustomLinkedList<T> {

    private Node<T> head;

    private Node<T> tail;

    private int size = 0;

    public Node<T> linkLast(T element) {

        final Node<T> newNode;

        if (size == 0) {
            newNode = new Node<>(null, element, null);
            head = newNode;
        }
        else {
            final Node<T> oldTail = tail;
            newNode = new Node<>(oldTail, element, null);
            oldTail.next = newNode;
        }

        tail = newNode;
        size++;

        return newNode;
    }

    public void delete(Node<T> element) {

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

    public ArrayList<T> getList(){
        ArrayList<T> list = new ArrayList<>();

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
