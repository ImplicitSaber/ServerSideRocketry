package io.github.implicitsaber.mod.server_side_rocketry.entity;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import eu.pb4.sgui.api.gui.SlotGuiInterface;
import io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry;
import io.github.implicitsaber.mod.server_side_rocketry.block_entity.CargoManagerBlockEntity;
import io.github.implicitsaber.mod.server_side_rocketry.reg.ModChunkTicketTypes;
import io.github.implicitsaber.mod.server_side_rocketry.reg.ModItems;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CargoRocketEntity extends AbstractRocketEntity implements Inventory {

    private static final Identifier MODEL = ServerSideRocketry.id("poly_entity/cargo_rocket");
    private static final ItemStack MODEL_STACK = new ItemStack(Items.STICK);

    private static final Text NOT_ON_CELESTIAL_BODY_TEXT = Text.translatable(ServerSideRocketry.MOD_ID + ".not_on_celestial")
            .formatted(Formatting.RED);
    private static final Text YOU_ARE_HERE_TEXT = Text.translatable(ServerSideRocketry.MOD_ID + ".you_are_here")
            .setStyle(Style.EMPTY.withColor(Formatting.GREEN).withItalic(false).withBold(true));
    private static final Text TARGET_TEXT = Text.translatable(ServerSideRocketry.MOD_ID + ".target")
            .setStyle(Style.EMPTY.withColor(Formatting.YELLOW).withItalic(false).withBold(true));
    private static final Text GUI_TITLE = Text.translatable(ServerSideRocketry.MOD_ID + ".cargo_rocket_config");
    private static final Text VIEW_INVENTORY_TEXT = Text.translatable(ServerSideRocketry.MOD_ID + ".view_inventory");
    private static final Text CARGO_TEXT = Text.translatable(ServerSideRocketry.MOD_ID + ".cargo");

    private static final Identifier SPACE_ITEM_MODEL = ServerSideRocketry.id("poly_gui/planet_select/space");
    private static final Identifier VIEW_INVENTORY_ITEM_MODEL = ServerSideRocketry.id("poly_gui/cargo_rocket/view_inventory");
    private static final Identifier FILLER_ITEM_MODEL = ServerSideRocketry.id("poly_gui/filler");

    static {
        MODEL_STACK.set(DataComponentTypes.ITEM_MODEL, MODEL);
    }

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
    private final Map<UUID, SimpleGui> activeGuis = new Object2ObjectOpenHashMap<>();
    private final Set<BlockPos> linkedManagers = new ObjectOpenHashSet<>();

    public CargoRocketEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.getEntityWorld() instanceof ServerWorld sw)
            sw.getChunkManager().addTicket(ModChunkTicketTypes.CARGO_ROCKET, this.getChunkPos(), 3);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if(this.travelState != TravelState.LANDED) return ActionResult.FAIL;
        if(!(player instanceof ServerPlayerEntity serverPlayer)) return ActionResult.SUCCESS;
        CelestialBody current = CelestialBody.ofWorld(this.getEntityWorld().getRegistryKey());
        if(current == null) {
            serverPlayer.sendMessage(NOT_ON_CELESTIAL_BODY_TEXT, true);
            return ActionResult.FAIL;
        }
        final UUID id = player.getUuid();
        SimpleGui gui = new SimpleGui(ScreenHandlerType.GENERIC_9X6, serverPlayer, false) {
            @Override
            public void onScreenHandlerClosed() {
                super.onScreenHandlerClosed();
                activeGuis.remove(id);
            }
        };
        gui.setTitle(GUI_TITLE);
        GuiElementBuilder space = new GuiElementBuilder(Items.BLACK_STAINED_GLASS_PANE)
                .setItemName(Text.empty())
                .model(SPACE_ITEM_MODEL)
                .hideTooltip();
        for(int i = 0; i < 45; i++) gui.setSlot(i, space);
        GuiElementBuilder filler = new GuiElementBuilder(Items.LIGHT_GRAY_STAINED_GLASS_PANE)
                .setItemName(Text.empty())
                .model(FILLER_ITEM_MODEL)
                .hideTooltip();
        for(int i = 45; i < 53; i++) gui.setSlot(i, filler);
        gui.setSlot(53, new GuiElementBuilder()
                .setItemName(VIEW_INVENTORY_TEXT)
                .model(VIEW_INVENTORY_ITEM_MODEL)
                .setCallback((i, clickType, slotActionType) -> {
                    this.openInvGui(serverPlayer);
                    gui.close();
                }));
        this.updateCelestialBodies(gui, current);
        gui.open();
        this.activeGuis.put(id, gui);
        return ActionResult.SUCCESS;
    }
    
    private void openInvGui(ServerPlayerEntity player) {
        final UUID id = player.getUuid();
        SimpleGui gui = new SimpleGui(ScreenHandlerType.GENERIC_9X3, player, false) {
            @Override
            public void onScreenHandlerClosed() {
                super.onScreenHandlerClosed();
                activeGuis.remove(id);
            }
        };
        gui.setTitle(CARGO_TEXT);
        for(int i = 0; i < 27; i++) gui.setSlotRedirect(i, new Slot(this, i, 0, 0));
        gui.open();
        this.activeGuis.put(id, gui);
    }

    private GuiElementBuilder createCelestialBody(CelestialBody body, CelestialBody current) {
        GuiElementBuilder builder = body.getElementSupplier().get();
        if(body == this.target) builder.addLoreLineRaw(TARGET_TEXT);
        if(body == current) {
            builder.addLoreLineRaw(YOU_ARE_HERE_TEXT);
            return builder;
        }
        builder.setCallback((i, clickType, slotActionType, gui) -> {
            this.target = body;
            this.updateCelestialBodies(gui, CelestialBody.ofWorld(this.getEntityWorld().getRegistryKey()));
        });
        return builder;
    }

    private void updateCelestialBodies(SlotGuiInterface gui, CelestialBody current) {
        gui.setSlot(14, createCelestialBody(CelestialBody.MOON, current));
        gui.setSlot(19, createCelestialBody(CelestialBody.MERCURY, current));
        gui.setSlot(21, createCelestialBody(CelestialBody.VENUS, current));
        gui.setSlot(23, createCelestialBody(CelestialBody.EARTH, current));
        gui.setSlot(25, createCelestialBody(CelestialBody.MARS, current));
    }

    @Override
    public ItemStack toStack() {
        ItemStack stack = new ItemStack(ModItems.CARGO_ROCKET);
        stack.set(DataComponentTypes.CUSTOM_NAME, this.getCustomName());
        return stack;
    }

    @Override
    protected @Nullable Entity getPossiblePassengersHolder() {
        return null;
    }

    @Override
    protected ItemStack toModel() {
        return MODEL_STACK;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    public void launch() {
        CelestialBody current = CelestialBody.ofWorld(this.getEntityWorld().getRegistryKey());
        if(this.travelState != TravelState.LANDED) return;
        if(current == null || this.target == current) return;
        this.travelState = TravelState.LAUNCHED;
        List<SimpleGui> guis = new ArrayList<>(activeGuis.values()); // to avoid ConcurrentModificationException
        for(SimpleGui gui : guis) gui.close();
    }

    @Override
    protected void onCompletingTravel(CelestialBody from) {
        this.target = from;
    }

    @Override
    protected TeleportTarget.PostDimensionTransition getMainPostDimensionTransition() {
        return e -> {
            if(e.getEntityWorld() instanceof ServerWorld sw)
                sw.getChunkManager().addTicket(ModChunkTicketTypes.CARGO_ROCKET, e.getChunkPos(), 3);
        };
    }

    @Override
    protected void onDestroyed() {
        ItemScatterer.spawn(this.getEntityWorld(), this, this);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        Inventories.writeData(view, this.inventory);
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        Inventories.readData(view, this.inventory);
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack stack : this.inventory) if(!stack.isEmpty()) return false;
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        if(slot < 0 || slot >= this.size()) return ItemStack.EMPTY;
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack stack = Inventories.splitStack(this.inventory, slot, amount);
        if(!stack.isEmpty()) this.markDirty();
        return stack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        if(slot < 0 || slot >= this.size()) return ItemStack.EMPTY;
        ItemStack stack = this.inventory.get(slot);
        if(stack.isEmpty()) return ItemStack.EMPTY;
        this.inventory.set(slot, ItemStack.EMPTY);
        this.markDirty();
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if(slot < 0 || slot >= this.size()) return;
        this.inventory.set(slot, stack);
        stack.capCount(this.getMaxCount(stack));
        this.markDirty();
    }

    @Override
    public void markDirty() {
        java.util.Iterator<BlockPos> it = this.linkedManagers.iterator();
        while(it.hasNext()) {
            BlockPos pos = it.next();
            BlockEntity be = this.getEntityWorld().getBlockEntity(pos);
            if(be instanceof CargoManagerBlockEntity c) {
                if(!c.updateFromLinked(this)) it.remove();
            }
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        this.inventory.clear();
        this.markDirty();
    }

    @Override
    protected void setWorld(World world) {
        for(BlockPos pos : this.linkedManagers) {
            BlockEntity be = this.getEntityWorld().getBlockEntity(pos);
            if(be instanceof CargoManagerBlockEntity c) c.unlinkIfLinkedTo(this);
        }
        super.setWorld(world);
    }

    @Override
    public void remove(RemovalReason reason) {
        for(BlockPos pos : this.linkedManagers) {
            BlockEntity be = this.getEntityWorld().getBlockEntity(pos);
            if(be instanceof CargoManagerBlockEntity c) c.unlinkIfLinkedTo(this);
        }
        super.remove(reason);
    }

    public boolean isLanded() {
        return this.travelState == TravelState.LANDED;
    }

    public void linkManager(BlockPos pos) {
        this.linkedManagers.add(pos);
    }

    public void unlinkManager(BlockPos pos) {
        this.linkedManagers.remove(pos);
    }

}
