/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package synchronizer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yasser
 */
public class GameTaskManager {
    
    public static final List<GameTask> taskList = new ArrayList<GameTask>();

    public static void reactivateAllTasks(int targetFrames){
        reactivateTasks(taskList, targetFrames);
    }

    public static void reactivateTasks(List<GameTask> tasks, int targetFrames){
        for(GameTask task : tasks){
            task.setTargetFrames(targetFrames);
        }
    }

    public static void printTaskTimes(){
        long totalTime = 0;
        for(GameTask task : taskList){
            totalTime += task.getExecutionTime();
            System.out.println(task +
                    " - Exe time: " + task.getExecutionTime() +
                    ", Wasted time " + task.getWastedTime() +
                    ", Extra waiting time " + task.getExtraWaitingTime()
                    );
        }
        System.out.println("Total time " + totalTime);
    }
}
