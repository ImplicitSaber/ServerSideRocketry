package io.github.implicitsaber.mod.server_side_rocketry.mixin;

import io.github.implicitsaber.mod.server_side_rocketry.util.SpaceEffectsManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract World getEntityWorld();

    @Inject(method = "setWorld", at = @At("TAIL"))
    protected void server_side_rocketry$afterSetWorld(World world, CallbackInfo ci) {
        // implemented in LivingEntityMixin
    }

    @Inject(method = "remove", at = @At("HEAD"))
    protected void server_side_rocketry$onRemove(Entity.RemovalReason reason, CallbackInfo ci) {
        // implemented in ServerPlayerEntityMixin
    }
    
}
