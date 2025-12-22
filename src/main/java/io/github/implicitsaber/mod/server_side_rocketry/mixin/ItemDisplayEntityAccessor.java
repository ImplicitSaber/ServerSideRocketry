package io.github.implicitsaber.mod.server_side_rocketry.mixin;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DisplayEntity.ItemDisplayEntity.class)
public interface ItemDisplayEntityAccessor {

    @Accessor("ITEM")
    static TrackedData<ItemStack> getItemData() {
        throw new AssertionError("Mixin transformation failed");
    }

    @Accessor("ITEM_DISPLAY")
    static TrackedData<Byte> getItemDisplayData() {
        throw new AssertionError("Mixin transformation failed");
    }

}
