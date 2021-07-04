/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.item;

import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.reference.Refs;
import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.block.BushBlock;
import net.minecraft.block.LilyPadBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class ItemSickle extends ToolItem {

    private Item customCraftingMaterial;

    private static final Set toolBlocks = Sets.newHashSet(ItemTags.LEAVES, Blocks.WHEAT, Blocks.POTATOES, Blocks.CARROTS,
            Blocks.NETHER_WART, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.SUGAR_CANE, Blocks.TALL_GRASS, Blocks.VINE, Blocks.LILY_PAD,
            BlockTags.SMALL_FLOWERS);

    public ItemSickle(IItemTier itemTier, String name, Item repairItem) {
        super(2,-1.4F, itemTier, toolBlocks, new Properties().tab(BPCreativeTabs.tools));
        this.setRegistryName(Refs.MODID + ":" + name);
        this.customCraftingMaterial = repairItem;
        BPItems.itemList.add(this);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if ((state.getMaterial() == Material.LEAVES) || (state.getMaterial() == Material.PLANT) || toolBlocks.contains(state)) {
            return this.speed;
        }
        return 1.0F;
    }

    @Override
    public boolean mineBlock(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving) {

        boolean used = false;

        if (!(entityLiving instanceof PlayerEntity)) return false;
        PlayerEntity player = (PlayerEntity) entityLiving;

        if (state.getBlock().getTags().contains(new ResourceLocation("minecraft:leaves")) || state.getBlock() instanceof  LeavesBlock) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    for (int k = -1; k <= 1; k++) {
                        BlockState blockToCheck = world.getBlockState(pos.offset(i,j,k));
                        if (blockToCheck.getBlock().getTags().contains(new ResourceLocation("minecraft:leaves")) || blockToCheck.getBlock() instanceof LeavesBlock) {
                            if (blockToCheck.canHarvestBlock(world, pos.offset(i,j,k), player)) {
                                world.destroyBlock(pos.offset(i,j,k), true);
                            }
                            used = true;
                        }
                    }
                }
            }
            if (used) {
                stack.hurtAndBreak(1, player, (playerEntity) ->
                        playerEntity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
            }
            return used;
        }

        if ((state.getBlock() instanceof LilyPadBlock)) {
            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    Block blockToCheck = world.getBlockState(pos.offset(i,0,j)).getBlock();
                    BlockState meta = world.getBlockState(pos.offset(i,0,j));
                    if (blockToCheck instanceof LilyPadBlock) {
                        if (blockToCheck.canHarvestBlock(meta, world, pos.offset(i,0,j), player)) {
                            world.destroyBlock(pos.offset(i,0,j), true);
                        }
                        used = true;
                    }
                }
            }
        }

        if (!(state.getBlock() instanceof LilyPadBlock)) {
            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    Block blockToCheck = world.getBlockState(pos.offset(i,0,j)).getBlock();
                    if (blockToCheck instanceof BushBlock && !(blockToCheck instanceof LilyPadBlock)) {
                        if (blockToCheck.canHarvestBlock(world.getBlockState(pos.offset(i,0,j)), world,  pos.offset(i,0,j), player)) {
                            world.destroyBlock(pos.offset(i,0,j), true);
                        }
                        used = true;
                    }
                }
            }
        }

        if (used) {
            stack.hurtAndBreak(1, player, (playerEntity) ->
                    playerEntity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
        }
        return used;
    }

    @Override
    public boolean isValidRepairItem(ItemStack is1, ItemStack is2) {
        return ((is1.getItem() == this || is2.getItem() == this) && (is1.getItem() == this.customCraftingMaterial || is2.getItem() == this.customCraftingMaterial));
    }
}
