package io.github.implicitsaber.mod.server_side_rocketry.entity;

import eu.pb4.polymer.core.api.entity.PolymerEntity;
import io.github.implicitsaber.mod.server_side_rocketry.mixin.DisplayEntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.World;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;

public class PassengersHolderEntity extends Entity implements PolymerEntity {

    private int ticksUntilDespawn = 5;

    public PassengersHolderEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        this.ticksUntilDespawn--;
        if(this.ticksUntilDespawn <= 0) this.discard();
    }

    @Override
    public void modifyRawTrackedData(List<DataTracker.SerializedEntry<?>> data, ServerPlayerEntity player, boolean initial) {
        data.add(DataTracker.SerializedEntry.of(DisplayEntityAccessor.getInterpolationDurationData(), 3));
        data.add(DataTracker.SerializedEntry.of(DisplayEntityAccessor.getTeleportDurationData(), 3));
    }

    @Override
    public EntityType<?> getPolymerEntityType(PacketContext context) {
        return EntityType.ITEM_DISPLAY;
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
        this.ticksUntilDespawn = view.getInt("TicksUntilDespawn", 5);
    }

    @Override
    protected void writeCustomData(WriteView view) {
        view.putInt("TicksUntilDespawn", this.ticksUntilDespawn);
    }

    public void preventDespawn() {
        this.ticksUntilDespawn = 5;
    }

}
