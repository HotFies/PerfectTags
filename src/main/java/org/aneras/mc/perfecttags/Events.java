package org.aneras.mc.perfecttags;

import org.aneras.mc.perfecttags.configs.Names;
import org.aneras.mc.perfecttags.configs.Tags;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.Objects;


public class Events implements Listener {
    private final Map<Player, Inventory> Inv1;
    private final Map<Player, Inventory> Inv2;
    public PerfectTags plugin;
    public Events(PerfectTags perfectTags, Map<Player, Inventory> tagInv, Map<Player, Inventory> tagInv2) {
        plugin = perfectTags;
        Inv1 = tagInv;
        Inv2 = tagInv2;
    }

    @EventHandler
    public void ClickInv(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
            if(e.getClickedInventory() == Inv1.get(p)) {
                if(e.getCurrentItem() != null) {
                    if(e.getCurrentItem().equals(e.getInventory().getItem(1))){
                        e.setCancelled(true);
                    }else if(e.getCurrentItem().equals(e.getInventory().getItem(48))) {
                        if(Objects.equals(Names.get().getString("Command"), "Null")){
                            p.closeInventory();
                        }else{
                            p.chat(Objects.requireNonNull(Names.get().getString("Command")));
                        }
                    }else if(e.getCurrentItem().equals(e.getInventory().getItem(49))) {
                        plugin.data.changeTag(p.getUniqueId(), Tags.get().getString("DefaultTag"));
                        p.chat("/tags");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Names.get().getString("SuccessChangeTag") + Tags.get().getString("DefaultTag")));
                        e.setCancelled(true);
                    }else if(e.getCurrentItem().equals(e.getInventory().getItem(50))) {
                        if(PerfectTags.isFull(Inv1.get(p))){
                            p.openInventory(Inv2.get(p));
                        }
                    }else{
                        String name = e.getCurrentItem().getItemMeta().getDisplayName();
                        if (p.hasPermission("PerfectTags." + name)) {
                            plugin.data.changeTag(p.getUniqueId(), name);
                            p.chat("/tags");
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Names.get().getString("SuccessChangeTag") + name));
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Names.get().getString("Permission"))));
                        }
                        e.setCancelled(true);
                    }
                }
            }else if(e.getClickedInventory() == Inv2.get(p)){
                if(e.getCurrentItem() != null) {
                    if(e.getCurrentItem().equals(e.getInventory().getItem(1))){
                        e.setCancelled(true);
                    }else if(e.getCurrentItem().equals(e.getInventory().getItem(48))) {
                        p.openInventory(Inv1.get(p));
                    }else if(e.getCurrentItem().equals(e.getInventory().getItem(49))) {
                        plugin.data.changeTag(p.getUniqueId(), Tags.get().getString("DefaultTag"));
                        p.chat("/tags");
                        p.openInventory(Inv2.get(p));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Names.get().getString("SuccessChangeTag") + Tags.get().getString("DefaultTag")));
                        e.setCancelled(true);
                    }
                    else{
                        String name = e.getCurrentItem().getItemMeta().getDisplayName();
                        if (p.hasPermission("PerfectTags." + name)) {
                            plugin.data.changeTag(p.getUniqueId(), name);
                            p.chat("/tags");
                            p.openInventory(Inv2.get(p));
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Names.get().getString("SuccessChangeTag") + name));
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Names.get().getString("Permission"))));
                        }
                        e.setCancelled(true);
                    }
                }
            }
    }

}
