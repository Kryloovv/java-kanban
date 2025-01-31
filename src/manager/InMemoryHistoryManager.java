package manager;

import task.Task;

import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> listHistory = new ArrayList<>() {
    };

    @Override
    public void add(Task task) {
        if (listHistory.size() == 10) {
            listHistory.remove(0);
        }
        listHistory.add(task);
    }

    @Override
    public List<Task> getDefaultHistory() {
        return listHistory;
    }
}
