package info.tritusk.inductivemachine

import ic2.core.block.TeBlockRegistry
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import ic2.api.event.TeBlockFinalCallEvent as TeBlockReg

@Mod(modid = "inductivemachine", useMetadata = true, modLanguage = "kotlin", modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object InductiveMachine {

    @JvmField val BLOCK_ID: ResourceLocation = ResourceLocation("inductivemachine", "machine")
    @JvmField val GUI: ResourceLocation = ResourceLocation("inductivemachine", "gui/inductive_machine.xml")

    init {
        MinecraftForge.EVENT_BUS.register(object : Any() {
            @SubscribeEvent fun onIC2TeReg(event: TeBlockReg) = TeBlockRegistry.addAll(BlockInductional::class.java, BLOCK_ID)
        })
    }

}
