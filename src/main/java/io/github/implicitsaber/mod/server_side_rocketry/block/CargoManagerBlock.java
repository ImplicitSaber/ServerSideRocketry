package io.github.implicitsaber.mod.server_side_rocketry.block;

import com.mojang.serialization.MapCodec;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import io.github.implicitsaber.mod.server_side_rocketry.block_entity.CargoManagerBlockEntity;
import io.github.implicitsaber.mod.server_side_rocketry.poly.PolyBlockModels;
import io.github.implicitsaber.mod.server_side_rocketry.reg.ModBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class CargoManagerBlock extends BlockWithEntity implements PolymerTexturedBlock {

    public static final MapCodec<CargoManagerBlock> CODEC = createCodec(CargoManagerBlock::new);

    private static final BlockState FALLBACK = Blocks.IRON_BLOCK.getDefaultState();

    public CargoManagerBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext ctx) {
        return PolyBlockModels.fallback(ctx, PolyBlockModels.CARGO_MANAGER_STATE, FALLBACK);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.CARGO_MANAGER.instantiate(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient() ? null : validateTicker(type, ModBlockEntityTypes.CARGO_MANAGER, CargoManagerBlockEntity::serverTick);
    }

    @Override
    protected int getComparatorOutput(BlockState state, World world, BlockPos pos, Direction direction) {
        return world.getBlockEntity(pos) instanceof CargoManagerBlockEntity be ? ScreenHandler.calculateComparatorOutput((Inventory) be) : 0;
    }

    @Override
    protected boolean hasComparatorOutput(BlockState state) {
        return true;
    }
}
