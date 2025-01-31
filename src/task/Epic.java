package task;

import java.util.List;
import java.util.ArrayList;


public class Epic extends Task {
    private List<Integer> subtaskIds = new ArrayList<>();;

    public Epic(String name, String description, int id) {
        super(name, description, id);
        this.status = Status.NEW;
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(List<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }


    @Override
    public String toString() {
        return "task.Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", subtaskIds=" + subtaskIds +
                "}";
    }
}