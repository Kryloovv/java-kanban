import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        // Создаем две задачи
        manager.addTask(new Task("task1", "task1", manager.generateCounterId(), Status.NEW));
        manager.addTask(new Task("task2", "task2", manager.generateCounterId(), Status.NEW));
        // также эпик с двумя подзадачами
        manager.addEpic(new Epic("epic1", "epic1", manager.generateCounterId()));
        manager.addSubtask(new Subtask("subtask1","subtask1", manager.generateCounterId(), Status.IN_PROGRESS,3));
        manager.addSubtask(new Subtask("subtask2", "subtask2", manager.generateCounterId(), Status.DONE, 3));
        // и эпик с одной подзадачей
        manager.addEpic(new Epic("epic2", "epic2", manager.generateCounterId()));
        manager.addSubtask(new Subtask("subtask3","subtask3", manager.generateCounterId(), Status.NEW, 6));

        // Выводим списки эпиков, задач и подзадач с помощью System.out.println(..).
        System.out.println("--- списки эпиков ---");
        System.out.println(manager.getEpicTable());
        System.out.println("--- списки подзадач ---");
        System.out.println(manager.getSubtaskTable());
        System.out.println("--- списки задач ---");
        System.out.println(manager.getTaskTable());

        // Меняем статусы созданных объектов и выводим их на экран.
        // Меняем статус подзадачи
        manager.updateSubtask(new Subtask("subtask1", "subtask1", 4, Status.DONE,3));
        // Получаем подзадачу по идентификатору и выводим на экран
        System.out.println("--- обновленый статус подзадачи ---");
        System.out.println(manager.getSubtask(4));

        // Меняем статус задачи
        manager.updateTask(new Task("task1", "task1", 1, Status.DONE));
        // Получаем задачу по идентификатору и выводим на экран
        System.out.println("--- обновленый статус задачи ---");
        System.out.println(manager.getTask(1));

        // Проверяем, что статус задачи и подзадачи остался неизменным,
        // а статус эпика правильно пересчитался по статусам подзадач.
        System.out.println("--- обновленый статус эпика ---");
        System.out.println(manager.getEpic(3));

        // Удаляем одну из задач и один из эпиков.
        System.out.println("--- удаляем одну из задач и один эпик ---");
        manager.deleteTaskById(1);
        manager.deleteTaskById(3);
        System.out.println("--- проверяем списки задач ---");
        System.out.println(manager.getAllTasks());
        System.out.println("--- история задач ---");
        System.out.println(manager.getListHistory());
    }
}
