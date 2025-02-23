package manager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import task.Status;
import task.Task;

class ManagersTest {
    // Проверяем, что утилитарный класс Managers
    // всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров.
    @Test
    public void shouldReturnInitializedTaskManagerInstance() {
        // Получаем экземпляр TaskManager через утилитарный класс Managers
        TaskManager manager = Managers.getDefault();
        assertNotNull(manager, "Экземпляр менеджера не проинициализирован");

        manager.addTask(new Task("task1", "task1", manager.generateCounterId(), Status.NEW));
        manager.addTask(new Task("task2", "task2", manager.generateCounterId(), Status.NEW));
        // Проверяем, что задачи были успешно добавлены и их можно получить по ID
        assertNotNull(manager.getTask(1), "Задачи с id-1 нет");
        assertNotNull(manager.getTask(2), "Задачи с id-2 нет");
    }
}
