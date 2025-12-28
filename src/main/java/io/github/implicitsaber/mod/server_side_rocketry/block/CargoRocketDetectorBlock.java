package io.github.implicitsaber.mod.server_side_rocketry.block;

import com.mojang.serialization.MapCodec;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import io.github.implicitsaber.mod.server_side_rocketry.block_entity.CargoRocketDetectorBlockEntity;
import io.github.implicitsaber.mod.server_side_rocketry.poly.PolyBlockModels;
import io.github.implicitsaber.mod.server_side_rocketry.reg.ModBlockEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class CargoRocketDetectorBlock extends BlockWithEntity implements PolymerTexturedBlock {

    public static final MapCodec<CargoRocketDetectorBlock> CODEC = createCodec(CargoRocketDetectorBlock::new);
    public static final BooleanProperty ROCKET_NEARBY = BooleanProperty.of("rocket_nearby");

    private static final BlockState FALLBACK = Blocks.IRON_BLOCK.getDefaultState();

    public CargoRocketDetectorBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(ROCKET_NEARBY, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ROCKET_NEARBY);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(ROCKET_NEARBY) ? 15 : 0;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext ctx) {
        return PolyBlockModels.fallback(ctx, PolyBlockModels.CARGO_ROCKET_DETECTOR_STATE, FALLBACK);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.CARGO_ROCKET_DETECTOR.instantiate(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient() ? null : validateTicker(type, ModBlockEntityTypes.CARGO_ROCKET_DETECTOR, CargoRocketDetectorBlockEntity::serverTick);
    }
}
