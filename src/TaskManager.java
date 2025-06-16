import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class TaskManager {
    private ArrayList<Task> taskList = new ArrayList<>();
    private int id = 1;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    public void addTask(String title, String description, LocalDateTime dueDate){
        taskList.add(new Task(id++,title,description,dueDate));
    }



    public void deleteTask(int id){
        for (Task task : taskList) {
            if (id == task.getId()) {
                taskList.remove(task);
            }
        }
    }
    public ArrayList<Task> getTaskList() {
        return taskList;
    }
    public Task getTask(int id){
        for (Task task : taskList) {
            if (id == task.getId()) {
                    return task;
            }
        }
        return null;
    }
}
