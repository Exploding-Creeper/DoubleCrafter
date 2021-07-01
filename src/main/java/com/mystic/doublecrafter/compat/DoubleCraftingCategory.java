package com.mystic.doublecrafter.compat;

import com.mystic.doublecrafter.BlockInit;
import com.mystic.doublecrafter.utils.Reference;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Label;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DoubleCraftingCategory implements DisplayCategory<DoubleCrafterDisplay> {

    private static final TranslatableText NAME = new TranslatableText("doublecrafter.gui.double_crafting");

    @Override
    public CategoryIdentifier<? extends DoubleCrafterDisplay> getCategoryIdentifier() {
        return DoubleCraftingPlugin.DOUBLE_CRAFTER;
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(BlockInit.DOUBLE_CRAFTER_BLOCK);
    }

    @Override
    public Text getTitle() {
        return NAME;
    }

    @Override
    @NotNull
    public List<Widget> setupDisplay(DoubleCrafterDisplay recipeDisplay, Rectangle bounds) {
        Point origin = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 27);

        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));

        List<EntryIngredient> inputs = recipeDisplay.getInputEntries();
        List<Slot> slots = new ArrayList<>();

        for (int y = 0; y < 1; y++) {
            for (int x = 0; x < 2; x++) {
                slots.add(Widgets.createSlot(new Point(origin.x + 20 + x * 18, origin.y + 1 * 18)).markInput());
            }
        }

        for (int i = 0; i < inputs.size(); i++) {
            if (!inputs.get(i).isEmpty()) {
                slots.get(i).entries(inputs.get(i));
            }
        }

        widgets.add(Widgets.createArrow(new Point(origin.x + 3.5 * 18, origin.y + 1 * 18)));

        widgets.addAll(slots);
        widgets.add(Widgets.createResultSlotBackground(new Point(origin.x + 95, origin.y + 19)));
        widgets.add(Widgets.createSlot(new Point(origin.x + 95, origin.y + 19)).entries(recipeDisplay.getOutputEntries().get(0)).disableBackground().markOutput());

        return widgets;
    }
}
