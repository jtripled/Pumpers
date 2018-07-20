package com.jtripled.pumpers.block;

import com.jtripled.pumpers.Pumpers;
import com.jtripled.pumpers.network.MessageFluidCapacity;
import com.jtripled.pumpers.tile.TileTank;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author jtripled
 */
public final class BlockTank extends Block
{
    public static final String NAME = "tank";
    public static final ResourceLocation RESOURCE = new ResourceLocation(Pumpers.ID, NAME);
    public static final int GUI_ID = 2;
    
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");
    
    public BlockTank()
    {
        super(Material.IRON);
        this.setUnlocalizedName(NAME);
        this.setRegistryName(RESOURCE);
        this.setCreativeTab(CreativeTabs.REDSTONE);
        this.setDefaultState(this.getDefaultState().withProperty(UP, false).withProperty(DOWN, false));
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            TileTank tank = ((TileTank) world.getTileEntity(pos)).getBaseTank();
            player.openGui(Pumpers.getInstance(), GUI_ID, world, tank.getPos().getX(), tank.getPos().getY(), tank.getPos().getZ());
        }
        return true;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {UP, DOWN});
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return state.withProperty(UP, canConnect(world.getBlockState(pos.up())))
                    .withProperty(DOWN, canConnect(world.getBlockState(pos.down())));
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }
    
    @Override
    public TileTank createTileEntity(World world, IBlockState state)
    {
        return new TileTank();
    }
    
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    public boolean isTopSolid(IBlockState state)
    {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    public static boolean canConnect(IBlockState otherState)
    {
        return otherState.getBlock() instanceof BlockTank;
    }
    
    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        TileTank baseTank = null;
        TileTank newTank = (TileTank) world.getTileEntity(pos);
        TileEntity tileUp = world.getTileEntity(pos.up());
        TileEntity tileDown = world.getTileEntity(pos.down());
        boolean up = tileUp instanceof TileTank;
        boolean down = tileDown instanceof TileTank;
        
        // Merge Tanks
        if (up && down)
        {
            baseTank = newTank.getBaseTank();
            TileTank aboveTank = (TileTank) tileUp;
            baseTank.setCapacity(baseTank.getCapacity() + aboveTank.getCapacity() + Fluid.BUCKET_VOLUME * 16);
            baseTank.fill(aboveTank.drain(Integer.MAX_VALUE, true), true);
            aboveTank.setCapacity(Fluid.BUCKET_VOLUME * 16);
            while (tileUp instanceof TileTank)
            {
                ((TileTank) tileUp).baseTank = null;
                tileUp = world.getTileEntity(tileUp.getPos().up());
            }
            if (!world.isRemote)
            Pumpers.getNetwork().sendToAll(new MessageFluidCapacity(baseTank.getPos(), baseTank.getCapacity()));
        }
        
        // Extend Down
        else if (up)
        {
            baseTank = newTank;
            TileTank aboveTank = (TileTank) tileUp;
            baseTank.setCapacity(aboveTank.getCapacity() + Fluid.BUCKET_VOLUME * 16);
            baseTank.fill(aboveTank.drain(Integer.MAX_VALUE, true), true);
            aboveTank.setCapacity(Fluid.BUCKET_VOLUME * 16);
            while (tileUp instanceof TileTank)
            {
                ((TileTank) tileUp).baseTank = null;
                tileUp = world.getTileEntity(tileUp.getPos().up());
            }
        }
        
        // Extend Up
        else if (down)
        {
            baseTank = newTank.getBaseTank();
            baseTank.setCapacity(baseTank.getCapacity() + Fluid.BUCKET_VOLUME * 16);
        }
    }
    
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileTank baseTank = null;
        TileTank lostTank = (TileTank) world.getTileEntity(pos);
        TileEntity tileUp = world.getTileEntity(pos.up());
        TileEntity tileDown = world.getTileEntity(pos.down());
        boolean up = tileUp instanceof TileTank;
        boolean down = tileDown instanceof TileTank;
        
        // Split Tanks
        if (up && down)
        {
            baseTank = lostTank.getBaseTank();
            int oldCapacity = baseTank.getCapacity();
            int newCapacity = (lostTank.getPos().getY() - baseTank.getPos().getY()) * Fluid.BUCKET_VOLUME * 16;
            TileTank aboveTank = (TileTank) tileUp;
            
            /* TODO: CHECK FOR NULL */
            FluidStack drained = baseTank.drain(baseTank.drain(Integer.MAX_VALUE, false).amount - newCapacity, true);
            
            baseTank.setCapacity(newCapacity);
            aboveTank.setCapacity(oldCapacity - newCapacity - Fluid.BUCKET_VOLUME * 16);
            aboveTank.fill(drained, true);
            while (tileUp instanceof TileTank)
            {
                ((TileTank) tileUp).baseTank = null;
                tileUp = world.getTileEntity(tileUp.getPos().up());
            }
        }
        
        // Shrink Bottom
        else if (up)
        {
            baseTank = (TileTank) tileUp;
            baseTank.setCapacity(lostTank.getCapacity() - Fluid.BUCKET_VOLUME * 16);
            baseTank.fill(lostTank.drain(Integer.MAX_VALUE, true), true);
            while (tileUp instanceof TileTank)
            {
                ((TileTank) tileUp).baseTank = null;
                tileUp = world.getTileEntity(tileUp.getPos().up());
            }
        }
        
        // Shrink Top
        else if (down)
        {
            baseTank = lostTank.getBaseTank();
            baseTank.setCapacity(baseTank.getCapacity() - Fluid.BUCKET_VOLUME * 16);
        }
        
        super.breakBlock(world, pos, state);
    }
}
