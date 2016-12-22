package info.tritusk.inductivemachine

import java.util.Locale
import java.util.Optional
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
import net.minecraft.util.EnumFacing

enum class BlockInductional private constructor(teClass: Class<out TileEntityBlock>) : ITeBlock {
	EFURNACE(TileInductionalEFurnace::class.java),
	MACERATOR(TileInductionalMacerator::class.java),
	COMPRESSOR(TileInductionalCompressor::class.java),
	EXTRACTOR(TileInductionalExtractor::class.java);

	val teClass: Class<out TileEntityBlock>
	
	var placeHandler: TeBlock.ITePlaceHandler? = null

	init {
		this.teClass = teClass
		TileEntity.addMapping(this.teClass, "inductivemachine_" + this.name.toLowerCase(Locale.ENGLISH))
	}

	override fun getId() = this.ordinal
	
	override fun getName() = this.name.toLowerCase(Locale.ENGLISH)

	override fun addSubBlocks(list: MutableList<ItemStack>, blockTe: BlockTileEntity, itemTe: ItemBlockTileEntity, tab: CreativeTabs) {
		BLOCK_VALUES.forEach { if (it.hasItem()) list.add(blockTe.getItemStack(it)) }
	}

	override fun allowWrenchRotating() = true

	override fun getDefaultDrop() = TeBlock.DefaultDrop.AdvMachine

	override fun getDummyTe() = null
	
	override fun getExplosionResistance() = 20.0F

	override fun getHardness() = 20.0F
	
	override fun getHarvestTool() = TeBlock.HarvestTool.None
	
	override fun getIdentifier() = BLOCK_ID
	
	override fun getRarity() = EnumRarity.UNCOMMON

	override fun getSupportedFacings() =  Util.horizontalFacings

	override fun getTeClass() = this.teClass
	
	override fun hasActive() = true

	override fun hasItem() = true

	override fun getMaterial() = Material.IRON
	
	override fun getPlaceHandler() = this.placeHandler
	
	override fun setPlaceHandler(handler : TeBlock.ITePlaceHandler) {
		this.placeHandler = Optional.of(handler).orElse(this.placeHandler)
	} 

}