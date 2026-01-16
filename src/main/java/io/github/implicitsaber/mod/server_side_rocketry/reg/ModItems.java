package io.github.implicitsaber.mod.server_side_rocketry.reg;

import io.github.implicitsaber.mod.server_side_rocketry.item.GuidebookItem;
import io.github.implicitsaber.mod.server_side_rocketry.item.OxygenCanisterItem;
import io.github.implicitsaber.mod.server_side_rocketry.item.RocketItem;
import io.github.implicitsaber.mod.server_side_rocketry.item.ServerItem;
import io.github.implicitsaber.mod.server_side_rocketry.keys.ModEquipmentAssetKeys;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Unit;

import static io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry.id;

public class ModItems {
    
    public static final RegistryKey<Item> SPACE_HELMET_KEY = RegistryKey.of(RegistryKeys.ITEM, id("space_helmet"));
    public static final ServerItem SPACE_HELMET = Registry.register(
            Registries.ITEM,
            SPACE_HELMET_KEY,
            new ServerItem(Items.IRON_HELMET, new Item.Settings().registryKey(SPACE_HELMET_KEY)
                    .component(DataComponentTypes.EQUIPPABLE, EquippableComponent.builder(EquipmentSlot.HEAD)
                            .equipSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC)
                            .model(ModEquipmentAssetKeys.SPACE_SUIT)
                            .build())
                    .component(ModDataComponentTypes.SPACE_ARMOR, Unit.INSTANCE))
    );

    public static final RegistryKey<Item> SPACE_CHESTPLATE_KEY = RegistryKey.of(RegistryKeys.ITEM, id("space_chestplate"));
    public static final ServerItem SPACE_CHESTPLATE = Registry.register(
            Registries.ITEM,
            SPACE_CHESTPLATE_KEY,
            new ServerItem(Items.IRON_CHESTPLATE, new Item.Settings().registryKey(SPACE_CHESTPLATE_KEY)
                    .component(DataComponentTypes.EQUIPPABLE, EquippableComponent.builder(EquipmentSlot.CHEST)
                            .equipSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC)
                            .model(ModEquipmentAssetKeys.SPACE_SUIT)
                            .build())
                    .component(ModDataComponentTypes.SPACE_ARMOR, Unit.INSTANCE))
    );

    public static final RegistryKey<Item> SPACE_LEGGINGS_KEY = RegistryKey.of(RegistryKeys.ITEM, id("space_leggings"));
    public static final ServerItem SPACE_LEGGINGS = Registry.register(
            Registries.ITEM,
            SPACE_LEGGINGS_KEY,
            new ServerItem(Items.IRON_LEGGINGS, new Item.Settings().registryKey(SPACE_LEGGINGS_KEY)
                    .component(DataComponentTypes.EQUIPPABLE, EquippableComponent.builder(EquipmentSlot.LEGS)
                            .equipSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC)
                            .model(ModEquipmentAssetKeys.SPACE_SUIT)
                            .build())
                    .component(ModDataComponentTypes.SPACE_ARMOR, Unit.INSTANCE))
    );

    public static final RegistryKey<Item> SPACE_BOOTS_KEY = RegistryKey.of(RegistryKeys.ITEM, id("space_boots"));
    public static final ServerItem SPACE_BOOTS = Registry.register(
            Registries.ITEM,
            SPACE_BOOTS_KEY,
            new ServerItem(Items.IRON_BOOTS, new Item.Settings().registryKey(SPACE_BOOTS_KEY)
                    .component(DataComponentTypes.EQUIPPABLE, EquippableComponent.builder(EquipmentSlot.FEET)
                            .equipSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC)
                            .model(ModEquipmentAssetKeys.SPACE_SUIT)
                            .build())
                    .component(ModDataComponentTypes.SPACE_ARMOR, Unit.INSTANCE))
    );

    public static final RegistryKey<Item> OXYGEN_CANISTER_KEY = RegistryKey.of(RegistryKeys.ITEM, id("oxygen_canister"));
    public static final OxygenCanisterItem OXYGEN_CANISTER = Registry.register(
            Registries.ITEM,
            OXYGEN_CANISTER_KEY,
            new OxygenCanisterItem(new Item.Settings().registryKey(OXYGEN_CANISTER_KEY).maxCount(1)
                    .component(ModDataComponentTypes.MAX_OXYGEN, 12000))
    );

    public static final RegistryKey<Item> ROCKET_KEY = RegistryKey.of(RegistryKeys.ITEM, id("rocket"));
    public static final RocketItem ROCKET = Registry.register(
            Registries.ITEM,
            ROCKET_KEY,
            new RocketItem(ModEntityTypes.ROCKET, new Item.Settings().registryKey(ROCKET_KEY).maxCount(1))
    );

    public static final RegistryKey<Item> CARGO_ROCKET_KEY = RegistryKey.of(RegistryKeys.ITEM, id("cargo_rocket"));
    public static final RocketItem CARGO_ROCKET = Registry.register(
            Registries.ITEM,
            CARGO_ROCKET_KEY,
            new RocketItem(ModEntityTypes.CARGO_ROCKET, new Item.Settings().registryKey(CARGO_ROCKET_KEY).maxCount(1))
    );

    public static final RegistryKey<Item> AUTOMATIC_GUIDANCE_CHIP_KEY = RegistryKey.of(RegistryKeys.ITEM, id("automatic_guidance_chip"));
    public static final ServerItem AUTOMATIC_GUIDANCE_CHIP = Registry.register(
            Registries.ITEM,
            AUTOMATIC_GUIDANCE_CHIP_KEY,
            new ServerItem(Items.GOLD_NUGGET, new Item.Settings().registryKey(AUTOMATIC_GUIDANCE_CHIP_KEY))
    );

    public static final RegistryKey<Item> GUIDEBOOK_KEY = RegistryKey.of(RegistryKeys.ITEM, id("guidebook"));
    public static final GuidebookItem GUIDEBOOK = Registry.register(
            Registries.ITEM,
            GUIDEBOOK_KEY,
            new GuidebookItem(new Item.Settings().registryKey(GUIDEBOOK_KEY))
    );

    public static void load() {}
    
}
