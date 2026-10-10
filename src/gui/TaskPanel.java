package gui;

import exceptions.ValidationException;
import model.Task;
import service.TaskService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskPanel extends JPanel {
    private final int userId; private final TaskService taskService=new TaskService(); private JTable table; private DefaultTableModel model; private List<Task> tasks;
    public TaskPanel(int userId){this.userId=userId;setLayout(new BorderLayout());setOpaque(false);build();loadTasks();}
    private void build(){
        JPanel card=Theme.card();card.setLayout(new BorderLayout(10,12));
        JPanel top=new JPanel(new BorderLayout());top.setOpaque(false);JPanel t=new JPanel();t.setOpaque(false);t.setLayout(new BoxLayout(t,BoxLayout.Y_AXIS));JLabel h=new JLabel("Your task board");h.setFont(Theme.H2);JLabel s=new JLabel("Review priorities, update progress or remove completed work.");s.setForeground(Theme.MUTED);s.setFont(Theme.SMALL);t.add(h);t.add(Box.createVerticalStrut(4));t.add(s);top.add(t,BorderLayout.WEST);JButton refresh=Theme.outlineButton("↻  Refresh");refresh.addActionListener(e->loadTasks());top.add(refresh,BorderLayout.EAST);card.add(top,BorderLayout.NORTH);
        String[] cols={"ID","TASK","DEADLINE","IMPORTANCE","PRIORITY","STATUS","PROGRESS"};model=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};table=new JTable(model);Theme.styleTable(table);Theme.statusRenderer(table,5);table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);card.add(Theme.scroll(table),BorderLayout.CENTER);
        JPanel bottom=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));bottom.setOpaque(false);JButton delete=Theme.outlineButton("Delete");JButton update=Theme.button("Update Task");delete.addActionListener(e->deleteSelected());update.addActionListener(e->updateSelected());bottom.add(delete);bottom.add(update);card.add(bottom,BorderLayout.SOUTH);add(card,BorderLayout.CENTER);
    }
    private void loadTasks(){if(model==null)return;model.setRowCount(0);tasks=taskService.getUserTasks(userId);DateTimeFormatter f=DateTimeFormatter.ofPattern("dd MMM yyyy  HH:mm");for(Task t:tasks)model.addRow(new Object[]{t.getTaskId(),t.getTitle(),t.getDeadline().format(f),t.getImportance(),String.format("%.2f",t.getPriorityScore()),t.getStatus(),t.getProgress()+"%"});}
    private void updateSelected(){int row=table.getSelectedRow();if(row<0){JOptionPane.showMessageDialog(this,"Select a task first.");return;}Task t=tasks.get(row);JSpinner progress=new JSpinner(new SpinnerNumberModel(t.getProgress(),0,100,5));JComboBox<String> status=new JComboBox<>(new String[]{"PENDING","IN_PROGRESS","COMPLETED"});status.setSelectedItem(t.getStatus());JPanel p=new JPanel(new GridLayout(2,2,10,10));p.setBorder(new EmptyBorder(10,10,5,10));p.add(new JLabel("Progress"));p.add(progress);p.add(new JLabel("Status"));p.add(status);if(JOptionPane.showConfirmDialog(this,p,"Update Task",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){t.setProgress((Integer)progress.getValue());t.setStatus((String)status.getSelectedItem());if(t.getProgress()==100)t.setStatus("COMPLETED");try{if(taskService.updateTask(t)){JOptionPane.showMessageDialog(this,"Task updated successfully.");loadTasks();}}catch(ValidationException ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"Validation",JOptionPane.WARNING_MESSAGE);}}}
    private void deleteSelected(){int row=table.getSelectedRow();if(row<0){JOptionPane.showMessageDialog(this,"Select a task first.");return;}Task t=tasks.get(row);if(JOptionPane.showConfirmDialog(this,"Delete \""+t.getTitle()+"\"?","Confirm deletion",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){if(taskService.deleteTask(t.getTaskId())){JOptionPane.showMessageDialog(this,"Task deleted.");loadTasks();}}}
}
