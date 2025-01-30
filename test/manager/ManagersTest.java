package manager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import task.Status;
import task.Task;
import java.util.ArrayList;

class ManagersTest {
    // Проверяем, что утилитарный класс
    // всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров;
    @Test
    public void shouldReturnInitializedTaskManagerInstance() {
        TaskManager manager = Managers.getDefault();
        assertNotNull(manager, "Экземпляр менеджера не проинициализирован");
        manager.addTask(new Task("task1", "task1", manager.generateCounterId(), Status.NEW));
        manager.addTask(new Task("task2", "task2", manager.generateCounterId(), Status.NEW));
        assertNotNull(manager.getTask(1), "Задачи с id-1 нет");
        assertNotNull(manager.getTask(2), "Задачи с id-2 нет");
    }
}