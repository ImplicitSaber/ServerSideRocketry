package io.github.implicitsaber.mod.server_side_rocketry.block_entity;

import io.github.implicitsaber.mod.server_side_rocketry.entity.CargoRocketEntity;
import io.github.implicitsaber.mod.server_side_rocketry.reg.ModBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class CargoManagerBlockEntity extends BlockEntity implements Inventory {

    private CargoRocketEntity linkedRocket;

    public CargoManagerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.CARGO_MANAGER, pos, state);
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, CargoManagerBlockEntity be) {
        if(be.linkedRocket == null) {
            Box box = new Box(pos).expand(3);
            List<CargoRocketEntity> rockets = world.getEntitiesByClass(CargoRocketEntity.class, box, e -> !e.isRemoved() && e.isLanded());
            CargoRocketEntity closest = null;
            double closestDist = Double.MAX_VALUE;
            for(CargoRocketEntity e : rockets) {
                double dist = pos.getSquaredDistance(e.getEntityPos());
                if(dist < closestDist) {
                    closest = e;
                    closestDist = dist;
                }
            }
            if(closest != null) {
                be.linkedRocket = closest;
                closest.linkManager(pos);
                world.updateComparators(pos, state.getBlock());
            }
        } else if(be.linkedRocket.isRemoved() ||
                !be.linkedRocket.isLanded() ||
                !pos.isWithinDistance(be.linkedRocket.getEntityPos(), 4)
        ) be.unlink();
    }

    @Override
    public int size() {
        if(this.linkedRocket == null) return 0;
        return this.linkedRocket.size();
    }

    @Override
    public boolean isEmpty() {
        if(this.linkedRocket == null) return true;
        return this.linkedRocket.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        if(this.linkedRocket == null) return ItemStack.EMPTY;
        return this.linkedRocket.getStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        if(this.linkedRocket == null) return ItemStack.EMPTY;
        return this.linkedRocket.removeStack(slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        if(this.linkedRocket == null) return ItemStack.EMPTY;
        return this.linkedRocket.removeStack(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if(this.linkedRocket == null) return;
        this.linkedRocket.setStack(slot, stack);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return this.linkedRocket != null;
    }

    @Override
    public void clear() {
        if(this.linkedRocket == null) return;
        this.linkedRocket.clear();
    }

    public void unlinkIfLinkedTo(CargoRocketEntity e) {
        if(this.linkedRocket == e) this.unlink();
    }

    public void unlink() {
        if(this.linkedRocket == null) return;
        this.linkedRocket.unlinkManager(this.pos);
        this.linkedRocket = null;
        if(this.world != null) this.world.updateComparators(this.pos, this.getCachedState().getBlock());
    }

    public boolean updateFromLinked(CargoRocketEntity e) {
        if(e != this.linkedRocket) return false;
        if(this.world == null) return true;
        this.world.updateComparators(this.pos, this.getCachedState().getBlock());
        return true;
    }

}
