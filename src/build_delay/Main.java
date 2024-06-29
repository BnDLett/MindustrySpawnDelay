package build_delay;

import arc.*;
import arc.util.CommandHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mindustry.game.EventType.*;
import mindustry.game.Team;
import mindustry.gen.Player;
import mindustry.mod.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main extends Plugin {
    private static String serverName;
    private static Integer delaySeconds;
    private final String pluginMessageName = "[gray]<[#003ec8]Spawn Delay Plugin[gray]>[white]";

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File configFile = new File("config/mods/spawn_delay_config.json");

    public void UpdateDelay(Integer newDelay) {
        ObjectNode jsonNode = objectMapper.createObjectNode();

        delaySeconds = newDelay;
        jsonNode.put("delayTime", newDelay);
        try {
            objectMapper.writeValue(configFile, jsonNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void LoadConfiguration() throws IOException {
        JsonNode jsonNode = objectMapper.readTree(configFile);

        serverName = jsonNode.get("serverName").asText("NON-CONFIGURED CONFIG FILE");
        Integer newDelay = jsonNode.get("delayTime").asInt(5);
        UpdateDelay(newDelay);
    }

    @Override
    public void init(){
        try {
            LoadConfiguration();
        } catch (IOException e) {
            delaySeconds = 0;
            serverName = "UNABLE TO LOAD CONFIGURATION";
        }

        Events.on(PlayerJoin.class, event -> {
            event.player.team(Team.derelict);

            String joinMessage = String.format("%s Welcome to " +
                    "%s! Please wait %d seconds before you can start interacting with the server. " +
                    "This is to ensure the safety of players' experiences.", pluginMessageName, serverName, delaySeconds);
            event.player.sendMessage(joinMessage);

            ChangeTeamFuture futureTask = new ChangeTeamFuture(event.player);
            executor.schedule(futureTask, delaySeconds, TimeUnit.SECONDS);
        });
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {
        handler.<Player>register("update_delay", "<text...>", "Update the spawn delay.", (args,
                                                                                        player) -> {
            if (!player.admin) {
                String noPermissionString = String.format("%s You do not have the permission to run this command.",
                        pluginMessageName);
                player.sendMessage(noPermissionString);
                return;
            }
            try {
                Integer newDelay = Integer.valueOf(args[0]);
                UpdateDelay(newDelay);

                String successString = String.format("%s Delay was successfully updated.", pluginMessageName);
                player.sendMessage(successString);
            } catch (NumberFormatException e) {
                String invalidValueString = String.format("%s The value that you provided is not a valid integer.",
                        pluginMessageName);
                player.sendMessage(invalidValueString);
            }
        });
    }
}
