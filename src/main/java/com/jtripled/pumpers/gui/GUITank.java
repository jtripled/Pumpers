package com.jtripled.pumpers.gui;

import com.jtripled.pumpers.container.ContainerTank;
import com.jtripled.voxen.gui.GUIContainerTile;
import com.jtripled.voxen.gui.GUIFluidGauge;

/**
 *
 * @author jtripled
 */
public final class GUITank extends GUIContainerTile<ContainerTank>
{
    public GUITank(ContainerTank container)
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
