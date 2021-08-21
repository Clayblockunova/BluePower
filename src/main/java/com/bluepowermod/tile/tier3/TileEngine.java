/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier3;

import com.bluepowermod.api.power.BlutricityFEStorage;
import com.bluepowermod.api.power.CapabilityBlutricity;
import com.bluepowermod.block.power.BlockEngine;
import com.bluepowermod.tile.BPBlockEntityType;
import com.bluepowermod.tile.TileBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 
 * @author TheFjong, MoreThanHidden
 *
 */
public class TileEngine extends TileMachineBase  {

	private Direction orientation = Direction.DOWN;
	public boolean isActive = false;
    public byte pumpTick;
    public byte pumpSpeed;

	private final BlutricityFEStorage storage = new BlutricityFEStorage(320){
		@Override
		public boolean canReceive() {
			return false;
		}
	};
	private LazyOptional<BlutricityFEStorage> blutricityCap;

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if(cap == CapabilityBlutricity.BLUTRICITY_CAPABILITY || cap == CapabilityEnergy.ENERGY){
			if( blutricityCap == null ) blutricityCap = LazyOptional.of( () -> storage );
			return blutricityCap.cast();
		}
		return LazyOptional.empty();
	}

	
	public TileEngine(BlockPos pos, BlockState state){
		super(BPBlockEntityType.ENGINE, pos, state);

		pumpTick  = 0;
		pumpSpeed = 16;
		
	}

	public static void tickEngine(Level level, BlockPos pos, BlockState state, TileEngine blockEntity) {
		TileBase.tickTileBase(level, pos, state, blockEntity);

		blockEntity.storage.resetCurrent();

		//Server side capability check
		blockEntity.isActive = false;
		if(level != null && !level.isClientSide && (blockEntity.storage.getEnergyStored() > 0 && level.hasNeighborSignal(blockEntity.worldPosition))){
			Direction facing = blockEntity.getBlockState().getValue(BlockEngine.FACING).getOpposite();
			BlockEntity tileEntity = level.getBlockEntity(pos.relative(facing));
			if (tileEntity != null) {
				tileEntity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).ifPresent(other -> {
					int simulated = blockEntity.storage.extractEnergy(320, true);
					int sent = other.receiveEnergy(simulated, false);
					int amount = blockEntity.storage.extractEnergy(sent, false);
					if(amount > 0) {
						blockEntity.isActive = true;
					}
				});
			}
		}

		//Update BlockState
		if(level != null && !level.isClientSide && blockEntity.getBlockState().getValue(BlockEngine.ACTIVE) != blockEntity.isActive){
			level.setBlockAndUpdate(pos, blockEntity.getBlockState().setValue(BlockEngine.ACTIVE, blockEntity.isActive));
			blockEntity.markForRenderUpdate();
		}

		//Update TESR from BlockState
		if(level != null && blockEntity.getBlockState().getValue(BlockEngine.ACTIVE)) {
			blockEntity.isActive = true;
			blockEntity.pumpTick++;
			if (blockEntity.pumpTick >= blockEntity.pumpSpeed * 2) {
				blockEntity.pumpTick = 0;
				if (blockEntity.pumpSpeed > 4) {
					blockEntity.pumpSpeed--;
				}
			}
		}else{
			blockEntity.isActive = false;
			blockEntity.pumpTick = 0;
		}

	}

    public void setOrientation(Direction orientation){
        this.orientation = orientation;
        setChanged();
    }

    public Direction getOrientation()
    {
        return orientation;
    }


	@Override
	protected void writeToPacketNBT(CompoundTag compound) {
		super.writeToPacketNBT(compound);
		int rotation = orientation.get3DDataValue();
		compound.putInt("rotation", rotation);
        compound.putByte("pumpspeed", pumpSpeed);
        compound.putByte("pumptick", pumpTick);
        Tag nbtstorage = CapabilityBlutricity.writeNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null);
		compound.put("energy", nbtstorage);

	}

	@Override
	protected void readFromPacketNBT(CompoundTag compound) {
		super.readFromPacketNBT(compound);
		orientation = Direction.from3DDataValue(compound.getInt("rotation"));
        pumpSpeed = compound.getByte("pumpspeed");
        pumpTick = compound.getByte("pumptick");
        if(compound.contains("energy")) {
            Tag nbtstorage = compound.get("energy");
            CapabilityBlutricity.readNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null, nbtstorage);
        }
	}

	@Override
	public void invalidateCaps(){
		super.invalidateCaps();
		if( blutricityCap != null )
		{
			blutricityCap.invalidate();
			blutricityCap = null;
		}
	}

}
