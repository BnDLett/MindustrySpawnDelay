package build_delay;

import mindustry.game.Team;
import mindustry.gen.Player;
import java.util.concurrent.Callable;

class ChangeTeamFuture implements Runnable {

    private final Player player;

    public ChangeTeamFuture(Player player)
    {
        this.player = player;
    }

    @Override
    public void run()
    {
        this.player.team(Team.sharded);
    }
}
