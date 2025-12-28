package io.github.implicitsaber.mod.server_side_rocketry.mixin;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.decoration.DisplayEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DisplayEntity.class)
public interface DisplayEntityAccessor {

    @Accessor("INTERPOLATION_DURATION")
    static TrackedData<Integer> getInterpolationDurationData() {
        throw new AssertionError("Mixin transformation failed");
    }

    @Accessor("TELEPORT_DURATION")
    static TrackedData<Integer> getTeleportDurationData() {
        throw new AssertionError("Mixin transformation failed");
    }

}
