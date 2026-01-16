package io.github.implicitsaber.mod.server_side_rocketry.mixin;

import net.minecraft.network.packet.c2s.common.CustomClickActionC2SPacket;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonNetworkHandler.class)
public abstract class ServerCommonNetworkHandlerMixin {

    @Inject(method = "onCustomClickAction", at = @At("TAIL"))
    protected void server_side_rocketry$onCustomClickAction(CustomClickActionC2SPacket packet, CallbackInfo ci) {
        // implemented in subclasses
    }

}
