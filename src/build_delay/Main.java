package build_delay;

import arc.*;
import mindustry.game.EventType.*;
import mindustry.game.Team;
import mindustry.mod.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends Plugin {
    public final String serverName = "[orange]mindustry.ddns.net[white]";
    public final Integer delaySeconds = 60;

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void init(){
        Events.on(PlayerJoin.class, event -> {
            event.player.team(Team.derelict);

            String joinMessage = String.format("[gray]<[#003ec8]Spawn Delay Plugin[gray]>[white] Welcome to " +
                    "%s! Please wait %d seconds before you can start interacting with the server. " +
                    "This is to ensure the safety of players' experiences.", serverName, delaySeconds);
            event.player.sendMessage(joinMessage);

            ChangeTeamFuture futureTask = new ChangeTeamFuture(event.player);
            executor.schedule(futureTask, delaySeconds, TimeUnit.SECONDS);
        });
    }
}
