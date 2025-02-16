package at.flori4n.mcmatchmakingteams.states.inGame;


import at.flori4n.mcmatchmakingteams.GameData;
import at.flori4n.mcmatchmakingteams.Manager;
import at.flori4n.mcmatchmakingteams.Team;
import at.flori4n.mcmatchmakingteams.states.gameOver.GameOverState;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.List;
import java.util.stream.Collectors;

public class InGameListeners implements Listener {
    GameData gameData = GameData.getInstance();

    @EventHandler
    public void blockBreakListener(BlockBreakEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void weatherChangeListener(WeatherChangeEvent e){
        e.setCancelled(true);
        e.getWorld().setThundering(false);
    }
    @EventHandler
    public void playerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(gameData.getLobbyLocation());
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Team team = gameData.getPlayerTeam(player);
        if (team == null)return;
        team.removePlayer(player);
        checkWin();
    }

    @EventHandler
    public void foodListener(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void onPlayerDeath (PlayerDeathEvent e){
        Player player = e.getEntity();
        Team team = gameData.getPlayerTeam(player);
        if (team == null)return;
        if (team.getLives()<=0){
            player.setGameMode(GameMode.SPECTATOR);
            team.getLivingPlayers().remove(player);
            checkWin();
        }else {
            team.setLives(team.getLives() - 1);
        }
    }
    public void checkWin(){
        List<Team> teamsAlive = gameData.getTeams().stream().filter(t -> !t.getLivingPlayers().isEmpty()).collect(Collectors.toList());
        if (teamsAlive.size()==1)Manager.getInstance().setState(new GameOverState(teamsAlive.get(0)));
    }
}
