package io.github.implicitsaber.mod.server_side_rocketry.reg;

import com.mojang.serialization.Codec;
import eu.pb4.polymer.core.api.other.PolymerComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Unit;

import static io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry.id;

public class ModDataComponentTypes {

    public static final RegistryKey<ComponentType<?>> SPACE_ARMOR_KEY = RegistryKey.of(RegistryKeys.DATA_COMPONENT_TYPE, id("space_armor"));
    public static final ComponentType<Unit> SPACE_ARMOR = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            SPACE_ARMOR_KEY,
            ComponentType.<Unit>builder().codec(Unit.CODEC).build()
    );

    public static final RegistryKey<ComponentType<?>> OXYGEN_KEY = RegistryKey.of(RegistryKeys.DATA_COMPONENT_TYPE, id("oxygen"));
    public static final ComponentType<Integer> OXYGEN = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            OXYGEN_KEY,
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static final RegistryKey<ComponentType<?>> MAX_OXYGEN_KEY = RegistryKey.of(RegistryKeys.DATA_COMPONENT_TYPE, id("max_oxygen"));
    public static final ComponentType<Integer> MAX_OXYGEN = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            MAX_OXYGEN_KEY,
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );


    static {
        PolymerComponent.registerDataComponent(SPACE_ARMOR);
        PolymerComponent.registerDataComponent(OXYGEN);
        PolymerComponent.registerDataComponent(MAX_OXYGEN);
    }

    public static void load() {}

}
