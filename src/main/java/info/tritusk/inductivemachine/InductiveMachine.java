package info.tritusk.inductivemachine;

import ic2.api.event.TeBlockFinalCallEvent;
import ic2.core.block.TeBlockRegistry;
import info.tritusk.inductivemachine.block.BlockInductional;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = "inductivemachine", name = "Inductive Machine", version = "0.0.1", useMetadata = true, dependencies = "required-after:IC2@[2.6.76,)")
public class InductiveMachine {
	
	static {
		MinecraftForge.EVENT_BUS.register(InductiveMachine.class);
	}
	
	@Mod.Instance("inductivemachine")
	public static InductiveMachine instance;
	
	@SubscribeEvent
	public static void onIC2TeRegFinalCall(TeBlockFinalCallEvent event) {
		TeBlockRegistry.addAll(BlockInductional.class, BlockInductional.ID);
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		TeBlockRegistry.get(BlockInductional.ID).setCreativeTab(new CreativeTabs("inductive_machine") {
			@Override
			public Item getTabIconItem() {
				return Item.getItemFromBlock(TeBlockRegistry.get(BlockInductional.ID));
			}		
		});
	}

}
