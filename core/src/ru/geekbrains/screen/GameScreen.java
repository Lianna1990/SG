package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.MainShip;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.utils.EnemyGenerator;

public class GameScreen extends BaseScreen {

    private Texture bg;
    private Background background;
    private TextureAtlas atlas;
    private Star starList[];

    private MainShip mainShip;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;

    private Music music;
    private Sound laserSound;
    private Sound bulletSound;



    private EnemyGenerator enemyGenerator;

    @Override
    public void show() {
        super.show();
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        bg = new Texture("textures/bg.png");
        background = new Background(new TextureRegion(bg));
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        starList = new Star[64];
        for (int i = 0; i < starList.length; i++) {
            starList[i] = new Star(atlas);
        }
        bulletPool = new BulletPool();
        mainShip = new MainShip(atlas, bulletPool, laserSound);
        enemyPool = new EnemyPool(bulletPool, bulletSound, worldBounds, mainShip);
        enemyGenerator = new EnemyGenerator(atlas, enemyPool, worldBounds);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollisions();
        freeAllDestroyedSprites();
        draw();
    }

    private void update(float delta) {
        for (Star star : starList) {
            star.update(delta);
        }
        mainShip.update(delta);
        bulletPool.updateActiveSprites(delta);
        enemyPool.updateActiveSprites(delta);
        enemyGenerator.generate(delta);
    }

    private void checkCollisions() {
        //попробовала реализовать таким образом

        Iterator<Enemy> i = enemy.iterator();
        Iterator<MainShip> b = mainShip.iterator();

        while (i.hasNext() && b.hasNext()) {
            Enemy enemies = i.next();
            MainShip mainShips = b.next();

            if (Math.abs(mainShips.x - enemies.x) <= (mainShips.width + enemies.width) / 2f
                    && Math.abs(mainShips.y - enemies.y) <= (ainShips.height + enemies.height) / 2f) {
                i.remove();
                b.remove();
            }

            Iterator<Enemy> i = enemy.iterator();
            while (i.hasNext()) {
                Enemy e = i.next();
                if (((MainShip) mainShip).getRect().intersects(e.getRect(), ((MainShip) mainShip).getRect())) {
                    i.remove();
                    break;
                }

                public Rect getRect()
                {
                    new Rect(x - width / 2, y - height / 2, x + width / 2, y + height / 2);

                }
            }
        }


    }
    private void freeAllDestroyedSprites() {
        bulletPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (Star star : starList) {
            star.draw(batch);
        }
        if (!mainShip.isDestroyed()) {
            mainShip.draw(batch);
        }
        bulletPool.drawActiveSprites(batch);
        enemyPool.drawActiveSprites(batch);
        batch.end();
    }
    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : starList) {
            star.resize(worldBounds);
        }
        mainShip.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        music.dispose();
        laserSound.dispose();
        bulletSound.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        mainShip.touchDown(touch, pointer);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        mainShip.touchUp(touch, pointer);
        return false;
    }
}
