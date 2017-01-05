package info.tritusk.inductivemachine

import ic2.api.recipe.IMachineRecipeManager
import ic2.api.recipe.IRecipeInput
import ic2.api.recipe.Recipes
import ic2.api.recipe.RecipeInputItemStack
import ic2.api.recipe.RecipeOutput
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.nbt.NBTTagCompound

class TileInductionalCompressor : TileGenericInductionMachine(Recipes.compressor)

class TileInductionalEFurnace : TileGenericInductionMachine(FuranceRecipeWrapper)

class TileInductionalExtractor : TileGenericInductionMachine(Recipes.extractor)

class TileInductionalMacerator : TileGenericInductionMachine(Recipes.macerator)

object FuranceRecipeWrapper : IMachineRecipeManager {
	
	private val smeltingListCache : MutableList<IMachineRecipeManager.RecipeIoContainer> = mutableListOf()
	
	override fun addRecipe(input : IRecipeInput, metadata : NBTTagCompound, replace : Boolean, vararg outputs : ItemStack) = false

	// adjustInput was discarded because it is somehow not possible
	override fun getOutputFor(input : ItemStack?, adjustInput : Boolean) : RecipeOutput? {
		val output : ItemStack? = FurnaceRecipes.instance().getSmeltingResult(input)
		return if (output == null)
			null
		else
			RecipeOutput(NBTTagCompound(), output);
	}

	// Very slow.
	override fun getRecipes() : Iterable<IMachineRecipeManager.RecipeIoContainer> {
		if (smeltingListCache.isEmpty()) {
			FurnaceRecipes.instance().smeltingList.forEach {
				smeltingListCache.add(IMachineRecipeManager.RecipeIoContainer(RecipeInputItemStack(it.key), RecipeOutput(NBTTagCompound(), it.value)))
			}
		}
		return smeltingListCache.asIterable()
	}
	
	override fun isIterable() = true
	
	fun clearCache() = smeltingListCache.clear()
}
