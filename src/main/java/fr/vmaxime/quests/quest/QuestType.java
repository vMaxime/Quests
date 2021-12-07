package fr.vmaxime.quests.quest;

public enum QuestType {

    WALK(WalkQuest.class),
    BREAK(BreakQuest.class),
    PLACE(PlaceQuest.class),
    KILL_MOBS(KillMobsQuest.class);

    private final Class<? extends Quest> clazz;

    QuestType(Class<? extends Quest> clazz) {
        this.clazz = clazz;
    }

    public Quest newInstance() {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
