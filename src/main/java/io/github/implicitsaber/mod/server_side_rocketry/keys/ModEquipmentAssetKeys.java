package io.github.implicitsaber.mod.server_side_rocketry.keys;

import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.registry.RegistryKey;

import static io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry.id;

public class ModEquipmentAssetKeys {

    public static final RegistryKey<EquipmentAsset> SPACE_SUIT = RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, id("space_suit"));

}
