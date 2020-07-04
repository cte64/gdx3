package gameCode.Living;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import gameCode.Infastructure.Component;
import gameCode.Infastructure.Entity;

public class HeroInput extends Component {


    float deltaX = 0.35f;

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
