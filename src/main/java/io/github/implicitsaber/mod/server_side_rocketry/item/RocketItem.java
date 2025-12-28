package io.github.implicitsaber.mod.server_side_rocketry.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import io.github.implicitsaber.mod.server_side_rocketry.entity.AbstractRocketEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class RocketItem extends Item implements PolymerItem {

    private final EntityType<? extends AbstractRocketEntity> type;
    private final Identifier modelId;

    public RocketItem(EntityType<? extends AbstractRocketEntity> type, Settings settings) {
        super(settings);
        this.type = type;
        this.modelId = settings.getModelId();
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient()) return ActionResult.PASS;
        HitResult result = raycast(world, user, RaycastContext.FluidHandling.NONE);
        if(result.getType() != HitResult.Type.BLOCK) return ActionResult.FAIL;
        AbstractRocketEntity rocket = type.create(world, SpawnReason.SPAWN_ITEM_USE);
        if(rocket == null) return ActionResult.FAIL;
        rocket.setPosition(result.getPos());
        ItemStack stack = user.getStackInHand(hand);
        EntityType.copier(world, stack, user).accept(rocket);
        if(!world.isSpaceEmpty(rocket, rocket.getBoundingBox())) return ActionResult.FAIL;
        world.spawnEntity(rocket);
        world.emitGameEvent(user, GameEvent.ENTITY_PLACE, result.getPos());
        stack.decrementUnlessCreative(1, user);
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return ActionResult.SUCCESS;
    }

    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        return this.modelId;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.STICK;
    }

}
