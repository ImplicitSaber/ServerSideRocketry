package io.github.implicitsaber.mod.server_side_rocketry.reg;

import eu.pb4.polymer.core.api.other.PolymerSoundEvent;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;

import static io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry.id;

public class ModSoundEvents {

    public static final RegistryKey<SoundEvent> ENTITY_ROCKET_ENGINE_KEY = RegistryKey.of(RegistryKeys.SOUND_EVENT, id("entity.rocket.engine"));
    public static final SoundEvent ENTITY_ROCKET_ENGINE = Registry.register(
            Registries.SOUND_EVENT,
            ENTITY_ROCKET_ENGINE_KEY,
            SoundEvent.of(ENTITY_ROCKET_ENGINE_KEY.getValue())
    );

    static {
        PolymerSoundEvent.registerOverlay(ENTITY_ROCKET_ENGINE);
    }

    public static void load() {}

}
