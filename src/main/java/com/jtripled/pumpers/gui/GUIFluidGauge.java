package com.jtripled.pumpers.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

/**
 *
 * @author jtripled
 */
public class GUIFluidGauge
{
    private final GUIFluidHandler gui;
    private final FluidTank tank;
    private final int x;
    private final int y;
    
    public GUIFluidGauge(GUIFluidHandler gui, FluidTank tank, int x, int y)
    {
        this.gui = gui;
        this.tank = tank;
        this.x = x;
        this.y = y;
    }
    
    public String[] getTooltip()
    {
        String[] tooltip = new String[2];
        FluidStack fluid = this.tank.getFluid();
        if (fluid == null)
        {
            TextComponentString message = new TextComponentString("Empty");
            message.getStyle().setColor(TextFormatting.RED);
            tooltip[0] = (message.getFormattedText());
            message = new TextComponentString("0/" + this.tank.getCapacity() + "mB");
            message.getStyle().setColor(TextFormatting.GRAY);
            tooltip[1] = (message.getFormattedText());
        }
        else
        {
            tooltip[0] = (fluid.getFluid().getLocalizedName(fluid));
            TextComponentString message = new TextComponentString(this.tank.getFluidAmount() + "/" + this.tank.getCapacity() + "mB");
            message.getStyle().setColor(TextFormatting.GRAY);
            tooltip[1] = (message.getFormattedText());
        }
        return tooltip;
    }
    
    public void draw(float ticks, int mouseX, int mouseY, int x, int y)
    {
        FluidStack fluid = tank.getFluid();
        if (fluid != null)
        {
            TextureAtlasSprite texture = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getFluid().getStill().toString());
            int fill = (int) Math.ceil(136 * (float) tank.getFluidAmount() / tank.getCapacity());
            int offset = 0;
            gui.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            while (fill > 0)
            {
                int xCoord = x + 16 + offset + 1;
                int widthIn = fill >= 17 ? 17 : fill;
                int yCoord = y + 23;
                int heightIn = 6;
                bufferbuilder.pos((double)(xCoord), (double)(yCoord + heightIn), (double)gui.getZLevel()).tex((double)texture.getMinU(), (double)texture.getInterpolatedV(6)).endVertex();
                bufferbuilder.pos((double)(xCoord + widthIn), (double)(yCoord + heightIn), (double)gui.getZLevel()).tex(fill >= 16 ? (double)texture.getMaxU() : (double)texture.getInterpolatedU(fill), (double)texture.getInterpolatedV(6)).endVertex();
                bufferbuilder.pos((double)(xCoord + widthIn), (double)(yCoord + 0), (double)gui.getZLevel()).tex(fill >= 16 ? (double)texture.getMaxU() : (double)texture.getInterpolatedU(fill), (double)texture.getMinV()).endVertex();
                bufferbuilder.pos((double)(xCoord), (double)(yCoord + 0), (double)gui.getZLevel()).tex((double)texture.getMinU(), (double)texture.getMinV()).endVertex();
                offset += 18;
                fill -= 17;
            }
            tessellator.draw();
        }
    }
}
