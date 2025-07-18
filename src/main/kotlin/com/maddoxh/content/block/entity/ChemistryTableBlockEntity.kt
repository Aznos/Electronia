package com.maddoxh.content.block.entity

import com.maddoxh.content.recipe.ChemistryTableRecipeInput
import com.maddoxh.content.recipe.PressRecipeInput
import com.maddoxh.content.screen.chemistryTable.ChemistryTableScreenHandler
import com.maddoxh.content.screen.crankpress.CrankPressScreenHandler
import com.maddoxh.content.screen.heater.HeaterScreenHandler
import com.maddoxh.registry.ModBlockEntities
import com.maddoxh.registry.ModRecipes
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.fluid.Fluids
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.RegistryWrapper
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos

class ChemistryTableBlockEntity(pos: BlockPos, state: BlockState)
    : BlockEntity(ModBlockEntities.CHEMISTRY_TABLE, pos, state), Inventory, ExtendedScreenHandlerFactory<BlockPos>
{
    var temperature: Double = 25.0
    val maxTemp: Double = 200.0
    @Suppress("UnusedPrivateProperty") // will be used later
    private var tickCounter = 0
    val inv: DefaultedList<ItemStack> = DefaultedList.ofSize(3, ItemStack.EMPTY)

    val tank = object : SingleVariantStorage<FluidVariant>() {
        override fun getBlankVariant() = FluidVariant.blank()
        override fun getCapacity(variant: FluidVariant): Long = FluidConstants.BUCKET
    }

    val fluidInputSlot = 0
    val itemInputSlot = 1
    val itemOutputSlot = 2

    fun serverTick() {
        val fluidStack = inv[fluidInputSlot]
        if(fluidStack.item == Items.WATER_BUCKET && tank.amount == 0L) {
            tank.variant = FluidVariant.of(Fluids.WATER)
            tank.amount = FluidConstants.BUCKET
            inv[fluidInputSlot] = ItemStack(Items.BUCKET)

            markDirty()
            sync()
        }

        tryCraft()
    }

    private fun sync() {
        world?.let { w ->
            val pkt = BlockEntityUpdateS2CPacket.create(this)
            w.server?.playerManager?.playerList
                ?.filterIsInstance<ServerPlayerEntity>()
                ?.filter { it.world == w }
                ?.forEach { it.networkHandler.sendPacket(pkt) }
        }
    }

    private fun tryCraft() {
        if(!canCraft()) return

        val w = world ?: return
        val s = w.server ?: return

        val recipeInput = ChemistryTableRecipeInput(inv[itemInputSlot], tank.variant, tank.amount)
        val recipe = s.recipeManager.getFirstMatch(ModRecipes.chemistryTableType, recipeInput, w).get().value()

        inv[itemInputSlot].decrement(1)
        if(recipe.fluid != null && recipe.fluidReq > 0) tank.amount -= recipe.fluidReq

        val out = inv[itemOutputSlot]
        if(out.isEmpty) {
            inv[itemOutputSlot] = recipe.result.copy()
        } else {
            out.increment(recipe.result.count)
        }

        markDirty()
        sync()
    }

    private fun canCraft(): Boolean {
        val w = world ?: return false
        if(w.isClient) return false

        val inputStack = inv[itemInputSlot]
        if(inputStack.isEmpty) return false

        val recipeInput = ChemistryTableRecipeInput(inputStack, tank.variant, tank.amount)
        val match = w.server?.recipeManager?.getFirstMatch(ModRecipes.chemistryTableType, recipeInput, w)
            ?: return false
        if(!match.isPresent) return false

        val recipe = match.get().value()
        if(recipe.fluid != null) {
            if(tank.variant.isBlank) return false
            if(tank.variant.fluid != recipe.fluid.fluid) return false
            if(tank.amount < recipe.fluidReq) return false
        }

        val res = recipe.result
        val outStack = inv[itemOutputSlot]

        if(outStack.isEmpty) return true
        if(!outStack.isStackable || !ItemStack.areItemsEqual(outStack, res)) return false

        return outStack.count + res.count <= outStack.maxCount
    }

    override fun size() = inv.size
    override fun isEmpty() = inv.all { it.isEmpty }
    override fun getStack(slot: Int) = inv[slot]
    override fun removeStack(slot: Int, amount: Int): ItemStack = Inventories.splitStack(inv, slot, amount)
    override fun removeStack(slot: Int): ItemStack = Inventories.removeStack(inv, slot)
    override fun getMaxCountPerStack(): Int = 1

    override fun setStack(slot: Int, stack: ItemStack) {
        inv[slot] = stack
    }

    override fun canPlayerUse(player: PlayerEntity) = world?.getBlockEntity(pos) === this &&
            player.squaredDistanceTo(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5) <= 64.0

    override fun clear() = inv.clear()
    override fun getDisplayName(): Text = Text.literal("Chemistry Table")

    override fun createMenu(
        syncId: Int,
        playerInventory: PlayerInventory,
        player: PlayerEntity
    ) = ChemistryTableScreenHandler(syncId, playerInventory, this)

    override fun getScreenOpeningData(p0: ServerPlayerEntity?): BlockPos? {
        return this.pos
    }

    override fun writeNbt(nbt: NbtCompound?, registryLookup: RegistryWrapper.WrapperLookup?) {
        super.writeNbt(nbt, registryLookup)
        Inventories.writeNbt(nbt, inv, registryLookup)

        nbt?.putDouble("Temp", temperature)
        SingleVariantStorage.writeNbt(tank, FluidVariant.CODEC, nbt, registryLookup)
    }

    override fun readNbt(nbt: NbtCompound?, registryLookup: RegistryWrapper.WrapperLookup?) {
        super.readNbt(nbt, registryLookup)
        if(nbt != null) {
            Inventories.readNbt(nbt, inv, registryLookup)
        }

        temperature = nbt?.getDouble("Temp") ?: 25.0
        SingleVariantStorage.readNbt(tank, FluidVariant.CODEC, FluidVariant::blank, nbt, registryLookup)
    }

    override fun toUpdatePacket(): BlockEntityUpdateS2CPacket = BlockEntityUpdateS2CPacket.create(this)
    override fun toInitialChunkDataNbt(registryLookup: RegistryWrapper.WrapperLookup?): NbtCompound =
        createNbt(registryLookup)
}