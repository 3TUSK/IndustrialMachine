package info.tritusk.inductivemachine

import ic2.api.event.TeBlockFinalCallEvent
import ic2.core.block.TeBlockRegistry
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod(
		modid = "inductivemachine",
		name = "Inductive Machine",
		version = "0.0.1",
		useMetadata = true,
		dependencies = "required-after:forgelin;required-after:IC2@[2.6.76,)",
		modLanguage = "kotlin",
		modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object InductiveMachine {
	
	@JvmStatic
	@Mod.InstanceFactory
	fun getInstance() = this
	
	init {
		MinecraftForge.EVENT_BUS.register(object : Any() {
			@SubscribeEvent fun onIC2TeReg(event: TeBlockFinalCallEvent) = TeBlockRegistry.addAll(BlockInductional::class.java, BLOCK_ID)
		})
	}
	
	@Mod.EventHandler
	fun postInit(event: FMLPostInitializationEvent?) {
		TeBlockRegistry.get(BLOCK_ID).setCreativeTab(object : CreativeTabs("inductive_machine") {
			override fun getTabIconItem() = Item.getItemFromBlock(TeBlockRegistry.get(BLOCK_ID))
		})
	}

}