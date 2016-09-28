package info.tritusk.inductivemachine.block;

import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;

public class TileInductionalEFurnace extends TileGenericInductionMachine {

	public TileInductionalEFurnace() {
		super(new IMachineRecipeManager() {

			@Override
			public boolean addRecipe(IRecipeInput input, NBTTagCompound metadata, boolean replace, ItemStack... outputs) {
				return false;
			}

			@Override
			public RecipeOutput getOutputFor(ItemStack input, boolean adjustInput) {
				ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input);
				if (adjustInput) {
					//TODO: make sure the adjustInput work
				}
				return new RecipeOutput(new NBTTagCompound(), output);
			}

			@Override
			public Iterable<RecipeIoContainer> getRecipes() {
				throw new UnsupportedOperationException("Iterating furace recipes is currently unavailable");
			}

			@Override
			public boolean isIterable() {
				return false;
			}
			
		});
	}

}
