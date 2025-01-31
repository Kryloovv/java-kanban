package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;
import java.util.HashMap;

public interface TaskManager {
    HashMap<Integer, Task> getTaskTable();

    HashMap<Integer, Epic> getEpicTable();

    HashMap<Integer, Subtask> getSubtaskTable();

    int generateCounterId();

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void updateEpicStatus(Epic epic);

    Task getTask(int taskId);

    Epic getEpic(int taskId);

    Subtask getSubtask(int taskId);

    void addHistory(Task task);

    List<Task> getListHistory();

    List<Subtask> getSubtasksByEpicId(int epicId);

    List<Object> getAllTasks();

    void deleteTaskById(int taskId);

    void deletingAllTasks();
}
