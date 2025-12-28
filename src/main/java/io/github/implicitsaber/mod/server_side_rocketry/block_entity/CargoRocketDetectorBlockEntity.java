package io.github.implicitsaber.mod.server_side_rocketry.block_entity;

import io.github.implicitsaber.mod.server_side_rocketry.block.CargoRocketDetectorBlock;
import io.github.implicitsaber.mod.server_side_rocketry.entity.CargoRocketEntity;
import io.github.implicitsaber.mod.server_side_rocketry.reg.ModBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class CargoRocketDetectorBlockEntity extends BlockEntity {

    public CargoRocketDetectorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.CARGO_ROCKET_DETECTOR, pos, state);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, CargoRocketDetectorBlockEntity be) {
        Box box = new Box(pos).expand(3);
        List<CargoRocketEntity> rockets = world.getEntitiesByClass(CargoRocketEntity.class, box, e -> !e.isRemoved() && e.isLanded());
        boolean shouldDetect = !rockets.isEmpty();
        boolean detecting = state.get(CargoRocketDetectorBlock.ROCKET_NEARBY);
        if(detecting != shouldDetect) world.setBlockState(pos, state.with(CargoRocketDetectorBlock.ROCKET_NEARBY, shouldDetect));
    }

}
