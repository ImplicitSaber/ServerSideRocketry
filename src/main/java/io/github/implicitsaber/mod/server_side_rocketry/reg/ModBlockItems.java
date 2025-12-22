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
            new ServerBlockItem(ModBlocks.MOON_ROCK, Items.STONE, new Item.Settings().registryKey(MOON_ROCK_KEY))
    );

    public static void load() {}

}
