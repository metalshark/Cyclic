package com.lothrazar.cyclic.compat.jei;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.generatorfluid.ScreenGeneratorFluid;
import com.lothrazar.cyclic.block.generatoritem.ContainerGeneratorDrops;
import com.lothrazar.cyclic.block.generatoritem.ScreenGeneratorDrops;
import com.lothrazar.cyclic.block.melter.ContainerMelter;
import com.lothrazar.cyclic.block.melter.ScreenMelter;
import com.lothrazar.cyclic.block.packager.ScreenPackager;
import com.lothrazar.cyclic.block.solidifier.ContainerSolidifier;
import com.lothrazar.cyclic.block.solidifier.ScreenSolidifier;
import com.lothrazar.cyclic.block.workbench.ContainerWorkbench;
import com.lothrazar.cyclic.item.crafting.CraftingBagContainer;
import com.lothrazar.cyclic.item.craftingsimple.CraftingStickContainer;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.util.UtilString;
import java.util.Objects;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

@JeiPlugin
public class CyclicPluginJEI implements IModPlugin {

  private static final int PLAYER_INV_SIZE = 4 * 9;
  private static final ResourceLocation ID = new ResourceLocation(ModCyclic.MODID, "jei");

  @Override
  public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
  }

  @Override
  public ResourceLocation getPluginUid() {
    return ID;
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {
    IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
    registry.addRecipeCategories(new MelterRecipeCategory(guiHelper));
    registry.addRecipeCategories(new SolidifierRecipeCategory(guiHelper));
    registry.addRecipeCategories(new GenitemRecipeCategory(guiHelper));
    registry.addRecipeCategories(new GenfluidRecipeCategory(guiHelper));
    registry.addRecipeCategories(new PackagerRecipeCategory(guiHelper));
  }

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(new ItemStack(BlockRegistry.PACKAGER.get()), PackagerRecipeCategory.ID);
    registration.addRecipeCatalyst(new ItemStack(BlockRegistry.crafter), VanillaRecipeCategoryUid.CRAFTING);
    registration.addRecipeCatalyst(new ItemStack(ItemRegistry.crafting_bag), VanillaRecipeCategoryUid.CRAFTING);
    registration.addRecipeCatalyst(new ItemStack(ItemRegistry.crafting_stick), VanillaRecipeCategoryUid.CRAFTING);
    registration.addRecipeCatalyst(new ItemStack(BlockRegistry.WORKBENCH.get()), VanillaRecipeCategoryUid.CRAFTING);
    registration.addRecipeCatalyst(new ItemStack(BlockRegistry.MELTER), MelterRecipeCategory.ID);
    registration.addRecipeCatalyst(new ItemStack(BlockRegistry.SOLIDIFIER), SolidifierRecipeCategory.ID);
    registration.addRecipeCatalyst(new ItemStack(BlockRegistry.GENERATOR_ITEM.get()), GenitemRecipeCategory.ID);
    registration.addRecipeCatalyst(new ItemStack(BlockRegistry.GENERATOR_FLUID.get()), GenfluidRecipeCategory.ID);
  }

  @Override
  public void registerRecipes(IRecipeRegistration registry) {
    ClientWorld world = Objects.requireNonNull(Minecraft.getInstance().world);
    registry.addRecipes(world.getRecipeManager().getRecipesForType(IRecipeType.CRAFTING), PackagerRecipeCategory.ID);
    registry.addRecipes(world.getRecipeManager().getRecipesForType(CyclicRecipeType.MELTER), MelterRecipeCategory.ID);
    registry.addRecipes(world.getRecipeManager().getRecipesForType(CyclicRecipeType.SOLID), SolidifierRecipeCategory.ID);
    registry.addRecipes(world.getRecipeManager().getRecipesForType(CyclicRecipeType.GENERATOR_ITEM), GenitemRecipeCategory.ID);
    registry.addRecipes(world.getRecipeManager().getRecipesForType(CyclicRecipeType.GENERATOR_FLUID), GenfluidRecipeCategory.ID);
    for (Item item : ForgeRegistries.ITEMS.getValues()) {
      ItemStack st = new ItemStack(item);
      if (!st.isEmpty() && UtilString.isCyclic(item.getRegistryName())) {
        registry.addIngredientInfo(st, VanillaTypes.ITEM, new TranslationTextComponent(item.getTranslationKey() + ".guide"));
      }
    }
  }

  @Override
  public void registerGuiHandlers(IGuiHandlerRegistration registry) {
    registry.addRecipeClickArea(ScreenMelter.class,
        75, 20,
        40, 26, MelterRecipeCategory.ID);
    registry.addRecipeClickArea(ScreenSolidifier.class,
        75, 20,
        40, 26, SolidifierRecipeCategory.ID);
    registry.addRecipeClickArea(ScreenGeneratorDrops.class,
        10, 10,
        40, 66, GenitemRecipeCategory.ID);
    registry.addRecipeClickArea(ScreenGeneratorFluid.class,
        50, 8,
        20, 20, GenfluidRecipeCategory.ID);
    //    registry.addRecipeClickArea(ScreenPackager.class,
    //        10, 8,
    //        50, 50, VanillaRecipeCategoryUid.CRAFTING);
    registry.addRecipeClickArea(ScreenPackager.class,
        60, 0,
        60, 30, PackagerRecipeCategory.ID);
  }

  @Override
  public void registerRecipeTransferHandlers(IRecipeTransferRegistration registry) {
    registry.addRecipeTransferHandler(ContainerMelter.class, MelterRecipeCategory.ID,
        0, 2, //recipeSLotStart, recipeSlotCount
        2, PLAYER_INV_SIZE); // inventorySlotStart, inventorySlotCount
    registry.addRecipeTransferHandler(ContainerSolidifier.class, SolidifierRecipeCategory.ID,
        0, 3, //recipeSLotStart, recipeSlotCount
        4, PLAYER_INV_SIZE); // inventorySlotStart, inventorySlotCount
    registry.addRecipeTransferHandler(CraftingBagContainer.class, VanillaRecipeCategoryUid.CRAFTING,
        1, 9, //recipeSLotStart, recipeSlotCount
        10, PLAYER_INV_SIZE); // inventorySlotStart, inventorySlotCount
    registry.addRecipeTransferHandler(CraftingStickContainer.class, VanillaRecipeCategoryUid.CRAFTING,
        1, 9, //recipeSLotStart, recipeSlotCount
        10, PLAYER_INV_SIZE); // inventorySlotStart, inventorySlotCount
    registry.addRecipeTransferHandler(ContainerWorkbench.class, VanillaRecipeCategoryUid.CRAFTING,
        1, 9, //recipeSLotStart, recipeSlotCount
        10, PLAYER_INV_SIZE);
    registry.addRecipeTransferHandler(ContainerGeneratorDrops.class, GenitemRecipeCategory.ID,
        0, 1, //recipeSLotStart, recipeSlotCount
        1, PLAYER_INV_SIZE); // inventorySlotStart, inventorySlotCount
    //JEI bug https://github.com/Lothrazar/Cyclic/issues/1742
    //    registry.addRecipeTransferHandler(ContainerCrafter.class, VanillaRecipeCategoryUid.CRAFTING,
    //        10, 9, //recipeSLotStart, recipeSlotCount
    //        30, PLAYER_INV_SIZE); // inventorySlotStart, inventorySlotCount
  }
}
