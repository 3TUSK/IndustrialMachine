package info.tritusk.inductivemachine.block;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.xml.sax.SAXException;

import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.RecipeOutput;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotProcessableGeneric;
import ic2.core.block.invslot.InvSlotUpgrade;
import ic2.core.block.machine.tileentity.TileEntityElectricMachine;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.DynamicGui;
import ic2.core.gui.dynamic.GuiParser;
import ic2.core.gui.dynamic.IGuiValueProvider;
import ic2.core.network.GuiSynced;
import ic2.core.upgrade.IUpgradableBlock;
import ic2.core.upgrade.UpgradableProperty;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileGenericInductionMachine extends TileEntityElectricMachine implements IHasGui, IGuiValueProvider, IUpgradableBlock {
	
	public static final byte MAX_HEAT = 100;
	
	public static final int ENERGY_CONSUMPTION_PER_TICK = 2500;
	
	public final InvSlotProcessableGeneric inputs;
	public final InvSlotOutput outputs;
	public final InvSlotUpgrade upgrades;
	@GuiSynced
	protected byte heat, progress;
	
	/**
	 * Max energy = 50k EU, Tier = 2, allowRedstoneControl = true
	 */
	protected TileGenericInductionMachine(IMachineRecipeManager recipeManager) {
		super(50000, 2, true);
		this.inputs = new InvSlotProcessableGeneric(this, "input", 6, recipeManager);
		this.outputs = new InvSlotOutput(this, "output", 6);
		this.upgrades = new InvSlotUpgrade(this, "upgrade", 2);
	}

	@Override
	public double getEnergy() {
		return this.energy.getEnergy();
	}

	@Override
	public Set<UpgradableProperty> getUpgradableProperties() {
		return EnumSet.of(UpgradableProperty.RedstoneSensitive, UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing);
	}

	@Override
	public boolean useEnergy(double amount) {
		return this.energy.useEnergy(amount);
	}

	@Override
	public double getGuiValue(String id) {
		switch (id) {
			case "progress": return progress;
			default: return 0D;
		}
	}

	@Override
	public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
		try {
			return DynamicGui.create(this, player, GuiParser.parse(BlockInductional.GUI, TileGenericInductionMachine.class));
		} catch (IOException | SAXException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ContainerBase<?> getGuiContainer(EntityPlayer player) {
		try {
			return DynamicContainer.create(this, player, GuiParser.parse(BlockInductional.GUI,TileGenericInductionMachine.class));
		} catch (IOException | SAXException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onGuiClosed(EntityPlayer player) {
		
	}
	
	@Override
	public void updateEntityServer() {
		if (this.energy.canUseEnergy(ENERGY_CONSUMPTION_PER_TICK)) {
			energy.useEnergy(ENERGY_CONSUMPTION_PER_TICK);
			this.progress += 1;
		}
		
		if (progress >= 100) {
			RecipeOutput outputs = processAll();
			this.outputs.add(outputs.items);
		}
		
		outputs.organize();
	}
	
	private RecipeOutput processAll() {
		List<ItemStack> stacks = new ArrayList<>();
		for (int index = 0; index < 6; index++) {
			ItemStack input = inputs.get(index);
			assert input != null;
			stacks.addAll(this.inputs.recipeManager.getOutputFor(input, true).items);
		}
		return new RecipeOutput(new NBTTagCompound(), stacks);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.heat = tag.getByte("heat");
		this.progress = tag.getByte("progress");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setByte("heat", this.heat);
		tag.setByte("progress", this.progress);
		return tag;
	}

}
