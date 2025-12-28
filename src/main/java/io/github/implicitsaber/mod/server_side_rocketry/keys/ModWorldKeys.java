package io.github.implicitsaber.mod.server_side_rocketry.keys;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

import static io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry.id;

public class ModWorldKeys {

    public static final RegistryKey<World> MOON = RegistryKey.of(RegistryKeys.WORLD, id("moon"));
    public static final RegistryKey<World> MERCURY = RegistryKey.of(RegistryKeys.WORLD, id("mercury"));
    public static final RegistryKey<World> VENUS = RegistryKey.of(RegistryKeys.WORLD, id("venus"));
    public static final RegistryKey<World> MARS = RegistryKey.of(RegistryKeys.WORLD, id("mars"));

}
