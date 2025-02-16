package at.flori4n.mcmatchmakingteams;

import at.flori4n.mcmatchmakingteams.commands.Commands;
import at.flori4n.mcmatchmakingteams.commands.StartCommand;
import at.flori4n.mcmatchmakingteams.states.lobby.LobbyState;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class McMatchmakingTeams extends JavaPlugin {

    @Getter
    private static McMatchmakingTeams plugin;

    @Override
    public void onEnable() {
        plugin = this;
        getCommand("mcMatchmaking").setExecutor(new Commands());
        if (GameData.getInstance().isStart()){
            Manager.getInstance().setState(new LobbyState());
            getCommand("start").setExecutor(new StartCommand());
        }
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
