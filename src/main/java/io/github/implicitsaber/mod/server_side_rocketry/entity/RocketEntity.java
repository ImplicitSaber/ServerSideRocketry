package io.github.implicitsaber.mod.server_side_rocketry.entity;

import eu.pb4.polymer.core.api.entity.PolymerEntity;
import eu.pb4.polymer.virtualentity.api.ElementHolder;
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment;
import eu.pb4.polymer.virtualentity.api.elements.InteractionElement;
import eu.pb4.polymer.virtualentity.api.elements.VirtualElement;
import io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry;
import io.github.implicitsaber.mod.server_side_rocketry.mixin.ItemDisplayEntityAccessor;
import io.github.implicitsaber.mod.server_side_rocketry.reg.ModEntityTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.world.World;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;
import java.util.UUID;

public class RocketEntity extends Entity implements PolymerEntity {

    private static final Identifier MODEL = ServerSideRocketry.id("poly_entity/rocket");
    private static final ItemStack STACK = new ItemStack(Items.STICK);

    static {
        STACK.set(DataComponentTypes.ITEM_MODEL, MODEL);
    }

    private ElementHolder holder = null;
    private UUID passengersHolderId = null;

    public RocketEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
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
        return player.startRiding(e) ? ActionResult.SUCCESS : ActionResult.FAIL;
    }

    @Override
    public EntityType<?> getPolymerEntityType(PacketContext context) {
        return EntityType.ITEM_DISPLAY;
    }

    @Override
    public void modifyRawTrackedData(List<DataTracker.SerializedEntry<?>> data, ServerPlayerEntity player, boolean initial) {
        data.add(DataTracker.SerializedEntry.of(ItemDisplayEntityAccessor.getItemData(), STACK));
        data.add(DataTracker.SerializedEntry.of(ItemDisplayEntityAccessor.getItemDisplayData(), ItemDisplayContext.GROUND.getIndex()));
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
    }

    @Override
    protected void writeCustomData(WriteView view) {
        view.putNullable("PassengersHolder", Uuids.INT_STREAM_CODEC, this.passengersHolderId);
    }

}
