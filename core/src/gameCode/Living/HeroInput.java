package gameCode.Living;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import gameCode.Infrastructure.Component;
import gameCode.Infrastructure.Entity;

public class HeroInput extends Component {


    float deltaX = 10.6f;

    public HeroInput() {
        type = "input";
    }

    public void update(Entity entity) {

        if(Gdx.input.isKeyPressed(Input.Keys.A)) { entity.x_pos -= deltaX; }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) { entity.x_pos += deltaX; }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) { entity.y_pos += deltaX; }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) { entity.y_pos -= deltaX; }
    }
}
