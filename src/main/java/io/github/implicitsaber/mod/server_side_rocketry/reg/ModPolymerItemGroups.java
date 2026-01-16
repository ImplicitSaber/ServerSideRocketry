package io.github.implicitsaber.mod.server_side_rocketry.reg;

import eu.pb4.polymer.core.api.item.PolymerItemGroupUtils;
import io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

import static io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry.id;

public class ModPolymerItemGroups {

    public static final RegistryKey<ItemGroup> MAIN_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, id("main"));
    public static final ItemGroup MAIN = PolymerItemGroupUtils.builder()
            .displayName(Text.translatable("itemGroup." + ServerSideRocketry.MOD_ID + ".main"))
            .icon(ModItems.ROCKET::getDefaultStack)
            .entries((ctx, e) -> {
                e.add(ModItems.GUIDEBOOK);
                e.add(ModItems.OXYGEN_CANISTER);
                e.add(ModBlockItems.OXYGEN_COMPRESSOR);
                e.add(ModItems.SPACE_HELMET);
                e.add(ModItems.SPACE_CHESTPLATE);
                e.add(ModItems.SPACE_LEGGINGS);
                e.add(ModItems.SPACE_BOOTS);
                e.add(ModItems.ROCKET);
                e.add(ModItems.CARGO_ROCKET);
                e.add(ModItems.AUTOMATIC_GUIDANCE_CHIP);
                e.add(ModBlockItems.CARGO_MANAGER);
                e.add(ModBlockItems.CARGO_LAUNCH_CONTROLLER);
                e.add(ModBlockItems.CARGO_ROCKET_DETECTOR);
                e.add(ModBlockItems.MOON_ROCK);
                e.add(ModBlockItems.MARS_ROCK);
                e.add(ModBlockItems.MERCURY_ROCK);
                e.add(ModBlockItems.VENUS_ROCK);
            })
            .build();

    static {
        PolymerItemGroupUtils.registerPolymerItemGroup(MAIN_KEY, MAIN);
    }

    public static void load() {}

}
