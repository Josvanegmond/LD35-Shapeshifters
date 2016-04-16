package nl.joozey.shapeshifter.util;

import com.badlogic.gdx.utils.Timer;

/**
 * Created by mint on 3-4-16.
 */
public class ConditionTimer extends Timer {

    public interface Task {
        void run();
        boolean check();
        void finished();
    }

    private Task _task;
    private float _interval;

    public ConditionTimer(final Task task, float interval) {
        _task = task;
        _interval = interval;
    }

    private void _run() {
        if(_task.check()) {
            _task.run();

            Timer.Task timerTask = new Timer.Task() {
                @Override
                public void run() {
                    _run();
                }
            };
            scheduleTask(timerTask, _interval);
        } else {
            _task.finished();
        }
    }

    public void begin() {
        _run();
    }
}
