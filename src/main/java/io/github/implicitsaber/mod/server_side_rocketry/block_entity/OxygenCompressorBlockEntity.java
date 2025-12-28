package io.github.implicitsaber.mod.server_side_rocketry.block_entity;

import io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry;
import io.github.implicitsaber.mod.server_side_rocketry.block.OxygenCompressorBlock;
import io.github.implicitsaber.mod.server_side_rocketry.reg.ModBlockEntityTypes;
import io.github.implicitsaber.mod.server_side_rocketry.reg.ModDataComponentTypes;
import io.github.implicitsaber.mod.server_side_rocketry.util.SpaceEffectsManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class OxygenCompressorBlockEntity extends BlockEntity implements SidedInventory, Nameable {

    private static final Text DEFAULT_NAME = Text.translatable("container." + ServerSideRocketry.MOD_ID + ".oxygen_compressor");
    private static final int[] AVAILABLE_SLOTS = {0};

    private final Map<UUID, Consumer<OxygenCompressorBlock.State>> stateUpdateCallbacks = new Object2ObjectOpenHashMap<>();
    private ItemStack stack = ItemStack.EMPTY;
    private Text customName = null;

    public OxygenCompressorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.OXYGEN_COMPRESSOR, pos, state);
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return AVAILABLE_SLOTS;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        if(slot != 0) return false;
        return stack.getOrDefault(ModDataComponentTypes.MAX_OXYGEN, 0) > 0;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        if(slot != 0) return false;
        return stack.getOrDefault(ModDataComponentTypes.OXYGEN, 0) >= stack.getOrDefault(ModDataComponentTypes.MAX_OXYGEN, 0);
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        if(slot != 0) return ItemStack.EMPTY;
        return this.stack;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        if(slot != 0) return ItemStack.EMPTY;
        if(this.stack.isEmpty()) return ItemStack.EMPTY;
        if(amount <= 0) return ItemStack.EMPTY;
        ItemStack split = this.stack.split(amount);
        this.markDirty();
        return split;
    }

    @Override
    public ItemStack removeStack(int slot) {
        if(slot != 0) return ItemStack.EMPTY;
        if(this.stack.isEmpty()) return ItemStack.EMPTY;
        ItemStack old = this.stack;
        this.stack = ItemStack.EMPTY;
        this.markDirty();
        return old;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if(slot != 0) return;
        this.stack = stack;
        stack.capCount(this.getMaxCount(stack));
        this.markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return Inventory.canPlayerUse(this, player);
    }

    @Override
    public void clear() {
        this.stack = ItemStack.EMPTY;
        this.markDirty();
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        if(!this.stack.isEmpty()) view.put("Item", ItemStack.CODEC, this.stack);
        view.putNullable("CustomName", TextCodecs.CODEC, this.customName);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        this.stack = view.read("Item", ItemStack.CODEC).orElse(ItemStack.EMPTY);
        this.customName = view.read("CustomName", TextCodecs.CODEC).orElse(null);
    }

    public void addStateUpdateCallback(UUID id, Consumer<OxygenCompressorBlock.State> callback) {
        stateUpdateCallbacks.put(id, callback);
    }

    public void removeStateUpdateCallback(UUID id) {
        stateUpdateCallbacks.remove(id);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, OxygenCompressorBlockEntity be) {
        boolean hasOxygen = SpaceEffectsManager.getSpaceEffectsFor(world.getRegistryKey()).hasOxygen();
        OxygenCompressorBlock.State machineState;
        if(hasOxygen) {
            if(!be.stack.isEmpty()) {
                int maxOxygen = be.stack.getOrDefault(ModDataComponentTypes.MAX_OXYGEN, 0);
                if(maxOxygen > 0) {
                    int oxygen = be.stack.getOrDefault(ModDataComponentTypes.OXYGEN, 0);
                    if(oxygen < maxOxygen) {
                        be.stack.set(ModDataComponentTypes.OXYGEN, Math.min(oxygen + 2, maxOxygen));
                        be.markDirty();
                        machineState = OxygenCompressorBlock.State.ON;
                    } else machineState = OxygenCompressorBlock.State.FULL;
                } else machineState = OxygenCompressorBlock.State.STANDBY;
            } else machineState = OxygenCompressorBlock.State.STANDBY;
        } else machineState = OxygenCompressorBlock.State.OFF;
        if(state.get(OxygenCompressorBlock.STATE) != machineState) {
            world.setBlockState(pos, state.with(OxygenCompressorBlock.STATE, machineState));
            for(Consumer<OxygenCompressorBlock.State> r : be.stateUpdateCallbacks.values()) r.accept(machineState);
        }
    }

    @Override
    public Text getName() {
        return Objects.requireNonNullElse(this.getCustomName(), DEFAULT_NAME);
    }

    @Override
    public @Nullable Text getCustomName() {
        return this.customName;
    }

    public void setCustomName(Text customName) {
        this.customName = customName;
    }

}
