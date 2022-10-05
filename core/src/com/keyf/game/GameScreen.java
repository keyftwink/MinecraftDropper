package com.keyf.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
    final MinecraftDrop game;

    Texture appleImg;
    Texture apple_goldenImg;
    Texture cakeImg;
    Texture sugarImg;
    Texture[] foodDropArray;
    Texture steveImg;
    Texture gameBackgroundImg;

    Sound pickSound;
    Music gameMusic;

    OrthographicCamera camera;
    SpriteBatch batch;

    Rectangle foodCatcher;
    Array<FoodDrop> foodDrops;

    long lastFoodTime;
    int speed = 200;
    long time = 1000000000;
    int combo = 0;
    boolean isModeActive = false;


    public GameScreen(final MinecraftDrop gam) {
        this.game = gam;

        appleImg = new Texture(Gdx.files.internal("apple.png"));
        apple_goldenImg = new Texture(Gdx.files.internal("apple_golden.png"));
        cakeImg = new Texture(Gdx.files.internal("cake.png"));
        sugarImg = new Texture(Gdx.files.internal("sugar.png"));
        gameBackgroundImg = new Texture(Gdx.files.internal("gameBackground.jpg"));
        steveImg = new Texture(Gdx.files.internal("steve.png"));

        foodDropArray = new Texture[] {appleImg, cakeImg, apple_goldenImg, sugarImg};

        pickSound = Gdx.audio.newSound(Gdx.files.internal("pick.wav"));
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        gameMusic.setLooping(true);

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        foodCatcher = new Rectangle();
        foodCatcher.x = 800 / 2 - 64 / 2;
        foodCatcher.y = 20;

        foodCatcher.width = 64;
        foodCatcher.height = 64;

        foodDrops = new Array<>();
        spawnFooddrop();

    }

    private void spawnFooddrop() {
        Circle food = new Circle();
        int type = 0;
        if (isModeActive & speed > 200){
            speed-=10;
        }
        if (isModeActive & time < 1000000000){
            time+=25000000;
        }
        food.x = MathUtils.random(0, 800 - 64);

        if(MathUtils.randomBoolean(0.40f)){
            type =0;
        }
        else if(MathUtils.randomBoolean(0.20f)){
            type = 1;
        }
        else if(MathUtils.randomBoolean(0.20f)){
            type = 2;
        }
        else if(MathUtils.randomBoolean(0.15f)){
            type = 3;
        }
        food.y = Gdx.graphics.getHeight();
        food.radius = 30;
        foodDrops.add(new FoodDrop(food, type));
        lastFoodTime = TimeUtils.nanoTime();
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(gameBackgroundImg,0,0);
        batch.draw(steveImg, foodCatcher.x, foodCatcher.y);

        game.font.draw(batch, "Collected food: " + combo, 20, 460);

        for (FoodDrop foodDrop : foodDrops) {
            batch.draw(foodDropArray[foodDrop.type], foodDrop.circle.x, foodDrop.circle.y);
        }
        batch.end();

        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            foodCatcher.x = touchPos.x - 64 / 2;
        }
        if (Gdx.input.isKeyPressed(Keys.LEFT))
            foodCatcher.x -= 600 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            foodCatcher.x += 600 * Gdx.graphics.getDeltaTime();

        if (foodCatcher.x < 0)
            foodCatcher.x = 0;
        if (foodCatcher.x > 800 - 64)
            foodCatcher.x = 800 - 64;

        if (TimeUtils.nanoTime() - lastFoodTime > 1000000000)
            spawnFooddrop();

        Array.ArrayIterator<FoodDrop> iter = foodDrops.iterator();
        while (iter.hasNext()) {
            FoodDrop foodDrop = iter.next();
            foodDrop.circle.y -= 200 * Gdx.graphics.getDeltaTime();
            if (foodDrop.circle.x + 64 < 0)
                iter.remove();
            if (Intersector.overlaps(foodDrop.circle, foodCatcher)) {
                if (foodDrop.type == 0) {
                    combo++;
                }
                if (foodDrop.type == 1) {
                    combo += 2;
                }
                iter.remove();
                if (foodDrop.type == 2) {
                    combo += 3;
                }
                if (foodDrop.type == 3) {
                    time = 600;
                    isModeActive = true;
                }
                pickSound.play();
            }
        }
    }
    public void show() {
        gameMusic.play();
    }
    class FoodDrop {
        Circle circle;
        int type;

        public FoodDrop(Circle circle, int type) {
            this.circle = circle;
            this.type = type;
        }
    }
    public void resize(int width, int height) {
    }
    public void hide() {
    }
    public void pause() {
    }
    public void resume() {
    }
    public void dispose() {
        appleImg.dispose();
        apple_goldenImg.dispose();
        sugarImg.dispose();
        cakeImg.dispose();
        steveImg.dispose();

        pickSound.dispose();
        gameMusic.dispose();

        batch.dispose();
    }
}