/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package synchronizer;

/**
 *
 * @author Yasser
 */
public class GameThreadManager {

    private GameThread[] threads;

    public GameThreadManager(int numOfThreads){
        threads = new GameThread[numOfThreads];
        for(int i = 0; i < numOfThreads; i++){
            threads[i] = new GameThread("Game Thread " + i);
            threads[i].setTasks(GameTaskManager.taskList);
        }
    }

    public void start(){
        for(GameThread thread : threads){
            thread.start();
        }
    }

    public void shutdown(){
        for(GameThread thread : threads){
            thread.kill();
        }
    }

    public void printThreadTimes(){
        System.out.println("Thread\tExe time\tTotal time\tExe ratio");
        for(GameThread thread : threads){
            System.out.println(thread.getName() + "\t" + 
                               thread.getExcutionTime() + "\t" +
                               thread.getTotallTime() + "\t" +
                               thread.getExecutionRatio());
        }
    }

    public String getThreadsSummary(){
        String res = ("Exe time   Total time   Exe ratio");
        for(GameThread thread : threads){
            res += "\n";
            res += (
                               thread.getExcutionTime() + "   " +
                               thread.getTotallTime() + "   " +
                               thread.getExecutionRatio());
        }

        return res;
    }
}
