# Spawn Delay Plugin
The below will assume that you have some experiences in using the Mindustry server application.
## How to use the plugin
1. Copy the source code into a git repository -- whether local or remote.
2. Change the variables `serverName` and `delaySeconds` inside of the `src/build_delay/Main.java` file into the desired
values. `serverName` is the name of your server and `delaySeconds` is the delay relative to the player's join time
before their team is switched to sharded.
3. cd into the root of the repository.
4. Run `./gradlew jar` (or whatever is appropriate for your system).
5. Copy the file `build/libs/build_delay.jar` into your server's mod folder.
6. Run or restart the server and ensure that everything is working properly.

If you have any issues with using or implementing the plugin, then please open an issue. 
