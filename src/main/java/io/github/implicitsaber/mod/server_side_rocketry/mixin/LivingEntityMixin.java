package io.github.implicitsaber.mod.server_side_rocketry.mixin;

import io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry;
import io.github.implicitsaber.mod.server_side_rocketry.keys.ModDamageTypeKeys;
import io.github.implicitsaber.mod.server_side_rocketry.reg.ModDataComponents;
import io.github.implicitsaber.mod.server_side_rocketry.util.SpaceEffectsManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {

    @Shadow
    public abstract AttributeContainer getAttributes();

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow
    public abstract boolean damage(ServerWorld world, DamageSource source, float amount);

    @Shadow
    public abstract boolean isInCreativeMode();

    @Shadow
    public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Unique
    private static final Identifier server_side_rocketry$MODIFIER_ID = ServerSideRocketry.id("gravity");

    @Inject(method = "tick", at = @At("TAIL"))
    private void server_side_rocketry$tick(CallbackInfo ci) {
        SpaceEffectsManager.SpaceEffects effects = SpaceEffectsManager.getSpaceEffectsFor(this.getEntityWorld().getRegistryKey());
        if(!effects.hasOxygen() && !this.isInCreativeMode()) {
            ItemStack helmet = this.getEquippedStack(EquipmentSlot.HEAD);
            boolean canBreathe = false;
            if(helmet.contains(ModDataComponents.SPACE_ARMOR)) canBreathe = server_side_rocketry$attemptConsumeOxygen();
            else server_side_rocketry$oxygenNotInUse();
            if(!canBreathe && this.getEntityWorld() instanceof ServerWorld sw) {
                DamageSource source = sw.getDamageSources().create(ModDamageTypeKeys.OXYGEN_LOSS);
                this.damage(sw, source, 1.0f);
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 150, 4, false, false));
            }
        } else server_side_rocketry$oxygenNotInUse();
    }

    @Unique
    protected boolean server_side_rocketry$attemptConsumeOxygen() {
        for(EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = this.getEquippedStack(slot);
            if(stack.contains(ModDataComponents.MAX_OXYGEN)) {
                int oxygen = stack.getOrDefault(ModDataComponents.OXYGEN, 0);
                if(oxygen > 0) {
                    stack.set(ModDataComponents.OXYGEN, oxygen - 1);
                    return true;
                }
            }
        }
        return false;
    }

    @Unique
    protected void server_side_rocketry$oxygenNotInUse() {
        // for subclasses
    }

    @Override
    protected void server_side_rocketry$afterSetWorld(World world, CallbackInfo ci) {
        server_side_rocketry$updateForWorld(world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void server_side_rocketry$afterConstructor(EntityType<?> type, World world, CallbackInfo ci) {
        server_side_rocketry$updateForWorld(world);
    }

    @Unique
    private void server_side_rocketry$updateForWorld(World world) {
        EntityAttributeInstance instance = this.getAttributes().getCustomInstance(EntityAttributes.GRAVITY);
        if(instance != null) {
            instance.removeModifier(server_side_rocketry$MODIFIER_ID);
            instance.addTemporaryModifier(new EntityAttributeModifier(
                    server_side_rocketry$MODIFIER_ID,
                    SpaceEffectsManager.getSpaceEffectsFor(world.getRegistryKey()).gravity() - 1,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ));
        }
    }

}
