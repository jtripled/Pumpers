package com.jtripled.pumpers.gui;

import com.jtripled.pumpers.Pumpers;
import com.jtripled.pumpers.container.ContainerFluidDuct;
import com.jtripled.voxen.gui.GUIContainerTile;
import com.jtripled.voxen.gui.GUIFluidGauge;
import net.minecraft.util.ResourceLocation;

/**
 *
 * @author jtripled
 */
public final class GUIFluidDuct extends GUIContainerTile<ContainerFluidDuct>
{
    public static final ResourceLocation TEXTURE = new ResourceLocation(Pumpers.ID, "textures/gui/tank.png");
    
    public GUIFluidDuct(ContainerFluidDuct container)
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
