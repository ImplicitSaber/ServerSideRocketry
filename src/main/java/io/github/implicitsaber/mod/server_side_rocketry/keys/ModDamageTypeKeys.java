package io.github.implicitsaber.mod.server_side_rocketry.keys;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import static io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry.id;

public class ModDamageTypeKeys {

    public static final RegistryKey<DamageType> OXYGEN_LOSS = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, id("oxygen_loss"));

}
