package gui;

import model.Task;
import model.TimeLog;
import service.TaskService;
import service.TimeTrackingService;
import threads.TaskTimerThread;

import javax.swing.*;
import java.awt.*;

public class TimeTrackingPanel extends JPanel {
    private final int userId; private final TaskService taskService=new TaskService(); private final TimeTrackingService trackingService=new TimeTrackingService();
    private JComboBox<Task> taskBox; private JLabel timerLabel,statusLabel,sessionLabel; private TaskTimerThread timerThread; private TimeLog activeLog; private Timer swingTimer;
    public TimeTrackingPanel(int userId){this.userId=userId;setLayout(new BorderLayout());setOpaque(false);build();refreshTasks();}
    private void build(){
        JPanel card=Theme.card();card.setLayout(new GridBagLayout());GridBagConstraints c=new GridBagConstraints();c.gridx=0;c.weightx=1;c.fill=GridBagConstraints.HORIZONTAL;c.insets=new Insets(8,30,8,30);
        JLabel cap=Theme.eyebrow("LIVE WORK SESSION");cap.setHorizontalAlignment(SwingConstants.CENTER);c.gridy=0;card.add(cap,c);timerLabel=new JLabel("00:00:00",SwingConstants.CENTER);timerLabel.setFont(Theme.MONO);timerLabel.setForeground(Theme.PRIMARY_2);c.gridy=1;card.add(timerLabel,c);statusLabel=new JLabel("Ready to track your focused work",SwingConstants.CENTER);statusLabel.setForeground(Theme.MUTED);c.gridy=2;card.add(statusLabel,c);
        taskBox=new JComboBox<>();Theme.styleInput(taskBox);c.gridy=3;c.insets=new Insets(20,100,8,100);card.add(taskBox,c);
        JPanel buttons=new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));buttons.setOpaque(false);JButton start=Theme.button("START TIMER");JButton stop=Theme.outlineButton("STOP & SAVE");JButton refresh=Theme.outlineButton("Refresh");start.addActionListener(e->start());stop.addActionListener(e->stop());refresh.addActionListener(e->refreshTasks());buttons.add(start);buttons.add(stop);buttons.add(refresh);c.gridy=4;c.insets=new Insets(10,20,15,20);card.add(buttons,c);
        sessionLabel=new JLabel("Time spent is saved into your task history.",SwingConstants.CENTER);sessionLabel.setForeground(Theme.MUTED);sessionLabel.setFont(Theme.SMALL);c.gridy=5;card.add(sessionLabel,c);add(card,BorderLayout.CENTER);
    }
    private void refreshTasks(){taskBox.removeAllItems();for(Task t:taskService.getUserTasks(userId))if(!"COMPLETED".equalsIgnoreCase(t.getStatus()))taskBox.addItem(t);}
    private void start(){Task t=(Task)taskBox.getSelectedItem();if(t==null){JOptionPane.showMessageDialog(this,"Select a task.");return;}if(timerThread!=null&&timerThread.isRunning()){JOptionPane.showMessageDialog(this,"Timer already running.");return;}activeLog=trackingService.startTracking(t.getTaskId(),userId);if(activeLog==null){JOptionPane.showMessageDialog(this,"Could not start timer.","Error",JOptionPane.ERROR_MESSAGE);return;}timerThread=new TaskTimerThread();timerThread.start();statusLabel.setText("Tracking: "+t.getTitle());if(swingTimer!=null)swingTimer.stop();swingTimer=new Timer(500,e->{if(timerThread!=null)timerLabel.setText(format(timerThread.getElapsedSeconds()));});swingTimer.start();}
    private void stop(){if(timerThread==null||activeLog==null){JOptionPane.showMessageDialog(this,"No active timer.");return;}timerThread.stopTimer();if(swingTimer!=null)swingTimer.stop();if(trackingService.stopTracking(activeLog)){statusLabel.setText("Session saved successfully");sessionLabel.setText("Saved " + activeLog.getDurationMinutes()+" minute(s) to task history.");}timerThread=null;activeLog=null;timerLabel.setText("00:00:00");refreshTasks();}
    private String format(int sec){return String.format("%02d:%02d:%02d",sec/3600,(sec%3600)/60,sec%60);}
}
