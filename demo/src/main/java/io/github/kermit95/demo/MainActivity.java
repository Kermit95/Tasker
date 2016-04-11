package io.github.kermit95.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.github.kermit95.tasker.Task;
import io.github.kermit95.tasker.TaskParam;
import io.github.kermit95.tasker.Tasker;

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

        // demo

        // test1
        new Tasker()
                .justDo(() -> doSomeThing()).on(Task.ThreadMode.BACKGROUND_THREAD)
                .justDo(() -> updateUI()).on(Task.ThreadMode.UI_THREAD)
                .justDo(() -> complete()).on(Task.ThreadMode.UI_THREAD)
                .execute();

        // test2
        new Tasker(new TaskParam(0, "-_-||", 1, ":P"))
                .next(taskParam -> step1(taskParam)).on(Task.ThreadMode.BACKGROUND_THREAD)
                .next(taskParam -> step2(taskParam)).on(Task.ThreadMode.BACKGROUND_THREAD)
                .next(taskParam -> step3(taskParam)).on(Task.ThreadMode.UI_THREAD)
                .execute();
        // demo end
    }

    private void complete(){
        Toast.makeText(MainActivity.this, "任务完成!", Toast.LENGTH_SHORT).show();
    }

    private void doSomeThing(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    private TaskParam step1(TaskParam taskParam){
        // pretend to do something...
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String s1 = taskParam.get(0) + " oops!";
        String s2 = taskParam.get(1) + " triky!";
        taskParam.put(0, s1);
        taskParam.put(1, s2);
        return taskParam;
    }

    private TaskParam step2(TaskParam taskParam){
        taskParam.put(2, "it's intersting...");
        return taskParam;
    }

    private TaskParam step3(TaskParam taskParam){
        for(int i = 0; i < taskParam.size(); ++i){
            Toast.makeText(MainActivity.this, taskParam.get(i).toString(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
