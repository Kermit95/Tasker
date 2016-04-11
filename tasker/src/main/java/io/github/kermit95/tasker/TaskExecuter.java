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
    private Queue<Task> mWorkFlow;

    TaskExecuter(Queue<Task> workFlow, @Nullable TaskParam taskParam) {
        this.mWorkFlow = workFlow;
        this.mTaskParam = taskParam;
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


    private boolean quit = false;
    void setQuit(boolean quit){
        this.quit = quit;
    }

    /**
     * Start execute the task in the taskqueue.
     */
    public void execute(){

        if (quit || mWorkFlow.isEmpty()){
            stopThread();
            mStatus = Status.FINISHED;
            return;
        }


        curTask = mWorkFlow.poll();

        if (mStatus == Status.FINISHED){
            startThread();
            mStatus = Status.RUNNING;
        }

        threadHandler.post(new Runnable() {
            @Override
            public void run() {
                // have checked the mWorkFlow not empty.
                doInHandlerThread();
            }
        });
    }

    private void doInHandlerThread(){
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

    private TaskParam mTaskParam = null;
    /**
     * execute a single task.
     * @param task
     */
    private void executeTask(@NonNull Task task) {
        task.setStatus(Task.Status.RUNNING);

        if (task.isCompose()) {
            mTaskParam = task.onExecute(mTaskParam);
        }else {
            task.onExecute(null);
        }

        task.setStatus(Task.Status.FINISHED);

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


