package io.github.kermit95.tasker;

/**
 * Created by kermit on 16/3/25.
 */
public abstract class Task {

    /**
     * The info and status of the Task.
     */
    private long id;

    private String name;

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

    public Task(ThreadMode threadMode){
        this.mThreadMode = threadMode;
    }

    public Task(long id, String name){
        this.id = id;
        this.name = name;
    }

    /**
     * The content what should be executed.
     * @param param
     * @return
     */
    public abstract TaskParam onExecute(TaskParam param);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
