package io.github.implicitsaber.mod.server_side_rocketry.entity;

import com.mojang.serialization.Codec;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import eu.pb4.polymer.virtualentity.api.elements.InteractionElement;
import eu.pb4.polymer.virtualentity.api.elements.VirtualElement;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry;
import io.github.implicitsaber.mod.server_side_rocketry.keys.ModWorldKeys;
import io.github.implicitsaber.mod.server_side_rocketry.mixin.DisplayEntityAccessor;
import io.github.implicitsaber.mod.server_side_rocketry.mixin.ItemDisplayEntityAccessor;
import io.github.implicitsaber.mod.server_side_rocketry.reg.ModSoundEvents;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.rule.GameRules;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractRocketEntity extends Entity implements PolymerEntity {

    protected TravelState travelState = TravelState.LANDED;
    protected CelestialBody target = CelestialBody.EARTH;

    private ElementHolder holder = null;

    public AbstractRocketEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        if(this.isAlwaysInvulnerableTo(source)) return false;
        if(this.isRemoved()) return false;
        this.remove(RemovalReason.KILLED);
        boolean creative = source.getAttacker() instanceof PlayerEntity p && p.isCreative();
        if(!creative && world.getGameRules().getValue(GameRules.ENTITY_DROPS)) this.dropStack(world, this.toStack());
        this.onDestroyed();
        return true;
    }

    public abstract ItemStack toStack();
    protected void onDestroyed() {}

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
            new EntityAttachment(holder, this, false);
        }
        holder.tick();
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
                    ServerWorld targetWorld = server.getWorld(this.target.getWorld());
                    this.travelState = TravelState.LANDING;
                    if(targetWorld != null) {
                        CelestialBody from = CelestialBody.ofWorld(this.getEntityWorld().getRegistryKey());
                        this.onCompletingTravel(from);
                        Entity e = this.getPossiblePassengersHolder();
                        if (e instanceof PassengersHolderEntity) e.teleportTo(new TeleportTarget(
                                targetWorld,
                                this.getEntityPos().add(0, 1, 0),
                                Vec3d.ZERO, 0, 0, TeleportTarget.NO_OP
                        ));
                        this.teleportTo(new TeleportTarget(
                                targetWorld,
                                this.getEntityPos(),
                                Vec3d.ZERO, 0, 0, this.getMainPostDimensionTransition()
                        ));
                    }
                }
            }
        }
        this.applyGravity();
        this.move(MovementType.SELF, this.getVelocity());
    }

    @Nullable
    protected abstract Entity getPossiblePassengersHolder();

    protected void onCompletingTravel(CelestialBody from) {}

    protected TeleportTarget.PostDimensionTransition getMainPostDimensionTransition() {
        return TeleportTarget.NO_OP;
    }

    @Override
    public void remove(RemovalReason reason) {
        if(holder != null) holder.destroy();
        super.remove(reason);
    }

    @Override
    public void modifyRawTrackedData(List<DataTracker.SerializedEntry<?>> data, ServerPlayerEntity player, boolean initial) {
        data.add(DataTracker.SerializedEntry.of(ItemDisplayEntityAccessor.getItemData(), this.toModel()));
        data.add(DataTracker.SerializedEntry.of(ItemDisplayEntityAccessor.getItemDisplayData(), ItemDisplayContext.GROUND.getIndex()));
        data.add(DataTracker.SerializedEntry.of(DisplayEntityAccessor.getInterpolationDurationData(), 3));
        data.add(DataTracker.SerializedEntry.of(DisplayEntityAccessor.getTeleportDurationData(), 3));
    }

    protected abstract ItemStack toModel();

    @Override
    protected void writeCustomData(WriteView view) {
        view.put("TravelState", TravelState.CODEC, this.travelState);
        view.put("Target", CelestialBody.CODEC, this.target);
    }

    @Override
    protected void readCustomData(ReadView view) {
        this.travelState = view.read("TravelState", TravelState.CODEC).orElse(TravelState.LANDED);
        this.target = view.read("Target", CelestialBody.CODEC).orElse(CelestialBody.EARTH);
    }

    @Override
    public EntityType<?> getPolymerEntityType(PacketContext context) {
        return EntityType.ITEM_DISPLAY;
    }

    private static final Map<RegistryKey<World>, CelestialBody> CELESTIAL_BODY_LOOKUP = new Object2ObjectOpenHashMap<>();
    public enum CelestialBody implements StringIdentifiable {
        EARTH(World.OVERWORLD, () -> new GuiElementBuilder(Items.DIRT)
                .setItemName(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.earth"))
                .model(ServerSideRocketry.id("poly_gui/planet_select/earth"))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.earth.desc")
                        .setStyle(Style.EMPTY.withItalic(true).withColor(Formatting.WHITE)))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.earth.stats.1")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.GREEN)))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.earth.stats.2")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.AQUA)))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.earth.stats.3")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.YELLOW)))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.earth.stats.4")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.BLUE)))
        ),
        MOON(ModWorldKeys.MOON, () -> new GuiElementBuilder(Items.STONE)
                .setItemName(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.moon"))
                .model(ServerSideRocketry.id("poly_gui/planet_select/moon"))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.moon.desc")
                        .setStyle(Style.EMPTY.withItalic(true).withColor(Formatting.WHITE)))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.moon.stats.1")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.GREEN)))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.moon.stats.2")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.AQUA)))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.moon.stats.3")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.YELLOW)))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.moon.stats.4")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.BLUE)))
        ),
        VENUS(ModWorldKeys.VENUS, () -> new GuiElementBuilder(Items.PHANTOM_MEMBRANE)
                .setItemName(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.venus"))
                .model(ServerSideRocketry.id("poly_gui/planet_select/venus"))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.venus.desc")
                        .setStyle(Style.EMPTY.withItalic(true).withColor(Formatting.WHITE)))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.venus.stats.1")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.GREEN)))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.venus.stats.2")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.AQUA)))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.venus.stats.3")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.YELLOW)))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.venus.stats.4")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.BLUE)))
        ),
        MERCURY(ModWorldKeys.MERCURY, () -> new GuiElementBuilder(Items.BLACKSTONE)
                .setItemName(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.mercury"))
                .model(ServerSideRocketry.id("poly_gui/planet_select/mercury"))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.mercury.desc")
                        .setStyle(Style.EMPTY.withItalic(true).withColor(Formatting.WHITE)))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.mercury.stats.1")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.GREEN)))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.mercury.stats.2")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.AQUA)))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.mercury.stats.3")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.YELLOW)))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.mercury.stats.4")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.BLUE)))
        ),
        MARS(ModWorldKeys.MARS, () -> new GuiElementBuilder(Items.NETHERRACK)
                .setItemName(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.mars"))
                .model(ServerSideRocketry.id("poly_gui/planet_select/mars"))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.mars.desc")
                        .setStyle(Style.EMPTY.withItalic(true).withColor(Formatting.WHITE)))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.mars.stats.1")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.GREEN)))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.mars.stats.2")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.AQUA)))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.mars.stats.3")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.YELLOW)))
                .addLoreLineRaw(Text.translatable(ServerSideRocketry.MOD_ID + ".celestial.mars.stats.4")
                        .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.BLUE)))
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

        public RegistryKey<World> getWorld() {
            return world;
        }

        public Supplier<GuiElementBuilder> getElementSupplier() {
            return elementSupplier;
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
