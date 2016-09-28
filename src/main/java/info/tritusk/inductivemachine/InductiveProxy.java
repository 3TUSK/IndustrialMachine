package info.tritusk.inductivemachine;

import ic2.api.event.TeBlockFinalCallEvent;
import ic2.core.block.TeBlockRegistry;
import info.tritusk.inductivemachine.block.BlockInductional;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InductiveProxy {
	
	static {
		MinecraftForge.EVENT_BUS.register(InductiveProxy.class);
	}
	
	@SubscribeEvent
	public static void onIC2TeRegFinalCall(TeBlockFinalCallEvent event) {
		TeBlockRegistry.addAll(BlockInductional.class, BlockInductional.ID);
	}

	public void postInit(FMLPostInitializationEvent event) {
		
	}

}
