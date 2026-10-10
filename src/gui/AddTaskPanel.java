package gui;

import exceptions.ValidationException;
import model.Category;
import model.Task;
import service.AdminService;
import service.TaskService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddTaskPanel extends JPanel {
    private final int userId; private final TaskService taskService=new TaskService(); private final AdminService adminService=new AdminService();
    private JTextField titleField,deadlineField; private JTextArea descriptionArea; private JComboBox<Category> categoryBox; private JSpinner importanceSpinner;
    public AddTaskPanel(int userId){this.userId=userId;setLayout(new BorderLayout());setOpaque(false);build();loadCategories();}
    private void build(){
        JPanel card=Theme.card();card.setLayout(new BorderLayout(0,14));
        JPanel head=new JPanel(new BorderLayout());head.setOpaque(false);JLabel h=new JLabel("Create a new task");h.setFont(Theme.H2);JLabel s=new JLabel("Give it a clear deadline and importance so Taskator can prioritize it.");s.setForeground(Theme.MUTED);s.setFont(Theme.SMALL);head.add(h,BorderLayout.NORTH);head.add(s,BorderLayout.SOUTH);card.add(head,BorderLayout.NORTH);
        JPanel form=new JPanel(new GridBagLayout());form.setOpaque(false);GridBagConstraints c=new GridBagConstraints();c.insets=new Insets(8,0,8,14);c.fill=GridBagConstraints.HORIZONTAL;c.weightx=1;
        titleField=new JTextField();descriptionArea=new JTextArea(5,20);descriptionArea.setLineWrap(true);descriptionArea.setWrapStyleWord(true);deadlineField=new JTextField();categoryBox=new JComboBox<>();importanceSpinner=new JSpinner(new SpinnerNumberModel(3,1,5,1));Theme.styleInput(titleField);Theme.styleInput(descriptionArea);Theme.styleInput(deadlineField);Theme.styleInput(categoryBox);importanceSpinner.setFont(Theme.BODY);
        field(form,c,0,"TASK TITLE",titleField);field(form,c,1,"DESCRIPTION",new JScrollPane(descriptionArea));field(form,c,2,"CATEGORY",categoryBox);field(form,c,3,"DEADLINE",deadlineField);field(form,c,4,"IMPORTANCE (1–5)",importanceSpinner);card.add(form,BorderLayout.CENTER);
        JPanel bottom=new JPanel(new BorderLayout());bottom.setOpaque(false);JLabel hint=new JLabel("Deadline format: yyyy-MM-dd HH:mm");hint.setForeground(Theme.MUTED);hint.setFont(Theme.SMALL);JPanel buttons=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));buttons.setOpaque(false);JButton clear=Theme.outlineButton("Clear");clear.addActionListener(e->clear());JButton save=Theme.button("CREATE TASK   →");save.addActionListener(e->saveTask());buttons.add(clear);buttons.add(save);bottom.add(hint,BorderLayout.WEST);bottom.add(buttons,BorderLayout.EAST);card.add(bottom,BorderLayout.SOUTH);add(card,BorderLayout.CENTER);
    }
    private void field(JPanel p,GridBagConstraints c,int row,String label,Component comp){c.gridy=row;c.gridx=0;c.weightx=.25;p.add(Theme.eyebrow(label),c);c.gridx=1;c.weightx=.75;p.add(comp,c);}
    private void loadCategories(){List<Category> cs=adminService.getCategories();for(Category c:cs)categoryBox.addItem(c);}
    private void saveTask(){try{String title=titleField.getText().trim();if(title.isEmpty())throw new ValidationException("Task title is required.");Category cat=(Category)categoryBox.getSelectedItem();if(cat==null)throw new ValidationException("Select a category.");LocalDateTime dl=LocalDateTime.parse(deadlineField.getText().trim(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));Task t=new Task(userId,cat.getCategoryId(),title,descriptionArea.getText().trim(),dl,(Integer)importanceSpinner.getValue());if(taskService.addTask(t)){JOptionPane.showMessageDialog(this,"Task created successfully.","Taskator",JOptionPane.INFORMATION_MESSAGE);clear();}else JOptionPane.showMessageDialog(this,"Task could not be created.","Error",JOptionPane.ERROR_MESSAGE);}catch(ValidationException|RuntimeException ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"Check task details",JOptionPane.WARNING_MESSAGE);}}
    private void clear(){titleField.setText("");descriptionArea.setText("");deadlineField.setText("");importanceSpinner.setValue(3);if(categoryBox.getItemCount()>0)categoryBox.setSelectedIndex(0);}
}
