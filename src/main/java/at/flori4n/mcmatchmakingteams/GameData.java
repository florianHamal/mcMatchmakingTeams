package at.flori4n.mcmatchmakingteams;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class GameData {

    private static GameData instance;

    @Getter
    @Setter
    private float borderStartingSize,borderDamage;
    @Getter
    @Setter
    private long borderShrinkingSpeed;
    @Getter
    @Setter
    private Location borderCenter;
    @Getter
    @Setter
    private float borderMovementSpeed;
    @Getter
    private List<Location> borderEndPoints = new ArrayList<>();
    @Getter
    @Setter
    private boolean useBorder,start;
    @Getter
    @Setter
    private int lobbyTime,playersToStart;
    @Getter
    @Setter
    private Location lobbyLocation;
    @Getter
    @Setter
    private List<Team> teams = new ArrayList<>();
    @Getter
    @Setter
    private boolean switchSpawns;
    @Getter
    private int maxPlayers;

    private void load(){
        FileConfiguration config = McMatchmakingTeams.getPlugin().getConfig();
        start = config.getBoolean("start");
        switchSpawns = config.getBoolean("switchSpawns");
        lobbyLocation = (Location) config.get("lobbyLocation");
        lobbyTime = config.getInt("lobbyTime");
        playersToStart = config.getInt("playersToStart");
        //border config
        useBorder = config.getBoolean("useBorder");
        borderStartingSize = config.getLong("borderStartingSize");
        borderDamage = (float) config.getDouble("borderDamage");
        borderShrinkingSpeed = (long) config.getDouble("borderShrinkingSpeed");
        borderCenter = (Location) config.get("borderCenter");
        borderMovementSpeed = (float) config.getDouble("borderMovementSpeed");
        List<Location> borderEndPoints = (List<Location>)config.get("borderEndPoints");
        if (borderEndPoints!=null)this.borderEndPoints = borderEndPoints;



        ConfigurationSection section = config.getConfigurationSection("teams");
        if (section==null) return;
        for (String s:section.getKeys(false)){
            ConfigurationSection teamSection = section.getConfigurationSection(s);
            System.out.println(teamSection.getName());
            Team team = new Team(
                    teamSection.getName(),
                    teamSection.getInt("size"),
                    (Location) teamSection.get("spawn"),
                    teamSection.getInt("lives")
            );
            teams.add(team);
        }
    }

    public void save(){
        FileConfiguration config = McMatchmakingTeams.getPlugin().getConfig();
        config.set("borderStartingSize", borderStartingSize);
        config.set("borderDamage", borderDamage);
        config.set("borderShrinkingSpeed", borderShrinkingSpeed);
        config.set("borderCenter", borderCenter);
        config.set("borderMovementSpeed", borderMovementSpeed);
        config.set("borderEndPoints", borderEndPoints);
        config.set("useBorder", useBorder);
        config.set("start", start);
        config.set("lobbyLocation", lobbyLocation);
        config.set("lobbyTime", lobbyTime);
        config.set("playersToStart", playersToStart);
        config.set("switchSpawns",switchSpawns);



        ConfigurationSection section = config.createSection("teams");
        for (Team team:teams){
            ConfigurationSection teamSection = section.createSection(team.getName());
            teamSection.set("size",team.getSize());
            teamSection.set("spawn",team.getSpawn());
            teamSection.set("lives",team.getLives());
        }

        McMatchmakingTeams.getPlugin().saveConfig();
    }

    private GameData() {
        load();
        maxPlayers = 0;
        teams.stream().forEach(t->maxPlayers+=t.getSize());
        if (switchSpawns)switchSpawns();
    }
    public void switchSpawns(){
        List<Location> spawnPoints = teams.stream().map(Team::getSpawn).collect(Collectors.toList());
        Random random = new Random();
        for (Team team:teams){
            Location spawn = spawnPoints.get(random.nextInt(spawnPoints.size()));
            team.setSpawn(spawn);
            spawnPoints.remove(spawn);
        }
    }

    public Team getTeamByName(String name){
        for (Team t :teams){
            if (Objects.equals(t.getName(), name))return t;
        }
        return null;
    }
    public void addTeam(Team team){
        teams.add(team);
    }

    public Team getPlayerTeam(Player player){
        for (Team t :teams){
            if (t.hasPlayer(player))return t;
        }
        return null;
    }
    public static GameData getInstance() {
        if (instance == null) instance = new GameData();
        return instance;
    }
}
