import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        taskTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setBounds(20, 70, 550, 250);
        add(scrollPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn){
            JTextField titleField = new JTextField();
            JTextField descField = new JTextField();
            JTextField dateField = new JTextField();

            Object[] fields = {"Title:",titleField,"Description:",descField,"Due Date:",dateField};
            int option = JOptionPane.showConfirmDialog(this,fields,"Add New Task",JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION){
                taskManager.addTask(titleField.getText(),descField.getText(),dateField.getText());
                refreshTable();
            }
        }
        else if (e.getSource()==deleteBtn){
            int [] rows  = taskTable.getSelectedRows();
            if (rows.length >= 0){//?????
                List<Integer> ids = new ArrayList<>();
                for (int row : rows){
                    int id = (int) tableModel.getValueAt(row,0);
                    ids.add(id);
                }
                taskManager.deleteTasks(ids);
                refreshTable();
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
                    JTextField dateField = new JTextField(task.getDueDate());

                    Object[] fields = {"Title:",titleField,"Description:",descField,"Due Date:",dateField};

                    int option = JOptionPane.showConfirmDialog(this, fields, "Edit Task", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        task.setTitle(titleField.getText());
                        task.setDescription(descField.getText());
                        task.setDueDate(dateField.getText());
                        refreshTable();
                    }
                }
            }
            else if (rows.length > 1){               JOptionPane.showMessageDialog(this, "Please select only one task to edit.","Unselected Task",JOptionPane.WARNING_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(this, "Please select a task to edit.","Unselected Task",JOptionPane.WARNING_MESSAGE);
            }
        }
        else if (e.getSource()==completeBtn){
            int [] rows  = taskTable.getSelectedRows();
            if (rows.length >= 0){
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
            tasks = tasks.stream().filter(Task::isCompleted).collect(Collectors.toList());//?????
        }
        else if (filter.equals("Incomplete")){
            tasks = tasks.stream().filter(t -> !t.isCompleted()).collect(Collectors.toList());//?????
        }
        for (Task task : tasks) {
            String completedStr = task.isCompleted() ? "Yes" : "No";
            tableModel.addRow(new Object[]{task.getId(),task.getTitle(),task.getDescription(),task.getDueDate(),completedStr});
        }
        }
    }

