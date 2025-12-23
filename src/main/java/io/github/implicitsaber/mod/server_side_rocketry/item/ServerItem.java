package io.github.implicitsaber.mod.server_side_rocketry.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

public class ServerItem extends Item implements PolymerItem {

    private final Item fallback;
    private final Identifier model;

    public ServerItem(Item fallback, Settings settings) {
        super(settings);
        this.fallback = fallback;
        this.model = settings.getModelId();
    }

    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext ctx) {
        return PolymerResourcePackUtils.hasMainPack(ctx) ? this.model : null;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return this.fallback;
    }

}
