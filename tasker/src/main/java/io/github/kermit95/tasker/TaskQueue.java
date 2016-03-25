package io.github.kermit95.tasker;

import android.support.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by kermit on 16/3/25.
 */
public class TaskQueue {

    /**
     * TaskQueue and HandlerThread share a same name;
     */
    private String name;
    private Queue<Task> mTaskQueue;


    /**
     * Initialize task queue.
     * @param name the name of the TaskQueue
     */
    TaskQueue(String name){
        mTaskQueue = new ArrayDeque<>();
        this.name = name;
    }

    public boolean offer(Task task){
        return mTaskQueue.offer(task);
    }

    public Task poll(){
        return mTaskQueue.poll();
    }

    public boolean isEmpty(){
        return mTaskQueue.isEmpty();
    }

    private static final Object mLock = new Object();
    public TaskQueue add(@NonNull Task task) {
        synchronized (mLock) {
            task.setId(System.currentTimeMillis());
            mTaskQueue.offer(task);
        }
        return this;
    }

    /**
     * setter and getter
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
