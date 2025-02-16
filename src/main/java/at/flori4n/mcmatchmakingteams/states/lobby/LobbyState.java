package at.flori4n.mcmatchmakingteams.states.lobby;

import at.flori4n.mcmatchmakingteams.*;
import at.flori4n.mcmatchmakingteams.states.inGame.InGameState;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.List;

public class LobbyState implements State {

    private final LobbyListeners lobbyListeners = new LobbyListeners(this);
    private final GuiListeners guiListeners = new GuiListeners();
    private int taskId;
    private GameData gameData = GameData.getInstance();
    @Getter
    private boolean taskRunning = false;

    @Override
    public void preaction() {
        System.out.println("initLobbyState");
        Bukkit.getPluginManager().registerEvents(lobbyListeners, McMatchmakingTeams.getPlugin());
        Bukkit.getPluginManager().registerEvents(guiListeners, McMatchmakingTeams.getPlugin());

    }

    @Override
    public void action() {
        System.out.println("runningLobbyState");
    }

    @Override
    public void postAction() {
        System.out.println("stopLobbyState");
        HandlerList.unregisterAll(lobbyListeners);
        HandlerList.unregisterAll(guiListeners);
        stopCounter();
        putPlayersInTeams();
    }
    public void putPlayersInTeams(){

        List<Team> teams = new ArrayList<>(gameData.getTeams());
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        while (!players.isEmpty()&&!teams.isEmpty()) {
            Player player = players.get(0);
            Team team = getLowestTeam(teams);
            if (team.isFull()) {
                teams.remove(team);
                continue;
            }
            if (gameData.getPlayerTeam(player) == null) team.addPlayer(player);
            players.remove(player);
        }
        System.out.println("playerS::");
        Bukkit.getOnlinePlayers().forEach(player -> {
            System.out.println(player.getName()+"\n");
        });
        System.out.println("playerS::");
    }
    public Team getLowestTeam(List<Team> teams){
        Team lowestTeam = teams.get(0);
        for (Team team: teams){
            if (team.getPlayers().size()<lowestTeam.getPlayers().size()){
                lowestTeam = team;
            }
        }
        return lowestTeam;
    }

    public void startCounter(){
        taskRunning = true;
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(McMatchmakingTeams.getPlugin(), new Runnable() {
            int counter = GameData.getInstance().getLobbyTime();
            @Override
            public void run() {

                if (counter%10 == 0 && counter>0){
                    Bukkit.broadcastMessage("Runde startet in "+counter+" Sekunden");
                    playSound();
                }
                if (counter<5){
                    Bukkit.broadcastMessage("Runde startet in "+counter+" Sekunden");
                    playSound();
                }
                if (counter<=0){
                    Manager.getInstance().setState(new InGameState());
                }
                counter --;
            }
            public void playSound(){
                for (Player player:Bukkit.getOnlinePlayers()){
                    player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM,1,1);
                }
            }
        }, 0, 20);

    }

    public void stopCounter(){
        Bukkit.getScheduler().cancelTask(taskId);
        taskRunning = false;
    }


}
