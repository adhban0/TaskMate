import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import com.github.lgooddatepicker.components.DateTimePicker;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class TaskManagerGUI extends JFrame implements ActionListener {
    private TaskManager taskManager;
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private JButton addBtn, deleteBtn,editBtn,completeBtn;
    private JComboBox<String> filterBox;
    private String CSV_FILE;
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public TaskManagerGUI(String username){
        CSV_FILE = "g://tasks//"+username+".csv";
        taskManager = new TaskManager();
        setTitle("TaskMate - Task Manager");
        setSize(600,400);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        addBtn = new JButton("Add Task");
        addBtn.addActionListener(this);
        deleteBtn = new JButton("Delete Task");
        deleteBtn.addActionListener(this);
        editBtn = new JButton("Edit Task");
        editBtn.addActionListener(this);
        completeBtn = new JButton("Mark Completed");
        completeBtn.addActionListener(this);
        filterBox = new JComboBox<>(new String[]{"All", "Completed", "Incomplete"});
        filterBox.addActionListener(this);
        tableModel = new DefaultTableModel(new Object[]{"ID", "Title", "Description", "Due Date", "Completed"}, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        taskTable = new JTable(tableModel);
        taskTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(taskTable);
        loadTasksFromCSV();
        refreshTable();
        topPanel.add(addBtn);
        topPanel.add(deleteBtn);
        topPanel.add(editBtn);
        topPanel.add(completeBtn);
        topPanel.add(filterBox);
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                    int option = JOptionPane.showConfirmDialog(TaskManagerGUI.this, "Do you want to save tasks before exiting?", "Exit", JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
                    if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                        return;
                    }
                    if (option == JOptionPane.YES_OPTION) {
                        saveTasksToCSV();
                    }
                    System.exit(0);
                }
        });
    }
    private void loadTasksFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = parseCSVLine(line);
                if (parts.length == 5) {
                    int id = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String description = parts[2];
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                    LocalDateTime dueDate = parts[3].isEmpty() ? null : LocalDateTime.parse(parts[3], formatter);
                    String completedStr = parts[4];
                    taskManager.addTask(title, description, dueDate);
                    Task created = taskManager.getTaskList().get(taskManager.getTaskList().size() - 1);
                    if (completedStr.equals("Yes"))
                    {created.setCompleted(true);}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void saveTasksToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            writer.write("ID,Title,Description,DueDate,Completed");
            writer.newLine();
            for (Task task : taskManager.getTaskList()) {
                String dueDateStr = task.getDueDate() != null ? task.getDueDate().format(formatter) : "";
                String completedStr = task.isCompleted() ? "Yes" : "No";
                writer.write(task.getId() + "," + escape(task.getTitle()) + "," + escape(task.getDescription()) + "," + dueDateStr + "," + completedStr);
                writer.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn){
            JTextField titleField = new JTextField();
            JTextField descField = new JTextField();
            DateTimePicker dateField = new DateTimePicker();
            dateField.getDatePicker().getComponentDateTextField().setEditable(false);
            dateField.getTimePicker().getComponentTimeTextField().setEditable(false);
            Object[] fields = {"Title:",titleField,"Description:",descField,"Due Date:",dateField};
            int option = JOptionPane.showConfirmDialog(this,fields,"Add New Task",JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION){
                if (titleField.getText().trim().isEmpty()){
                    JOptionPane.showMessageDialog(this,"Title is required.","Missing Title",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                LocalDateTime dueDate = dateField.getDateTimeStrict();
                if (dueDate == null && dateField.getDatePicker().getDate() != null) {
                    dueDate = dateField.getDatePicker().getDate().atTime(0, 0);
                }
                taskManager.addTask(titleField.getText().trim(),descField.getText().trim(),dueDate);
                refreshTable();
            }
        }
        else if (e.getSource()==deleteBtn){
            int [] rows  = taskTable.getSelectedRows();
            if (rows.length >= 1){
                int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete these task(s)?", "Delete", JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
                if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION || option == JOptionPane.NO_OPTION) {
                    return;
                }
                if (option == JOptionPane.YES_OPTION) {
                    List<Integer> ids = new ArrayList<>();
                    for (int row : rows){
                        int id = (int) tableModel.getValueAt(row,0);
                        ids.add(id);
                    }
                    taskManager.deleteTasks(ids);
                    refreshTable();
                }
            }
            else{
                JOptionPane.showMessageDialog(this, "Please select at least one task to delete.","Unselected Task",JOptionPane.WARNING_MESSAGE);
            }
        }
        else if (e.getSource()==editBtn) {
            int [] rows = taskTable.getSelectedRows();
            if (rows.length == 1) {
                int row = rows[0];
                int id = (int) tableModel.getValueAt(row, 0);
                Task task = taskManager.getTask(id);
                if (task != null) {
                    JTextField titleField = new JTextField(task.getTitle());
                    JTextField descField = new JTextField(task.getDescription());
                    DateTimePicker dateField = new DateTimePicker();
                    dateField.setDateTimePermissive(task.getDueDate());
                    dateField.getDatePicker().getComponentDateTextField().setEditable(false);
                    dateField.getTimePicker().getComponentTimeTextField().setEditable(false);
                    Object[] fields = {"Title:",titleField,"Description:",descField,"Due Date:",dateField};
                    int option = JOptionPane.showConfirmDialog(this, fields, "Edit Task", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        if (titleField.getText().trim().isEmpty()){
                            JOptionPane.showMessageDialog(this,"Title is required.","Missing Title",JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        LocalDateTime dueDate = dateField.getDateTimeStrict();
                        if (dueDate == null && dateField.getDatePicker().getDate() != null) {
                            dueDate = dateField.getDatePicker().getDate().atTime(0, 0);
                        }
                        task.setTitle(titleField.getText().trim());
                        task.setDescription(descField.getText().trim());
                        task.setDueDate(dueDate);
                        refreshTable();
                    }
                }
            }
            else if (rows.length > 1){
                JOptionPane.showMessageDialog(this, "Please select only one task to edit.","Too Many Tasks",JOptionPane.WARNING_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(this, "Please select a task to edit.","Unselected Task",JOptionPane.WARNING_MESSAGE);
            }
        }
        else if (e.getSource()==completeBtn){
            int [] rows  = taskTable.getSelectedRows();
            if (rows.length >= 1){
                for (int row : rows){
                    int id = (int) tableModel.getValueAt(row, 0);
                    Task task = taskManager.getTask(id);
                    if (task != null && !task.isCompleted()) {
                        task.markCompleted();
                    }
                }
                refreshTable();
            }
            else {
                JOptionPane.showMessageDialog(this, "Please select at least one task to mark as completed.","Unselected Task",JOptionPane.WARNING_MESSAGE);
            }
        }
            else if (e.getSource()==filterBox){
            refreshTable();
            }
        }

    private void refreshTable(){
        tableModel.setRowCount(0);
        List<Task> tasks = taskManager.getTaskList();
        String filter = (String) filterBox.getSelectedItem();
        if (filter.equals("Completed")){
            tasks = tasks.stream().filter(Task::isCompleted).collect(Collectors.toList());
        }
        else if (filter.equals("Incomplete")){
            tasks = tasks.stream().filter(t -> !t.isCompleted()).collect(Collectors.toList());
        }
        for (Task task : tasks) {
            String completedStr = task.isCompleted() ? "Yes" : "No";
            tableModel.addRow(new Object[]{task.getId(),task.getTitle(),task.getDescription(),task.getDueDate() != null ?task.getDueDate().format(FORMATTER):"",completedStr});
        }
        }
    private String escape(String text) {
        if (text.contains(",") || text.contains("\"")) {
            text = text.replace("\"", "\"\"");
            return "\"" + text + "\"";
        }
        return text;
    }
    private String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean insideQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                insideQuotes = !insideQuotes;
            } else if (c == ',' && !insideQuotes) {
                result.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString().trim());
        return result.toArray(new String[0]);
    }
}