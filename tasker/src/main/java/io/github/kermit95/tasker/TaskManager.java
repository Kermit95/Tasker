package io.github.kermit95.tasker;

/**
 * Created by kermit on 16/3/25.
 */
public class TaskManager {

    private static final String DEFAULT_TASKQUEUE = "DEFAULT_TASKQUEUE";

    private static TaskManager instance;

    private TaskExecuter mTaskExecuter;
    private TaskQueue mTaskQueue;

    private TaskManager(){
        mTaskQueue = new TaskQueue(DEFAULT_TASKQUEUE);
        mTaskExecuter = new TaskExecuter(mTaskQueue);
    }

    public static TaskManager getInstance(){
        if (instance == null){
            instance = new TaskManager();
        }
        return instance;
    }

    public TaskQueue queue(){
        return mTaskQueue;
    }

    public TaskExecuter executer(){
        return mTaskExecuter;
    }

}
