package io.github.implicitsaber.mod.server_side_rocketry;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import io.github.implicitsaber.mod.server_side_rocketry.keys.ModWorldKeys;
import io.github.implicitsaber.mod.server_side_rocketry.reg.*;
import io.github.implicitsaber.mod.server_side_rocketry.util.GravityManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class ServerSideRocketry implements ModInitializer {

    public static final String MOD_ID = "server_side_rocketry";

    @Override
    public void onInitialize() {
        PolymerResourcePackUtils.addModAssets(MOD_ID);

        ModItems.load();
        ModDataComponents.load();
        ModBlocks.load();
        ModBlockItems.load();
        ModEntityTypes.load();

        GravityManager.register(ModWorldKeys.MOON, 0.17);
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

}
