package info.tritusk.inductivemachine

import ic2.api.event.TeBlockFinalCallEvent as TeBlockReg
import ic2.core.block.TeBlockRegistry
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

const val MODID = "inductivemachine"
const val NAME = "Inductive Machine"

@JvmField val BLOCK_ID: ResourceLocation = ResourceLocation(MODID, "machine")
@JvmField val GUI: ResourceLocation = ResourceLocation(MODID, "gui/inductive_machine.xml")

@JvmField val BLOCK_VALUES = BlockInductional.values() 

@Mod(modid = MODID, name = NAME, version = "0.0.1", useMetadata = true,
	dependencies = "required-after:IC2@[2.6.134,);required-after:forgelin", modLanguage = "kotlin",
	modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object InductiveMachine {

	init {
		MinecraftForge.EVENT_BUS.register(object: Any() {
			@SubscribeEvent fun onIC2TeReg(@Suppress("UNUSED_PARAMETER") event: TeBlockReg) = TeBlockRegistry.addAll(BlockInductional::class.java, BLOCK_ID)
		})
	}

}
