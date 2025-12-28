package io.github.implicitsaber.mod.server_side_rocketry.reg;

import eu.pb4.polymer.core.api.block.PolymerBlockUtils;
import io.github.implicitsaber.mod.server_side_rocketry.block_entity.CargoManagerBlockEntity;
import io.github.implicitsaber.mod.server_side_rocketry.block_entity.CargoRocketDetectorBlockEntity;
import io.github.implicitsaber.mod.server_side_rocketry.block_entity.OxygenCompressorBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import static io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry.id;

public class ModBlockEntityTypes {

    public static final RegistryKey<BlockEntityType<?>> OXYGEN_COMPRESSOR_KEY = RegistryKey.of(RegistryKeys.BLOCK_ENTITY_TYPE, id("oxygen_compressor"));
    public static final BlockEntityType<OxygenCompressorBlockEntity> OXYGEN_COMPRESSOR = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            OXYGEN_COMPRESSOR_KEY,
            FabricBlockEntityTypeBuilder.create(OxygenCompressorBlockEntity::new)
                    .addBlock(ModBlocks.OXYGEN_COMPRESSOR)
                    .build()
    );

    public static final RegistryKey<BlockEntityType<?>> CARGO_ROCKET_DETECTOR_KEY = RegistryKey.of(RegistryKeys.BLOCK_ENTITY_TYPE, id("cargo_rocket_detector"));
    public static final BlockEntityType<CargoRocketDetectorBlockEntity> CARGO_ROCKET_DETECTOR = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            CARGO_ROCKET_DETECTOR_KEY,
            FabricBlockEntityTypeBuilder.create(CargoRocketDetectorBlockEntity::new)
                    .addBlock(ModBlocks.CARGO_ROCKET_DETECTOR)
                    .build()
    );

    public static final RegistryKey<BlockEntityType<?>> CARGO_MANAGER_KEY = RegistryKey.of(RegistryKeys.BLOCK_ENTITY_TYPE, id("cargo_manager"));
    public static final BlockEntityType<CargoManagerBlockEntity> CARGO_MANAGER = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            CARGO_MANAGER_KEY,
            FabricBlockEntityTypeBuilder.create(CargoManagerBlockEntity::new)
                    .addBlock(ModBlocks.CARGO_MANAGER)
                    .build()
    );

    static {
        PolymerBlockUtils.registerBlockEntity(OXYGEN_COMPRESSOR);
        PolymerBlockUtils.registerBlockEntity(CARGO_ROCKET_DETECTOR);
        PolymerBlockUtils.registerBlockEntity(CARGO_MANAGER);
    }

    public static void load() {}

}
