package io.github.implicitsaber.mod.server_side_rocketry;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;

public class ServerSideRocketry implements ModInitializer {

    public static final String MOD_ID = "server_side_rocketry";

    @Override
    public void onInitialize() {
        PolymerResourcePackUtils.addModAssets(MOD_ID);
        PolymerResourcePackUtils.markAsRequired();
    }

}
