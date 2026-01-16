package io.github.implicitsaber.mod.server_side_rocketry.mixin;

import io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry;
import io.github.implicitsaber.mod.server_side_rocketry.keys.ModDialogKeys;
import io.github.implicitsaber.mod.server_side_rocketry.keys.ModRecipeKeys;
import io.github.implicitsaber.mod.server_side_rocketry.util.RecipeUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.packet.c2s.common.CustomClickActionC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin extends ServerCommonNetworkHandlerMixin {

    @Shadow
    public ServerPlayerEntity player;

    @Unique
    private static final Map<Identifier, BiConsumer<Optional<NbtElement>, ServerPlayerEntity>> server_side_rocketry$customClickRegistry = new Object2ObjectOpenHashMap<>();

    static {
        server_side_rocketry$customClickRegistry.put(ServerSideRocketry.id("unlock_recipes_pg2"), (payload, p) -> {
            RecipeUtils.unlockRecipeIds(p,
                    ModRecipeKeys.SPACE_HELMET_RECIPE,
                    ModRecipeKeys.SPACE_CHESTPLATE_RECIPE,
                    ModRecipeKeys.SPACE_LEGGINGS_RECIPE,
                    ModRecipeKeys.SPACE_BOOTS_RECIPE
            );
            p.getRegistryManager().getOptionalEntry(ModDialogKeys.GUIDEBOOK_PAGE_2).ifPresent(p::openDialog);
        });
        server_side_rocketry$customClickRegistry.put(ServerSideRocketry.id("unlock_recipes_pg3"), (payload, p) -> {
            RecipeUtils.unlockRecipeIds(p,
                    ModRecipeKeys.OXYGEN_CANISTER_RECIPE,
                    ModRecipeKeys.OXYGEN_COMPRESSOR_RECIPE
            );
            p.getRegistryManager().getOptionalEntry(ModDialogKeys.GUIDEBOOK_PAGE_3).ifPresent(p::openDialog);
        });
        server_side_rocketry$customClickRegistry.put(ServerSideRocketry.id("unlock_recipes_pg4"), (payload, p) -> {
            RecipeUtils.unlockRecipeIds(p,
                    ModRecipeKeys.ROCKET_RECIPE
            );
            p.getRegistryManager().getOptionalEntry(ModDialogKeys.GUIDEBOOK_PAGE_4).ifPresent(p::openDialog);
        });
        server_side_rocketry$customClickRegistry.put(ServerSideRocketry.id("unlock_recipes_pg5"), (payload, p) -> {
            RecipeUtils.unlockRecipeIds(p,
                    ModRecipeKeys.CARGO_ROCKET_RECIPE,
                    ModRecipeKeys.AUTOMATIC_GUIDANCE_CHIP_RECIPE
            );
            p.getRegistryManager().getOptionalEntry(ModDialogKeys.GUIDEBOOK_PAGE_5).ifPresent(p::openDialog);
        });
    }

    @Override
    protected void server_side_rocketry$onCustomClickAction(CustomClickActionC2SPacket packet, CallbackInfo ci) {
        BiConsumer<Optional<NbtElement>, ServerPlayerEntity> action = server_side_rocketry$customClickRegistry.get(packet.id());
        if(action != null) action.accept(packet.payload(), this.player);
    }

}
