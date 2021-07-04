/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.container;

import com.bluepowermod.client.gui.BPContainerType;
import com.bluepowermod.client.gui.GuiContainerBase;
import com.bluepowermod.tile.tier1.TileAlloyFurnace;
import com.bluepowermod.tile.tier3.TileCircuitDatabase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import com.bluepowermod.ClientProxy;
import com.bluepowermod.api.item.IDatabaseSaveable;
import com.bluepowermod.container.slot.SlotPhantom;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerCircuitDatabaseMain extends ContainerGhosts {

    public int curUploadProgress, curCopyProgress, selectedShareOption;
    private final IInventory circuitDatabase;

    public ContainerCircuitDatabaseMain(int windowId, PlayerInventory invPlayer, IInventory inventory) {
        super(BPContainerType.CIRCUITDATABASE_MAIN, windowId);
        this.circuitDatabase = inventory;
        addSlot(new SlotPhantom(circuitDatabase, 0, 57, 64) {

            @Override
            public boolean mayPlace(ItemStack stack) {

                return stack.getItem() instanceof IDatabaseSaveable && ((IDatabaseSaveable) stack.getItem()).canGoInCopySlot(stack);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        addSlot(new Slot(circuitDatabase, 1, 108, 64) {

            @Override
            public boolean mayPlace(ItemStack stack) {

                return stack.getItem() instanceof IDatabaseSaveable && ((IDatabaseSaveable) stack.getItem()).canGoInCopySlot(stack);
            }
        });

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(circuitDatabase, j + i * 9, 8 + j * 18, 95 + i * 18));
            }
        }

        bindPlayerInventory(invPlayer);
    }


    public ContainerCircuitDatabaseMain( int id, PlayerInventory player )    {
        this( id, player, new Inventory( TileCircuitDatabase.SLOTS ));
    }

    protected void bindPlayerInventory(PlayerInventory invPlayer) {

        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 142 + i * 18));
            }
        }

        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlot(new Slot(invPlayer, j, 8 + j * 18, 200));
        }
    }

    @Override
    public boolean stillValid(PlayerEntity player) {

        return true;
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void setData(int id, int value) {

        if (id == 0) {
            curUploadProgress = value;
        }
        if (id == 1) {
            curCopyProgress = value;
        }
        if (id == 2) {
            selectedShareOption = value;
            ((GuiContainerBase) ClientProxy.getOpenedGui()).redraw();
        }
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int par2) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) slots.get(par2);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (par2 < 20) {
                if (!moveItemStackTo(itemstack1, 20, 55, false))
                    return ItemStack.EMPTY;
            } else {
                if (!moveItemStackTo(itemstack1, 2, 20, false))
                    return ItemStack.EMPTY;
            }
            if (itemstack1.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemstack1.getCount() != itemstack.getCount()) {
                slot.onQuickCraft(itemstack, itemstack1);
            } else {
                return ItemStack.EMPTY;
            }
        }
        return itemstack;
    }

}
