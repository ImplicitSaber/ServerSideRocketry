package io.github.implicitsaber.mod.server_side_rocketry.util;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecipeUtils {

    @SafeVarargs
    public static void unlockRecipeIds(ServerPlayerEntity p, RegistryKey<Recipe<?>>... keys) {
        List<RecipeEntry<?>> entries = new ArrayList<>();
        for(RegistryKey<Recipe<?>> recipe : keys) {
            Optional<RecipeEntry<?>> entry = p.getEntityWorld().getRecipeManager().get(recipe);
            if(entry.isEmpty()) continue;
            entries.add(entry.get());
        }
        if(!entries.isEmpty()) p.unlockRecipes(entries);
    }

}
