package manager;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    // Проверяем, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @Test
    public void testHistoryManagerPreservesPreviousTaskVersion() {
        TaskManager manager = Managers.getDefault();
        manager.addTask(new Task("task1", "task1", manager.generateCounterId(), Status.NEW));
        manager.addEpic(new Epic("epic1", "description", 2));
        manager.addSubtask(new Subtask("subtask1", "description", 3, Status.NEW, 2));

        manager.getTask(1);
        manager.getEpic(2);
        manager.getSubtask(3);

        ArrayList<Task> listHistory = manager.getListHistory();
        assertNotNull(listHistory, "История не пустая.");
        assertEquals(3, listHistory.size(), "Количество элементов в истории не совпадает.");

        manager.updateTask(new Task("task1", "task1", manager.generateCounterId(), Status.DONE));
        manager.getTask(1);
        int idHistory = manager.getListHistory().size() - 1;
        assertEquals(manager.getListHistory().get(idHistory), new Task("task1", "task1", 1, Status.NEW),  "Описание задачи не совпадает.");

        manager.updateEpic(new Epic("epic1", "description", 2));
        manager.getTask(1);
    }
}