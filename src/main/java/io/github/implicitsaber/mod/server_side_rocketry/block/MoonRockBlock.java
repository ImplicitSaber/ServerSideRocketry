package io.github.implicitsaber.mod.server_side_rocketry.block;

import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import io.github.implicitsaber.mod.server_side_rocketry.poly.PolyBlockModels;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import xyz.nucleoid.packettweaker.PacketContext;

public class MoonRockBlock extends Block implements PolymerTexturedBlock {

    private static final BlockState FALLBACK = Blocks.SMOOTH_STONE.getDefaultState();

    public MoonRockBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext ctx) {
        return PolyBlockModels.fallback(ctx, PolyBlockModels.MOON_ROCK_STATE, FALLBACK);
    }

}
