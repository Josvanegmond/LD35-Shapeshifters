package nl.joozey.shapeshifter.util;

import com.badlogic.gdx.InputMultiplexer;

/**
 * Created by mint on 16-4-16.
 */
public class InputManager extends InputMultiplexer {

    private static InputManager _instance;

    public static InputManager getInstance() {
        if (_instance == null) {
            _instance = new InputManager();
        }

        return _instance;
    }
}
