package com.mystic.doublecrafter;

import com.mystic.doublecrafter.block.DoubleCrafterBlock;
import com.mystic.doublecrafter.utils.Reference;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Objects;
import java.util.function.Function;

public class BlockInit {

    public static void init(){}

    public static final DoubleCrafterBlock DOUBLE_CRAFTER_BLOCK = (DoubleCrafterBlock) register("double_crafter_block", new DoubleCrafterBlock(FabricBlockSettings.of(Material.WOOD)));

    private static Block baseRegister(String name, Block block, Function<Block, Item> item) {
        Registry.register(Registry.BLOCK, new Identifier(Reference.MODID, name), block);
        register(name, item.apply(block));
        return block;
    }

    private static Block register(String name, Block block) {
        return baseRegister(name, block, BlockInit::registerBlockItem);
    }

    private static BlockItem registerBlockItem(Block block) {
        return new BlockItem(Objects.requireNonNull(block), new Item.Settings());
    }

    public static Item register(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Reference.MODID, name), item);
    }

}
