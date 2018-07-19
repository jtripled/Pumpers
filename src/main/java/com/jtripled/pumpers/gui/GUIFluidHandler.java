package com.jtripled.pumpers.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

/**
 *
 * @author jtripled
 */
public abstract class GUIFluidHandler extends GuiContainer
{
    public GUIFluidHandler(Container container)
    {
        super(container);
    }
    
    public float getZLevel()
    {
        return zLevel;
    }
}
