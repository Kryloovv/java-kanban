package task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    // проверяем, что наследники класса Task равны друг другу, если равен их id;
    @Test
    public void testEpicsAreEqualWhenIdsMatch() {
        Epic epic = new Epic("epic1", "description", 2);
        Epic epicCopy = new Epic("epic1", "description", 2);
        assertEquals(epic, epicCopy, "Эпики не совпадают.");
    }
}