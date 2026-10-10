package threads;

import model.Task;
import java.time.LocalDateTime;
import java.util.List;

public class TaskReminderThread extends Thread {
    private final List<Task> tasks;
    private volatile boolean running=true;

    public TaskReminderThread(List<Task> tasks){ this.tasks=tasks; }

    public synchronized void stopReminder(){ running=false; interrupt(); }

    @Override
    public void run(){
        while(running){
            synchronized(tasks){
                LocalDateTime now=LocalDateTime.now();
                for(Task t:tasks){
                    if(t.getDeadline()!=null &&
                       !t.getDeadline().isBefore(now) &&
                       t.getDeadline().isBefore(now.plusHours(24)) &&
                       !"COMPLETED".equalsIgnoreCase(t.getStatus())){
                        System.out.println("REMINDER: "+t.getTitle()+" is due within 24 hours.");
                    }
                }
            }
            try{ Thread.sleep(30000); }
            catch(InterruptedException e){ if(!running) break; }
        }
    }
}
