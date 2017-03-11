package info.tritusk.inductivemachine

import java.util.Locale
import ic2.core.block.BlockTileEntity
import ic2.core.block.ITeBlock
import ic2.core.block.TileEntityBlock
import ic2.core.item.block.ItemBlockTileEntity
import ic2.core.ref.TeBlock
import ic2.core.util.Util
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity

enum class BlockInductional(val theTeClass: Class<out TileEntityBlock>) : ITeBlock {
	INDUSTRIAL_EFURNACE(TileInductionalEFurnace::class.java),
	INDUSTRIAL_MACERATOR(TileInductionalMacerator::class.java),
	INDUSTRIAL_COMPRESSOR(TileInductionalCompressor::class.java),
	INDUSTRIAL_EXTRACTOR(TileInductionalExtractor::class.java);

	init { TileEntity.addMapping(theTeClass, "inductivemachine_" + this.getName()) }

	//Named so because Kotlin has getter and setter which will conflict with ITeBlock interface
	var thePlaceHandler: TeBlock.ITePlaceHandler? = null

	override fun getId() = this.ordinal
	
	override fun getName() = this.name.toLowerCase(Locale.ENGLISH)

	override fun addSubBlocks(list: MutableList<ItemStack>, blockTe: BlockTileEntity,
			itemTe: ItemBlockTileEntity, tab: CreativeTabs) =
			BlockInductional.values().forEach { list.add(blockTe.getItemStack(it)) }

	override fun allowWrenchRotating() = true

	override fun getDefaultDrop() = TeBlock.DefaultDrop.AdvMachine

	override fun getDummyTe() = null
	
	override fun getExplosionResistance() = 20.0F

	override fun getHardness() = 20.0F
	
	override fun getHarvestTool() = TeBlock.HarvestTool.None
	
	override fun getIdentifier() = InductiveMachine.BLOCK_ID
	
	override fun getRarity() = EnumRarity.UNCOMMON

	override fun getSupportedFacings() = Util.horizontalFacings

	override fun getTeClass() = this.theTeClass
	
	override fun hasActive() = true

	override fun hasItem() = true

	override fun getMaterial() = Material.IRON
	
	override fun getPlaceHandler() = this.thePlaceHandler
	
	override fun setPlaceHandler(handler: TeBlock.ITePlaceHandler?) {
		this.thePlaceHandler = handler
	}

	override fun isTransparent() = false

}