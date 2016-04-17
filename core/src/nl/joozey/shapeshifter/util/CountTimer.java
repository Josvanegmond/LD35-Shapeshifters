package nl.joozey.shapeshifter.util;

import com.badlogic.gdx.utils.Timer;

/**
 * Created by mint on 7-3-16.
 */
public class CountTimer extends Timer {

    private Task _task;
    private Timer.Task _timerTask;

    public CountTimer(Task task, float intervals) {
        this(task, intervals, 0);
    }

    public CountTimer(Task task, float intervals, int delaySeconds) {
        this(task, intervals, delaySeconds, 0.01f);
    }

    public CountTimer(Task task, final float intervals, float delaySeconds, float intervalSeconds) {
        _task = task;
        _timerTask = new Timer.Task() {
            private float _count;

            @Override
            public void run() {
                _count += 1f / intervals;
                _task.count(_count);
                if(_count >= 1f) {
                    _task.finish();
                    _count = 0;
                }
            }
        };

        super.scheduleTask(_timerTask, delaySeconds, intervalSeconds, (int)intervals);
    }

    public boolean isBusy() {
        return _timerTask.isScheduled();
    }

    public interface Task {
        void count(float amount);
        void finish();
    }
}
