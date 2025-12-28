package io.github.implicitsaber.mod.server_side_rocketry.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry;
import io.github.implicitsaber.mod.server_side_rocketry.reg.ModDataComponentTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class OxygenCanisterItem extends Item implements PolymerItem {

    private static final Function<Integer, Identifier> TO_MODEL = Util.memoize(i ->
            ServerSideRocketry.id("oxygen_canister/" + i));
    private static final String OXYGEN_TRANSLATION_KEY = ServerSideRocketry.MOD_ID + ".oxygen";

    public OxygenCanisterItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getPolymerItemStack(ItemStack stack, TooltipType tooltipType, PacketContext context) {
        ItemStack poly = PolymerItem.super.getPolymerItemStack(stack, tooltipType, context);
        int maxOxygen = stack.getOrDefault(ModDataComponentTypes.MAX_OXYGEN, 0);
        int oxygen = stack.getOrDefault(ModDataComponentTypes.OXYGEN, 0);
        List<Text> lore = new ArrayList<>(poly.getOrDefault(DataComponentTypes.LORE, LoreComponent.DEFAULT).lines());
        if(lore.size() < 256) lore.add(Text.translatable(OXYGEN_TRANSLATION_KEY, oxygen, maxOxygen)
                .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.AQUA)));
        poly.set(DataComponentTypes.LORE, new LoreComponent(lore));
        return poly;
    }

    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
        if(!PolymerResourcePackUtils.hasMainPack(context)) return null;
        int maxOxygen = stack.getOrDefault(ModDataComponentTypes.MAX_OXYGEN, 0);
        if(maxOxygen < 1) return TO_MODEL.apply(0);
        int oxygen = stack.getOrDefault(ModDataComponentTypes.OXYGEN, 0);
        float d = (float) oxygen / maxOxygen;
        int textureId = (int) Math.clamp(d * 8, 0, 8);
        return TO_MODEL.apply(textureId);
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.DIAMOND;
    }

}
