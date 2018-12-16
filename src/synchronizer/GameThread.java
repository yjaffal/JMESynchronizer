/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package synchronizer;

import java.util.List;

/**
 *
 * @author Yasser
 */
public class GameThread extends Thread {

    protected List<GameTask> tasks;
    protected boolean alive = true;
    protected long executionTime, totalTime;
    protected long tmp, tmp1;

    /**
     * Constructs new game thread
     * @param name name of the thread
     */
    public GameThread(String name) {
        super(name);
    }

    /**
     * Iterates over list of game tasks and tries to execute them
     */
    @Override
    public void run() {
        tmp1 = System.currentTimeMillis();
        boolean executed;
        while (alive) {
            //Flag to indicate execution of at least one task
            executed = false;
            //Loop over task list
            for (int i = 0; i < getTasks().size(); i++) {
                GameTask task = getTasks().get(i);
                tmp = System.currentTimeMillis();
                //Try to execute task
                if (task.execute()) {
                    executed = true;
                    executionTime += System.currentTimeMillis() - tmp;
                }
            }

            //If no task executed, sleep for a while or until notified
            if (!executed) {
                synchronized (Synchronizer.THREAD_YIELD_LOCK) {
                    try {
                        Synchronizer.THREAD_YIELD_LOCK.wait(5);
                    } catch (InterruptedException err) {
                    }
                }
            }
        }
        totalTime = System.currentTimeMillis() - tmp1;
    }

    /**
     * Terminates thread
     */
    public void kill() {
        alive = false;
        while (isAlive()) {
        }
    }

    /**
     * Sets task list
     * @param tasks new task list
     */
    public void setTasks(List<GameTask> tasks) {
        this.tasks = tasks;
    }

    /**
     * Gets task list
     * @return current task list
     */
    public List<GameTask> getTasks() {
        return tasks;
    }

    public long getExcutionTime() {
        return executionTime;
    }

    public long getTotallTime() {
        return totalTime;
    }

    public float getExecutionRatio() {
        return (float) executionTime / (float) totalTime;
    }
}
