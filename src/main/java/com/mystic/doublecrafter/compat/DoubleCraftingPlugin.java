package com.mystic.doublecrafter.compat;

import com.mystic.doublecrafter.BlockInit;
import com.mystic.doublecrafter.gui.DoubleCraftingRecipe;
import com.mystic.doublecrafter.utils.Reference;
import dev.architectury.event.EventResult;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.List;
import java.util.Objects;

public class DoubleCraftingPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<DoubleCrafterDisplay> DOUBLE_CRAFTER = CategoryIdentifier.of(Reference.MODID, "double_crafting");

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new DoubleCraftingCategory());

        registry.addWorkstations(DOUBLE_CRAFTER, EntryStacks.of(BlockInit.DOUBLE_CRAFTER_BLOCK));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(DoubleCraftingRecipe.class, DoubleCrafterDisplay::new);

        registry.registerVisibilityPredicate((category, display) -> {
            if (Objects.equals(category.getCategoryIdentifier(), DOUBLE_CRAFTER)) {
                if (display.getOutputEntries().stream().flatMap(List::stream)
                        .anyMatch(entryStack -> entryStack.getValue() instanceof ItemStack stack && stack.getItem() == Items.NETHERITE_SWORD))
                    return EventResult.interruptFalse();
            }
            return EventResult.pass();
        });
    }

    @Override
    public void registerEntries(EntryRegistry registry) {
        registry.removeEntry(EntryStacks.of(Items.NETHERITE_SWORD));
    }
}
