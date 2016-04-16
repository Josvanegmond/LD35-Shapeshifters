package nl.joozey.shapeshifter;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mint on 16-4-16.
 */
public class CollisionHelper {

    private static List<GameObject> _gameObjectList = new ArrayList<>();

    public static void setGameObjectList(List<GameObject> gameObjectList) {
        _gameObjectList = gameObjectList;
    }

    public static Rectangle check(GameObject collider, Rectangle end) {

        Rectangle newEnd = new Rectangle(end);
        System.out.println("Gameobjects: " + _gameObjectList.size());
        for (GameObject gameObject : _gameObjectList) {

            if (gameObject == collider) {
                continue;
            }

            if (gameObject.getDimension().overlaps(end) || end.overlaps(gameObject.getDimension())) {

                if(gameObject.isGrabbableBy(collider)) {
                    collider.hitObject(gameObject);

                } else {
                    Rectangle rect = gameObject.getDimension();
                    Rectangle colliderRect = collider.getDimension();

                    Rectangle left = new Rectangle(end.x, colliderRect.y, 1, colliderRect.height);
                    Rectangle right = new Rectangle(end.x + end.width, colliderRect.y, 1, colliderRect.height);
                    if (rect.overlaps(left) ||
                            rect.overlaps(right)) {
                        newEnd.x = collider.getPosition().x;
                    }

                    Rectangle top = new Rectangle(colliderRect.x, end.y, colliderRect.getWidth(), 1);
                    Rectangle bottom = new Rectangle(colliderRect.x, end.y + end.height, colliderRect.getWidth(), 1);
                    if (rect.overlaps(top) ||
                            rect.overlaps(bottom)) {
                        newEnd.y = collider.getPosition().y;
                    }
                }
            }
        }

        return newEnd;
    }
}
