package io.github.implicitsaber.mod.server_side_rocketry.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

public class EnchantmentUtils {

    public static boolean hasEnchantment(ItemStack stack, RegistryKey<Enchantment> enchantment, DynamicRegistryManager mgr) {
        RegistryEntry<Enchantment> entry = mgr.getOptionalEntry(enchantment).orElse(null);
        if(entry == null) return false;
        return stack.getEnchantments().getLevel(entry) != 0;
    }

}
