package io.github.implicitsaber.mod.server_side_rocketry.block;

import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import io.github.implicitsaber.mod.server_side_rocketry.poly.PolyBlockModels;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import xyz.nucleoid.packettweaker.PacketContext;

public class ServerTexturedBlock extends Block implements PolymerTexturedBlock {

    private final BlockState fallback;
    private final BlockState model;

    public ServerTexturedBlock(Settings settings, BlockState fallback, BlockState model) {
        super(settings);
        this.fallback = fallback;
        this.model = model;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext ctx) {
        return PolyBlockModels.fallback(ctx, this.model, this.fallback);
    }

}
