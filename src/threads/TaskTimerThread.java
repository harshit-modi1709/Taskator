package threads;

public class TaskTimerThread extends Thread {
    private int elapsedSeconds=0;
    private boolean running=false;

    public synchronized void stopTimer(){ running=false; interrupt(); }
    public synchronized int getElapsedSeconds(){ return elapsedSeconds; }
    public synchronized boolean isRunning(){ return running; }

    @Override
    public synchronized void start(){
        if(running) return;
        running=true;
        super.start();
    }

    @Override
    public void run(){
        while(isRunning()){
            try{
                Thread.sleep(1000);
                synchronized(this){ if(running) elapsedSeconds++; }
            }catch(InterruptedException e){
                if(!isRunning()) break;
            }
        }
    }
}
