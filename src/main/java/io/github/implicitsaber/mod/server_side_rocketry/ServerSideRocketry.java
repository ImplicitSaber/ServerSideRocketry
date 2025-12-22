package io.github.implicitsaber.mod.server_side_rocketry;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import io.github.implicitsaber.mod.server_side_rocketry.reg.ModBlockItems;
import io.github.implicitsaber.mod.server_side_rocketry.reg.ModBlocks;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class ServerSideRocketry implements ModInitializer {

    public static final String MOD_ID = "server_side_rocketry";

    @Override
    public void onInitialize() {
        PolymerResourcePackUtils.addModAssets(MOD_ID);

        ModBlocks.load();
        ModBlockItems.load();
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

}
