package nl.joozey.shapeshifter.gameobject;

import com.badlogic.gdx.graphics.g2d.Batch;

import nl.joozey.shapeshifter.level.Level;

/**
 * Created by mint on 16-4-16.
 */
public class DialogObject extends GameObject {

    private Level _level;
    private boolean _consumed;
    private boolean _activated;
    private String _message;
    private boolean _consumable;

    public DialogObject(Level level, float x, float y, float w, float h, String message, boolean consumable) {
        setPosition(x, y);
        setSize(w, h);
        _message = message;
        _level = level;
        _consumable = consumable;
    }

    @Override
    public void draw(Batch batch) {}

    @Override
    public void act() {}

    @Override
    public void hitObject(GameObject gameObject) {
        if(gameObject == null && _activated) {
            _activated = false;
            _level.setMessage("");
        }
    }

    @Override
    public boolean isGrabbableBy(GameObject gameObject) {
        if(!_consumed && gameObject instanceof Jeff) {
            _activated = true;
            _level.setMessage(_message);
            if(_consumable) {
                _consumed = true;
            }
            return true;
        }
        return false;
    }

    public boolean isGrabbed() {
        return _consumed;
    }
}
