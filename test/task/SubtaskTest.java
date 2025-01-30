package task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    // проверяем, что наследники класса Task равны друг другу, если равен их id;
    @Test
    public void testSubtasksAreEqualWhenIdsMatch() {
        Epic epic = new Epic("epic1", "description", 2);
        Subtask subtask = new Subtask("subtask1", "description", 1, Status.NEW, epic.getId());
        Subtask subtaskCopy = new Subtask("subtask1", "description", 1, Status.NEW, epic.getId());
        assertEquals(subtask, subtaskCopy, "Подзадачи не совпадают.");
    }
}