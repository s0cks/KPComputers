package kpc.common;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import kpc.common.entity.EntityRobit;
import kpc.common.items.ItemScrewdriver;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public final class KPCEventHandler{
    private static KPCEventHandler instance;

    public static KPCEventHandler instance(){
        return instance == null ? instance = new KPCEventHandler() : instance;
    }

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent e){
        if(e.target instanceof EntityRobit){
            if(e.entityPlayer.getHeldItem().getItem() instanceof ItemScrewdriver){
                if(e.entityPlayer.isSneaking()){
                    ((EntityRobit) e.target).kill();
                } else{

                }
            }
        }
    }
}