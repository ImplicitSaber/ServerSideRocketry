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

    public static final PolymerBlockModel OXYGEN_COMPRESSOR_OFF_NORTH = PolymerBlockModel.of(id("block/oxygen_compressor/off"));
    public static final BlockState OXYGEN_COMPRESSOR_OFF_NORTH_STATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, OXYGEN_COMPRESSOR_OFF_NORTH);

    public static final PolymerBlockModel OXYGEN_COMPRESSOR_OFF_EAST = PolymerBlockModel.of(id("block/oxygen_compressor/off"), 0, 90);
    public static final BlockState OXYGEN_COMPRESSOR_OFF_EAST_STATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, OXYGEN_COMPRESSOR_OFF_EAST);

    public static final PolymerBlockModel OXYGEN_COMPRESSOR_OFF_SOUTH = PolymerBlockModel.of(id("block/oxygen_compressor/off"), 0, 180);
    public static final BlockState OXYGEN_COMPRESSOR_OFF_SOUTH_STATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, OXYGEN_COMPRESSOR_OFF_SOUTH);

    public static final PolymerBlockModel OXYGEN_COMPRESSOR_OFF_WEST = PolymerBlockModel.of(id("block/oxygen_compressor/off"), 0, 270);
    public static final BlockState OXYGEN_COMPRESSOR_OFF_WEST_STATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, OXYGEN_COMPRESSOR_OFF_WEST);

    public static final PolymerBlockModel OXYGEN_COMPRESSOR_STANDBY_NORTH = PolymerBlockModel.of(id("block/oxygen_compressor/standby"));
    public static final BlockState OXYGEN_COMPRESSOR_STANDBY_NORTH_STATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, OXYGEN_COMPRESSOR_STANDBY_NORTH);

    public static final PolymerBlockModel OXYGEN_COMPRESSOR_STANDBY_EAST = PolymerBlockModel.of(id("block/oxygen_compressor/standby"), 0, 90);
    public static final BlockState OXYGEN_COMPRESSOR_STANDBY_EAST_STATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, OXYGEN_COMPRESSOR_STANDBY_EAST);

    public static final PolymerBlockModel OXYGEN_COMPRESSOR_STANDBY_SOUTH = PolymerBlockModel.of(id("block/oxygen_compressor/standby"), 0, 180);
    public static final BlockState OXYGEN_COMPRESSOR_STANDBY_SOUTH_STATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, OXYGEN_COMPRESSOR_STANDBY_SOUTH);

    public static final PolymerBlockModel OXYGEN_COMPRESSOR_STANDBY_WEST = PolymerBlockModel.of(id("block/oxygen_compressor/standby"), 0, 270);
    public static final BlockState OXYGEN_COMPRESSOR_STANDBY_WEST_STATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, OXYGEN_COMPRESSOR_STANDBY_WEST);

    public static final PolymerBlockModel OXYGEN_COMPRESSOR_ON_NORTH = PolymerBlockModel.of(id("block/oxygen_compressor/on"));
    public static final BlockState OXYGEN_COMPRESSOR_ON_NORTH_STATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, OXYGEN_COMPRESSOR_ON_NORTH);

    public static final PolymerBlockModel OXYGEN_COMPRESSOR_ON_EAST = PolymerBlockModel.of(id("block/oxygen_compressor/on"), 0, 90);
    public static final BlockState OXYGEN_COMPRESSOR_ON_EAST_STATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, OXYGEN_COMPRESSOR_ON_EAST);

    public static final PolymerBlockModel OXYGEN_COMPRESSOR_ON_SOUTH = PolymerBlockModel.of(id("block/oxygen_compressor/on"), 0, 180);
    public static final BlockState OXYGEN_COMPRESSOR_ON_SOUTH_STATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, OXYGEN_COMPRESSOR_ON_SOUTH);

    public static final PolymerBlockModel OXYGEN_COMPRESSOR_ON_WEST = PolymerBlockModel.of(id("block/oxygen_compressor/on"), 0, 270);
    public static final BlockState OXYGEN_COMPRESSOR_ON_WEST_STATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, OXYGEN_COMPRESSOR_ON_WEST);

    public static final PolymerBlockModel OXYGEN_COMPRESSOR_FULL_NORTH = PolymerBlockModel.of(id("block/oxygen_compressor/full"));
    public static final BlockState OXYGEN_COMPRESSOR_FULL_NORTH_STATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, OXYGEN_COMPRESSOR_FULL_NORTH);

    public static final PolymerBlockModel OXYGEN_COMPRESSOR_FULL_EAST = PolymerBlockModel.of(id("block/oxygen_compressor/full"), 0, 90);
    public static final BlockState OXYGEN_COMPRESSOR_FULL_EAST_STATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, OXYGEN_COMPRESSOR_FULL_EAST);

    public static final PolymerBlockModel OXYGEN_COMPRESSOR_FULL_SOUTH = PolymerBlockModel.of(id("block/oxygen_compressor/full"), 0, 180);
    public static final BlockState OXYGEN_COMPRESSOR_FULL_SOUTH_STATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, OXYGEN_COMPRESSOR_FULL_SOUTH);

    public static final PolymerBlockModel OXYGEN_COMPRESSOR_FULL_WEST = PolymerBlockModel.of(id("block/oxygen_compressor/full"), 0, 270);
    public static final BlockState OXYGEN_COMPRESSOR_FULL_WEST_STATE = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, OXYGEN_COMPRESSOR_FULL_WEST);

    public static BlockState fallback(PacketContext context, BlockState modelState, BlockState fallbackState) {
        if(!PolymerResourcePackUtils.hasMainPack(context)) return fallbackState;
        return modelState == null ? fallbackState : modelState;
    }

}
