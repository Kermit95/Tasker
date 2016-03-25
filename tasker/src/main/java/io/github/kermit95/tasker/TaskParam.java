package io.github.kermit95.tasker;

import android.util.SparseArray;

/**
 * Created by kermit on 16/3/25.
 */
public class TaskParam {

    private SparseArray<Object> params;

    public TaskParam(){
        params = new SparseArray<>();
    }

    public void put(int key, Object param){
        params.put(key, param);
    }

    public Object get(int key){
        return params.get(key);
    }
}
