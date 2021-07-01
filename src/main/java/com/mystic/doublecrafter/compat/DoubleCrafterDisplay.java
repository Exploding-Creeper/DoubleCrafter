package com.mystic.doublecrafter.compat;

import com.mystic.doublecrafter.gui.DoubleCraftingRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DoubleCrafterDisplay implements Display {
    protected DoubleCraftingRecipe display;
    protected List<EntryIngredient> inputA;
    protected List<EntryIngredient> inputB;
    protected List<EntryIngredient> output;
    protected ArrayList<EntryIngredient> list;
    public DoubleCrafterDisplay(DoubleCraftingRecipe recipe) {
        this.display = recipe;

        this.inputA = EntryIngredients.ofIngredients(List.of(recipe.getInputA()));

        this.inputB = EntryIngredients.ofIngredients(List.of(recipe.getInputB()));

        this.output = Collections.singletonList(EntryIngredients.of(recipe.getOutput()));

        list = new ArrayList<>();
    }

    @Override
    public @NotNull List<EntryIngredient> getInputEntries() {
        list.addAll(inputA);
        list.addAll(inputB);
        return list;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return DoubleCraftingPlugin.DOUBLE_CRAFTER;
    }

    @Override
    public @NotNull List<EntryIngredient> getOutputEntries() {
        return output;
    }
}
