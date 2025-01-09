import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> taskTable = new HashMap<>();
    private HashMap<Integer, Epic> epicTable = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskTable = new HashMap<>();
    private int counterId = 0;

    public HashMap<Integer, Task> getTaskTable() {
        return taskTable;
    }

    public HashMap<Integer, Epic> getEpicTable() {
        return epicTable;
    }

    public HashMap<Integer, Subtask> getSubtaskTable() {
        return subtaskTable;
    }

    public int getCounterId() {
        return ++counterId;
    }

    public void addTask(Task task) {
        taskTable.put(task.id, task);
    }

    public void addEpic(Epic epic) {
        epicTable.put(epic.id, epic);
    }

    public void addSubtask(Subtask subtask) {
        subtaskTable.put(subtask.id, subtask);
        Epic epic = epicTable.get(subtask.getEpicId());
        epic.addSubtaskId(subtask.id);
        updateEpicStatus(epic); // Обновляем статус эпика на основе новой подзадачи
    }

    public void updateTask(Task task) {
        if (taskTable.containsKey(task.id)) {
            taskTable.put(task.id, task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epicTable.containsKey(epic.id)) {
            Epic currentEpic = epicTable.get(epic.id);
            ArrayList<Integer> subtaskIds = currentEpic.getSubtaskIds();

            epicTable.put(epic.id, epic);
            epic.setSubtaskIds(subtaskIds); // Сохраняем существующий список подзадач
            updateEpicStatus(epic); // Пересчитываем статус эпика
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtaskTable.containsKey(subtask.id)) {
            subtaskTable.put(subtask.id, subtask);
            Epic epic = epicTable.get(subtask.getEpicId());
            updateEpicStatus(epic); // Обновляем статус эпика при изменении подзадачи
        }
    }

    public void updateEpicStatus(Epic epic) {
        if (epic.getSubtaskIds().isEmpty()) {
            return; // Если подзадач нет, статус эпика не изменяем
        }
        // Флаги для определения статуса эпика
        boolean isStatusNew = false;
        boolean isStatusInProgress = false;
        boolean isStatusDone = false;

        // Определяем статус эпика на основе статусов подзадач
        for (Integer subtaskId : epic.getSubtaskIds()) {
            if (subtaskTable.get(subtaskId).getStatus() == Status.NEW) {
                isStatusNew = true;
            } else if (subtaskTable.get(subtaskId).getStatus() == Status.IN_PROGRESS) {
                isStatusInProgress = true;
            } else {
                isStatusDone = true;
            }
        }

        // Устанавливаем статус эпика
        if (isStatusNew && (!isStatusInProgress) && (!isStatusDone)) {
            epic.setStatus(Status.NEW);
        } else if ((!isStatusNew) && (!isStatusInProgress) && isStatusDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    public Object getTaskById(int taskId) {
        // Ищем задачу по ID в таблицах задач, эпиков и подзадач
        if (taskTable.containsKey(taskId)) {
            return taskTable.get(taskId);
        } else if (epicTable.containsKey(taskId)) {
            return epicTable.get(taskId);
        } else if (subtaskTable.containsKey(taskId)) {
            return subtaskTable.get(taskId);
        }
        return null; // Возвращаем null, если задача не найдена
    }

    public ArrayList<Subtask> getSubtasksByEpicId(int epicId) {
        if (!epicTable.isEmpty()) {
            Epic epic = epicTable.get(epicId);
            ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
            ArrayList<Subtask> subtaskList = new ArrayList<>();
            // Создаем список подзадач по их ID
            for (Integer subtaskId : subtaskIds) {
                subtaskList.add(subtaskTable.get(subtaskId));
            }
            return subtaskList;
        }
        return null; // Если эпик не найден
    }

    public ArrayList<Object> getAllTasks() {
        ArrayList<Object> allTasks = new ArrayList<>();
        // Добавляем задачи
        if (!taskTable.isEmpty()) {
            for (Task task : taskTable.values()) {
                allTasks.add(task);
            }
        }
        // Добавляем эпики
        if (!epicTable.isEmpty()) {
            for (Epic epic : epicTable.values()) {
                allTasks.add(epic);
            }
        }
        // Добавляем подзадачи
        if (!subtaskTable.isEmpty()) {
            for (Subtask subtask : subtaskTable.values()) {
                allTasks.add(subtask);
            }
        }
        return allTasks;
    }

    public void deleteTaskById(int taskId) {
        if (taskTable.containsKey(taskId)) {
            taskTable.remove(taskId);
        } else if (epicTable.containsKey(taskId)) {
            // Удаляем все подзадачи, связанные с эпиком
            Epic epic = epicTable.get(taskId);
            ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
            if (!subtaskIds.isEmpty()) {
                for (Integer subtaskId : subtaskIds) {
                    subtaskTable.remove(subtaskId);
                }
            }
            epicTable.remove(taskId); // Удаляем эпик после подзадач
        } else if (subtaskTable.containsKey(taskId)) {
            // Удаляем подзадачу и обновляем список подзадач в эпике
            Subtask subtask = subtaskTable.get(taskId);
            Epic epic = epicTable.get(subtask.getEpicId());

            ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
            subtaskIds.remove(Integer.valueOf(taskId));
            epic.setSubtaskIds(subtaskIds);
            updateEpicStatus(epic); // Обновляем статус эпика после удаления подзадачи
            subtaskTable.remove(taskId);
        }
    }

    public void deletingAllTasks() {
        // Очищаем все таблицы задач, эпиков и подзадач
        taskTable.clear();
        epicTable.clear();
        subtaskTable.clear();
    }
}
