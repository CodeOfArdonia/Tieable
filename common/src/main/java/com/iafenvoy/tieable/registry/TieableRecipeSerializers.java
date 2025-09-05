package com.iafenvoy.tieable.registry;

import com.iafenvoy.tieable.Tieable;
import com.iafenvoy.tieable.recipe.TiedBlockRecipe;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Supplier;

public final class TieableRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(Tieable.MOD_ID, RegistryKeys.RECIPE_SERIALIZER);

    public static final RegistrySupplier<RecipeSerializer<TiedBlockRecipe>> TIED_BLOCK = register("tied_block", () -> TiedBlockRecipe.Serializer.INSTANCE);

    public static <T extends Recipe<?>> RegistrySupplier<RecipeSerializer<T>> register(String id, Supplier<RecipeSerializer<T>> obj) {
        return REGISTRY.register(id, obj);
    }
}
