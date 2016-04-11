package io.github.kermit95.tasker;

import android.support.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.Queue;

import io.github.kermit95.tasker.function.Func;
import io.github.kermit95.tasker.function.Func0;

/**
 * Created by kermit on 16/3/25.
 */
public class Tasker {

    private final Queue<Task> mWorkFlow;

    /**
     * TaskExecuter
     */
    private final TaskExecuter mExecuter;

    private Task curTask;


    public Tasker(TaskParam taskParam){
        mWorkFlow = new ArrayDeque<>();
        mExecuter = new TaskExecuter(mWorkFlow, taskParam);
    }

    public Tasker(){
        mWorkFlow = new ArrayDeque<>();
        mExecuter = new TaskExecuter(mWorkFlow, null);
    }

    public Tasker justDo(@NonNull final Func0 func0) {
        curTask = new Task() {
            @Override
            public TaskParam onExecute(TaskParam param) {
                func0.call();
                return null;
            }
        };
        mWorkFlow.offer(curTask);
        return this;
    }

    public Tasker next(@NonNull final Func<TaskParam, TaskParam> func){
        curTask = new Task() {
            @Override
            public TaskParam onExecute(TaskParam param) {
                return func.call(param);
            }
        };


        curTask.setCompose(true);

        mWorkFlow.offer(curTask);
        return this;
    }

    public Tasker on(@NonNull Task.ThreadMode thread){
        curTask.setThreadMode(thread);
        return this;
    }

    public void execute(){
        mExecuter.execute();
    }

    public void cancel(){
        mExecuter.setQuit(true);
    }

}
