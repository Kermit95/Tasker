package io.github.kermit95.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.github.kermit95.tasker.Task;
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
        mTextView.setOnClickListener(v -> Log.d(TAG, "hello"));

        //init param
        TaskParam taskParam = new TaskParam();
        taskParam.put(1, new String("Hello, World!"));

        // demo
        new TaskQueue(taskParam)
                .nextThread(Task.ThreadMode.BACKGROUND_THREAD)
                .next(new Task() {
                    @Override
                    public TaskParam onExecute(TaskParam param) {
                        return null;
                    }
                })
                .nextThread(Task.ThreadMode.UI_THREAD)
                .next(new Task() {
                    @Override
                    public TaskParam onExecute(TaskParam param) {
                        updateUI();
                        return param;
                    }
                })
                .nextThread(Task.ThreadMode.UI_THREAD)
                .next(new Task() {
                    @Override
                    public TaskParam onExecute(TaskParam param) {
                        Toast.makeText(MainActivity.this, "hello?", Toast.LENGTH_SHORT).show();
                        return param;
                    }
                })
                .next(taskParam1 -> null )
                .execute();
        // demo end
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
