package com.bluepowermod.compat.cc;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnectionCache;
import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.redstone.BundledConnectionCache;
import com.bluepowermod.redstone.RedstoneApi;

import dan200.computercraft.api.ComputerCraftAPI;

public class BundledDeviceCCComputer implements IBundledDevice {

    private static List<BundledDeviceCCComputer> list = new ArrayList<BundledDeviceCCComputer>();

    public static BundledDeviceCCComputer getDeviceAt(World world, int x, int y, int z) {

        Vec3i loc = new Vec3i(x, y, z, world);

        for (BundledDeviceCCComputer c : list)
            if (c.loc.equals(loc))
                return c;

        BundledDeviceCCComputer c = new BundledDeviceCCComputer(loc);
        list.add(c);
        return c;
    }

    private Vec3i loc;
    private byte[][] curPow = new byte[6][16];
    private BundledConnectionCache connections = RedstoneApi.getInstance().createBundledConnectionCache(this);

    public BundledDeviceCCComputer(Vec3i loc) {

        this.loc = loc;
    }

    @Override
    public World getWorld() {

        return loc.getWorld();
    }

    @Override
    public int getX() {

        return loc.getX();
    }

    @Override
    public int getY() {

        return loc.getY();
    }

    @Override
    public int getZ() {

        return loc.getZ();
    }

    @Override
    public boolean canConnect(EnumFacing side, IBundledDevice dev, ConnectionType type) {

        return type == ConnectionType.STRAIGHT || side != EnumFacing.UNKNOWN;
    }

    @Override
    public IConnectionCache<? extends IBundledDevice> getBundledConnectionCache() {

        return connections;
    }

    @Override
    public byte[] getBundledOutput(EnumFacing side) {

        int out = ComputerCraftAPI.getBundledRedstoneOutput(getWorld(), getX(), getY(), getZ(), side.ordinal());

        if (out < 0)
            return new byte[16];

        return CCUtils.unpackDigital(out);
    }

    @Override
    public void setBundledPower(EnumFacing side, byte[] power) {

        if (side == EnumFacing.UNKNOWN)
            return;

        curPow[side.ordinal()] = power;
    }

    @Override
    public byte[] getBundledPower(EnumFacing side) {

        return getBundledOutput(side);
    }

    @Override
    public void onBundledUpdate() {

        getWorld().notifyBlockOfNeighborChange(getX(), getY(), getZ(), Blocks.air);
    }

    public byte[] getCurPow(EnumFacing side) {

        return curPow[side.ordinal()];
    }

    @Override
    public MinecraftColor getBundledColor(EnumFacing side) {

        return MinecraftColor.NONE;
    }

    @Override
    public boolean isNormalFace(EnumFacing side) {

        return true;
    }

}
