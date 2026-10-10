package gui;

import model.Task;
import service.TaskService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ProgressPanel extends JPanel {
    private final int userId; private final TaskService service=new TaskService(); private JLabel total,pending,running,completed; private JProgressBar progress; private JPanel breakdown;
    public ProgressPanel(int userId){this.userId=userId;setLayout(new BorderLayout(0,14));setOpaque(false);build();refresh();}
    private void build(){
        JPanel cards=new JPanel(new GridLayout(1,4,12,0));cards.setOpaque(false);total=stat(cards,"TOTAL",Theme.PRIMARY);pending=stat(cards,"PENDING",Theme.WARNING);running=stat(cards,"IN PROGRESS",Theme.INFO);completed=stat(cards,"COMPLETED",Theme.SUCCESS);add(cards,BorderLayout.NORTH);
        JPanel center=Theme.card();center.setLayout(new BorderLayout(0,14));JPanel head=new JPanel(new BorderLayout());head.setOpaque(false);JLabel h=new JLabel("Progress analytics");h.setFont(Theme.H2);head.add(h,BorderLayout.WEST);JButton r=Theme.outlineButton("↻  Refresh");r.addActionListener(e->refresh());head.add(r,BorderLayout.EAST);center.add(head,BorderLayout.NORTH);
        JPanel middle=new JPanel(new BorderLayout(22,0));middle.setOpaque(false);progress=new JProgressBar(0,100);progress.setStringPainted(true);progress.setFont(new Font("Segoe UI",Font.BOLD,15));progress.setForeground(Theme.PRIMARY);progress.setBackground(Theme.SURFACE_3);progress.setBorder(BorderFactory.createEmptyBorder());progress.setPreferredSize(new Dimension(0,48));middle.add(progress,BorderLayout.NORTH);
        breakdown=new JPanel();breakdown.setOpaque(false);breakdown.setLayout(new BoxLayout(breakdown,BoxLayout.Y_AXIS));middle.add(breakdown,BorderLayout.CENTER);center.add(middle,BorderLayout.CENTER);JLabel note=new JLabel("Completion is calculated from tasks marked COMPLETED.");note.setForeground(Theme.MUTED);note.setFont(Theme.SMALL);center.add(note,BorderLayout.SOUTH);add(center,BorderLayout.CENTER);
    }
    private JLabel stat(JPanel p,String name,Color color){JPanel box=Theme.card();box.setLayout(new BorderLayout(8,0));JPanel bar=new JPanel();bar.setBackground(color);bar.setPreferredSize(new Dimension(4,0));JPanel x=new JPanel();x.setOpaque(false);x.setLayout(new BoxLayout(x,BoxLayout.Y_AXIS));JLabel l=Theme.eyebrow(name);JLabel v=new JLabel("0");v.setFont(new Font("Segoe UI",Font.BOLD,26));x.add(l);x.add(Box.createVerticalStrut(3));x.add(v);box.add(bar,BorderLayout.WEST);box.add(x,BorderLayout.CENTER);p.add(box);return v;}
    private void refresh(){Map<String,Integer>s=service.getStatusSummary(userId);int t=s.get("TOTAL"),c=s.get("COMPLETED");total.setText(""+t);pending.setText(""+s.get("PENDING"));running.setText(""+s.get("IN_PROGRESS"));completed.setText(""+c);progress.setValue(t==0?0:c*100/t);breakdown.removeAll();List<Task>tasks=service.getUserTasks(userId);for(Task task:tasks){JPanel row=new JPanel(new BorderLayout(10,0));row.setOpaque(false);JLabel name=new JLabel(task.getTitle());name.setForeground(Theme.TEXT);name.setFont(Theme.BODY_BOLD);JProgressBar bar=new JProgressBar(0,100);bar.setValue(task.getProgress());bar.setString(task.getProgress()+"%");bar.setStringPainted(true);bar.setForeground(task.getProgress()==100?Theme.SUCCESS:Theme.PRIMARY);bar.setBackground(Theme.SURFACE_3);bar.setPreferredSize(new Dimension(260,24));row.add(name,BorderLayout.CENTER);row.add(bar,BorderLayout.EAST);row.setBorder(BorderFactory.createEmptyBorder(7,2,7,2));breakdown.add(row);}breakdown.revalidate();breakdown.repaint();}
}
