/*
 * This file is part of Blue Power.
 *
 *      Blue Power is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      Blue Power is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.block.worldgen;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import java.util.Collections;
import java.util.List;

import net.minecraft.world.level.storage.loot.LootContext;

/**
 * Created by Quetzi on 03/09/14.
 */
public class BlockBasalt extends BlockStoneOre {
    public BlockBasalt() {
    }

    @Deprecated
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        ResourceLocation resourcelocation = this.getLootTable();
        if (resourcelocation == LootTable.EMPTY.getLootTableId()) {
            return Collections.emptyList();
        } else {
            LootContext lootcontext = builder.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
            ServerLevel serverworld = lootcontext.getLevel();
            LootTable loottable = serverworld.getServer().getLootTables().get(resourcelocation);
            return loottable.getRandomItems(lootcontext);
        }
    }

}
