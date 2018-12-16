/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package synchronizer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Yasser
 */
public abstract class GameTask {

    //Locking and unlocking values
    protected final boolean UNLOCKED = true;
    protected final boolean LOCKED = false;
    //Mutex lock
    protected final AtomicBoolean lock = new AtomicBoolean(UNLOCKED);
    //total frames and target frames
    protected volatile int totalFrames = 0, targetFrames = 0;
    //Variables for performance analysis
    protected long executionTime, wastedTime, extraWait;
    public float time;
    protected long tmp, wtmp, extmp;

    /**
     * Tries to acquire task lock [once] and executes the task
     * @return true if lock aquired and task executed, false otherwise
     */
    public boolean execute() {
        tmp = getTime();
        boolean executed = false;
        //Try to acquire task lock
        if (lock.compareAndSet(UNLOCKED, LOCKED)) {
            //Check if task is ready for execution
            if (totalFrames < targetFrames) {
                wastedTime += getTime() - wtmp;
                try {
                    //Execute task logic
                    performJob();
                } finally {
                    //increment total frames
                    extmp = getTime();
                    totalFrames++;
                    executed = true;
                }
            }
            //Finally, release task lock
            lock.set(UNLOCKED);
        }

        executionTime += getTime() - tmp;
        return executed;
    }

    protected abstract void performJob();

    public int getFrames() {
        return totalFrames;
    }

    /**
     * Sets target frames value and notifies sleeping game threads
     * @param targetFrames
     */
    public void setTargetFrames(int targetFrames) {
        this.targetFrames = targetFrames;
        synchronized (Synchronizer.THREAD_YIELD_LOCK) {
            Synchronizer.THREAD_YIELD_LOCK.notifyAll();
        }
        wtmp = getTime();
    }

    protected long getTime() {
        return System.currentTimeMillis();
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public long getWastedTime() {
        return wastedTime;
    }

    public long getExtraWaitingTime() {
        return extraWait;
    }

    /**
     * Blocks caller until this task finishes execution
     */
    public void waitForTask() {
        
       while(totalFrames != targetFrames){
           Thread.yield();
       }
       extraWait += getTime() - extmp;
    }
}
