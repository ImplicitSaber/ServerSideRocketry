package io.github.implicitsaber.mod.server_side_rocketry.block;

import com.mojang.serialization.MapCodec;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import io.github.implicitsaber.mod.server_side_rocketry.poly.PolyBlockModels;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import org.jspecify.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class OxygenCompressorBlock extends BlockWithEntity implements PolymerTexturedBlock {

    public static final MapCodec<OxygenCompressorBlock> CODEC = createCodec(OxygenCompressorBlock::new);
    public static final Property<State> STATE = EnumProperty.of("state", State.class);

    private static final BlockState FALLBACK = Blocks.IRON_BLOCK.getDefaultState();

    public OxygenCompressorBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(STATE, State.OFF));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STATE);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext ctx) {
        return PolyBlockModels.fallback(ctx, state.get(STATE).modelState, FALLBACK);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    public enum State implements StringIdentifiable {
        OFF(PolyBlockModels.OXYGEN_COMPRESSOR_OFF_STATE),
        STANDBY(PolyBlockModels.OXYGEN_COMPRESSOR_STANDBY_STATE),
        ON(PolyBlockModels.OXYGEN_COMPRESSOR_ON_STATE),
        FULL(PolyBlockModels.OXYGEN_COMPRESSOR_FULL_STATE);

        private final BlockState modelState;
        private final String stringId;

        State(BlockState modelState) {
            this.modelState = modelState;
            this.stringId = this.name().toLowerCase();
        }

        @Override
        public String asString() {
            return this.stringId;
        }
    }

}
