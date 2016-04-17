package nl.joozey.shapeshifter.gameobject;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by mint on 17-4-16.
 */
public interface GameObjectObserver {
    void onChangeDimension(GameObject gameObject, Rectangle dimension);
}
