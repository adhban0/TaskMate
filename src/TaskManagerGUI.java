import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import com.github.lgooddatepicker.components.DateTimePicker;

public class TaskManagerGUI extends JFrame implements ActionListener {
    private TaskManager taskManager;
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private JButton addBtn, deleteBtn,editBtn,completeBtn;
    private JComboBox<String> filterBox;
    public TaskManagerGUI(){
        taskManager = new TaskManager();
        setTitle("TaskMate - Task Manager");
        setSize(600,400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        addBtn = new JButton("Add Task");
        addBtn.setBounds(20, 20, 120, 30);
        addBtn.addActionListener(this);
        add(addBtn);

        deleteBtn = new JButton("Delete Task");
        deleteBtn.setBounds(160, 20, 120, 30);
        deleteBtn.addActionListener(this);
        add(deleteBtn);

        editBtn = new JButton("Edit Task");
        editBtn.setBounds(280, 20, 120, 30);
        editBtn.addActionListener(this);
        add(editBtn);

        completeBtn = new JButton("Mark Completed");
        completeBtn.setBounds(410, 20, 140, 30);
        completeBtn.addActionListener(this);
        add(completeBtn);

        filterBox = new JComboBox<>(new String[]{"All", "Completed", "Incomplete"});
        filterBox.setBounds(560, 20, 100, 30);
        filterBox.addActionListener(this);
        add(filterBox);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Title", "Description", "Due Date", "Completed"}, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        taskTable = new JTable(tableModel);
//        taskTable.editab
        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setBounds(20, 70, 550, 250);
        add(scrollPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn){
            JTextField titleField = new JTextField();
            JTextField descField = new JTextField();
            DateTimePicker dateField = new DateTimePicker();

            Object[] fields = {"Title:",titleField,"Description:",descField,"Due Date:",dateField};
            int option = JOptionPane.showConfirmDialog(this,fields,"Add New Task",JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION){
                if (titleField.getText().trim().isEmpty()){
                    JOptionPane.showMessageDialog(this,"Title is required.","Missing Title",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                LocalDateTime dueDate = dateField.getDateTimeStrict();
                taskManager.addTask(titleField.getText().trim(),descField.getText().trim(),dueDate);
                refreshTable();
            }
        }
        else if (e.getSource()==deleteBtn){
            int row  = taskTable.getSelectedRow();
            if (row >= 0){//?????
                int id = (int) tableModel.getValueAt(row,0);
                taskManager.deleteTask(id);
                refreshTable();
            }
            else{
                JOptionPane.showMessageDialog(this, "Please select a task to delete.");
            }
        }
        else if (e.getSource()==editBtn) {
            int row = taskTable.getSelectedRow();
            if (row >= 0) {
                int id = (int) tableModel.getValueAt(row, 0);
                Task task = taskManager.getTask(id);
                if (task != null) {
                    JTextField titleField = new JTextField(task.getTitle());
                    JTextField descField = new JTextField(task.getDescription());
                    DateTimePicker dateField = new DateTimePicker();
                    dateField.setDateTimePermissive(task.getDueDate());

                    Object[] fields = {"Title:",titleField,"Description:",descField,"Due Date:",dateField};

                    int option = JOptionPane.showConfirmDialog(this, fields, "Edit Task", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        if (titleField.getText().trim().isEmpty()){
                            JOptionPane.showMessageDialog(this,"Title is required.","Missing Title",JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        LocalDateTime dueDate = dateField.getDateTimeStrict();
                        task.setTitle(titleField.getText().trim());
                        task.setDescription(descField.getText().trim());
                        task.setDueDate(dueDate);
                        refreshTable();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to edit.");
            }
        }
        else if (e.getSource()==completeBtn){
            int row = taskTable.getSelectedRow();
            if (row >= 0) {
                int id = (int) tableModel.getValueAt(row, 0);
                Task task = taskManager.getTask(id);
                if (task != null && !task.isCompleted()) {
                    task.markCompleted();
                    refreshTable();
                }
            }
        }
            else if (e.getSource()==filterBox){
            refreshTable();
            }
            else {
                JOptionPane.showMessageDialog(this, "Please select a task to mark as completed.");
            }
        }

    private void refreshTable(){
        tableModel.setRowCount(0);
        List<Task> tasks = taskManager.getTaskList();
        String filter = (String) filterBox.getSelectedItem();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        if (filter.equals("Completed")){
            tasks = tasks.stream().filter(Task::isCompleted).collect(Collectors.toList());//?????
        }
        else if (filter.equals("Incomplete")){
            tasks = tasks.stream().filter(t -> !t.isCompleted()).collect(Collectors.toList());//?????
        }
        for (Task task : tasks) {
            tableModel.addRow(new Object[]{task.getId(),task.getTitle(),task.getDescription(),task.getDueDate() != null ?task.getDueDate().format(formatter):"",task.isCompleted()});
        }
        }
    }

