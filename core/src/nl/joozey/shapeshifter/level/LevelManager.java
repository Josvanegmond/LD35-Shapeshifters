package nl.joozey.shapeshifter.level;

import java.util.ArrayList;
import java.util.List;

import nl.joozey.shapeshifter.gameobject.FairyBlimp;
import nl.joozey.shapeshifter.gameobject.GameObject;
import nl.joozey.shapeshifter.gameobject.Jeff;
import nl.joozey.shapeshifter.gameobject.WallObject;
import nl.joozey.shapeshifter.helper.CollisionHelper;

/**
 * Created by mint on 16-4-16.
 */
public class LevelManager {

    private static LevelManager _instance;

    private List<FairyBlimp> _blimpList;
    private List<WallObject> _wallList;
    private List<GameObject> _characterList;

    public static LevelManager getInstance() {
        if (_instance == null) {
            _instance = new LevelManager();
        }
        return _instance;
    }

    private LevelManager() {
        _blimpList = new ArrayList<>();
        _wallList = new ArrayList<>();
        _characterList = new ArrayList<>();
    }

    public List<GameObject> getAllGameObjects() {
        List<GameObject> list = new ArrayList<>();
        list.addAll(_blimpList);
        list.addAll(_wallList);
        list.addAll(_characterList);
        return list;
    }

    public GameObject createBlimp(Level level, float x, float y) {
        FairyBlimp blimp = new FairyBlimp(x, y);
        _blimpList.add(blimp);
        return blimp;
    }

    public GameObject createWall(Level level, float x, float y, float w, float h) {
        WallObject wall = new WallObject(x, y, w, h);
        _wallList.add(wall);
        return wall;
    }

    public GameObject createJeff(Level level, float x, float y) {
        GameObject jeff = new Jeff(x, y);
        _characterList.add(jeff);
        return jeff;
    }

    public int getBlimpCount() {
        return _blimpList.size();
    }

    public int getTakenBlimpCount() {
        int count = 0;
        for(FairyBlimp blimp : _blimpList) {
            if(blimp.isGrabbed()) {
                count++;
            }
        }
        return count;
    }
}
