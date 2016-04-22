package nl.joozey.shapeshifter.helper;

import com.badlogic.gdx.math.Rectangle;

import nl.joozey.shapeshifter.gameobject.GameObject;
import nl.joozey.shapeshifter.level.LevelManager;

/**
 * Created by mint on 16-4-16.
 */
public class CollisionHelper {

    public static boolean isColliding(GameObject collider) {

        for (GameObject gameObject : LevelManager.getInstance().getAllGameObjects()) {

            if (gameObject == collider) {
                continue;
            }

            if (gameObject.getDimension().overlaps(collider.getDimension())) {
                if (!gameObject.isGrabbableBy(collider)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static Rectangle check(GameObject collider, Rectangle end) {
        return check(collider, end, null);
    }

    public static Rectangle check(GameObject collider, Rectangle end, Rectangle hitDirections) {

        Rectangle newEnd = new Rectangle(end);
        for (GameObject gameObject : LevelManager.getInstance().getAllGameObjects()) {

            if (gameObject == collider) {
                continue;
            }

            if (gameObject.getDimension().overlaps(end) || end.overlaps(gameObject.getDimension())) {

                gameObject.hitObject(collider);
                if (gameObject.isGrabbableBy(collider)) {
                    collider.hitObject(gameObject);

                } else {
                    Rectangle rect = gameObject.getDimension();
                    Rectangle colliderRect = collider.getDimension();

                    Rectangle left = new Rectangle(end.x - 5, colliderRect.y, 5, colliderRect.height);
                    Rectangle right = new Rectangle(end.x + end.width, colliderRect.y, 5, colliderRect.height);
                    if (rect.overlaps(left) ||
                            rect.overlaps(right)) {
                        newEnd.x = collider.getPosition().x;
                    }

                    Rectangle top = new Rectangle(colliderRect.x, end.y - 5, colliderRect.getWidth(), 5);
                    Rectangle bottom = new Rectangle(colliderRect.x, end.y + end.height, colliderRect.getWidth(), 5);
                    if (rect.overlaps(top) ||
                            rect.overlaps(bottom)) {
                        newEnd.y = collider.getPosition().y;
                    }

                    if (hitDirections != null) {
                        hitDirections.x = (rect.contains(left)) ? 1 : 0;
                        hitDirections.y = (rect.contains(top)) ? 1 : 0;
                        hitDirections.width = (rect.contains(right)) ? 1 : 0;
                        hitDirections.height = (rect.contains(bottom)) ? 1 : 0;
                    }
                }
            } else {
                gameObject.unhitObject(collider);
            }
        }

        return newEnd;
    }
}
