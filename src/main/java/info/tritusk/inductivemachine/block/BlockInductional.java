package info.tritusk.inductivemachine.block;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import ic2.core.block.BlockTileEntity;
import ic2.core.block.ITeBlock;
import ic2.core.block.TileEntityBlock;
import ic2.core.item.block.ItemBlockTileEntity;
import ic2.core.ref.TeBlock.DefaultDrop;
import ic2.core.ref.TeBlock.HarvestTool;
import ic2.core.ref.TeBlock.ITePlaceHandler;
import ic2.core.util.Util;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public enum BlockInductional implements ITeBlock {
	
	EFURNACE(0, TileInductionalEFurnace.class),
	MACERATOR(1, TileInductionalMacerator.class),
	COMPRESSOR(2, TileInductionalCompressor.class),
	EXTRACTOR(3, TileInductionalExtractor.class);
	
	public static final ResourceLocation ID = new ResourceLocation("inductivemachine", "machine");
	public static final ResourceLocation GUI = new ResourceLocation("inductivemachine", "gui/inductive_machine.xml");
	
	public static final BlockInductional[] VALUES = BlockInductional.values();
	
	private final Class<? extends TileEntityBlock> teClass;
	private final int meta;
	private ITePlaceHandler handler;
	
	private BlockInductional(int meta, Class<? extends TileEntityBlock> teClass) {
		this.meta = meta;
		this.teClass = teClass;
		TileEntity.addMapping(this.teClass, "inductivemachine_" + this.name().toLowerCase(Locale.ENGLISH));
	}

	@Override
	public int getId() {
		return meta;
	}

	@Override
	public String getName() {
		return this.name().toLowerCase(Locale.ENGLISH);
	}

	@Override
	public void addSubBlocks(List<ItemStack> list, BlockTileEntity blockTe, ItemBlockTileEntity itemTe, CreativeTabs tab) {
		for (BlockInductional blockDef : VALUES) {
			if (blockDef.hasItem())
				list.add(blockTe.getItemStack(blockDef));
		}
	}

	@Override
	public boolean allowWrenchRotating() {
		return true;
	}

	@Override
	public DefaultDrop getDefaultDrop() {
		return DefaultDrop.AdvMachine;
	}

	@Override
	public TileEntityBlock getDummyTe() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getExplosionResistance() {
		return 20.0F;
	}

	@Override
	public float getHardness() {
		return 20.0F;
	}

	@Override
	public HarvestTool getHarvestTool() {
		return HarvestTool.None;
	}

	@Override
	public ResourceLocation getIdentifier() {
		return ID;
	}

	@Override
	public ITePlaceHandler getPlaceHandler() {
		return this.handler;
	}

	@Override
	public EnumRarity getRarity() {
		return EnumRarity.UNCOMMON;
	}

	@Override
	public Set<EnumFacing> getSupportedFacings() {
		return Util.horizontalFacings;
	}

	@Override
	public Class<? extends TileEntityBlock> getTeClass() {
		return this.teClass;
	}

	@Override
	public boolean hasActive() {
		return true;
	}

	@Override
	public boolean hasItem() {
		return this.teClass != null;
	}

	@Override
	public void setPlaceHandler(ITePlaceHandler handler) {
		if (this.handler != null)
			this.handler = handler;
	}

}
