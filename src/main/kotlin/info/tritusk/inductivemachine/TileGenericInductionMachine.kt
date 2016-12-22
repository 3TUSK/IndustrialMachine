package info.tritusk.inductivemachine

import java.io.IOException
import java.util.ArrayList
import java.util.EnumSet
import org.xml.sax.SAXException
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
const val ENERGY_CONSUMPTION_PER_TICK = 2500

open class TileGenericInductionMachine
/**
 * Max energy = 50k EU, Tier = 2, allowRedstoneControl = true
 */
protected constructor(recipeManager: IMachineRecipeManager?) : TileEntityElectricMachine(50000, 2, true), IHasGui, IGuiValueProvider, IUpgradableBlock {
	
	val inputs: InvSlotProcessableGeneric
	val outputs: InvSlotOutput
	val upgrades: InvSlotUpgrade
	
	@GuiSynced protected var heat: Byte = 0
	@GuiSynced protected var progress: Byte = 0

	init {
		this.inputs = InvSlotProcessableGeneric(this, "input", 6, recipeManager)
		this.outputs = InvSlotOutput(this, "output", 6)
		this.upgrades = InvSlotUpgrade(this, "upgrade", 2)
	}

	// If energy is null then something goes extremely wrong with ic2 internal
	override fun getEnergy() = this.energy!!.getEnergy()
	
	// Same as above
	override fun useEnergy(amount : Double) = this.energy!!.useEnergy(amount)
	
	override fun getUpgradableProperties() = EnumSet.of(UpgradableProperty.RedstoneSensitive, UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing)

	override fun getGuiValue(id: String?): Double {
		return when (id) {
			"progress" -> progress.toDouble()
			else -> 0.0
		}
	}

	override fun getGui(player: EntityPlayer?, isAdmin: Boolean): GuiScreen {
		try {
			return DynamicGui.create(this, player, GuiParser.parse(GUI, TileGenericInductionMachine::class.java))
		} catch (e: IOException) {
			throw RuntimeException(e)
		} catch (e: SAXException) {
			throw RuntimeException(e)
		}
	}

	override fun getGuiContainer(player: EntityPlayer?): ContainerBase<*> {
		try {
			return DynamicContainer.create(this, player, GuiParser.parse(GUI, TileGenericInductionMachine::class.java))
		} catch (e: IOException) {
			throw RuntimeException(e)
		} catch (e: SAXException) {
			throw RuntimeException(e)
		}
	}

	override fun onGuiClosed(player: EntityPlayer?) = Unit

	override fun updateEntityServer() {
		if (this.energy.canUseEnergy(ENERGY_CONSUMPTION_PER_TICK.toDouble())) {
			energy.useEnergy(ENERGY_CONSUMPTION_PER_TICK.toDouble())
			this.progress.unaryPlus() //Wat
		}
		if (progress >= 100) {
			val outputs = processAll()
			this.outputs.add(outputs!!.items)
		}
		outputs.organize()
	}

	private fun processAll(): RecipeOutput? {
		val stacks : MutableList<ItemStack> = mutableListOf()
		for (index in 0..5) {
			val input = inputs.get(index)
			assert(input != null)
			val output = this.inputs.recipeManager?.getOutputFor(input, true)
			if (output != null)
				stacks.addAll(output.items)
		}
		return RecipeOutput(NBTTagCompound(), stacks)
	}

	override fun readFromNBT(tag: NBTTagCompound) {
		super.readFromNBT(tag)
		this.heat = tag.getByte("heat")
		this.progress = tag.getByte("progress")
	}

	override fun writeToNBT(tag: NBTTagCompound): NBTTagCompound? {
		tag.setByte("heat", this.heat)
		tag.setByte("progress", this.progress)
		return super.writeToNBT(tag)
	}

}