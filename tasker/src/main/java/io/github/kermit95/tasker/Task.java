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
        this.mThreadMode = threadMode;
    }

    /**
     * The content what should be executed.
     * @param param
     * @return
     */
    public abstract TaskParam onExecute(TaskParam param);

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
}
