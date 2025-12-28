package io.github.implicitsaber.mod.server_side_rocketry.reg;

import io.github.implicitsaber.mod.server_side_rocketry.item.ServerBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import static io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry.id;

public class ModBlockItems {

    public static final RegistryKey<Item> MOON_ROCK_KEY = RegistryKey.of(RegistryKeys.ITEM, id("moon_rock"));
    public static final ServerBlockItem MOON_ROCK = Registry.register(
            Registries.ITEM,
            MOON_ROCK_KEY,
            new ServerBlockItem(ModBlocks.MOON_ROCK, Items.SMOOTH_STONE, new Item.Settings().registryKey(MOON_ROCK_KEY))
    );

    public static final RegistryKey<Item> OXYGEN_COMPRESSOR_KEY = RegistryKey.of(RegistryKeys.ITEM, id("oxygen_compressor"));
    public static final ServerBlockItem OXYGEN_COMPRESSOR = Registry.register(
            Registries.ITEM,
            OXYGEN_COMPRESSOR_KEY,
            new ServerBlockItem(ModBlocks.OXYGEN_COMPRESSOR, Items.IRON_BLOCK, new Item.Settings().registryKey(OXYGEN_COMPRESSOR_KEY))
    );

    public static final RegistryKey<Item> CARGO_LAUNCH_CONTROLLER_KEY = RegistryKey.of(RegistryKeys.ITEM, id("cargo_launch_controller"));
    public static final ServerBlockItem CARGO_LAUNCH_CONTROLLER = Registry.register(
            Registries.ITEM,
            CARGO_LAUNCH_CONTROLLER_KEY,
            new ServerBlockItem(ModBlocks.CARGO_LAUNCH_CONTROLLER, Items.IRON_BLOCK, new Item.Settings().registryKey(CARGO_LAUNCH_CONTROLLER_KEY))
    );

    public static final RegistryKey<Item> CARGO_ROCKET_DETECTOR_KEY = RegistryKey.of(RegistryKeys.ITEM, id("cargo_rocket_detector"));
    public static final ServerBlockItem CARGO_ROCKET_DETECTOR = Registry.register(
            Registries.ITEM,
            CARGO_ROCKET_DETECTOR_KEY,
            new ServerBlockItem(ModBlocks.CARGO_ROCKET_DETECTOR, Items.IRON_BLOCK, new Item.Settings().registryKey(CARGO_ROCKET_DETECTOR_KEY))
    );

    public static final RegistryKey<Item> CARGO_MANAGER_KEY = RegistryKey.of(RegistryKeys.ITEM, id("cargo_manager"));
    public static final ServerBlockItem CARGO_MANAGER = Registry.register(
            Registries.ITEM,
            CARGO_MANAGER_KEY,
            new ServerBlockItem(ModBlocks.CARGO_MANAGER, Items.IRON_BLOCK, new Item.Settings().registryKey(CARGO_MANAGER_KEY))
    );

    public static void load() {}

}
