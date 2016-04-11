package io.github.kermit95.tasker;

/**
 * Created by kermit on 16/3/25.
 */
public abstract class Task{

    /**
     * The info and status of the Task.
     */

    private boolean isCancelled;

    private Status mStatus = Status.RUNNING;

    private ThreadMode mThreadMode = ThreadMode.UI_THREAD;

    private TaskParam mTaskParam;

    private boolean compose = false;

    public enum Status{
        RUNNING,
        FINISHED,
    }

    public enum ThreadMode{
        BACKGROUND_THREAD,
        UI_THREAD,
    }

    public Task(){}

    public Task(ThreadMode threadMode){
        this(threadMode, null);
    }

    public Task(TaskParam taskParam){
        this(null, taskParam);
    }

    public Task(ThreadMode threadMode, TaskParam taskParam){
        this.mThreadMode = threadMode;
        this.mTaskParam = taskParam;
    }

    /**
     * The content what should be executed.
     * @param param
     * @return
     */
    protected abstract TaskParam onExecute(TaskParam param);

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mStatus = status;
    }

    public ThreadMode getThreadMode() {
        return mThreadMode;
    }

    public void setThreadMode(ThreadMode threadMode) {
        mThreadMode = threadMode;
    }

    public TaskParam getTaskParam() {
        return mTaskParam;
    }

    public void setTaskParam(TaskParam taskParam) {
        mTaskParam = taskParam;
    }

    public boolean isCompose() {
        return compose;
    }

    public void setCompose(boolean compose) {
        this.compose = compose;
    }
}
