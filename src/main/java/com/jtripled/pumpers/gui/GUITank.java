package com.jtripled.pumpers.gui;

import com.jtripled.pumpers.Pumpers;
import com.jtripled.pumpers.container.ContainerTank;
import com.jtripled.pumpers.tile.TileTank;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import scala.actors.threadpool.Arrays;

/**
 *
 * @author jtripled
 */
public final class GUITank extends GUIFluidHandler
{
    public static final ResourceLocation TEXTURE = new ResourceLocation(Pumpers.ID, "textures/gui/tank.png");
    
    private final ContainerTank container;
    private final TileTank tile;
    private final GUIFluidGauge gauge;
    
    public GUITank(ContainerTank container)
    {
        super(container);
        this.ySize = 132;
        this.container = container;
        this.tile = container.getTile();
        this.gauge = new GUIFluidGauge(this, tile.getTank(), 15, 22);
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float ticks, int mouseX, int mouseY)
    {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        
        gauge.draw(ticks, mouseX, mouseY, x, y);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        
        fontRenderer.drawString("Inventory", 8, ySize - 93, 0x404040);
        fontRenderer.drawString(Pumpers.getProxy().localize("tile.tank.name"), 8, 6, 0x404040);
        
        if (mouseX >= x + 15 && mouseY >= y + 22 && mouseX < x + 160 && mouseY < y + 30)
        {
            drawHoveringText(Arrays.asList(gauge.getTooltip()), mouseX - (width - xSize) / 2, mouseY - (height - ySize) / 2);
        }
    }
}
