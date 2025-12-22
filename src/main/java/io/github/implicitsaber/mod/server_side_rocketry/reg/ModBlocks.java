package io.github.implicitsaber.mod.server_side_rocketry.reg;

import io.github.implicitsaber.mod.server_side_rocketry.block.MoonRockBlock;
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
    public static final MoonRockBlock MOON_ROCK = Registry.register(
            Registries.BLOCK,
            MOON_ROCK_KEY,
            new MoonRockBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE).registryKey(MOON_ROCK_KEY))
    );

    public static void load() {}

}
