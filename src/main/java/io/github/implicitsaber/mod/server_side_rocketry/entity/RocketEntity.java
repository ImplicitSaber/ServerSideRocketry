package io.github.implicitsaber.mod.server_side_rocketry.entity;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry;
import io.github.implicitsaber.mod.server_side_rocketry.reg.ModEntityTypes;
import io.github.implicitsaber.mod.server_side_rocketry.reg.ModItems;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RocketEntity extends AbstractRocketEntity {

    private static final Identifier MODEL = ServerSideRocketry.id("poly_entity/rocket");
    private static final ItemStack MODEL_STACK = new ItemStack(Items.STICK);

    private static final Text NOT_ON_CELESTIAL_BODY_TEXT = Text.translatable(ServerSideRocketry.MOD_ID + ".not_on_celestial")
            .formatted(Formatting.RED);
    private static final Text YOU_ARE_HERE_TEXT = Text.translatable(ServerSideRocketry.MOD_ID + ".you_are_here")
            .setStyle(Style.EMPTY.withColor(Formatting.GREEN).withItalic(false).withBold(true));
    private static final Text GUI_TITLE = Text.translatable(ServerSideRocketry.MOD_ID + ".planet_select");

    private static final Identifier SPACE_ITEM_MODEL = ServerSideRocketry.id("poly_gui/planet_select/space");

    static {
        MODEL_STACK.set(DataComponentTypes.ITEM_MODEL, MODEL);
    }

    private final Map<UUID, SimpleGui> activeGuis = new Object2ObjectOpenHashMap<>();

    private UUID passengersHolderId = null;

    public RocketEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.getEntityWorld().isClient()) return;
        if(this.passengersHolderId != null) {
            Entity e = this.getEntityWorld().getEntity(this.passengersHolderId);
            if(e instanceof PassengersHolderEntity p) {
                p.preventDespawn();
                p.setPosition(this.getEntityPos().add(0, 1, 0));
            } else createPassengersHolder();
        } else createPassengersHolder();
    }

    @Override
    protected @Nullable Entity getPossiblePassengersHolder() {
        if(this.passengersHolderId == null) return null;
        return this.getEntityWorld().getEntity(this.passengersHolderId);
    }

    private void createPassengersHolder() {
        PassengersHolderEntity e = ModEntityTypes.PASSENGERS_HOLDER.create(this.getEntityWorld(), SpawnReason.LOAD);
        if(e == null) return;
        e.setPosition(this.getEntityPos().add(0, 1, 0));
        this.getEntityWorld().spawnEntity(e);
        this.passengersHolderId = e.getUuid();
    }

    @Override
    public void remove(RemovalReason reason) {
        if(reason.shouldDestroy() && this.passengersHolderId != null) {
            Entity e = this.getEntityWorld().getEntity(this.passengersHolderId);
            if(e instanceof PassengersHolderEntity) e.remove(reason);
            this.passengersHolderId = null;
        }
        super.remove(reason);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if(this.passengersHolderId == null) return ActionResult.FAIL;
        Entity e = this.getEntityWorld().getEntity(this.passengersHolderId);
        if(!(e instanceof PassengersHolderEntity)) return ActionResult.FAIL;
        boolean success = player.startRiding(e);
        if(success) {
            if(this.travelState == TravelState.LANDED && player instanceof ServerPlayerEntity s) createGuiFor(s);
            return ActionResult.SUCCESS;
        } else return ActionResult.FAIL;
    }

    private void createGuiFor(ServerPlayerEntity player) {
        CelestialBody current = CelestialBody.ofWorld(this.getEntityWorld().getRegistryKey());
        if(current == null) {
            player.sendMessage(NOT_ON_CELESTIAL_BODY_TEXT, true);
            return;
        }
        final UUID id = player.getUuid();
        SimpleGui gui = new SimpleGui(ScreenHandlerType.GENERIC_9X5, player, false) {
            @Override
            public void onScreenHandlerClosed() {
                super.onScreenHandlerClosed();
                activeGuis.remove(id);
            }
        };
        gui.setTitle(GUI_TITLE);
        GuiElementBuilder filler = new GuiElementBuilder(Items.BLACK_STAINED_GLASS_PANE)
                .setItemName(Text.empty())
                .setComponent(DataComponentTypes.ITEM_MODEL, SPACE_ITEM_MODEL)
                .hideTooltip();
        for(int i = 0; i < 45; i++) gui.setSlot(i, filler);
        gui.setSlot(14, createCelestialBody(CelestialBody.MOON, current));
        gui.setSlot(19, createCelestialBody(CelestialBody.MERCURY, current));
        gui.setSlot(21, createCelestialBody(CelestialBody.VENUS, current));
        gui.setSlot(23, createCelestialBody(CelestialBody.EARTH, current));
        gui.setSlot(25, createCelestialBody(CelestialBody.MARS, current));
        gui.open();
        this.activeGuis.put(id, gui);
    }

    private GuiElementBuilder createCelestialBody(CelestialBody body, CelestialBody current) {
        GuiElementBuilder builder = body.getElementSupplier().get();
        if(body == current) {
            builder.addLoreLineRaw(YOU_ARE_HERE_TEXT);
            return builder;
        }
        builder.setCallback((i, clickType, slotActionType) -> {
            this.target = body;
            this.travelState = TravelState.LAUNCHED;
            List<SimpleGui> guis = new ArrayList<>(activeGuis.values()); // to avoid ConcurrentModificationException
            for(SimpleGui gui : guis) gui.close();
        });
        return builder;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    @Override
    public ItemStack toStack() {
        ItemStack stack = new ItemStack(ModItems.ROCKET);
        stack.set(DataComponentTypes.CUSTOM_NAME, this.getCustomName());
        return stack;
    }

    @Override
    protected ItemStack toModel() {
        return MODEL_STACK;
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        this.passengersHolderId = view.read("PassengersHolder", Uuids.INT_STREAM_CODEC).orElse(null);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.putNullable("PassengersHolder", Uuids.INT_STREAM_CODEC, this.passengersHolderId);
    }

}
