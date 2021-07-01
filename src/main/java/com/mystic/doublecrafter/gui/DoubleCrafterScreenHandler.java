package com.mystic.doublecrafter.gui;

import com.mystic.doublecrafter.BlockInit;
import com.mystic.doublecrafter.DoubleCrafter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class DoubleCrafterScreenHandler extends AbstractRecipeScreenHandler<CraftingInventory> {
        public static final int field_30781 = 0;
        private static final int field_30782 = 1;
        private static final int field_30783 = 10;
        private static final int field_30784 = 10;
        private static final int field_30785 = 37;
        private static final int field_30786 = 37;
        private static final int field_30787 = 46;
        private final CraftingInventory input;
        private final CraftingResultInventory result;
        private final ScreenHandlerContext context;
        private final PlayerEntity player;
        private final CraftingInventory input2;
        private final CraftingResultInventory result2;
        private final CraftingResultInventory result3;

    public DoubleCrafterScreenHandler(int syncId, PlayerInventory playerInventory) {
            this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
        }

        public DoubleCrafterScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
            super(DoubleCrafter.DOUBLE_CRAFTER_SCREEN_HANDLER, syncId);
            this.input = new CraftingInventory(this, 3, 3);
            this.input2 = new CraftingInventory(this, 3, 3);
            this.result = new CraftingResultInventory();
            this.result2 = new CraftingResultInventory();
            this.result3 = new CraftingResultInventory();
            this.context = context;
            this.player = playerInventory.player;
            this.addSlot(new CraftingResultSlot(playerInventory.player, this.input, this.result, 2, 41, 98));
            this.addSlot(new CraftingResultSlot(playerInventory.player, this.input2, this.result2, 2, 117, 98));
            this.addSlot(new Slot(this.result3, 0, 181, 34) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return false;
                }

                @Override
                public void onTakeItem(PlayerEntity player, ItemStack stack) {
                    clearCraftingSlots();
                }
            });
            int m;
            int l;
            for(m = 0; m < 3; ++m) {
                for(l = 0; l < 3; ++l) {
                    this.addSlot(new Slot(this.input, l + m * 3, 23 + l * 18, 16 + m * 18));
                }
            }

            for(m = 0; m < 3; ++m) {
                for(l = 0; l < 3; ++l) {
                    this.addSlot(new Slot(this.input2, l + m * 3, 98 + l * 18, 16 + m * 18));
                }
            }

            for(m = 0; m < 3; ++m) {
                for(l = 0; l < 9; ++l) {
                    this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 27 + l * 18, 123 + m * 18));
                }
            }

            for(m = 0; m < 9; ++m) {
                this.addSlot(new Slot(playerInventory, m, 27 + m * 18, 181));
            }

        }

    protected static void updateResult(ScreenHandler screenHandler, World world, PlayerEntity player, CraftingInventory craftingInventory, CraftingResultInventory resultInventory) {
            if (!world.isClient) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
                ItemStack itemStack = ItemStack.EMPTY;
                Optional<CraftingRecipe> optional = world.getServer().getRecipeManager().getFirstMatch(RecipeType.CRAFTING, craftingInventory, world);
                if (optional.isPresent()) {
                    CraftingRecipe craftingRecipe = optional.get();
                    if (resultInventory.shouldCraftRecipe(world, serverPlayerEntity, craftingRecipe)) {
                        itemStack = craftingRecipe.craft(craftingInventory);
                    }
                }

                resultInventory.setStack(0, itemStack);
                screenHandler.setPreviousTrackedSlot(0, itemStack);
                serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(screenHandler.syncId, 0, itemStack));
            }
        }

        public void updateResultMerge(ScreenHandler screenHandler, World world, PlayerEntity player, CraftingResultInventory resultInventory){
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
            ItemStack itemStack = ItemStack.EMPTY;
            Inventory mergingInventory = new SimpleInventory(2);
            mergingInventory.setStack(0, result.getStack(0));
            mergingInventory.setStack(1, result2.getStack(0));

            Optional<DoubleCraftingRecipe> match = world.getRecipeManager().getFirstMatch(DoubleCraftingRecipe.Type.INSTANCE, mergingInventory , world);
            if(match.isPresent()){
                Recipe<Inventory> recipe = match.get();
                if(resultInventory.shouldCraftRecipe(world, serverPlayerEntity, recipe)) {
                    itemStack = recipe.craft(mergingInventory);
                }
            }

            resultInventory.setStack(0, itemStack);
            screenHandler.setPreviousTrackedSlot(0, itemStack);
            serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(screenHandler.syncId, 0, itemStack));
        }

        public void onContentChanged(Inventory inventory) {

            this.context.run((world, blockPos) -> {
                updateResult(this, world, this.player, this.input, this.result);
                updateResult(this, world, this.player, this.input2, this.result2);
                updateResultMerge(this, world, this.player, this.result3);
            });
        }

        public void populateRecipeFinder(RecipeMatcher finder) {
            this.input.provideRecipeInputs(finder);
            this.input2.provideRecipeInputs(finder);
        }

        public void clearCraftingSlots() {
            this.input.clear();
            this.result.clear();
            this.input2.clear();
            this.result2.clear();
            this.result3.clear();
        }

        public boolean matches(Recipe<? super CraftingInventory> recipe) {
            if (input2.isEmpty()) {
                return recipe.matches(this.input, this.player.world);
            } else if(input.isEmpty()) {
                return recipe.matches(this.input2, this.player.world);
            }
            return recipe.matches(this.input, this.player.world);
        }

        public void close(PlayerEntity playerEntity) {
            super.close(playerEntity);
            this.context.run((world, blockPos) -> {
                this.dropInventory(playerEntity, this.input);
                this.dropInventory(playerEntity, this.input2);
            });
        }

        public boolean canUse(PlayerEntity player) {
            return canUse(this.context, player, BlockInit.DOUBLE_CRAFTER_BLOCK);
        }

        public ItemStack transferSlot(PlayerEntity player, int index) {
            ItemStack itemStack = ItemStack.EMPTY;
            Slot slot = (Slot)this.slots.get(index);
            if (slot != null && slot.hasStack()) {
                ItemStack itemStack2 = slot.getStack();
                itemStack = itemStack2.copy();
                if (index == 0) {
                    this.context.run((world, blockPos) -> {
                        itemStack2.getItem().onCraft(itemStack2, world, player);
                    });
                    if (!this.insertItem(itemStack2, 10, 46, true)) {
                        return ItemStack.EMPTY;
                    }

                    slot.onQuickTransfer(itemStack2, itemStack);
                } else if (index >= 10 && index < 46) {
                    if (!this.insertItem(itemStack2, 1, 10, false)) {
                        if (index < 37) {
                            if (!this.insertItem(itemStack2, 37, 46, false)) {
                                return ItemStack.EMPTY;
                            }
                        } else if (!this.insertItem(itemStack2, 10, 37, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                } else if (!this.insertItem(itemStack2, 10, 46, false)) {
                    return ItemStack.EMPTY;
                }

                if (itemStack2.isEmpty()) {
                    slot.setStack(ItemStack.EMPTY);
                } else {
                    slot.markDirty();
                }

                if (itemStack2.getCount() == itemStack.getCount()) {
                    return ItemStack.EMPTY;
                }

                slot.onTakeItem(player, itemStack2);
                if (index == 0) {
                    player.dropItem(itemStack2, false);
                }
            }

            return itemStack;
        }

        public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
            if(result.isEmpty()) {
                return slot.inventory != this.result2 && super.canInsertIntoSlot(stack, slot);
            } else if (result2.isEmpty()) {
                return slot.inventory != this.result && super.canInsertIntoSlot(stack, slot);
            }
            return slot.inventory != this.result && super.canInsertIntoSlot(stack, slot);
        }

        public int getCraftingResultSlotIndex() {
            return 0;
        }

        public int getCraftingWidth() {
            if(input2.isEmpty()){
                return this.input.getWidth();
            } else if (input.isEmpty()){
                return this.input2.getWidth();
            }
            return this.input.getWidth();
        }

        public int getCraftingHeight() {
            if(input2.isEmpty()){
                return this.input.getHeight();
            } else if (input.isEmpty()){
                return this.input2.getHeight();
            }
            return this.input.getHeight();
        }

        public int getCraftingSlotCount() {
            return 10;
        }

        public RecipeBookCategory getCategory() {
            return RecipeBookCategory.CRAFTING;
        }

        public boolean canInsertIntoSlot(int index) {
            return index != this.getCraftingResultSlotIndex();
        }
}
