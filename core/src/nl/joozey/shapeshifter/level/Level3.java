package nl.joozey.shapeshifter.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import nl.joozey.shapeshifter.gameobject.CorruptEnemy;
import nl.joozey.shapeshifter.gameobject.FairyBlimp;
import nl.joozey.shapeshifter.gameobject.GameObject;
import nl.joozey.shapeshifter.gameobject.Jeff;
import nl.joozey.shapeshifter.gameobject.Rinn;
import nl.joozey.shapeshifter.util.CountTimer;

/**
 * Created by mint on 16-4-16.
 */
public class Level3 extends Level {

    private Jeff _jeff;
    private Rinn _rinn;

    public Level3() {
    }

    public void load(int dir) {
        super.load(dir);

        //floor
        _levelManager.createWall(this, 0, 0, Gdx.graphics.getWidth(), _floorLevel);

        //blimp
        if(_jeff != null && _jeff.getPower() <= 6) {
            _levelManager.createBlimp(this, 300, 530);
        }

        //rinn
        Rinn rinn = _levelManager.getRinn();
        if(rinn == null || (rinn != null && rinn.getStage() == 0)) {
            _rinn = _levelManager.createRinn(Level3.this, 650, _floorLevel);
            _rinn.clearObservers();
            _rinn.stopMoving();
            _rinn.jump(false);

            //dialogs
            _levelManager.createDialog(this, 100, _floorLevel, 60, 200, "Hi Jeff.", false);
            _levelManager.createDialog(this, 300, _floorLevel, 60, 200, "I saw you fell from the sky.", false);
            _levelManager.createDialog(this, 500, _floorLevel, 60, 200, "I'm Rinn. Will you join me?", false);
        }

        //player
        if (dir == 0) {
            _jeff = _levelManager.createJeff(Level3.this, 20, _floorLevel);
        } else {
            _jeff = _levelManager.createJeff(Level3.this, 720, _floorLevel);
        }

        if(_jeff.getPower() > 2) {
            _levelManager.createCorrupt(this, 300, _floorLevel, 200, 100);
        }
    }

    @Override
    public void drawScene(Batch batch) {
        _shapeRenderer.end();

        for (GameObject gameObject : _levelManager.getAllGameObjects()) {
            gameObject.draw(batch);
        }

        _shapeRenderer.begin();
    }

    @Override
    public Level getRight() {
        if(_jeff != null && _jeff.getPower() <= 6) {
            return LevelManager.getInstance().level4;
        } else {
            return null;
        }
    }

    @Override
    public Level getLeft() {

        if(_jeff != null && _jeff.getPower() <= 6) {
            return LevelManager.getInstance().level2;
        } else {
            return null;
        }

    }

    @Override
    public void onTouched(GameObject gameObject) {

        if(gameObject instanceof FairyBlimp) {
            _jeff.setPower(7);
            _jeff.rainbow(10);
            setMessage("The surge feels empty.");

            _startFinalBattle();
        }

        if (_rinn.getStage() == 0 && gameObject instanceof Rinn) {
            ((Rinn) gameObject).moveRight();
        }
    }

    private void _startFinalBattle() {
        _levelManager.clearDialogs();
        _levelManager.createWall(this, -49, _floorLevel, 50, 600);
        _levelManager.createWall(this, 799, _floorLevel, 50, 600);

        CountTimer enemySpawner = new CountTimer(new CountTimer.Task() {

            String[] finalBattleMessages = new String[]{
                    "Hi Jeff.",
                    "I'm Rinn.",
                    "Aren't they beautiful?.",
                    "They make this world green.",
                    "Fantastic! Come meet me...",
                    "I saw you fell from the sky.",
                    "... Jeff ...",
                    "What have you done?",
                    "please stop ... please ...",
                    "I'm hurt ...",
                    "..."
            };

            @Override
            public void count(float amount) {
                System.out.println(amount);
                if((int)(amount*100)%8 == 0) {
                    setMessage(finalBattleMessages[(int)(amount*10)]);
                    _levelManager.createCorruptEnemy(Level3.this, -100, (float)Math.random() * 600);
                    _levelManager.createCorruptEnemy(Level3.this, 900, (float)Math.random() * 600);
                    _levelManager.createCorruptEnemy(Level3.this, (float)Math.random() * 800, -100);
                    _levelManager.createCorruptEnemy(Level3.this, (float)Math.random() * 800, 700);
                }
            }

            @Override
            public void finish() {
                if(_jeff.getSize().x > 0) {
                    _startFinalAnimation();
                } else {
                    _startDeathAnimation();
                }
            }
        }, 200, 1, .1f);
    }

    private void _startDeathAnimation() {
        new CountTimer(new CountTimer.Task() {

            @Override
            public void count(float amount) {
                for(CorruptEnemy e : _levelManager.getCorruptEnemies()) {
                    e.invade();
                }

                _jeff.freeze();
            }

            @Override
            public void finish() {
                _jeff.freeze();
                setMessage("From your ash, the fairy blimps escape and heal the land. THE END.");

                for(int i = 0; i < 7; i++) {
                    _shootFairy(_jeff.getPosition(), i);
                }
            }
        }, 300, 2, 0.03f);
    }

    private void _shootFairy(Vector2 pos, int i) {
        final FairyBlimp fairyBlimp = _levelManager.createBlimp(Level3.this, pos.x, pos.y);
        new CountTimer(new CountTimer.Task() {
            @Override
            public void count(float amount) {
                fairyBlimp.setRadius(200 * amount);
            }

            @Override
            public void finish() {

            }
        }, 100f, i, 0.01f);
    }

    private void _startFinalAnimation() {

        new CountTimer(new CountTimer.Task() {
            private float jeffX = _jeff.getPosition().x;
            private float jeffY = _jeff.getPosition().y;

            @Override
            public void count(float amount) {
                for(CorruptEnemy e : _levelManager.getCorruptEnemies()) {
                    e.getAbsorbed();
                }

                _jeff.paralyze();
                _jeff.setSize(_jeff.getSize().x+.3f, _jeff.getSize().x+.3f);
                _jeff.setPosition(jeffX - _jeff.getSize().x * .5f, jeffY - _jeff.getSize().x * .5f);
            }

            @Override
            public void finish() {
                _jeff.freeze();
                setMessage("Congratulations. You now reign over the corrupted lands. THE END.");
            }
        }, 300, 5, 0.03f);
    }
}
