package io.github.kermit95.tasker;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

/**
 * Created by kermit on 16/3/25.
 */
public class TaskExecuter {

    private static final int MESSAGE_UI_WORK = 0x01;
    private Task curTask;
    private TaskQueue mTaskQueue;

    TaskExecuter(TaskQueue taskQueue) {
        this.mTaskQueue = taskQueue;
        mTaskParam = new TaskParam();
    }

    private Status mStatus = Status.FINISHED;

    enum Status{
        RUNNING,
        FINISHED,
    }


    private Handler uiHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE_UI_WORK:
                    executeTask(curTask);
                    runNextTask();
                    break;
            }
        }
    };

    /**
     * Start execute the task in the taskqueue.
     */
    public void execute(){

        if (mTaskQueue.isEmpty()){
            stopThread();
            mStatus = Status.FINISHED;
            return;
        }

        if (mStatus == Status.FINISHED){
            startThread();
            mStatus = Status.RUNNING;
        }

        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                doInHandlerThread();
            }
        });
    }

    private void doInHandlerThread(){
        curTask = mTaskQueue.poll();
        switch (curTask.getThreadMode()){
            case UI_THREAD:
                Message message = uiHandler.obtainMessage();
                message.what = MESSAGE_UI_WORK;
                message.sendToTarget();
                break;
            case BACKGROUND_THREAD:
                executeTask(curTask);
                runNextTask();
                break;
        }
    }

    private TaskParam mTaskParam;
    /**
     * execute a single task.
     * @param task
     */
    private void executeTask(@NonNull Task task){
        task.setStatus(Task.Status.RUNNING);
        mTaskParam = task.onExecute(mTaskParam);
        task.setStatus(Task.Status.FINISHED);
    }


    public TaskParam getParam() {
        return mTaskParam;
    }

    public void setParams(TaskParam taskParam) {
        mTaskParam = taskParam;
    }

    private Handler threadHandler;
    private HandlerThread mHandlerThread;

    /**
     * Thread Control.
     */
    private void startThread(){
        mHandlerThread = new HandlerThread(mTaskQueue.getName());
        mHandlerThread.start();
        threadHandler = new Handler(mHandlerThread.getLooper());
    }

    private void stopThread(){
        if (mHandlerThread != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mHandlerThread.quitSafely();
            }else{
                mHandlerThread.quit();
            }
            mHandlerThread = null;
            threadHandler = null;
        }
    }


    /**
     * run next task, if not have next task, stop the mHandlerThread.
     */
    private void runNextTask(){
        if (!isCurTaskRunning()){
            execute();
        }
    }

    private boolean isCurTaskRunning(){
        if (curTask.getStatus() == Task.Status.RUNNING){
            return true;
        }else{
            return false;
        }
    }
}


