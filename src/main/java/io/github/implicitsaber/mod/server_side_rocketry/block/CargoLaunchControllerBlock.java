package io.github.implicitsaber.mod.server_side_rocketry.block;

import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import io.github.implicitsaber.mod.server_side_rocketry.entity.CargoRocketEntity;
import io.github.implicitsaber.mod.server_side_rocketry.poly.PolyBlockModels;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jspecify.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;

public class CargoLaunchControllerBlock extends Block implements PolymerTexturedBlock {

    public static final BooleanProperty POWERED = Properties.POWERED;

    private static final BlockState FALLBACK = Blocks.IRON_BLOCK.getDefaultState();

    public CargoLaunchControllerBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(POWERED, false));
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        if(world.isClient()) return;
        boolean shouldPower = world.isReceivingRedstonePower(pos);
        boolean isPowered = state.get(POWERED);
        if(shouldPower != isPowered) {
            if(shouldPower) world.scheduleBlockTick(pos, this, 1);
            world.setBlockState(pos, state.with(POWERED, shouldPower));
        }
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        Box box = new Box(pos).expand(3);
        List<CargoRocketEntity> rockets = world.getEntitiesByClass(CargoRocketEntity.class, box, EntityPredicates.VALID_ENTITY);
        for(CargoRocketEntity rocket : rockets) rocket.launch();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext ctx) {
        return PolyBlockModels.fallback(ctx, PolyBlockModels.CARGO_LAUNCH_CONTROLLER_STATE, FALLBACK);
    }

}
