package at.flori4n.mcmatchmakingteams;

public interface State {
    public void preaction();
    public void action();
    public void postAction();
}
