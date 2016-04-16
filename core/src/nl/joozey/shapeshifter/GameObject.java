package nl.joozey.shapeshifter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by mint on 16-4-16.
 */
public abstract class GameObject {

    private Rectangle _dimension;

    public GameObject() {
        _dimension = new Rectangle();
    }

    public abstract void draw(Batch batch);

    public abstract void act();

    public boolean isGrabbableBy(GameObject gameObject) {
        return false;
    }

    public void hitObject(GameObject gameObject) {

    }

    public void setPosition(Vector2 pos) {
        _dimension.set(pos.x, pos.y, _dimension.width, _dimension.height);
    }

    public void setPosition(float x, float y) {
        _dimension.set(x, y, _dimension.width, _dimension.height);
    }

    public Vector2 getPosition() {
        return _dimension.getPosition(new Vector2());
    }

    public void setSize(float w, float h) {
        _dimension.width = w;
        _dimension.height = h;
    }

    public Vector2 getSize() {
        return _dimension.getSize(new Vector2());
    }

    public Rectangle getDimension() {
        return new Rectangle(_dimension);
    }
}
