package com.mystic.doublecrafter.itemgroup;

import com.mystic.doublecrafter.BlockInit;
import com.mystic.doublecrafter.DoubleCrafter;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

public class DoubleCrafterItemGroup
{
    public static void init(){
        FabricItemGroupBuilder.create(DoubleCrafter.id("general")).icon(() -> BlockInit.DOUBLE_CRAFTER_BLOCK.asItem().getDefaultStack()).appendItems((stacks) -> {
            Registry.ITEM.stream().filter((item) -> {
                return Registry.ITEM.getId(item).getNamespace().equals("doublecrafter");
            }).forEach((item) -> stacks.add(new ItemStack(item)));
        }).build();
    }
}
