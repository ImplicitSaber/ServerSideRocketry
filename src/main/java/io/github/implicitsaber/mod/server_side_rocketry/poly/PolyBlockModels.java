package io.github.implicitsaber.mod.server_side_rocketry.poly;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.block.BlockState;
import xyz.nucleoid.packettweaker.PacketContext;

import static io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry.id;

public class PolyBlockModels {

    public static final PolymerBlockModel MOON_ROCK = PolymerBlockModel.of(id("block/moon_rock"));
    public static final BlockState MOON_ROCK_STATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, MOON_ROCK);

    public static BlockState fallback(PacketContext context, BlockState modelState, BlockState fallbackState) {
        if(!PolymerResourcePackUtils.hasMainPack(context)) return fallbackState;
        return modelState == null ? fallbackState : modelState;
    }

}
