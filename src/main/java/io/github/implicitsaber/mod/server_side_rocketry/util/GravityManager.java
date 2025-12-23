package io.github.implicitsaber.mod.server_side_rocketry.util;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.slf4j.Logger;

public class GravityManager {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Object2DoubleOpenHashMap<RegistryKey<World>> GRAVITY_MAP = new Object2DoubleOpenHashMap<>();

    public static void register(RegistryKey<World> world, double gravity) {
        if(GRAVITY_MAP.containsKey(world)){
            LOGGER.warn("Attempted to overwrite gravity value for world {}. Keeping original value.", world);
            return;
        }
        GRAVITY_MAP.put(world, gravity);
    }

    public static double getGravityMultiplierFor(RegistryKey<World> world) {
        return GRAVITY_MAP.getOrDefault(world, 1);
    }

}
