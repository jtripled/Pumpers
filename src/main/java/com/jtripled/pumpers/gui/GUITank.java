package com.jtripled.pumpers.gui;

import com.jtripled.pumpers.Pumpers;
import com.jtripled.pumpers.container.ContainerTank;
import com.jtripled.voxen.gui.GUIContainerTile;
import com.jtripled.voxen.gui.GUIFluidGauge;
import net.minecraft.util.ResourceLocation;

/**
 *
 * @author jtripled
 */
public final class GUITank extends GUIContainerTile<ContainerTank>
{
    public static final ResourceLocation TEXTURE = new ResourceLocation(Pumpers.ID, "textures/gui/tank.png");
    
    public GUITank(ContainerTank container)
    {
        super(container);
        this.ySize = 132;
    }
    
    @Override
    public void addElements(int x, int y)
    {
        this.addElement(new GUIFluidGauge(this, container.getTile().getInternalTank(), 15, 22));
    }

    @Override
    public ResourceLocation getTexture()
    {
        return TEXTURE;
    }
}
