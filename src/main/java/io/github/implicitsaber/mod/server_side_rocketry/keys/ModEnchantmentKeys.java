package io.github.implicitsaber.mod.server_side_rocketry.keys;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import static io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry.id;

public class ModEnchantmentKeys {

    public static final RegistryKey<Enchantment> INSULATION = RegistryKey.of(RegistryKeys.ENCHANTMENT, id("insulation"));
    public static final RegistryKey<Enchantment> PRESSURE_RESISTANCE = RegistryKey.of(RegistryKeys.ENCHANTMENT, id("pressure_resistance"));

}
