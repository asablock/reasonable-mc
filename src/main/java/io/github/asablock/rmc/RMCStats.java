package io.github.asablock.rmc;

import net.minecraft.stat.Stat;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.StatType;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RMCStats {
    public static final Identifier INTERACT_WITH_CHOPPINGBLOCK = register("interact_with_choppingblock", StatFormatter.DEFAULT);
    public static final Identifier INTERACT_WITH_FLETCHINGTABLE;
    public static final Identifier INTERACT_WITH_MILL = register("interact_with_mill", StatFormatter.DEFAULT);
    public static final Identifier INTERACT_WITH_STONE_MORTAR = register("interact_with_stone_mortar", StatFormatter.DEFAULT);

    private static Identifier register(String id, StatFormatter formatter) {
        Identifier identifier = ReasonableMCMod.identifier(id);
        Registry.register(Registry.CUSTOM_STAT, id, identifier);
        Stats.CUSTOM.getOrCreateStat(identifier, formatter);
        return identifier;
    }

    private static <T> StatType<T> registerType(String id, Registry<T> registry) {
        return Registry.register(Registry.STAT_TYPE, ReasonableMCMod.identifier(id), new StatType<>(registry));
    }

    static {
        INTERACT_WITH_FLETCHINGTABLE = new Identifier("interact_with_fletchingtable");
        Registry.register(Registry.CUSTOM_STAT, "interact_with_fletchingtable", INTERACT_WITH_FLETCHINGTABLE);
        Stats.CUSTOM.getOrCreateStat(INTERACT_WITH_FLETCHINGTABLE, StatFormatter.DEFAULT);
    }
}
