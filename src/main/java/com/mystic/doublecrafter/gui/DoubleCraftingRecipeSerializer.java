package com.mystic.doublecrafter.gui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mystic.doublecrafter.utils.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DoubleCraftingRecipeSerializer implements RecipeSerializer<DoubleCraftingRecipe> {

    private DoubleCraftingRecipeSerializer() {
    }

    public static final DoubleCraftingRecipeSerializer INSTANCE = new DoubleCraftingRecipeSerializer();

    public static final Identifier ID = new Identifier(Reference.MODID, "double_crafting");

    @Override
    public DoubleCraftingRecipe read(Identifier id, JsonObject json) {
        DoubleCrafterRecipeJson recipeJson = new Gson().fromJson(json, DoubleCrafterRecipeJson.class);


        if (recipeJson.inputA == null || recipeJson.inputB == null || recipeJson.outputItem == null) {
            throw new JsonSyntaxException("A required attribute is missing!");
        }

        if (recipeJson.outputAmount == 0) recipeJson.outputAmount = 1;

        Ingredient inputA = Ingredient.fromJson(recipeJson.inputA);
        Ingredient inputB = Ingredient.fromJson(recipeJson.inputB);
        Item outputItem = Registry.ITEM.getOrEmpty(new Identifier(recipeJson.outputItem))
                .orElseThrow(() -> new JsonSyntaxException("No such item " + recipeJson.outputItem));
        ItemStack output = new ItemStack(outputItem, recipeJson.outputAmount);

        return new DoubleCraftingRecipe(id, output, inputA, inputB);
    }

    @Override
    public DoubleCraftingRecipe read(Identifier id, PacketByteBuf buf) {
        Ingredient inputA = Ingredient.fromPacket(buf);
        Ingredient inputB = Ingredient.fromPacket(buf);
        ItemStack output = buf.readItemStack();
        return new DoubleCraftingRecipe(id, output, inputA, inputB);
    }

    @Override
    public void write(PacketByteBuf buf, DoubleCraftingRecipe recipe) {
        recipe.getInputA().write(buf);
        recipe.getInputB().write(buf);
        buf.writeItemStack(recipe.getOutput());
    }
}
