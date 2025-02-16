package at.flori4n.mcmatchmakingteams.states.gameOver;


import at.flori4n.mcmatchmakingteams.McMatchmakingTeams;
import at.flori4n.mcmatchmakingteams.State;
import at.flori4n.mcmatchmakingteams.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.github.paperspigot.Title;

public class GameOverState implements State {

    private final GameOverListeners gameOverListeners = new GameOverListeners();

    private Team winner;
    public GameOverState(Team winner){
        this.winner = winner;
    }
    @Override
    public void preaction() {
        Bukkit.getPluginManager().registerEvents(gameOverListeners, McMatchmakingTeams.getPlugin());
    }

    @Override
    public void action() {
        String players ="";
        for (Player player: winner.getPlayers()){
            players+= player.getName()+" ";
        }
        for (Player player: Bukkit.getOnlinePlayers()){
            player.sendTitle(new Title("§2Team "+winner.getName()+"§2 hat gewonnen",players,1*20,10*20,1*20));
        }
        Bukkit.broadcastMessage("#------------------------------#\n"+
                "§2Team "+ ChatColor.GOLD+winner.getName()+"§2 hat gewonnen\n " +
                "-> "+players +" <-\n" +
                ChatColor.WHITE +"#------------------------------#");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(McMatchmakingTeams.getPlugin(), new Runnable() {
            int counter = 20;
            @Override
            public void run() {
                counter --;
                if (counter<=0){
                    Bukkit.shutdown();
                }
            }
        }, 0, 20);

    }

    @Override
    public void postAction() {
        HandlerList.unregisterAll(gameOverListeners);
    }
}
