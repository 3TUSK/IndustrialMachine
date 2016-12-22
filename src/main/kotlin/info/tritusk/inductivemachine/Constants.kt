package info.tritusk.inductivemachine

import net.minecraft.util.ResourceLocation

const val MOD_ID = "inductivemachine"

@JvmField val BLOCK_ID: ResourceLocation? = ResourceLocation("inductivemachine", "machine")
@JvmField val GUI: ResourceLocation? = ResourceLocation("inductivemachine", "gui/inductive_machine.xml")

@JvmField val BLOCK_VALUES = BlockInductional.values() 