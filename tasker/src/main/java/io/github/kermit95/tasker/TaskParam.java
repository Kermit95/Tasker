package io.github.kermit95.tasker;

import android.support.annotation.NonNull;
import android.util.SparseArray;

/**
 * Created by kermit on 16/3/25.
 */
public class TaskParam {

    private SparseArray<Object> params = new SparseArray<>();

    public TaskParam(){}

    public TaskParam(@NonNull Object...obj){

        if (obj.length == 0 || obj.length % 2 != 0){
            throw new IllegalArgumentException("Please check key-value's amount.");
        }

        for(int i = 0; i < obj.length; i += 2){
            int key = (int) obj[i];
            Object value = obj[i+1];

            put(key, value);
        }
    }

    public void put(int key, Object param){
        params.put(key, param);
    }

    public Object get(int key){
        return params.get(key);
    }

    public int size(){
        return params.size();
    }
}
