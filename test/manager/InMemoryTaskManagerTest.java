package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

class InMemoryTaskManagerTest {

    private InMemoryTaskManager manager;

    // Инициализация manager перед каждым тестом
    @BeforeEach
    public void setUpManager() {
        manager = new InMemoryTaskManager();
    }


    // Проверяем, что InMemoryTaskManager добавляет задачи разного типа и можно найти их по id.
    @Test
    public void shouldAddTasksOfDifferentTypesAndFindById() {
        manager.addTask(new Task("task1", "task1", manager.generateCounterId(), Status.NEW));
        // Проверяем, что можно получить задачу по её id и она совпадает с ожидаемой
        assertEquals(manager.getTask(1), new Task("task1", "task1", 1, Status.NEW), "Задачи не совпадают.");

        // Добавляем эпик
        manager.addEpic(new Epic("epic1", "epic1", manager.generateCounterId()));
        assertEquals(manager.getEpic(2), new Epic("epic1", "epic1", 2), "Задачи не совпадают.");
        // Добавляем подзадачу, привязанную к эпику с id 2
        manager.addSubtask(new Subtask("subtask3", "subtask3", manager.generateCounterId(), Status.NEW, 2));
        assertEquals(manager.getSubtask(3), new Subtask("subtask3", "subtask3", 3, Status.NEW, 2), "Задачи не совпадают.");
    }

    // Проверяем, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера.
    @Test
    public void shouldNotConflictTasksWithSameAndGeneratedIds() {
        // Добавляем задачи с авто-сгенерированными id
        manager.addTask(new Task("task1", "task1", manager.generateCounterId(), Status.NEW));
        manager.addTask(new Task("task1", "task1", manager.generateCounterId(), Status.NEW));
        // Добавляем задачи с заданными id
        manager.addTask(new Task("task1", "task1", 1, Status.NEW));
        manager.addTask(new Task("task1", "task1", 2, Status.NEW));
    }

    // Проверяем, что объект задачи остаётся неизменным после добавления в менеджер.
    @Test
    public void shouldNotChangeTaskWhenAddedToManager() {
        // Создаём задачу и добавляем её в менеджер
        Task task = new Task("task1", "task1", manager.generateCounterId(), Status.NEW);
        manager.addTask(task);
        // Проверяем, что полученная из менеджера задача совпадает с исходной
        assertEquals(manager.getTask(1), new Task("task1", "task1", 1, Status.NEW));
        // Создаём эпик и добавляем его в менеджер
        Epic epic = new Epic("epic1", "epic1", manager.generateCounterId());
        manager.addEpic(epic);
        // Проверяем, что эпик в менеджере совпадает с исходным
        assertEquals(epic, new Epic("epic1", "epic1", 2));
    }

    // Проверяем, что при удалении подзадачи из эпика, поле epicId у подзадачи устанавливается в -1
    @Test
    public void removeEpicIdOnSubtaskDeletion() {
        manager.addEpic(new Epic("epic1", "epic1", 1));
        Subtask subtask = new Subtask("subtask2", "subtask2", 2, Status.NEW, 1);
        manager.addSubtask(subtask);

        // Удаляем подзадачу по id
        manager.deleteTaskById(2);
        // Проверяем, что после удаления подзадачи поле epicId у подзадачи установлено в -1
        assertEquals(-1, subtask.getEpicId());
    }

    @Test
    public void removeObsoleteSubtaskIdsFromEpic() {
        Epic epic = new Epic("epic1", "epic1", 1);
        manager.addEpic(epic);
        manager.addSubtask(new Subtask("subtask2", "subtask2", 2, Status.NEW, 1));
        manager.addSubtask(new Subtask("subtask3", "subtask3", 3, Status.NEW, 1));

        // Удаляем подзадачу по id
        manager.deleteTaskById(3);
        // Проверяем, что после удаления подзадачи поле список getSubtaskIds обновился
        assertFalse(epic.getSubtaskIds().contains(3), "Epic содержит id удаленной подзадачи");
    }
}