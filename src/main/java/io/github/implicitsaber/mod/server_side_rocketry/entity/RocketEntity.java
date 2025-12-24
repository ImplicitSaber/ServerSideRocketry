package io.github.implicitsaber.mod.server_side_rocketry.entity;

import com.mojang.serialization.Codec;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import eu.pb4.polymer.virtualentity.api.elements.InteractionElement;
import eu.pb4.polymer.virtualentity.api.elements.VirtualElement;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry;
import io.github.implicitsaber.mod.server_side_rocketry.keys.ModWorldKeys;
import io.github.implicitsaber.mod.server_side_rocketry.mixin.DisplayEntityAccessor;
import io.github.implicitsaber.mod.server_side_rocketry.mixin.ItemDisplayEntityAccessor;
import io.github.implicitsaber.mod.server_side_rocketry.reg.ModEntityTypes;
import io.github.implicitsaber.mod.server_side_rocketry.reg.ModSoundEvents;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.*;
import java.util.function.Supplier;

public class RocketEntity extends Entity implements PolymerEntity {

    private static final Identifier MODEL = ServerSideRocketry.id("poly_entity/rocket");
    private static final ItemStack STACK = new ItemStack(Items.STICK);

    private static final Text NOT_ON_CELESTIAL_BODY_TEXT = Text.translatable(ServerSideRocketry.MOD_ID + ".not_on_celestial")
            .formatted(Formatting.RED);
    private static final Text YOU_ARE_HERE_TEXT = Text.translatable(ServerSideRocketry.MOD_ID + ".you_are_here")
            .setStyle(Style.EMPTY.withColor(Formatting.GREEN).withItalic(false).withBold(true));
    private static final Text GUI_TITLE = Text.translatable(ServerSideRocketry.MOD_ID + ".planet_select");

    private static final Identifier SPACE_ITEM_MODEL = ServerSideRocketry.id("poly_gui/planet_select/space");

    private static final DustParticleEffect THRUSTER_PARTICLE = new DustParticleEffect(0xAAAAAA, 1);

    static {
        STACK.set(DataComponentTypes.ITEM_MODEL, MODEL);
    }

    private final Map<UUID, SimpleGui> activeGuis = new Object2ObjectOpenHashMap<>();

    private ElementHolder holder = null;
    private UUID passengersHolderId = null;
    private TravelState travelState = TravelState.LANDED;
    private CelestialBody target = CelestialBody.EARTH;

