package io.github.implicitsaber.mod.server_side_rocketry.reg;

import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import io.github.implicitsaber.mod.server_side_rocketry.entity.PassengersHolderEntity;
import io.github.implicitsaber.mod.server_side_rocketry.entity.RocketEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import static io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry.id;

public class ModEntityTypes {

    public static final RegistryKey<EntityType<?>> ROCKET_KEY = RegistryKey.of(RegistryKeys.ENTITY_TYPE, id("rocket"));
    public static final EntityType<RocketEntity> ROCKET = Registry.register(
            Registries.ENTITY_TYPE,
            ROCKET_KEY,
            EntityType.Builder.create(RocketEntity::new, SpawnGroup.MISC)
                    .dropsNothing()
                    .dimensions(1, 3)
                    .maxTrackingRange(8)
                    .build(ROCKET_KEY)
    );

    public static final RegistryKey<EntityType<?>> PASSENGERS_HOLDER_KEY = RegistryKey.of(RegistryKeys.ENTITY_TYPE, id("passengers_holder"));
    public static final EntityType<PassengersHolderEntity> PASSENGERS_HOLDER = Registry.register(
            Registries.ENTITY_TYPE,
            PASSENGERS_HOLDER_KEY,
            EntityType.Builder.create(PassengersHolderEntity::new, SpawnGroup.MISC)
                    .dropsNothing()
                    .dimensions(1, 1)
                    .maxTrackingRange(8)
                    .build(PASSENGERS_HOLDER_KEY)
    );

    static {
        PolymerEntityUtils.registerType(ROCKET);
        PolymerEntityUtils.registerType(PASSENGERS_HOLDER);
    }

    public static void load() {}

}
