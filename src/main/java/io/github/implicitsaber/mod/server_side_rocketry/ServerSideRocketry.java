package io.github.implicitsaber.mod.server_side_rocketry;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import io.github.implicitsaber.mod.server_side_rocketry.keys.ModWorldKeys;
import io.github.implicitsaber.mod.server_side_rocketry.reg.*;
import io.github.implicitsaber.mod.server_side_rocketry.util.Climate;
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
        ModDataComponentTypes.load();
        ModPolymerItemGroups.load();
        ModBlocks.load();
        ModBlockItems.load();
        ModBlockEntityTypes.load();
        ModEntityTypes.load();
        ModSoundEvents.load();
        ModChunkTicketTypes.load();

        SpaceEffectsManager.register(ModWorldKeys.MOON, new SpaceEffectsManager.SpaceEffects(
                0.1654, false, Climate.EXTREME, false));
        SpaceEffectsManager.register(ModWorldKeys.MERCURY, new SpaceEffectsManager.SpaceEffects(
                0.38, false, Climate.VERY_EXTREME, false));
        SpaceEffectsManager.register(ModWorldKeys.VENUS, new SpaceEffectsManager.SpaceEffects(
                0.904, false, Climate.VERY_EXTREME, true));
        SpaceEffectsManager.register(ModWorldKeys.MARS, new SpaceEffectsManager.SpaceEffects(
                0.3794, false, Climate.EXTREME, false));
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

}
