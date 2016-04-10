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
    private final Queue<Task> mWorkQueue;


    /**
     * TaskExecuter
     */
    private final TaskExecuter mExecuter;


    public TaskQueue(TaskParam taskParam){
        mWorkQueue = new ArrayDeque<>();
        mExecuter = new TaskExecuter(mWorkQueue, taskParam);
    }

    public TaskQueue next(@NonNull Task task) {
        mWorkQueue.offer(task);
        return this;
    }

    public TaskQueue next(@NonNull final Func<TaskParam, TaskParam> func){
        mWorkQueue.offer(new Task() {
            @Override
            public TaskParam onExecute(TaskParam param) {
                return func.call(param);
            }
        });
        return this;
    }

    public TaskQueue nextThread(@NonNull Task.ThreadMode thread){
        mWorkQueue.element().setThreadMode(thread);
        return this;
    }

    public TaskQueue param(@NonNull TaskParam taskParam){
        mExecuter.setParams(taskParam);
        return this;
    }

    public void execute(){
        mExecuter.execute();
    }
}
