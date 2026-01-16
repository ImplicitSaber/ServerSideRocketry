package io.github.implicitsaber.mod.server_side_rocketry.keys;

import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import static io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry.id;

public class ModRecipeKeys {

    public static final RegistryKey<Recipe<?>> SPACE_HELMET_RECIPE = RegistryKey.of(RegistryKeys.RECIPE, id("space_helmet"));
    public static final RegistryKey<Recipe<?>> SPACE_CHESTPLATE_RECIPE = RegistryKey.of(RegistryKeys.RECIPE, id("space_chestplate"));
    public static final RegistryKey<Recipe<?>> SPACE_LEGGINGS_RECIPE = RegistryKey.of(RegistryKeys.RECIPE, id("space_leggings"));
    public static final RegistryKey<Recipe<?>> SPACE_BOOTS_RECIPE = RegistryKey.of(RegistryKeys.RECIPE, id("space_boots"));
    public static final RegistryKey<Recipe<?>> OXYGEN_CANISTER_RECIPE = RegistryKey.of(RegistryKeys.RECIPE, id("oxygen_canister"));
    public static final RegistryKey<Recipe<?>> OXYGEN_COMPRESSOR_RECIPE = RegistryKey.of(RegistryKeys.RECIPE, id("oxygen_compressor"));
    public static final RegistryKey<Recipe<?>> ROCKET_RECIPE = RegistryKey.of(RegistryKeys.RECIPE, id("rocket"));
    public static final RegistryKey<Recipe<?>> CARGO_ROCKET_RECIPE = RegistryKey.of(RegistryKeys.RECIPE, id("cargo_rocket"));
    public static final RegistryKey<Recipe<?>> AUTOMATIC_GUIDANCE_CHIP_RECIPE = RegistryKey.of(RegistryKeys.RECIPE, id("automatic_guidance_chip"));

}
