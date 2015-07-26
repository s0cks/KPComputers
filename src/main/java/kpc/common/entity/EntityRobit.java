package kpc.common.entity;

import kpc.common.KPComputers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public final class EntityRobit
extends Entity{
    public static final int DATA_RUNNING = 0x2;

    public EntityRobit(World world) {
        super(world);
    }

    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(DATA_RUNNING, 0);
    }

    @Override
    public void kill(){
        super.kill();
        if(!this.worldObj.isRemote){
            ItemStack stack = new ItemStack(KPComputers.itemRobit, 1);
            EntityItem entityItem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, stack);
            entityItem.delayBeforeCanPickup = 15;
            this.worldObj.spawnEntityInWorld(entityItem);
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound comp) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound comp) {

    }
}