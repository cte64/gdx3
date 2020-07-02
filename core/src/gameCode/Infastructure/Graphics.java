package gameCode.Infastructure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import gameCode.Infastructure.World;

import java.util.HashMap;

public class Graphics implements Disposable {

    private TextureAtlas spriteAtlas;
    private SpriteBatch batch;
    private Camera camera;

    private int a = 0;

    private static HashMap<String, Sprite> spriteMap;

    public Graphics()  {

        //Set up the camera ======================================================================
        camera = new OrthographicCamera(World.getViewPortWidth(), World.getViewPortHeight());
        camera.position.set(0, 0, 0);
        camera.update();

        //Set up the sprites =====================================================================
        batch = new SpriteBatch();
        spriteMap = new HashMap<String, Sprite>();
        spriteAtlas = new TextureAtlas("/Users/me/Desktop/gdx3/core/assets/atlas.atlas");
        addSprite("tile");
    }

    private void addSprite(String filename) {
        TextureRegion region = spriteAtlas.findRegion(filename);
        Sprite sprite = new Sprite(region);
        Sprite put = spriteMap.put(filename, sprite);
    }

    public void update() {

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        camera.position.set(a, 0, 0);
        a++;
        camera.update();

        batch.begin();
        for(Entity ent: World.getEntList()) { batch.draw(spriteMap.get(ent.spriteName), ent.x_pos, ent.y_pos); }
        batch.end();
    }

    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    @Override
    public void dispose() {
        batch.dispose();
    }



}
