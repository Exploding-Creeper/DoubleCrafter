package com.mystic.doublecrafter;

import com.mystic.doublecrafter.gui.DoubleCrafterScreenHandler;
import com.mystic.doublecrafter.gui.DoubleCraftingRecipe;
import com.mystic.doublecrafter.gui.DoubleCraftingRecipeSerializer;
import com.mystic.doublecrafter.itemgroup.DoubleCrafterItemGroup;
import com.mystic.doublecrafter.utils.Reference;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DoubleCrafter implements ModInitializer {

	public static final Identifier DOUBLE_CRAFTER_ID = new Identifier(Reference.MODID, "double_crafter");
	public static final ScreenHandlerType<DoubleCrafterScreenHandler> DOUBLE_CRAFTER_SCREEN_HANDLER;

	static {
		DOUBLE_CRAFTER_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(DOUBLE_CRAFTER_ID, DoubleCrafterScreenHandler::new);
	}

	@Override
	public void onInitialize() {
		BlockInit.init();
		DoubleCrafterItemGroup.init();
		Registry.register(Registry.RECIPE_SERIALIZER, DoubleCraftingRecipeSerializer.ID,
				DoubleCraftingRecipeSerializer.INSTANCE);
		Registry.register(Registry.RECIPE_TYPE, DoubleCraftingRecipe.Type.ID, DoubleCraftingRecipe.Type.INSTANCE);
	}

	public static Identifier id(String id) {
		return new Identifier("doublecrafter", id);
	}
}
