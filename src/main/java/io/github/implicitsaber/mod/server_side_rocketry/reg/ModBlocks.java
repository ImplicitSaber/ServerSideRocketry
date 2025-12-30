package io.github.implicitsaber.mod.server_side_rocketry.reg;

import io.github.implicitsaber.mod.server_side_rocketry.block.*;
import io.github.implicitsaber.mod.server_side_rocketry.poly.PolyBlockModels;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import static io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry.id;

public class ModBlocks {

    public static final RegistryKey<Block> MOON_ROCK_KEY = RegistryKey.of(RegistryKeys.BLOCK, id("moon_rock"));
    public static final ServerTexturedBlock MOON_ROCK = Registry.register(
            Registries.BLOCK,
            MOON_ROCK_KEY,
            new ServerTexturedBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE).registryKey(MOON_ROCK_KEY),
                    Blocks.SMOOTH_STONE.getDefaultState(), PolyBlockModels.MOON_ROCK_STATE)
    );

    public static final RegistryKey<Block> MARS_ROCK_KEY = RegistryKey.of(RegistryKeys.BLOCK, id("mars_rock"));
    public static final ServerTexturedBlock MARS_ROCK = Registry.register(
            Registries.BLOCK,
            MARS_ROCK_KEY,
            new ServerTexturedBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE).registryKey(MARS_ROCK_KEY),
                    Blocks.NETHERRACK.getDefaultState(), PolyBlockModels.MARS_ROCK_STATE)
    );

    public static final RegistryKey<Block> MERCURY_ROCK_KEY = RegistryKey.of(RegistryKeys.BLOCK, id("mercury_rock"));
    public static final ServerTexturedBlock MERCURY_ROCK = Registry.register(
            Registries.BLOCK,
            MERCURY_ROCK_KEY,
            new ServerTexturedBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE).registryKey(MERCURY_ROCK_KEY),
                    Blocks.NETHERRACK.getDefaultState(), PolyBlockModels.MERCURY_ROCK_STATE)
    );

    public static final RegistryKey<Block> VENUS_ROCK_KEY = RegistryKey.of(RegistryKeys.BLOCK, id("venus_rock"));
    public static final ServerTexturedBlock VENUS_ROCK = Registry.register(
            Registries.BLOCK,
            VENUS_ROCK_KEY,
            new ServerTexturedBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE).registryKey(VENUS_ROCK_KEY),
                    Blocks.NETHERRACK.getDefaultState(), PolyBlockModels.VENUS_ROCK_STATE)
    );

    public static final RegistryKey<Block> OXYGEN_COMPRESSOR_KEY = RegistryKey.of(RegistryKeys.BLOCK, id("oxygen_compressor"));
    public static final OxygenCompressorBlock OXYGEN_COMPRESSOR = Registry.register(
            Registries.BLOCK,
            OXYGEN_COMPRESSOR_KEY,
            new OxygenCompressorBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).registryKey(OXYGEN_COMPRESSOR_KEY))
    );

    public static final RegistryKey<Block> CARGO_LAUNCH_CONTROLLER_KEY = RegistryKey.of(RegistryKeys.BLOCK, id("cargo_launch_controller"));
    public static final CargoLaunchControllerBlock CARGO_LAUNCH_CONTROLLER = Registry.register(
            Registries.BLOCK,
            CARGO_LAUNCH_CONTROLLER_KEY,
            new CargoLaunchControllerBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).registryKey(CARGO_LAUNCH_CONTROLLER_KEY))
    );

    public static final RegistryKey<Block> CARGO_ROCKET_DETECTOR_KEY = RegistryKey.of(RegistryKeys.BLOCK, id("cargo_rocket_detector"));
    public static final CargoRocketDetectorBlock CARGO_ROCKET_DETECTOR = Registry.register(
            Registries.BLOCK,
            CARGO_ROCKET_DETECTOR_KEY,
            new CargoRocketDetectorBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).registryKey(CARGO_ROCKET_DETECTOR_KEY))
    );

    public static final RegistryKey<Block> CARGO_MANAGER_KEY = RegistryKey.of(RegistryKeys.BLOCK, id("cargo_manager"));
    public static final CargoManagerBlock CARGO_MANAGER = Registry.register(
            Registries.BLOCK,
            CARGO_MANAGER_KEY,
            new CargoManagerBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).registryKey(CARGO_MANAGER_KEY))
    );

    public static void load() {}

}
