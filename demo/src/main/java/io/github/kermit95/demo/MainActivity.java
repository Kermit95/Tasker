package io.github.kermit95.demo;

import android.os.Bundle;
import android.provider.Contacts;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.github.kermit95.tasker.Task;
import io.github.kermit95.tasker.TaskExecuter;
import io.github.kermit95.tasker.TaskManager;
import io.github.kermit95.tasker.TaskParam;
import io.github.kermit95.tasker.TaskQueue;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView mTextView;
    private ViewGroup mViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.hello_world);
        mViewGroup = (ViewGroup) findViewById(R.id.rl);

        //demo begin

        TaskExecuter executer = TaskManager.getInstance().executer();
        TaskQueue taskQueue = TaskManager.getInstance().queue();
        TaskParam taskParam = new TaskParam();
        taskParam.put(1, new String("Hello, World!"));
        executer.setParams(taskParam);

        Task task1 = new Task(Task.ThreadMode.BACKGROUND_THREAD) {
            @Override
            public TaskParam onExecute(TaskParam param) {
                Log.d(TAG, "onExecute: background thread!");
                return param;
            }
        };

        Task task2 = new Task(Task.ThreadMode.UI_THREAD) {
            @Override
            public TaskParam onExecute(TaskParam param) {
                updateUI();
                return param;
            }
        };

        Task task3 = new Task(Task.ThreadMode.UI_THREAD) {
            @Override
            public TaskParam onExecute(TaskParam param) {
                Toast.makeText(MainActivity.this, (String) param.get(1), Toast.LENGTH_SHORT).show();
                return null;
            }
        };

        taskQueue.add(task1).add(task2).add(task3);
        executer.execute();

        //demo end
    }

    private void updateUI(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mTextView.setText("Hello, AnyBody?");
        ImageView imageView = new ImageView(MainActivity.this);
        imageView.setImageResource(R.mipmap.ic_launcher);
        mViewGroup.addView(imageView);
    }
}
