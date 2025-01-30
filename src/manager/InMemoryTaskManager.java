package manager;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> taskTable = new HashMap<>();
    private final HashMap<Integer, Epic> epicTable = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskTable = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int counterId = 0;

    @Override
    public HashMap<Integer, Task> getTaskTable() {
        return taskTable;
    }

    @Override
    public HashMap<Integer, Epic> getEpicTable() {
        return epicTable;
    }

    @Override
    public HashMap<Integer, Subtask> getSubtaskTable() {
        return subtaskTable;
    }

    @Override
    public int generateCounterId() {
        return ++counterId;
    }

    @Override
    public void addTask(Task task) {
        taskTable.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epicTable.put(epic.getId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtaskTable.put(subtask.getId(), subtask);
        Epic epic = epicTable.get(subtask.getEpicId());
        epic.addSubtaskId(subtask.getId());
        updateEpicStatus(epic); // Обновляем статус эпика на основе новой подзадачи
    }

    @Override
    public void updateTask(Task task) {
        if (taskTable.containsKey(task.getId())) {
            taskTable.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epicTable.containsKey(epic.getId())) {
            Epic currentEpic = epicTable.get(epic.getId());
            ArrayList<Integer> subtaskIds = currentEpic.getSubtaskIds();

            epicTable.put(epic.getId(), epic);
            epic.setSubtaskIds(subtaskIds); // Сохраняем существующий список подзадач
            updateEpicStatus(epic); // Пересчитываем статус эпика
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtaskTable.containsKey(subtask.getId())) {
            subtaskTable.put(subtask.getId(), subtask);
            Epic epic = epicTable.get(subtask.getEpicId());
            updateEpicStatus(epic); // Обновляем статус эпика при изменении подзадачи
        }
    }

    @Override
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

    @Override
    public Task getTask(int taskId) {
        if (taskTable.containsKey(taskId)) {
            Task task = taskTable.get(taskId);
            addHistory(task);
            return taskTable.get(taskId);
        }
        return null;
    }

    @Override
    public Epic getEpic(int taskId) {
        if (epicTable.containsKey(taskId)) {
            Epic epic = epicTable.get(taskId);
            addHistory(epic);
            return epicTable.get(taskId);
        }
        return null;
    }

    @Override
    public Subtask getSubtask(int taskId) {
        if (subtaskTable.containsKey(taskId)) {
            Subtask subtask = subtaskTable.get(taskId);
            addHistory(subtask);
            return subtaskTable.get(taskId);
        }
        return null;
    }

    @Override
    public void addHistory(Task task) {
        if (historyManager.getDefaultHistory().size() == 10) {
            historyManager.getDefaultHistory().remove(0);
        }
        historyManager.getDefaultHistory().add(task);
    }

    @Override
    public ArrayList<Task> getListHistory() {
        return historyManager.getDefaultHistory();
    }

//    public Object getTaskById(int taskId) {
//        // Ищем задачу по ID в таблицах задач, эпиков и подзадач
//        if (taskTable.containsKey(taskId)) {
//            return taskTable.get(taskId);
//        } else if (epicTable.containsKey(taskId)) {
//            return epicTable.get(taskId);
//        } else if (subtaskTable.containsKey(taskId)) {
//            return subtaskTable.get(taskId);
//        }
//        return null; // Возвращаем null, если задача не найдена
//    }

    @Override
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

    @Override
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

    @Override
    public void deleteTaskById(int taskId) {
        // Ищем и удаляем задачу по ID в таблицах задач, эпиков и подзадач
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

    @Override
    public void deletingAllTasks() {
        // Очищаем все таблицы задач, эпиков и подзадач
        taskTable.clear();
        epicTable.clear();
        subtaskTable.clear();
    }
}
