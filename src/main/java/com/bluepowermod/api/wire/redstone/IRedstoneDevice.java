package com.bluepowermod.api.wire.redstone;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnectionCache;
import com.bluepowermod.api.misc.MinecraftColor;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public interface IRedstoneDevice {

    /**
     * Returns whether or not the device passed as an argument can be connected to this device on the specified side. It also takes a ConnectionType,
     * which determines the type of connection to this device.
     */
    public boolean canConnect(Direction side, IRedstoneDevice dev, ConnectionType type);

    /**
     * Returns a cache of all the connections of other devices with this one. Create an instance of this class by calling
     * {@link IRedstoneApi#createRedstoneConnectionCache(IRedstoneDevice)}
     */
    public IConnectionCache<? extends IRedstoneDevice> getRedstoneConnectionCache();

    /**
     * Gets the output of this device on the specified side.
     */
    public byte getRedstonePower(Direction side);

    /**
     * Sets the power level on the specified side to a set power level.
     */
    public void setRedstonePower(Direction side, byte power);

    /**
     * Notifies the device of a power change. (Usually called after propagation)
     */
    public void onRedstoneUpdate();

    /**
     * Returns whether or not this is a full face (if face devices should be able to connect to it)
     */
    public boolean isNormalFace(Direction side);


    static Tag writeNBT(Capability<IRedstoneDevice> capability, IRedstoneDevice instance, Direction direction) {
        CompoundTag nbt = new CompoundTag();
        nbt.putByte("power", instance.getRedstonePower(direction));
        return nbt;
    }

    static void readNBT(Capability<IRedstoneDevice> capability, IRedstoneDevice instance, Direction side, Tag nbt) {
        CompoundTag tags = (CompoundTag) nbt;
        byte power = tags.getByte("power");
        instance.setRedstonePower(side, power);
    }

}
