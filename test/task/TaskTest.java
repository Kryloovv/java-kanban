package task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    // проверяем, что экземпляры класса Task равны друг другу, если равен их id;
    @Test
    public void testTasksAreEqualWhenIdsMatch() {
        Task task = new Task("task1", "description", 1, Status.NEW);
        Task taskCopy = new Task("task1", "description", 1, Status.NEW);
        assertEquals(task, taskCopy, "Задачи не совпадают.");
    }
}