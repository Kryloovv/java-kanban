package manager;

import task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {
    // Хранит соответствие между ID задачи и узлом (Node) в двусвязном списке
    private final HashMap<Integer, Node> nodeMap = new HashMap<>();
    // Указатели на первый (head) и последний (tail) элементы двусвязного списка
    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        if (nodeMap.containsKey(task.getId())) {
            // Если задача уже есть, удаляем старый узел перед добавлением нового
            remove(task.getId());
        }
        addLast(task);
        nodeMap.put(task.getId(), tail);
    }

    public void addLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            // Если список был пуст, обновляем head
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
    }

    @Override
    public void remove(int id) {
        if (nodeMap.isEmpty()) {
            return;
        }
        removeNode(nodeMap.get(id));
        nodeMap.remove(id);
    }

    public void removeNode(Node node) {
        if (Objects.nonNull(node.prev)) {
            // Пропускаем удаляемый узел, обновляя ссылку у предыдущего
            node.prev.next = node.next;
        }
        if (Objects.nonNull(node.next)) {
            // Пропускаем удаляемый узел, обновляя ссылку у следующего
            node.next.prev = node.prev;
        }
        if (node.equals(head)) {
            // Если удаляем head, обновляем ссылку на новый первый узел
            head = node.next;
        }
        if (node.equals(tail)) {
            // Если удаляем tail, обновляем ссылку на новый последний узел
            tail = node.prev;
        }
    }

    @Override
    public List<Task> getDefaultHistory() {
        List<Task> result = new ArrayList<>();
        Node node = head;
        while (Objects.nonNull(node)) {
            result.add(node.getTask());
            node = node.next;
        }
        return result;
    }

    private static class Node {
        Task task;
        Node prev;
        Node next;

        private Node(Node prev, Task task, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }

        private Task getTask() {
            return task;
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Node node)) return false;
            return Objects.equals(task, node.task) && Objects.equals(prev, node.prev) && Objects.equals(next, node.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(task, prev, next);
        }
    }
}