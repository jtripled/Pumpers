package com.jtripled.pumpers.gui;

import com.jtripled.pumpers.container.ContainerFluidDuct;
import com.jtripled.voxen.gui.GUIContainerTile;
import com.jtripled.voxen.gui.GUIFluidGauge;

/**
 *
 * @author jtripled
 */
public final class GUIFluidDuct extends GUIContainerTile<ContainerFluidDuct>
{
    public GUIFluidDuct(ContainerFluidDuct container)
    {
        super(container);
        this.setType(Type.INVENTORY_1);
    }
    
    @Override
    public void addElements(int x, int y)
    {
        this.addElement(new GUIFluidGauge(this, container.getTile().getInternalTank(), 15, 22));
    }
}
