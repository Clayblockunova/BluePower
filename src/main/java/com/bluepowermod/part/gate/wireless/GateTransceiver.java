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

package com.bluepowermod.part.gate.wireless;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.helper.MathHelper;
import uk.co.qmunity.lib.helper.RedstoneHelper;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartPlacement;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.util.Dir;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnection;
import com.bluepowermod.api.gate.IGateLogic;
import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.IBundledConductor.IAdvancedBundledConductor;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor.IAdvancedRedstoneConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedwire;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.api.wireless.IFrequency;
import com.bluepowermod.api.wireless.IWirelessDevice;
import com.bluepowermod.client.gui.gate.GuiGateWireless;
import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.network.message.MessageWirelessFrequencySync;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.part.gate.connection.GateConnectionAnalogue;
import com.bluepowermod.part.gate.connection.GateConnectionBundledAnalogue;
import com.bluepowermod.part.gate.connection.GateConnectionBundledDigital;
import com.bluepowermod.part.gate.connection.GateConnectionDigital;
import com.bluepowermod.redstone.RedstoneApi;
import com.bluepowermod.util.DebugHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateTransceiver extends GateBase implements IGateLogic<GateTransceiver>, IWirelessDevice, IWirelessGate, IGuiButtonSensitive,
        IAdvancedRedstoneConductor, IAdvancedBundledConductor {

    private static final List<GateTransceiver> transceivers = new ArrayList<GateTransceiver>();

    private boolean isBundled;
    private boolean isAnalogue;

    private Frequency frequency = null;

    protected WirelessMode mode = WirelessMode.BOTH;

    protected byte input = 0;
    protected byte[] inputBundled = new byte[16];

    protected byte freqPow = 0;
    protected byte[] freqPowBundled = new byte[16];

    public GateTransceiver(Boolean isBundled, Boolean isAnalogue) {

        this.isBundled = isBundled;
        this.isAnalogue = isAnalogue;
    }

    @Override
    public void initConnections() {

        front(
                isBundled ? (isAnalogue ? new GateConnectionBundledAnalogue(this, Dir.FRONT) : new GateConnectionBundledDigital(this, Dir.FRONT))
                        : (isAnalogue ? new GateConnectionAnalogue(this, Dir.FRONT) : new GateConnectionDigital(this, Dir.FRONT))).setEnabled(true);
    }

    @Override
    public void initComponents() {

    }

    @Override
    public String getGateType() {

        return "wirelesstransceiver" + (isAnalogue ? ".analog" : "") + (isBundled ? ".bundled" : "");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        super.renderStatic(translation, renderer, renderBlocks, pass);

        if (getParent() != null)
            renderer.addTransformation(new Rotation(0, 180, 0));

        IIcon obsidian = Blocks.obsidian.getIcon(0, 0);
        IIcon quartz = Blocks.quartz_block.getIcon(0, 0);
        IIcon iron = Blocks.iron_block.getIcon(0, 0);
        IIcon gold = Blocks.gold_block.getIcon(0, 0);

        // Base
        renderer.renderBox(new Vec3dCube(7 / 16D, 2 / 16D, 7 / 16D, 9 / 16D, 8 / 16D, 9 / 16D), obsidian);

        if (getParent() == null || getWorld() == null)
            renderer.addTransformation(new Rotation(0, -(System.currentTimeMillis() / 100D) % 360, 0));
        renderer.addTransformation(new Rotation(45, 0, 0));

        // Post
        renderer.renderBox(new Vec3dCube(15 / 32D, 9 / 16D, 15 / 32D, 17 / 32D, 10 / 16D, 17 / 32D), iron);
        // Ball thingy
        renderer.renderBox(new Vec3dCube(7 / 16D, 10 / 16D, 7 / 16D, 9 / 16D, 12 / 16D, 9 / 16D), gold);

        renderer.renderBox(new Vec3dCube(6 / 16D, 8 / 16D, 5 / 16D, 10 / 16D, 9 / 16D, 11 / 16D), quartz);
        renderer.renderBox(new Vec3dCube(5 / 16D, 8 / 16D, 6 / 16D, 11 / 16D, 9 / 16D, 10 / 16D), quartz);

        for (int i = 0; i < 4; i++) {
            renderer.renderBox(new Vec3dCube(5 / 16D, 9 / 16D, 10 / 16D, 6 / 16D, 10 / 16D, 11 / 16D).rotate(0, i * 90, 0, Vec3d.center), quartz);

            renderer.renderBox(new Vec3dCube(4 / 16D, 9 / 16D, 6 / 16D, 5 / 16D, 10 / 16D, 10 / 16D).rotate(0, i * 90, 0, Vec3d.center), quartz);
            renderer.renderBox(new Vec3dCube(4 / 16D, 10 / 16D, 5 / 16D, 5 / 16D, 11 / 16D, 11 / 16D).rotate(0, i * 90, 0, Vec3d.center), quartz);

            renderer.renderBox(new Vec3dCube(4 / 16D, 11 / 16D, 11 / 16D, 5 / 16D, 12 / 16D, 12 / 16D).rotate(0, i * 90, 0, Vec3d.center), quartz);

            renderer.renderBox(new Vec3dCube(3 / 16D, 11 / 16D, 4 / 16D, 4 / 16D, 12 / 16D, 12 / 16D).rotate(0, i * 90, 0, Vec3d.center), quartz);
        }

        renderer.resetTransformations();

        return true;
    }

    private boolean propagating = false;

    private void propagate() {

        if (propagating)
            return;

        propagating = true;
        RedstoneApi.getInstance().getRedstonePropagator(this, ForgeDirection.UNKNOWN).propagate();
        propagating = false;
    }

    @Override
    public void doLogic() {

    }

    @Override
    public void tick() {

    }

    @Override
    public boolean changeMode() {

        return false;
    }

    @Override
    public IGateLogic<? extends GateBase> logic() {

        return this;
    }

    @Override
    public GateTransceiver getGate() {

        return this;
    }

    @Override
    public void setFrequency(IFrequency freq) {

        frequency = (Frequency) freq;
        if (!getWorld().isRemote)
            propagate();
    }

    @Override
    public Frequency getFrequency() {

        return frequency;
    }

    @Override
    public WirelessMode getMode() {

        return mode;
    }

    @Override
    public void setMode(WirelessMode mode) {

        this.mode = mode;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setInteger("mode", mode.ordinal());

        if (frequency != null)
            frequency.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        mode = WirelessMode.values()[tag.getInteger("mode")];

        if (tag.hasKey("freq_name")) {
            Frequency f = new Frequency();
            f.readFromNBT(tag);
            frequency = (Frequency) WirelessManager.COMMON_INSTANCE.getFrequency(f.getAccessibility(), f.getFrequencyName(), f.getOwner());
        } else {
            frequency = null;
        }
    }

    @Override
    public void writeUpdateData(DataOutput buffer) throws IOException {

        super.writeUpdateData(buffer);
        buffer.writeInt(mode.ordinal());

        buffer.writeBoolean(frequency != null);
        if (frequency != null)
            frequency.writeToBuffer(buffer);
    }

    @Override
    public void readUpdateData(DataInput buffer, int channel) throws IOException {

        super.readUpdateData(buffer, channel);
        mode = WirelessMode.values()[buffer.readInt()];

        if (buffer.readBoolean()) {
            if (frequency == null)
                frequency = new Frequency();
            frequency.readFromBuffer(buffer);
        } else {
            frequency = null;
        }
    }

    @Override
    protected void handleGUIServer(EntityPlayer player) {

        sendUpdatePacket();
        BPNetworkHandler.INSTANCE.sendTo(new MessageWirelessFrequencySync(player), (EntityPlayerMP) player);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected GuiScreen getGui(EntityPlayer player) {

        return new GuiGateWireless(this, isBundled, mode);
    }

    @Override
    protected boolean hasGUI() {

        return true;
    }

    @Override
    public boolean isBundled() {

        return isBundled;
    }

    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {

        if (messageId == 0)
            mode = WirelessMode.values()[value];
        if (messageId == 1) {
            setFrequency(null);
            BPNetworkHandler.INSTANCE.sendTo(new MessageWirelessFrequencySync(player), (EntityPlayerMP) player);
        }

        sendUpdatePacket();
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

        if (side == ForgeDirection.UNKNOWN)
            freqPow = power;
        else if (side == Dir.FRONT.toForgeDirection(getFace(), getRotation()) && mode == WirelessMode.SEND)
            input = power;
        else
            input = 0;
    }

    @Override
    public void setBundledPower(ForgeDirection side, byte[] power) {

        if (side == ForgeDirection.UNKNOWN)
            freqPowBundled = power;
        else if (side == Dir.FRONT.toForgeDirection(getFace(), getRotation()) && mode == WirelessMode.SEND)
            inputBundled = power;
        else
            inputBundled = new byte[16];
    }

    @Override
    public byte getRedstonePower(ForgeDirection side) {

        if (side == ForgeDirection.UNKNOWN)
            return input;
        else if (side == Dir.FRONT.toForgeDirection(getFace(), getRotation()) && mode == WirelessMode.RECEIVE)
            return freqPow;
        return 0;
    }

    @Override
    public byte[] getBundledPower(ForgeDirection side) {

        if (side == ForgeDirection.UNKNOWN)
            return freqPowBundled;
        else if (side == Dir.FRONT.toForgeDirection(getFace(), getRotation()) && mode == WirelessMode.RECEIVE)
            return inputBundled;
        return new byte[16];
    }

    @Override
    public byte[] getBundledOutput(ForgeDirection side) {

        if (side == ForgeDirection.UNKNOWN)
            return inputBundled;
        else if (side == Dir.FRONT.toForgeDirection(getFace(), getRotation()) && mode == WirelessMode.RECEIVE)
            return freqPowBundled;
        return new byte[16];
    }

    @Override
    public void onRedstoneUpdate() {

        System.out.println("Hey!");

        propagate();
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
            RedstoneHelper.notifyRedstoneUpdate(getWorld(), getX(), getY(), getZ(), d, true);
    }

    @Override
    public int getWeakPower(ForgeDirection side) {

        if (mode == WirelessMode.SEND)
            return 0;
        return Dir.FRONT.toForgeDirection(getFace(), getRotation()) == side ? MathHelper.map(freqPow & 0xFF, 0, 255, 0, 15) : 0;
    }

    @Override
    public int getStrongPower(ForgeDirection side) {

        if (mode == WirelessMode.SEND)
            return 0;
        return Dir.FRONT.toForgeDirection(getFace(), getRotation()) == side ? MathHelper.map(freqPow & 0xFF, 0, 255, 0, 15) : 0;
    }

    @Override
    public boolean canConnectRedstone(ForgeDirection side) {

        return Dir.FRONT.toForgeDirection(getFace(), getRotation()) == side;
    }

    @Override
    public boolean hasLoss(ForgeDirection side) {

        return false;
    }

    @Override
    public boolean isAnalogue(ForgeDirection side) {

        return isAnalogue;
    }

    @Override
    public boolean canPropagateFrom(ForgeDirection fromSide) {

        return mode == WirelessMode.BOTH && !isBundled;
    }

    @Override
    public boolean canPropagateBundledFrom(ForgeDirection fromSide) {

        return mode == WirelessMode.BOTH && isBundled;
    }

    @Override
    public void propagate(ForgeDirection fromSide, Collection<IConnection<IRedstoneDevice>> propagation) {

        if (frequency == null || mode != WirelessMode.BOTH)
            return;

        if (mode == WirelessMode.BOTH)
            propagation.add(getRedstoneConnectionCache().getConnectionOnSide(Dir.FRONT.toForgeDirection(getFace(), getRotation())));

        for (IWirelessDevice d : WirelessManager.COMMON_INSTANCE.getDevices()) {
            if (d != this && d.getFrequency() != null && d.getFrequency().equals(getFrequency())) {
                if (d instanceof GateTransceiver) {
                    propagation.add(RedstoneApi.getInstance().createConnection(this, (IRedstoneDevice) d, ForgeDirection.UNKNOWN,
                            ForgeDirection.UNKNOWN, ConnectionType.STRAIGHT));
                    if (((GateTransceiver) d).mode == WirelessMode.BOTH) {
                        IConnection<IRedstoneDevice> c = ((GateTransceiver) d).getRedstoneConnectionCache().getConnectionOnSide(
                                Dir.FRONT.toForgeDirection(((GateTransceiver) d).getFace(), ((GateTransceiver) d).getRotation()));
                        if (c != null)
                            propagation.add(c);
                    }
                }
            }
        }
    }

    @Override
    public void propagateBundled(ForgeDirection fromSide, Collection<IConnection<IBundledDevice>> propagation) {

    }

    @Override
    public boolean canConnect(ForgeDirection side, IRedstoneDevice device, ConnectionType type) {

        if (device instanceof IRedwire) {
            RedwireType rwt = ((IRedwire) device).getRedwireType(type == ConnectionType.STRAIGHT ? side.getOpposite()
                    : (type == ConnectionType.CLOSED_CORNER ? getFace() : getFace().getOpposite()));
            if (rwt == null)
                return false;
            if (rwt.isAnalogue() != isAnalogue(side))
                return false;
        }

        return !isBundled && super.canConnect(side, device, type);
    }

    @Override
    public boolean canConnect(ForgeDirection side, IBundledDevice device, ConnectionType type) {

        if (device instanceof IRedwire) {
            RedwireType rwt = ((IRedwire) device).getRedwireType(type == ConnectionType.STRAIGHT ? side.getOpposite()
                    : (type == ConnectionType.CLOSED_CORNER ? getFace() : getFace().getOpposite()));
            if (rwt == null)
                return false;
            if (rwt.isAnalogue() != isAnalogue(side))
                return false;
        }

        return isBundled && super.canConnect(side, device, type);
    }

    @Override
    public void onAdded() {

        super.onAdded();
        if (!transceivers.contains(this))
            transceivers.add(this);
        WirelessManager.COMMON_INSTANCE.registerWirelessDevice(this);
    }

    @Override
    public void onLoaded() {

        super.onLoaded();
        if (!transceivers.contains(this))
            transceivers.add(this);
        WirelessManager.COMMON_INSTANCE.registerWirelessDevice(this);
    }

    @Override
    public void onRemoved() {

        super.onRemoved();
        transceivers.remove(this);
        WirelessManager.COMMON_INSTANCE.unregisterWirelessDevice(this);
    }

    @Override
    public void onUnloaded() {

        super.onUnloaded();
        transceivers.remove(this);
        WirelessManager.COMMON_INSTANCE.unregisterWirelessDevice(this);
    }

    @Override
    public IPartPlacement getPlacement(IPart part, World world, Vec3i location, ForgeDirection face, MovingObjectPosition mop, EntityPlayer player) {

        if (!DebugHelper.isDebugModeEnabled())
            return null;

        return super.getPlacement(part, world, location, face, mop, player);
    }

    @Override
    public void addTooltip(ItemStack item, List<String> tip) {

        if (!DebugHelper.isDebugModeEnabled())
            tip.add(MinecraftColor.RED + I18n.format("Disabled temporarily. Still not fully working."));
        else
            tip.add(MinecraftColor.CYAN + I18n.format("Disabled temporarily. Still not fully working."));
    }

}
