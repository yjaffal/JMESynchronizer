/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package synchronizer;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Yasser
 */
public class Synchronizer {


    private static final AtomicInteger rootLock = new AtomicInteger(0);
    private static final AtomicBoolean vehiclesLock = new AtomicBoolean(true);
    private static final AtomicBoolean wheelLock = new AtomicBoolean(true);
    private static long vehicleLockTime = 0, tmp;
    public static final Object THREAD_YIELD_LOCK = new Object();
    public static final Object WAIT_FOR_TASK_LOCK = new Object();
    
    public static final void acquireRootLock(boolean write){
        if(write){
            while(!rootLock.compareAndSet(0, -1)){Thread.yield();}
        } else {
            incrementRootReaders();
        }
    }

    public static final void releaseRootLock(){
        if(!rootLock.compareAndSet(-1, 0)){
            decrementRootReaders();
        }
    }

    public static void acquireVehiclesLock(){
        while(!vehiclesLock.compareAndSet(true, false)){}
        tmp = System.currentTimeMillis();
    }

    public static void releaseVehiclesLock(){
        vehiclesLock.set(true);
        vehicleLockTime += System.currentTimeMillis() - tmp;
    }

    public static long getVehicleLockTime(){
        return vehicleLockTime;
    }

    private static final void incrementRootReaders(){
        int val = rootLock.get();
        while(val == -1 || !rootLock.compareAndSet(val, val + 1)){
            Thread.yield();
            val = rootLock.get();
        }
    }

    private static final void decrementRootReaders(){
        int val = rootLock.get();
        while(!rootLock.compareAndSet(val, val - 1)){
            Thread.yield();
            val = rootLock.get();
        }
    }

}
