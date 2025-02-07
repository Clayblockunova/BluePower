/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.gui;

import com.bluepowermod.container.ContainerAlloyFurnace;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;

import com.bluepowermod.container.ContainerDiskDrive;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.TileDiskDrive;
import net.minecraft.network.chat.Component;

public class GuiDiskDrive extends GuiContainerBaseBP<ContainerDiskDrive> implements MenuAccess<ContainerDiskDrive> {

    private final ContainerDiskDrive diskDrive;

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID + ":textures/gui/diskdrivegui.png");

    public GuiDiskDrive(ContainerDiskDrive container, Inventory playerInventory, Component title){
        super(container, playerInventory, title, resLoc);
        this.diskDrive = container;
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {

    }
}
