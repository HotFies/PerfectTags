package org.aneras.mc.perfecttags.commands;

import org.aneras.mc.perfecttags.PerfectTags;
import org.aneras.mc.perfecttags.configs.Names;
import org.aneras.mc.perfecttags.configs.Tags;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TagCommand implements CommandExecutor {
    private final Map<Player, Inventory> Inv1;
        private final Map<Player, Inventory> Inv2;
    public PerfectTags plugin;
    public TagCommand(PerfectTags perfectTags, Map<Player, Inventory> tagInv, Map<Player, Inventory> tagInv2) {
        plugin = perfectTags;
        Inv1 = tagInv;
        Inv2 = tagInv2;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        if(command.getName().equalsIgnoreCase("tags")){
            if(p.hasPermission("PerfectTags.Commands.Tags")){
                if(Names.get().getBoolean("Lobby")) {
                    if (args.length == 0) {
                        if(p.hasPermission("PerfectTags")) {
                            Inv1.remove(p);
                            Inv2.remove(p);
                            plugin.tagInvCreate(p);
                            plugin.tagInv2Create(p);
                            ArrayList<String> addTagLore = new ArrayList<>();
                            ItemStack addTag = new ItemStack(Material.NAME_TAG);
                            ItemMeta addTagMeta = addTag.getItemMeta();
                            List<String> rarityCheck = Tags.get().getStringList("Rarity");
                            ItemStack nextPage = PerfectTags.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjU0ZmFiYjE2NjRiOGI0ZDhkYjI4ODk0NzZjNmZlZGRiYjQ1MDVlYmE0Mjg3OGM2NTNhNWQ3OTNmNzE5YjE2In19fQ==");
                            ItemMeta nextPageMeta = nextPage.getItemMeta();
                            nextPageMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Names.get().getString("NextPage"))));
                            nextPage.setItemMeta(nextPageMeta);
                            for (String check : rarityCheck) {
                                List<String> checkTag = Tags.get().getStringList(check);
                                for (String tag : checkTag) {
                                    addTagLore.add(ChatColor.translateAlternateColorCodes('&', Names.get().getString("LoreForTags") + check));
                                    addTagMeta.setLore(addTagLore);
                                    addTagMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', tag));
                                    addTagMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                    addTag.setItemMeta(addTagMeta);
                                    if(plugin.data.getTag(p.getUniqueId()).equals(tag.replace('&', '§'))){
                                        addTag.addUnsafeEnchantment(Enchantment.LUCK, 1);
                                    }
                                    if(!PerfectTags.isFull(Inv1.get(p))) {
                                        Inv1.get(p).addItem(addTag);
                                    }else{
                                        Inv2.get(p).addItem(addTag);
                                    }
                                    addTagLore.clear();
                                }
                            }
                            if(PerfectTags.isFull(Inv1.get(p))) {
                                Inv1.get(p).setItem(50, nextPage);
                            }
                            p.openInventory(Inv1.get(p));
                        }else{
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Names.get().getString("Permission"))));
                        }
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("change")){
                        if(args.length == 1){
                            p.sendMessage(ChatColor.GOLD + "/tags change %Name%");
                            return true;
                        }
                        if (p.hasPermission("PerfectTags.Commands.TagChange")){
                            List<String> rarity = Tags.get().getStringList("Rarity");
                            for(String rar : rarity){
                                List<String> newList = Tags.get().getStringList(rar);
                                if(newList.contains(args[1])){
                                    if(p.hasPermission("PerfectTags.Tags" + args[1])){
                                        plugin.data.changeTag(p.getUniqueId(), args[1]);
                                        return true;
                                    }else{
                                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Names.get().getString("Permission"))));
                                    }
                                }
                            }
                        }
                    }else if (args[0].equalsIgnoreCase("create")) {
                        if (p.hasPermission("PerfectTags.Commands.TagCreate")) {
                            if (args.length == 1) {
                                p.sendMessage(ChatColor.GOLD + "/tag create rarity %NameOfRarity%");
                                p.sendMessage(ChatColor.GOLD + "/tag create add %NameOfRarity% %NameOfTag%");
                                return true;
                            }
                            if (args[1].equalsIgnoreCase("rarity")) {
                                List<String> rarity = Tags.get().getStringList("Rarity");
                                if(rarity.contains(args[2])){
                                    p.sendMessage(ChatColor.RED + "You can not add this rarity because this rarity already exist");
                                    return true;
                                }
                                rarity.add(args[2]);
                                Tags.get().set("Rarity", rarity);
                                List<String> newList = new ArrayList<>();
                                Tags.get().set(args[2], newList);
                                p.sendMessage(ChatColor.GREEN + "You successful created new rarity");
                                Tags.save();
                                Tags.reload();
                                return true;
                            } else if (args[1].equalsIgnoreCase("add")) {
                                List<String> newList = Tags.get().getStringList(args[2]);
                                if (newList.contains(args[3])) {
                                    p.sendMessage(ChatColor.RED + "You can not add this tag, because it exists");
                                    return true;
                                } else {
                                    ArrayList<String> newTagLore = new ArrayList<>();
                                    ItemStack newTag = new ItemStack(Material.NAME_TAG);
                                    ItemMeta newTagMeta = newTag.getItemMeta();
                                    newTagLore.add(ChatColor.translateAlternateColorCodes('&', Names.get().getString("LoreForTags") + args[2]));
                                    newTagMeta.setLore(newTagLore);
                                    newTagMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', args[3]));
                                    newTag.setItemMeta(newTagMeta);

                                    if(!PerfectTags.isFull(Inv1.get(p))){
                                        Inv1.get(p).addItem(newTag);
                                    }else{
                                        Inv2.get(p).addItem(newTag);
                                    }
                                    newList.add(args[3]);
                                }
                                Tags.get().set(args[2], newList);
                                Tags.save();
                                Tags.reload();
                                p.sendMessage(ChatColor.GREEN + "You successful added new Tag");
                                return true;
                            }
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Names.get().getString("Permission"))));
                        }
                    }
                }else{
                    p.sendMessage(Objects.requireNonNull(Names.get().getString("CanOpenMenu")));
                }
            }else{
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Names.get().getString("Permission"))));
            }
        }
        return true;
    }
}
