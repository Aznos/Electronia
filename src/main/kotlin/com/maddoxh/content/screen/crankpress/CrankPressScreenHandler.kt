package com.maddoxh.content.screen.crankpress

import com.maddoxh.registry.ModScreenHandlers.CRANK_PRESS_SCREEN_HANDLER
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot
import net.minecraft.util.math.BlockPos

class CrankPressScreenHandler(syncID: Int, playerInventory: PlayerInventory?, be: BlockEntity?) :
    net.minecraft.screen.ScreenHandler(CRANK_PRESS_SCREEN_HANDLER, syncID) {
    private val inventory: Inventory

    constructor(syncID: Int, playerInventory: PlayerInventory, pos: BlockPos?) : this(
        syncID,
        playerInventory,
        playerInventory.player.world.getBlockEntity(pos)
    )

    init {
        checkSize(be as Inventory, 1)
        this.inventory = be as Inventory
        this.addSlot(Slot(inventory, 0, 80, 35))

        addPlayerInventory(playerInventory)
        addPlayerHotbar(playerInventory)
    }

    override fun quickMove(player: PlayerEntity?, invSlot: Int): ItemStack? {
        var newStack = ItemStack.EMPTY
        val slot = this.slots[invSlot]
        if(slot.hasStack()) {
            val originalStack = slot.stack
            newStack = originalStack.copy()

            if(invSlot < this.inventory.size()) {
                if(!this.insertItem(originalStack, this.inventory.size(), this.slots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if(!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY
            }

            if(originalStack.isEmpty) {
                slot.stack = ItemStack.EMPTY
            } else {
                slot.markDirty()
            }
        }

        return newStack
    }

    override fun canUse(player: PlayerEntity?): Boolean {
        return this.inventory.canPlayerUse(player)
    }

    private fun addPlayerInventory(playerInventory: PlayerInventory?) {
        for(i in 0..2) {
            for(l in 0..8) {
                this.addSlot(Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18))
            }
        }
    }

    private fun addPlayerHotbar(playerInventory: PlayerInventory?) {
        for(i in 0..8) {
            this.addSlot(Slot(playerInventory, i, 8 + i * 18, 142))
        }
    }
}
