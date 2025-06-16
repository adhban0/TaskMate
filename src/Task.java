public class Task {
    private int id;
    private String Title;
    private String description;
    private String dueDate;
    private boolean isCompleted;

    public Task(int id, String title, String description, String dueDate) {
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void markCompleted() {
        isCompleted = true;
    }
}
