import java.time.LocalDateTime;

public class Task {
    private int id;
    private String Title;
    private String description;
    private LocalDateTime dueDate;
    private boolean isCompleted;

    public Task(int id, String title, String description, LocalDateTime dueDate) {
        this.id = id;
        Title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isCompleted = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void markCompleted() {
        isCompleted = true;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}