package at.flori4n.mcmatchmakingteams;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private org.bukkit.scoreboard.Team team;
    @Getter
    @Setter
    private Location spawn;
    @Getter
    @Setter
    private int size;
    @Getter
    @Setter
    private int lives;
    @Getter
    @Setter
    private List<Player> livingPlayers = new ArrayList<>();

    public Team(String name, int size, Location spawn, int lives) {
        this.size = size;
        this.spawn = spawn;
        this.lives = lives;
        setupTeam(name);
    }
    private void setupTeam(String name){
        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(name) != null) {
            Bukkit.getScoreboardManager().getMainScoreboard().getTeam(name).unregister();
        }
        team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(name);
        team.setPrefix(name+": ");
        team.setAllowFriendlyFire(false);
    }

    public void addPlayer(OfflinePlayer player){
        Team prevTeam = GameData.getInstance().getPlayerTeam((Player)player);
        if (prevTeam!=null) {
            prevTeam.removePlayer(player);
        }
        if (isFull())throw new RuntimeException("teamIstVoll");
        livingPlayers.add((Player) player);
        team.addEntry(player.getName());
    }
    public boolean isFull(){
        return (team.getSize() >= size);
    }
    public void removePlayer(OfflinePlayer player){
        team.removeEntry(player.getName());
        livingPlayers.remove((Player) player);
    }
    public boolean hasPlayer(OfflinePlayer player){
        return team.hasEntry(player.getName());
    }
    public List<Player> getPlayers(){
        List<Player> players = new ArrayList<>();
        for (String name : team.getEntries()){
            System.out.println(name);
            players.add(Bukkit.getPlayer(name));
        }
        System.out.println(players.size());
        return players;
    }
    public String getName(){
        return team.getName();
    }

}
