package kpc.api;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public final class ComputerPosition{
    public final World world;
    public final int x;
    public final int y;
    public final int z;

    public ComputerPosition(World world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean exists(){
        return this.world.blockExists(this.x, this.y, this.z);
    }

    public TileEntity tile(){
        return this.world.getTileEntity(this.x, this.y, this.z);
    }

    public TileEntity tile(ForgeDirection dir){
        if(dir == ForgeDirection.UNKNOWN){
            return null;
        }

        return this.world.getTileEntity(this.x + dir.offsetX, this.y + dir.offsetY, this.z + dir.offsetZ);
    }

    public Block block(){
        return this.world.getBlock(this.x, this.y, this.z);
    }

    public int metadata(){
        return this.world.getBlockMetadata(this.x, this.y, this.z);
    }
}