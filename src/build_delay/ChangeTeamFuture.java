package build_delay;

import mindustry.game.Team;
import mindustry.gen.Player;

class ChangeTeamFuture implements Runnable {

    private final Player player;
    private final Integer delaySeconds;

    public ChangeTeamFuture(Player player, Integer delayInSeconds)
    {
        this.player = player;
        this.delaySeconds = delayInSeconds;
    }

    @Override
    public void run()
    {
        try {
            Thread.sleep(delaySeconds * 1000);
            this.player.team(Team.sharded);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
