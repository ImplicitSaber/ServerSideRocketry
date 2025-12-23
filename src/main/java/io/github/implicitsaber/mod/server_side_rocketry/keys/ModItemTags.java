package io.github.implicitsaber.mod.server_side_rocketry.keys;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

import static io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry.id;

public class ModItemTags {

    public static final TagKey<Item> REPAIRS_SPACE_SUIT = TagKey.of(RegistryKeys.ITEM, id("repairs_space_suit"));

}
