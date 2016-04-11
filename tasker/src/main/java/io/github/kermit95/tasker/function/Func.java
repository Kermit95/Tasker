package io.github.kermit95.tasker.function;

/**
 * Created by kermit on 16/4/10.
 */
public interface Func<R, T> {
    R call(T t);
}
