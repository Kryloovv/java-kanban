package manager;

import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    // Проверяем, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данные.
    @Test
    public void testHistoryManagerPreservesPreviousTaskVersion() {
        TaskManager manager = Managers.getDefault(); // Получаем экземпляр менеджера задач

        // Добавляем задачи разных типов в менеджер
        manager.addTask(new Task("task1", "task1", 1, Status.NEW));
        manager.addEpic(new Epic("epic1", "description", 2));
        manager.addSubtask(new Subtask("subtask1", "description", 3, Status.NEW, 2));

        // Запрашиваем задачи, чтобы они попали в историю просмотров
        manager.getTask(1);
        manager.getEpic(2);
        manager.getSubtask(3);

        // Проверяем, что история не пустая
        List<Task> listHistory = manager.getListHistory();
        assertNotNull(listHistory, "История пустая.");
        // Проверяем, что в истории 3 задачи
        assertEquals(3, listHistory.size(), "Количество элементов в истории не совпадает.");

        // Обновляем задачу, изменяя её статус
        manager.updateTask(new Task("task1", "task1", 1, Status.DONE));
        // Повторно запрашиваем обновлённую задачу, чтобы она снова попала в историю
        manager.getTask(1);
        // Получаем последний элемент истории
        int idHistory = manager.getListHistory().size() - 1;
        // Проверяем, что в истории хранится обновленная версия задачи (в стутсе DONE)
        assertEquals(
                manager.getListHistory().get(idHistory),
                new Task("task1", "task1", 1, Status.DONE),
                "Описание задачи не совпадает."
        );

        // Повторно запрашиваем задачу, чтобы она попала в историю
        manager.getTask(1);
        manager.getTask(1);

        // Получаем ID последней задачи в истории
        idHistory = manager.getListHistory().size() - 1;
        // Проверяем, что задача не дублируется в истории
        assertFalse(
                manager.getListHistory().get(idHistory) == manager.getListHistory().get(idHistory - 1),
                "Задача дублируется в истории"
        );
    }
}