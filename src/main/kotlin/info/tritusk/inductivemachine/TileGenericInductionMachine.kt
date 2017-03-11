package info.tritusk.inductivemachine

import ic2.api.recipe.IMachineRecipeManager
import ic2.api.recipe.RecipeOutput
import ic2.core.IHasGui
import ic2.core.block.invslot.InvSlotOutput
import ic2.core.block.invslot.InvSlotProcessableGeneric
import ic2.core.block.invslot.InvSlotUpgrade
import ic2.core.block.machine.tileentity.TileEntityElectricMachine
import ic2.core.gui.dynamic.DynamicContainer
import ic2.core.gui.dynamic.DynamicGui
import ic2.core.gui.dynamic.GuiParser
import ic2.core.gui.dynamic.IGuiValueProvider
import ic2.core.network.GuiSynced
import ic2.core.upgrade.IUpgradableBlock
import ic2.core.upgrade.UpgradableProperty
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

abstract class TileGenericInductionMachine
/**
 * Max energy = 50k EU, Tier = 2, allowRedstoneControl = true
 */
protected constructor(val recipeManager: IMachineRecipeManager): TileEntityElectricMachine(50000, 2, true), IHasGui, IGuiValueProvider, IUpgradableBlock {

	val inputs: InvSlotProcessableGeneric = InvSlotProcessableGeneric(this, "input", 6, recipeManager)
	val outputs: InvSlotOutput = InvSlotOutput(this, "output", 6)
	val upgrades: InvSlotUpgrade = InvSlotUpgrade(this, "upgrade", 2)
	
	@GuiSynced protected var heat: Int = 0
	@GuiSynced protected var progress: Int = 0

	override fun getEnergy() = this.energy?.energy ?: 0.0
	
	override fun useEnergy(amount: Double) = this.energy?.useEnergy(amount) ?: false
	
	override fun getUpgradableProperties() = setOf(UpgradableProperty.RedstoneSensitive, UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing)

	override fun getGuiValue(id: String?) = when (id) {
		"progress" -> progress.toDouble()
		else -> 0.0
	}

	override fun getGui(player: EntityPlayer?, isAdmin: Boolean) =
			DynamicGui.create(this, player, GuiParser.parse(InductiveMachine.GUI, TileGenericInductionMachine::class.java))

	override fun getGuiContainer(player: EntityPlayer?) =
			DynamicContainer.create(this, player, GuiParser.parse(InductiveMachine.GUI, TileGenericInductionMachine::class.java))

	override fun onGuiClosed(player: EntityPlayer?) = Unit

	override fun updateEntityServer() {
		val energyCost = 2500 * this.upgrades.energyDemandMultiplier // 2500.0 is the base energy cost
		if (this.energy.canUseEnergy(energyCost)) {
			energy.useEnergy(energyCost)
			++progress
		}
		if (progress >= 100) { // Maximum progress is always locked at 100
			this.outputs.add(processAll().items)
			outputs.organize()
		}
	}

	private fun processAll(): RecipeOutput = RecipeOutput(NBTTagCompound(), mutableListOf<ItemStack>().run {
		inputs.forEach { recipeManager.getOutputFor(it, true)?.items?.filterNotNullTo(this) }
		this
	})

	override fun readFromNBT(tag: NBTTagCompound) = super.readFromNBT(tag.also {
		this.heat = it.getInteger("heat")
		this.progress = it.getInteger("progress")
	})

	override fun writeToNBT(tag: NBTTagCompound) = super.writeToNBT(tag.also {
		it.setInteger("heat", this.heat)
		it.setInteger("progress", this.progress)
	})

}