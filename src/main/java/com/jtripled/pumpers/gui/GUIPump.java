package com.jtripled.pumpers.gui;

import com.jtripled.pumpers.Pumpers;
import com.jtripled.pumpers.container.ContainerPump;
import com.jtripled.pumpers.tile.TilePump;
import java.util.Arrays;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 *
 * @author jtripled
 */
public final class GUIPump extends GUIFluidHandler
{
    public static final ResourceLocation TEXTURE = new ResourceLocation(Pumpers.getID(), "textures/gui/pump.png");
    
    private final ContainerPump container;
    private final TilePump tile;
    private final GUIFluidGauge gauge;
    
    public GUIPump(ContainerPump container)
    {
        super(container);
        this.ySize = 150;
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
        
        float progress = 1.0f - (float) tile.getBucketCooldown() / 25.0f;
        drawTexturedModalRect(x + 77, y + 36, 0, ySize, (int) Math.ceil(22 * progress), 16);
        
        gauge.draw(ticks, mouseX, mouseY, x, y);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        
        fontRenderer.drawString(Pumpers.getProxy().localize("container.inventory"), 8, ySize - 93, 0x404040);
        fontRenderer.drawString(Pumpers.getProxy().localize("tile.pump.name"), 8, 6, 0x404040);
        
        if (mouseX >= x + 15 && mouseY >= y + 22 && mouseX < x + 160 && mouseY < y + 30)
        {
            drawHoveringText(Arrays.asList(gauge.getTooltip()), mouseX - (width - xSize) / 2, mouseY - (height - ySize) / 2);
        }
    }
}
