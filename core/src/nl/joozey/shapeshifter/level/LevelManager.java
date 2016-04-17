package nl.joozey.shapeshifter.level;

import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;
import java.util.List;

import nl.joozey.shapeshifter.gameobject.DialogObject;
import nl.joozey.shapeshifter.gameobject.FairyBlimp;
import nl.joozey.shapeshifter.gameobject.GameObject;
import nl.joozey.shapeshifter.gameobject.Jeff;
import nl.joozey.shapeshifter.gameobject.Rinn;
import nl.joozey.shapeshifter.gameobject.WallObject;

/**
 * Created by mint on 16-4-16.
 */
public class LevelManager {

    private static final float TOTAL_BLIMPS = 10;

    private static LevelManager _instance;

    public final Level level1 = new Level1();
    public final Level level2 = new Level2();
    public final Level level3 = new Level3();
    public final Level level4 = new Level4();
    public final Level level5 = new Level5();

    private Jeff _jeff;
    private Rinn _rinn;

    private List<FairyBlimp> _blimpList;
    private List<WallObject> _wallList;
    private List<GameObject> _characterList;
    private List<DialogObject> _dialogList;

    private float _dimLevel = TOTAL_BLIMPS;

    private List<String> _unlockedPowerupHints;

    private Level _currentLevel;

    public static LevelManager getInstance() {
        if (_instance == null) {
            _instance = new LevelManager();
        }
        return _instance;
    }

    private LevelManager() {
        _unlockedPowerupHints = new ArrayList<String>();
        _unlockedPowerupHints.add("Pressing TAB gives you this clear overview of nothing!");
        _unlockedPowerupHints.add("Press E, F, SHIFT or ENTER to unlock your shape power");

        _blimpList = new ArrayList<FairyBlimp>();
        _wallList = new ArrayList<WallObject>();
        _characterList = new ArrayList<GameObject>();
        _dialogList = new ArrayList<DialogObject>();
    }

    public void clearDialogs() {
        _dialogList.clear();
    }

    public List<GameObject> getAllGameObjects() {
        List<GameObject> list = new ArrayList<GameObject>();
        list.addAll(_blimpList);
        list.addAll(_wallList);
        list.addAll(_characterList);
        list.addAll(_dialogList);
        return list;
    }

    public GameObject createBlimp(Level level, float x, float y) {
        FairyBlimp blimp = new FairyBlimp(level, x, y);
        _blimpList.add(blimp);
        return blimp;
    }

    public GameObject createWall(Level level, float x, float y, float w, float h) {
        WallObject wall = new WallObject(x, y, w, h);
        _wallList.add(wall);
        return wall;
    }

    public Jeff createJeff(Level level, float x, float y) {
        if(_jeff == null) {
            _jeff = new Jeff(level ,x, y);
        } else {
            _jeff.setLevel(level);
            _jeff.setPosition(x, y);
        }
        _characterList.add(_jeff);
        return _jeff;
    }

    public Rinn createRinn(Level level, int x, float y) {
        if(_rinn == null) {
            _rinn = new Rinn(level, x, y);
        } else {
            _rinn.setLevel(level);
            _rinn.setPosition(x, y);
        }
        _characterList.add(_rinn);
        return _rinn;
    }

    public Jeff getJeff() {
        return _jeff;
    }

    public Rinn getRinn() {
        return _rinn;
    }

    public DialogObject createDialog(Level level, float x, float y, float w, float h, String message, boolean consumable) {
        DialogObject dialog = new DialogObject(level, x, y, w, h, message, consumable);
        _dialogList.add(dialog);
        return dialog;
    }

    public void dim() {
        _dimLevel--;
    }

    public float getDimFactor() {
        return _dimLevel / TOTAL_BLIMPS;
    }

    private void _clearLists() {
        _dialogList.clear();
        _blimpList.clear();
        _characterList.clear();
        _wallList.clear();
    }

    public void loadLevel(Level level, int dir) {
        _clearLists();
        _currentLevel = level;
        _currentLevel.load(dir);
    }

    public void loadLeft() {
        loadLevel(_currentLevel.getLeft(), 1);
    }

    public void loadRight() {
        loadLevel(_currentLevel.getRight(), 0);
    }

    public void run(Batch batch) {
        _currentLevel.run();
        _currentLevel.draw(batch);
    }

    public List<String> getPowerupHints() {
        return _unlockedPowerupHints;
    }
}
