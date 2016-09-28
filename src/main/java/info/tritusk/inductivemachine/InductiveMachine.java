package info.tritusk.inductivemachine;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = "inductivemachine", name = "Inductive Machine", version = "0.0.1", useMetadata = true)
public class InductiveMachine {
	
	@Mod.Instance("inductivemachine")
	public static InductiveMachine instance;
	
	@SidedProxy(serverSide = "info.tritusk.inductivemachine.common.InductiveProxy", clientSide = "info.tritusk.inductivemachine.common.InductiveProxy")
	public static InductiveProxy proxy;
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

}
