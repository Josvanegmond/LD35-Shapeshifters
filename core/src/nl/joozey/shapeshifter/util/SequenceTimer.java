package nl.joozey.shapeshifter.util;

import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mint on 11-3-16.
 */
public class SequenceTimer extends Timer {

    private List<Task> _taskList = new ArrayList<Task>();
    private boolean _began = false;
    private boolean _finished = false;

    public void begin() {
        if(!_began) {
            _began = true;
            scheduleNext();
            start();
        }
    }

    public void scheduleNext() {
        if (_taskList.size() > 0) {
            final Task task = _taskList.remove(0);
            scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    task.run();
                }
            }, 0, 0, 0);
        } else {
            _finished = true;
        }
    }

    public void queue(final Task task) {
        _taskList.add(task);
    }

    public static abstract class Task {
        private SequenceTimer _sequenceTimer;

        public Task(SequenceTimer sequenceTimer) {
            _sequenceTimer = sequenceTimer;
        }

        public abstract void run();

        public void finish() {
            _sequenceTimer.scheduleNext();
        }
    }

    public boolean inProgress() {
        return !_finished;
    }
}
