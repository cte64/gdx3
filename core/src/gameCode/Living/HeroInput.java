package gameCode.Living;

import gameCode.Infastructure.Component;
import gameCode.Infastructure.Entity;

public class HeroInput extends Component {


    public HeroInput() {
        type = "input";
    }

    public void update(Entity entity) {
        entity.x_pos += 0.1;
    }
}
