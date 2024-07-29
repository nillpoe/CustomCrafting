package xyz.nillpoe.crafting.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import xyz.nillpoe.crafting.inventory.BaseInventory;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        handleInventoryEvent(event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        handleInventoryEvent(event);
    }

    private void handleInventoryEvent(InventoryEvent event) {
        if (event.getInventory().getHolder() instanceof BaseInventory holder) {
            if (event instanceof InventoryClickEvent) {
                holder.onClick((InventoryClickEvent) event);
            } else if (event instanceof InventoryCloseEvent) {
                holder.onClose((InventoryCloseEvent) event);
            }
        }
    }
}