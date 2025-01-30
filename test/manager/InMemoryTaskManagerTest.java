package manager;

import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    // проверяем, что InMemoryTaskManager
    // действительно добавляет задачи разного типа и можно найти их по id;
    @Test
    public void shouldAddTasksOfDifferentTypesAndFindById() {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        manager.addTask(new Task("task1", "task1", manager.generateCounterId(), Status.NEW));
        assertEquals(manager.getTask(1), new Task("task1", "task1", 1, Status.NEW),  "Задачи не совпадают.");

        manager.addEpic(new Epic("epic1", "epic1", manager.generateCounterId()));
        assertEquals(manager.getEpic(2), new Epic("epic1", "epic1", 2),  "Задачи не совпадают.");

        manager.addSubtask(new Subtask("subtask3","subtask3", manager.generateCounterId(), Status.NEW, 2));
        assertEquals(manager.getSubtask(3), new Subtask("subtask3","subtask3", 3, Status.NEW, 2),  "Задачи не совпадают.");
//        manager.addTask(new Task("task1", "task1", manager.generateCounterId(), Status.NEW));
//        manager.addTask(new Task("task1", "task1", 4, Status.NEW));
    }
    // проверяем, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;

    @Test
    public void shouldNotConflictTasksWithSameAndGeneratedIds() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.addTask(new Task("task1", "task1", manager.generateCounterId(), Status.NEW));
        manager.addTask(new Task("task1", "task1", manager.generateCounterId(), Status.NEW));
        manager.addTask(new Task("task1", "task1", 1, Status.NEW));
        manager.addTask(new Task("task1", "task1", 2, Status.NEW));
    }

    // Проверяем неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    public void shouldNotChangeTaskWhenAddedToManager() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task = new Task("task1", "task1", manager.generateCounterId(), Status.NEW);
        manager.addTask(task);
        assertEquals(manager.getTask(1), new Task("task1", "task1", 1, Status.NEW));

        Epic epic = new Epic("epic1", "epic1", manager.generateCounterId());
        manager.addEpic(epic);
        assertEquals(epic, new Epic("epic1", "epic1", 2));
    }
}