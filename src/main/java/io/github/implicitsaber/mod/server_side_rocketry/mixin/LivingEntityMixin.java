package io.github.implicitsaber.mod.server_side_rocketry.mixin;

import io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry;
import io.github.implicitsaber.mod.server_side_rocketry.util.GravityManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {

    @Shadow
    public abstract AttributeContainer getAttributes();

    @Unique
    private static final Identifier server_side_rocketry$MODIFIER_ID = ServerSideRocketry.id("gravity");

    @Override
    protected void server_side_rocketry$getFinalGravity(CallbackInfoReturnable<Double> cir) {
        // simply disable gravity adjustment for living entities, they have attribute modifiers for that
    }

    @Override
    protected void server_side_rocketry$afterSetWorld(World world, CallbackInfo ci) {
        server_side_rocketry$updateForWorld(world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    protected void server_side_rocketry$afterConstructor(EntityType<?> type, World world, CallbackInfo ci) {
        server_side_rocketry$updateForWorld(world);
    }

    @Unique
    private void server_side_rocketry$updateForWorld(World world) {
        EntityAttributeInstance instance = this.getAttributes().getCustomInstance(EntityAttributes.GRAVITY);
        if(instance != null) {
            instance.removeModifier(server_side_rocketry$MODIFIER_ID);
            instance.addTemporaryModifier(new EntityAttributeModifier(
                    server_side_rocketry$MODIFIER_ID,
                    GravityManager.getGravityMultiplierFor(world.getRegistryKey()) - 1,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ));
        }
    }

}
