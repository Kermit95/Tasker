package io.github.kermit95.tasker;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Queue;

/**
 * Created by kermit on 16/3/25.
 */
public class TaskExecuter {

    private static final String HANDLER_NAME= "DEFAULT_NAME";

    private static final int MESSAGE_UI_WORK = 0x01;
    private Task curTask;
    private Queue<Task> mWorkQueue;

    TaskExecuter(Queue<Task> workQueue, @Nullable TaskParam taskParam) {
        this.mWorkQueue = workQueue;
        this.mTaskParam = taskParam;
    }

    TaskExecuter(Queue<Task> workQueue){
        this(workQueue, null);
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

        if (mWorkQueue.isEmpty()){
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
                // have checked the mWorkQueue not empty.
                doInHandlerThread();
            }
        });
    }

    private void doInHandlerThread(){
        curTask = mWorkQueue.poll();
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
        if (mTaskParam != null){
            mTaskParam = task.onExecute(mTaskParam);
        }else{
            // FIXME: 16/4/10 task
        }
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
        mHandlerThread = new HandlerThread(HANDLER_NAME);
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


