package io.github.implicitsaber.mod.server_side_rocketry.reg;

import eu.pb4.polymer.core.api.other.PolymerComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Unit;

import static io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry.id;

public class ModDataComponents {

    public static final RegistryKey<ComponentType<?>> SPACE_ARMOR_KEY = RegistryKey.of(RegistryKeys.DATA_COMPONENT_TYPE, id("space_armor"));
    public static final ComponentType<Unit> SPACE_ARMOR = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            SPACE_ARMOR_KEY,
            ComponentType.<Unit>builder().codec(Unit.CODEC).build()
    );

    static {
        PolymerComponent.registerDataComponent(SPACE_ARMOR);
    }

    public static void load() {}

}
