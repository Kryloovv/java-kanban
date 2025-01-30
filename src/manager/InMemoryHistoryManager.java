package manager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> listHistory = new ArrayList<>() {
    };

    @Override
    public void add(Task task) {
        if (listHistory.size() == 10) {
            listHistory.remove(0);
        }
        listHistory.add(task);
    }

    @Override
    public ArrayList<Task> getDefaultHistory() {
        return listHistory;
    }
}
