package io.github.implicitsaber.mod.server_side_rocketry.mixin;

import io.github.implicitsaber.mod.server_side_rocketry.util.GravityManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
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

    @Inject(method = "getFinalGravity", at = @At("RETURN"), cancellable = true)
    protected void server_side_rocketry$getFinalGravity(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(cir.getReturnValueD() * GravityManager.getGravityMultiplierFor(this.getEntityWorld().getRegistryKey()));
    }

    @Inject(method = "setWorld", at = @At("TAIL"))
    protected void server_side_rocketry$afterSetWorld(World world, CallbackInfo ci) {
        // implemented in LivingEntityMixin
    }
    
}
