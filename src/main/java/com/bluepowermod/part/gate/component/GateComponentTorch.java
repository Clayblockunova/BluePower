package com.bluepowermod.part.gate.component;

import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.part.gate.GateBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.transform.Translation;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3dHelper;

import java.awt.image.BufferedImage;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Random;

public class GateComponentTorch extends GateComponent {

    private final Random rnd = new Random();

    protected boolean state = false;

    private int layoutColor = -1;
    protected double x = 0, z = 0;
    protected double height;

    private boolean digital;

    public GateComponentTorch(GateBase<?, ?, ?, ?, ?, ?> gate, int color, double height, boolean digital) {

        super(gate);

        layoutColor = color;
        onLayoutRefresh();

        this.height = height;

        this.digital = digital;
    }

    public GateComponentTorch(GateBase<?, ?, ?, ?, ?, ?> gate, double x, double z, double height, boolean digital) {

        super(gate);

        this.x = x;
        this.z = z;
        this.height = height;

        this.digital = digital;
    }

    @Override
    public void renderStatic(Vec3i translation, RenderHelper renderer, int pass) {

        TextureAtlasSprite icon = digital ? (state ? IconSupplier.bluestoneTorchOn : IconSupplier.bluestoneTorchOff) :
                (state ? Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(Blocks.REDSTONE_TORCH.getRegistryName().toString()) :
                        Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(Blocks.UNLIT_REDSTONE_TORCH.getRegistryName().toString()));
        TextureAtlasSprite override = renderer.getOverrideTexture();
        renderer.setOverrideTexture(null);

        double height = 10 / 16D - this.height;

        renderer.addTransformation(new Translation(x - 7 / 16D, 2 / 16D - height, z - 7 / 16D));

        renderer.setRenderSides(false, false, false, false, true, true);
        renderer.renderBox(new Vec3dCube(0, height, 7 / 16D, 1, 1, 9 / 16D), icon);

        renderer.setRenderSides(false, false, true, true, false, false);
        renderer.renderBox(new Vec3dCube(7 / 16D, height, 0, 9 / 16D, 1, 1), icon);

        renderer.addTransformation(new Translation(0, 0, 1 / 16D));

        renderer.setRenderSides(false, true, false, false, false, false);
        renderer.renderBox(new Vec3dCube(7 / 16D, 10 / 16D, 6 / 16D, 9 / 16D, 10 / 16D, 8 / 16D), icon);
        renderer.resetRenderedSides();

        renderer.removeTransformations(2);

        renderer.setOverrideTexture(override);
    }

    @Override
    public void onLayoutRefresh() {

        if (layoutColor == -1)
            return;

        BufferedImage img = getGate().getLayout().getLayout(layoutColor);
        x = img.getWidth();
        z = img.getHeight();
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getWidth(); y++) {
                if ((img.getRGB(x, y) & 0xFFFFFF) != 0) {
                    this.x = Math.min(this.x, x);
                    z = Math.min(z, y);
                }
            }
        }
        x = x / (img.getWidth());
        z = z / (img.getHeight());
    }

    @Override
    public void tick() {

        super.tick();

        if (!state)
            return;

        GateBase<?, ?, ?, ?, ?, ?> gate = getGate();

        if (!gate.getWorld().isRemote)
            return;

        Vec3d v = Vec3dHelper.rotate(Vec3dHelper.rotate(new Vec3d(x + 1 / 16D, height + 2 / 16D, z + 1 / 16D)
                .subtract(Vec3dHelper.CENTER), 0, 90 * -gate.getRotation(), 0)
                .add(Vec3dHelper.CENTER), gate.getFace(), Vec3dHelper.CENTER);
        if (rnd.nextInt(10) == 0)
            gate.getWorld().spawnParticle(EnumParticleTypes.REDSTONE, gate.getPos().getX() + v.xCoord, gate.getPos().getY() + v.yCoord, gate.getPos().getZ() + v.zCoord,
                    digital ? -1 : 0, 0, digital ? 1 : 0);
    }

    public GateComponentTorch setState(boolean state) {

        // System.out.println(state + " vs " + this.state + " -> " + (state != this.state));
        if (state != this.state)
            setNeedsSyncing(true);
        this.state = state;

        return this;
    }

    public boolean getState() {

        return state;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setBoolean("state", state);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        state = tag.getBoolean("state");
    }

    @Override
    public void writeData(DataOutput buffer) throws IOException {

        super.writeData(buffer);
        buffer.writeBoolean(state);
    }

    @Override
    public void readData(DataInput buffer) throws IOException {

        super.readData(buffer);
        state = buffer.readBoolean();
    }

    public void setX(double x) {

        this.x = x;
    }

    public void setZ(double z) {

        this.z = z;
    }

    public double getX() {

        return x;
    }

    public double getZ() {

        return z;
    }

}