    public RocketEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.getEntityWorld().isClient()) return;
        if(holder == null) {
            holder = new ElementHolder();
            InteractionElement element = new InteractionElement(VirtualElement.InteractionHandler.redirect(this));
            element.setWidth(this.getWidth());
            element.setHeight(this.getHeight());
            element.setInitialPosition(this.getBoundingBox().getCenter());
            holder.addElement(element);
            new EntityAttachment(holder, this, true);
        }
        if(this.passengersHolderId != null) {
            Entity e = this.getEntityWorld().getEntity(this.passengersHolderId);
            if(e instanceof PassengersHolderEntity p) {
                p.preventDespawn();
                p.setPosition(this.getEntityPos().add(0, 1, 0));
            } else createPassengersHolder();
        } else createPassengersHolder();
        this.applyGravity();
        this.move(MovementType.SELF, this.getVelocity());
        if(this.travelState == TravelState.LANDING) {
            this.setVelocity(new Vec3d(0, -0.3, 0));
            if(this.age % 4 == 0) this.playSound(ModSoundEvents.ENTITY_ROCKET_ENGINE, 0.25f, 1.0f);
            if(this.getEntityWorld() instanceof ServerWorld sw) for(int i = 0; i < 5; i++ ) sw.spawnParticles(
                    ParticleTypes.FLAME,
                    this.getX(), this.getY(), this.getZ(),
                    0, this.getRandom().nextTriangular(0, 0.5), -1, this.getRandom().nextTriangular(0, 0.5),
                    2
            );
            if(this.isOnGround()) this.travelState = TravelState.LANDED;
        } else if(this.travelState == TravelState.LAUNCHED) {
            this.setVelocity(new Vec3d(0, 0.8, 0));
            if(this.age % 4 == 0) this.playSound(ModSoundEvents.ENTITY_ROCKET_ENGINE, 1.0f, 1.0f);
            if(this.getEntityWorld() instanceof ServerWorld sw) for(int i = 0; i < 5; i++ ) sw.spawnParticles(
                    ParticleTypes.FLAME,
                    this.getX(), this.getY(), this.getZ(),
                    0, this.getRandom().nextTriangular(0, 0.5), -1, this.getRandom().nextTriangular(0, 0.5),
                    8
            );
            if(this.getY() > 400) {
                MinecraftServer server = this.getEntityWorld().getServer();
                if(server != null) {
                    ServerWorld targetWorld = server.getWorld(this.target.world);
                    this.travelState = TravelState.LANDING;
                    if(targetWorld != null) {
                        Entity e = this.getEntityWorld().getEntity(this.passengersHolderId);
                        if (e instanceof PassengersHolderEntity) e.teleportTo(new TeleportTarget(
                                targetWorld,
                                this.getEntityPos().add(0, 1, 0),
                                Vec3d.ZERO, 0, 0, TeleportTarget.NO_OP
                        ));
                        this.teleportTo(new TeleportTarget(
                                targetWorld,
                                this.getEntityPos(),
                                Vec3d.ZERO, 0, 0, TeleportTarget.NO_OP
                        ));
                    }
                }
            }
        }
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
        if(holder != null) holder.destroy();
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
        CelestialBody current = CelestialBody.ofWorld(player.getEntityWorld().getRegistryKey());
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
        activeGuis.put(id, gui);
    }

    private GuiElementBuilder createCelestialBody(CelestialBody body, CelestialBody current) {
        GuiElementBuilder builder = body.elementSupplier.get();
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
    public EntityType<?> getPolymerEntityType(PacketContext context) {
        return EntityType.ITEM_DISPLAY;
    }

    @Override
    public void modifyRawTrackedData(List<DataTracker.SerializedEntry<?>> data, ServerPlayerEntity player, boolean initial) {
        data.add(DataTracker.SerializedEntry.of(ItemDisplayEntityAccessor.getItemData(), STACK));
        data.add(DataTracker.SerializedEntry.of(ItemDisplayEntityAccessor.getItemDisplayData(), ItemDisplayContext.GROUND.getIndex()));
        data.add(DataTracker.SerializedEntry.of(DisplayEntityAccessor.getInterpolationDurationData(), 3));
        data.add(DataTracker.SerializedEntry.of(DisplayEntityAccessor.getTeleportDurationData(), 3));
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void readCustomData(ReadView view) {
        this.passengersHolderId = view.read("PassengersHolder", Uuids.INT_STREAM_CODEC).orElse(null);
        this.travelState = view.read("TravelState", TravelState.CODEC).orElse(TravelState.LANDED);
        this.target = view.read("Target", CelestialBody.CODEC).orElse(CelestialBody.EARTH);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        view.putNullable("PassengersHolder", Uuids.INT_STREAM_CODEC, this.passengersHolderId);
        view.put("TravelState", TravelState.CODEC, this.travelState);
        view.put("Target", CelestialBody.CODEC, this.target);
    }

    private static final Map<RegistryKey<World>, CelestialBody> CELESTIAL_BODY_LOOKUP = new Object2ObjectOpenHashMap<>();
    public enum CelestialBody implements StringIdentifiable {
        // implemented
        EARTH(World.OVERWORLD, () -> new GuiElementBuilder(Items.DIRT)
                .setItemName(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.earth"))
                .setComponent(DataComponentTypes.ITEM_MODEL, ServerSideRocketry.id("poly_gui/planet_select/earth"))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.earth.desc")
                        .setStyle(Style.EMPTY.withItalic(true).withColor(Formatting.WHITE)))
        ),
        MOON(ModWorldKeys.MOON, () -> new GuiElementBuilder(Items.STONE)
                .setItemName(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.moon"))
                .setComponent(DataComponentTypes.ITEM_MODEL, ServerSideRocketry.id("poly_gui/planet_select/moon"))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.moon.desc")
                        .setStyle(Style.EMPTY.withItalic(true).withColor(Formatting.WHITE)))
        ),
        // not implemented
        VENUS(World.END, () -> new GuiElementBuilder(Items.PHANTOM_MEMBRANE)
                .setItemName(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.venus"))
                .setComponent(DataComponentTypes.ITEM_MODEL, ServerSideRocketry.id("poly_gui/planet_select/venus"))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.venus.desc")
                        .setStyle(Style.EMPTY.withItalic(true).withColor(Formatting.WHITE)))
        ),
        MERCURY(World.END, () -> new GuiElementBuilder(Items.BLACKSTONE)
                .setItemName(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.mercury"))
                .setComponent(DataComponentTypes.ITEM_MODEL, ServerSideRocketry.id("poly_gui/planet_select/mercury"))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.mercury.desc")
                        .setStyle(Style.EMPTY.withItalic(true).withColor(Formatting.WHITE)))
        ),
        MARS(World.END, () -> new GuiElementBuilder(Items.NETHERRACK)
                .setItemName(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.mars"))
                .setComponent(DataComponentTypes.ITEM_MODEL, ServerSideRocketry.id("poly_gui/planet_select/mars"))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.mars.desc")
                        .setStyle(Style.EMPTY.withItalic(true).withColor(Formatting.WHITE)))
        );

        public static final Codec<CelestialBody> CODEC = StringIdentifiable.createCodec(CelestialBody::values);

        private final RegistryKey<World> world;
        private final Supplier<GuiElementBuilder> elementSupplier;
        private final String stringId;

        CelestialBody(RegistryKey<World> world, Supplier<GuiElementBuilder> elementSupplier) {
            this.world = world;
            this.elementSupplier = elementSupplier;
            this.stringId = this.name().toLowerCase();
            CELESTIAL_BODY_LOOKUP.put(world, this);
        }

        public static CelestialBody ofWorld(RegistryKey<World> world) {
            return CELESTIAL_BODY_LOOKUP.get(world);
        }

        @Override
        public String asString() {
            return this.stringId;
        }
    }

    public enum TravelState implements StringIdentifiable {
        LANDED,
        LAUNCHED,
        LANDING;

        public static final Codec<TravelState> CODEC = StringIdentifiable.createCodec(TravelState::values);

        private final String stringId;

        TravelState() {
            this.stringId = this.name().toLowerCase();
        }

        @Override
        public String asString() {
            return this.stringId;
        }
    }

}
