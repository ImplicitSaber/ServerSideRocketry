package io.github.implicitsaber.mod.server_side_rocketry.block;

import com.mojang.serialization.MapCodec;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry;
import io.github.implicitsaber.mod.server_side_rocketry.block_entity.OxygenCompressorBlockEntity;
import io.github.implicitsaber.mod.server_side_rocketry.poly.PolyBlockModels;
import io.github.implicitsaber.mod.server_side_rocketry.reg.ModBlockEntityTypes;
import io.github.implicitsaber.mod.server_side_rocketry.reg.ModDataComponentTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.EnumMap;
import java.util.UUID;

public class OxygenCompressorBlock extends BlockWithEntity implements PolymerTexturedBlock {

    public static final MapCodec<OxygenCompressorBlock> CODEC = createCodec(OxygenCompressorBlock::new);
    public static final Property<State> STATE = EnumProperty.of("state", State.class);
    public static final Property<Direction> FACING = Properties.HORIZONTAL_FACING;

    private static final BlockState FALLBACK = Blocks.IRON_BLOCK.getDefaultState();
    private static final Identifier FILLER_MODEL = ServerSideRocketry.id("poly_gui/filler");

    public OxygenCompressorBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(STATE, State.OFF).with(FACING, Direction.NORTH));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if(world.isClient()) return ActionResult.SUCCESS;
        if(!(player instanceof ServerPlayerEntity spe)) return ActionResult.SUCCESS;
        if(!(world.getBlockEntity(pos) instanceof OxygenCompressorBlockEntity be)) return ActionResult.SUCCESS;
        final UUID uuid = spe.getUuid();
        SimpleGui gui = new SimpleGui(ScreenHandlerType.HOPPER, spe, false) {
            @Override
            public void onScreenHandlerClosed() {
                super.onScreenHandlerClosed();
                be.removeStateUpdateCallback(uuid);
            }
        };
        gui.setTitle(be.getName());
        GuiElementBuilder filler = new GuiElementBuilder(Items.LIGHT_GRAY_STAINED_GLASS_PANE)
                .model(FILLER_MODEL)
                .setItemName(Text.empty())
                .hideTooltip();
        gui.setSlot(0, filler);
        gui.setSlot(2, filler);
        gui.setSlot(4, filler);
        gui.setSlotRedirect(1, new Slot(be, 0, 0, 0) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getOrDefault(ModDataComponentTypes.MAX_OXYGEN, 0) > 0;
            }
        });
        updateGui(gui, state.get(STATE));
        be.addStateUpdateCallback(uuid, s -> updateGui(gui, s));
        gui.open();
        return ActionResult.SUCCESS;
    }

    private void updateGui(SimpleGui gui, State state) {
        gui.setSlot(3, state.guiElement);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if(world.isClient()) return;
        Text customName = stack.getCustomName();
        if(customName != null && world.getBlockEntity(pos) instanceof OxygenCompressorBlockEntity be) be.setCustomName(customName);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STATE, FACING);
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext ctx) {
        return PolyBlockModels.fallback(ctx, state.get(STATE).modelStates.getOrDefault(state.get(FACING), FALLBACK), FALLBACK);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.OXYGEN_COMPRESSOR.instantiate(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient() ? null : validateTicker(type, ModBlockEntityTypes.OXYGEN_COMPRESSOR, OxygenCompressorBlockEntity::serverTick);
    }

    public enum State implements StringIdentifiable {
        OFF(Util.make(new EnumMap<>(Direction.class), m -> {
            m.put(Direction.NORTH, PolyBlockModels.OXYGEN_COMPRESSOR_OFF_NORTH_STATE);
            m.put(Direction.EAST, PolyBlockModels.OXYGEN_COMPRESSOR_OFF_EAST_STATE);
            m.put(Direction.SOUTH, PolyBlockModels.OXYGEN_COMPRESSOR_OFF_SOUTH_STATE);
            m.put(Direction.WEST, PolyBlockModels.OXYGEN_COMPRESSOR_OFF_WEST_STATE);
        }), new GuiElementBuilder(Items.RED_STAINED_GLASS_PANE)
                .setItemName(Text.translatable(ServerSideRocketry.MOD_ID + ".oxygen_compressor.off").formatted(Formatting.RED))
                .model(ServerSideRocketry.id("poly_gui/oxygen_compressor/off"))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".oxygen_compressor.off.desc").setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.WHITE)))
        ),
        STANDBY(Util.make(new EnumMap<>(Direction.class), m -> {
            m.put(Direction.NORTH, PolyBlockModels.OXYGEN_COMPRESSOR_STANDBY_NORTH_STATE);
            m.put(Direction.EAST, PolyBlockModels.OXYGEN_COMPRESSOR_STANDBY_EAST_STATE);
            m.put(Direction.SOUTH, PolyBlockModels.OXYGEN_COMPRESSOR_STANDBY_SOUTH_STATE);
            m.put(Direction.WEST, PolyBlockModels.OXYGEN_COMPRESSOR_STANDBY_WEST_STATE);
        }), new GuiElementBuilder(Items.YELLOW_STAINED_GLASS_PANE)
                .setItemName(Text.translatable(ServerSideRocketry.MOD_ID + ".oxygen_compressor.standby").formatted(Formatting.YELLOW))
                .model(ServerSideRocketry.id("poly_gui/oxygen_compressor/standby"))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".oxygen_compressor.standby.desc").setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.WHITE)))
        ),
        ON(Util.make(new EnumMap<>(Direction.class), m -> {
            m.put(Direction.NORTH, PolyBlockModels.OXYGEN_COMPRESSOR_ON_NORTH_STATE);
            m.put(Direction.EAST, PolyBlockModels.OXYGEN_COMPRESSOR_ON_EAST_STATE);
            m.put(Direction.SOUTH, PolyBlockModels.OXYGEN_COMPRESSOR_ON_SOUTH_STATE);
            m.put(Direction.WEST, PolyBlockModels.OXYGEN_COMPRESSOR_ON_WEST_STATE);
        }), new GuiElementBuilder(Items.LIME_STAINED_GLASS_PANE)
                .setItemName(Text.translatable(ServerSideRocketry.MOD_ID + ".oxygen_compressor.on").formatted(Formatting.GREEN))
                .model(ServerSideRocketry.id("poly_gui/oxygen_compressor/on"))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".oxygen_compressor.on.desc").setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.WHITE)))
        ),
        FULL(Util.make(new EnumMap<>(Direction.class), m -> {
            m.put(Direction.NORTH, PolyBlockModels.OXYGEN_COMPRESSOR_FULL_NORTH_STATE);
            m.put(Direction.EAST, PolyBlockModels.OXYGEN_COMPRESSOR_FULL_EAST_STATE);
            m.put(Direction.SOUTH, PolyBlockModels.OXYGEN_COMPRESSOR_FULL_SOUTH_STATE);
            m.put(Direction.WEST, PolyBlockModels.OXYGEN_COMPRESSOR_FULL_WEST_STATE);
        }), new GuiElementBuilder(Items.LIGHT_BLUE_STAINED_GLASS_PANE)
                .setItemName(Text.translatable(ServerSideRocketry.MOD_ID + ".oxygen_compressor.full").formatted(Formatting.AQUA))
                .model(ServerSideRocketry.id("poly_gui/oxygen_compressor/full"))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".oxygen_compressor.full.desc").setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.WHITE)))
        );

        private final EnumMap<Direction, BlockState> modelStates;
        private final GuiElementBuilder guiElement;
        private final String stringId;

        State(EnumMap<Direction, BlockState> modelStates, GuiElementBuilder guiElement) {
            this.modelStates = modelStates;
            this.guiElement = guiElement;
            this.stringId = this.name().toLowerCase();
        }

        @Override
        public String asString() {
            return this.stringId;
        }
    }

}
