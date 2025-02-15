package at.flori4n.mcmatchmakingteams.states.lobby;


import at.flori4n.mcmatchmakingteams.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Random;

public class InGameState implements State {
    InGameListeners inGameListeners = new InGameListeners();
    @Override
    public void preaction() {
        System.out.println("initIngGmeState");
        Bukkit.getPluginManager().registerEvents(inGameListeners, McMatchmakingTeams.getPlugin());
        GameData gameData = GameData.getInstance();

        for (Player player:Bukkit.getOnlinePlayers()){
            player.setGameMode(GameMode.SPECTATOR);
            player.getInventory().clear();
        }

        for (Team team:gameData.getTeams()){
            for (Player p :team.getPlayers()){
                p.setGameMode(GameMode.SURVIVAL);
                p.teleport(team.getSpawn());
                p.setBedSpawnLocation(team.getSpawn());
            }
        }


        if (GameData.getInstance().isUseBorder()){
            BorderManager.getInstance().start();
        }
    }

    @Override
    public void action() {
        System.out.println("runningInGameState");
    }

    @Override
    public void postAction() {
        System.out.println("stopInGameState");
        HandlerList.unregisterAll(inGameListeners);
    }
}
