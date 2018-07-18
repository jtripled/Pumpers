package com.jtripled.pumpers.gui;

import com.jtripled.pumpers.container.ContainerPump;
import com.jtripled.voxen.gui.GUIContainerTile;
import com.jtripled.voxen.gui.GUIFluidGauge;
import com.jtripled.voxen.gui.GUIInventorySlot;
import com.jtripled.voxen.gui.GUIProgressIndicator;

/**
 *
 * @author jtripled
 */
public final class GUIPump extends GUIContainerTile<ContainerPump>
{
    public GUIPump(ContainerPump container)
    {
        super(container);
        this.setType(Type.INVENTORY_2);
    }
    
    @Override
    public void addElements(int x, int y)
    {
        this.addElement(new GUIFluidGauge(this, container.getTile().getInternalTank(), 15, 22));
        this.addElement(new GUIInventorySlot(this, 54, 35));
        this.addElement(new GUIInventorySlot(this, 104, 35));
        this.addElement(new GUIProgressIndicator(this, 77, 37) {
            @Override
            public float getProgress() {
                return 1.0f - (float) container.getTile().getBucketCooldown() / 25.0f;
            }
        });
    }
}
