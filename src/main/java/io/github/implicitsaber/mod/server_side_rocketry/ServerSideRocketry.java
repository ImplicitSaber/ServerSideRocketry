package io.github.implicitsaber.mod.server_side_rocketry;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import io.github.implicitsaber.mod.server_side_rocketry.keys.ModWorldKeys;
import io.github.implicitsaber.mod.server_side_rocketry.reg.*;
import io.github.implicitsaber.mod.server_side_rocketry.util.SpaceEffectsManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class ServerSideRocketry implements ModInitializer {

    public static final String MOD_ID = "server_side_rocketry";

    @Override
    public void onInitialize() {
        PolymerResourcePackUtils.addModAssets(MOD_ID);
        PolymerResourcePackUtils.markAsRequired();

        ModItems.load();
        ModDataComponents.load();
        ModPolymerItemGroups.load();
        ModBlocks.load();
        ModBlockItems.load();
        ModBlockEntities.load();
        ModEntityTypes.load();
        ModSoundEvents.load();

        SpaceEffectsManager.register(ModWorldKeys.MOON, new SpaceEffectsManager.SpaceEffects(0.17, false));
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

}
