package io.github.implicitsaber.mod.server_side_rocketry.item;

import eu.pb4.polymer.core.api.item.PolymerItem;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry;
import io.github.implicitsaber.mod.server_side_rocketry.keys.ModDialogKeys;
import net.minecraft.dialog.type.Dialog;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.Optional;

public class GuidebookItem extends Item implements PolymerItem {

    private static final Identifier MODEL = ServerSideRocketry.id("guidebook");

    public GuidebookItem(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext ctx) {
        return PolymerResourcePackUtils.hasMainPack(ctx) ? MODEL : null;
    }

    @Override
    public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
        return Items.BOOK;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if(!(user instanceof ServerPlayerEntity player)) return ActionResult.PASS;
        Optional<RegistryEntry.Reference<Dialog>> opt = world.getRegistryManager().getOptionalEntry(ModDialogKeys.GUIDEBOOK_PAGE_1);
        if(opt.isEmpty()) return ActionResult.FAIL;
        player.openDialog(opt.get());
        return ActionResult.SUCCESS;
    }

}
