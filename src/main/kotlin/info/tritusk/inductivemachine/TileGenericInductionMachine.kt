package info.tritusk.inductivemachine

import java.util.EnumSet
import ic2.api.recipe.IMachineRecipeManager
import ic2.api.recipe.RecipeOutput
import ic2.core.ContainerBase
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
import net.minecraft.client.gui.GuiScreen
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

const val MAX_HEAT = 100
const val ENERGY_CONSUMPTION_PER_TICK = 2500.0

open class TileGenericInductionMachine
/**
 * Max energy = 50k EU, Tier = 2, allowRedstoneControl = true
 */
protected constructor(recipeManager: IMachineRecipeManager?) : TileEntityElectricMachine(50000, 2, true), IHasGui, IGuiValueProvider, IUpgradableBlock {
	
	val inputs: InvSlotProcessableGeneric = InvSlotProcessableGeneric(this, "input", 6, recipeManager)
	val outputs: InvSlotOutput = InvSlotOutput(this, "output", 6)
	val upgrades: InvSlotUpgrade = InvSlotUpgrade(this, "upgrade", 2)
	
	@GuiSynced protected var heat: Int = 0
	@GuiSynced protected var progress: Int = 0

	override fun getEnergy() = this.energy?.energy ?: 0.0
	
	override fun useEnergy(amount : Double) = this.energy?.useEnergy(amount) ?: false
	
	override fun getUpgradableProperties() = EnumSet.of(UpgradableProperty.RedstoneSensitive, UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing)

	override fun getGuiValue(id: String?) = when (id) {
		"progress" -> progress.toDouble()
		else -> 0.0
	}

	override fun getGui(player: EntityPlayer?, isAdmin: Boolean) = try {
			DynamicGui.create(this, player, GuiParser.parse(GUI, TileGenericInductionMachine::class.java))
		} catch (e: Exception) {
			throw RuntimeException(e)
		}
	

	override fun getGuiContainer(player: EntityPlayer?) = try {
			DynamicContainer.create(this, player, GuiParser.parse(GUI, TileGenericInductionMachine::class.java))
		} catch (e: Exception) {
			throw RuntimeException(e)
		}

	override fun onGuiClosed(player: EntityPlayer?) = Unit

	override fun updateEntityServer() {
		if (this.energy.canUseEnergy(ENERGY_CONSUMPTION_PER_TICK)) {
			energy.useEnergy(ENERGY_CONSUMPTION_PER_TICK)
			++progress
		}
		if (progress >= 100) {
			this.outputs.add(processAll().items)
			outputs.organize()
		}
	}

	private fun processAll(): RecipeOutput = RecipeOutput(NBTTagCompound(), mutableListOf<ItemStack>().run {
		inputs.forEach {
			inputs.recipeManager?.getOutputFor(it, true)?.items?.filterNotNullTo(this)
		}
		this
	})

	override fun readFromNBT(tag: NBTTagCompound) = super.readFromNBT(tag.also {
		this.heat = tag.getInteger("heat")
		this.progress = tag.getInteger("progress")
	})

	override fun writeToNBT(tag: NBTTagCompound) = super.writeToNBT(tag.also {
		it.setInteger("heat", this.heat)
		it.setInteger("progress", this.progress)
	})

}