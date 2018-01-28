package com.jtripled.pumpers.gui;

import com.jtripled.pumpers.Pumpers;
import com.jtripled.pumpers.container.ContainerPump;
import com.jtripled.voxen.gui.GUIContainerTile;
import com.jtripled.voxen.gui.GUIFluidGauge;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidTank;

/**
 *
 * @author jtripled
 */
public final class GUIPump extends GUIContainerTile<ContainerPump>
{
    public static final ResourceLocation TEXTURE = new ResourceLocation(Pumpers.ID, "textures/gui/pump.png");
    
    private final FluidTank tank;
    
    public GUIPump(ContainerPump container)
    {
        super(container);
        this.tank = container.getTile().getInternalTank();
        this.ySize = 150;
    }
    
    @Override
    public void addElements(int x, int y)
    {
        this.addElement(new GUIFluidGauge(this, container.getTile().getInternalTank(), 15, 22));
    }
    
    @Override
    public void drawBackground(float ticks, int mouseX, int mouseY, int x, int y)
    {
        int progress = 22 - (int) Math.ceil(22 * (float) this.getContainer().getTile().getBucketCooldown() / 25);
        drawTexturedModalRect(x + 77, y + 37, 176, 0, progress, 16);
        drawTexturedModalRect(x + 17, y + 23, 0, 250, 141, 6);
    }

    @Override
    public ResourceLocation getTexture()
    {
        return TEXTURE;
    }
}
