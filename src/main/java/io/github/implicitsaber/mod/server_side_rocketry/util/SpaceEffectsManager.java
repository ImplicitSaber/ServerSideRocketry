package io.github.implicitsaber.mod.server_side_rocketry.util;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.slf4j.Logger;

public class SpaceEffectsManager {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Object2ObjectOpenHashMap<RegistryKey<World>, SpaceEffects> EFFECTS_MAP = new Object2ObjectOpenHashMap<>();

    public static void register(RegistryKey<World> world, SpaceEffects effects) {
        if(EFFECTS_MAP.containsKey(world)){
            LOGGER.warn("Attempted to overwrite space effects for world {}. Keeping original value.", world);
            return;
        }
        EFFECTS_MAP.put(world, effects);
    }

    public static SpaceEffects getSpaceEffectsFor(RegistryKey<World> world) {
        return EFFECTS_MAP.getOrDefault(world, SpaceEffects.DEFAULT);
    }

    public record SpaceEffects(double gravity, boolean hasOxygen, Climate climate, boolean highPressure) {
        public static final SpaceEffects DEFAULT = new SpaceEffects(1, true, Climate.NORMAL, false);
    }

}
