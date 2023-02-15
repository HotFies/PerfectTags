package org.aneras.mc.perfecttags;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.aneras.mc.perfecttags.MySQLPackage.UseMySQL;
import org.aneras.mc.perfecttags.MySQLPackage.MySQL;
import org.aneras.mc.perfecttags.PlaceHolders.RegisterPlaceHolder;
import org.aneras.mc.perfecttags.commands.TagCommand;
import org.aneras.mc.perfecttags.configs.Names;
import org.aneras.mc.perfecttags.configs.Tags;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;


public final class PerfectTags extends JavaPlugin implements Listener {
    public MySQL SQL;
    public UseMySQL data;
    private static final Map<Player, Inventory> tagInv = new HashMap<>();
    private static final Map<Player, Inventory> tagInv2 = new HashMap<>();
    @Override
    public void onEnable() {
        if(Bukkit.getPluginManager().getPlugin("PlaceHolderAPI") != null){
            if(Bukkit.getPluginManager().getPlugin("LuckPerms") == null) {
                getLogger().warning(ChatColor.RED + "The LuckPerms wasn't found. The PerfectTags may not work correctly");

            }
            new RegisterPlaceHolder(this).register();
            Bukkit.getPluginManager().registerEvents(this, this);

            List<String> rarity = new ArrayList<>();
            Tags.setup();
            Tags.get().addDefault("DefaultTag", "Player");
            Tags.get().addDefault("Rarity", rarity);
            Tags.get().options().copyDefaults(true);
            Tags.save();
            Names.setup();
            /////////////////////////////////////
            Names.get().addDefault("Storage", "MySQL");
            Names.get().addDefault("host", "localhost");
            Names.get().addDefault("port", "3306");
            Names.get().addDefault("database", "PerfectTable");
            Names.get().addDefault("username", "Default");
            Names.get().addDefault("password", "Default");
            Names.get().addDefault("NameOfInventory", "Tags");
            Names.get().addDefault("SuccessChangeTag", "[PerfectTags]You successful changed tag to ");
            Names.get().addDefault("Permission", "[PerfectTags]You don't have permission");
            Names.get().addDefault("Close", "Close");
            Names.get().addDefault("Back", "Back");
            Names.get().addDefault("Default", "Default Tag");
            Names.get().addDefault("NextPage", "Next page");
            Names.get().addDefault("NameOfInv", "Tags");
            Names.get().addDefault("LoreForTags", "Rarity: ");
            Names.get().addDefault("Command", "Null");

            Names.get().addDefault("HeadSkin", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxZDZlZGE4M2VkMmMyNGRjZGNjYjFlMzNkZjM2OTRlZWUzOTdhNTcwMTIyNTViZmM1NmEzYzI0NGJjYzQ3NCJ9fX0=");
            Names.get().addDefault("Lobby", true);
            Names.get().options().copyDefaults(true);
            Names.save();

            this.SQL = new MySQL();
            data = new UseMySQL(this);
            if(Objects.equals(Names.get().get("Storage"), "MySQL")) {
                try {
                    SQL.Connect();
                } catch (ClassNotFoundException | SQLException e) {
                    //e.printStackTrace();
                    Bukkit.getConsoleSender().sendMessage("[PerfectTags] " + ChatColor.RED + "Database is not connected");
                }
                if (SQL.isConnected()) {
                    Bukkit.getLogger().info(ChatColor.AQUA + "PerfectTags: " + ChatColor.GREEN + "Database is connected");
                    data.createTable();

                }
            }

            Objects.requireNonNull(getServer().getPluginCommand("tags")).setExecutor(new TagCommand(this, tagInv, tagInv2));
            Bukkit.getPluginManager().registerEvents(new Events(this, tagInv, tagInv2), this);
            Bukkit.getConsoleSender().sendMessage("[PerfectTags] " + "PerfectTags loaded");
        }else{
            getLogger().warning("[PerfectTags] " + ChatColor.RED + "PlaceHolderAPI doesn't exists. This plugin is required");
            Bukkit.getPluginManager().disablePlugin(this);
        }

    }

    @Override
    public void onDisable() {
        this.SQL.Disconnect();
        Bukkit.getConsoleSender().sendMessage("[PerfectTags] " + "Database disconnected");
        Bukkit.getConsoleSender().sendMessage("[PerfectTags] " + "PerfectTags disabled");
    }
    @EventHandler
    public void OnJoin(PlayerJoinEvent e){
        data.createPlayer(e.getPlayer());
    }

    public static boolean isFull(Inventory inv){
        for(ItemStack item : inv.getContents()){
            if(item == null){
                return false;
            }
        }
        return true;
    }
    public void tagInvCreate(Player p){
        Inventory Inv1 = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Names.get().getString("NameOfInv"))));
        ItemStack newTag = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta newTagMeta = newTag.getItemMeta();
        ItemStack close = PerfectTags.getSkull(Names.get().getString("HeadSkin"));
        ItemMeta closeMeta = close.getItemMeta();
        ItemStack close2 = PerfectTags.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGMzMDFhMTdjOTU1ODA3ZDg5ZjljNzJhMTkyMDdkMTM5M2I4YzU4YzRlNmU0MjBmNzE0ZjY5NmE4N2ZkZCJ9fX0=");
        ItemMeta closeMeta2 = close2.getItemMeta();
        ItemStack Default = new ItemStack(Material.NAME_TAG);
        ItemMeta DefaultMeta = Default.getItemMeta();
        ItemStack nextPage = PerfectTags.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjU0ZmFiYjE2NjRiOGI0ZDhkYjI4ODk0NzZjNmZlZGRiYjQ1MDVlYmE0Mjg3OGM2NTNhNWQ3OTNmNzE5YjE2In19fQ==");
        ItemMeta nextPageMeta = nextPage.getItemMeta();

        closeMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Names.get().getString("Close"))));
        close.setItemMeta(closeMeta);
        closeMeta2.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Names.get().getString("Back"))));
        close2.setItemMeta(closeMeta2);
        DefaultMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Names.get().getString("Default"))));
        DefaultMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        Default.setItemMeta(DefaultMeta);
        if(data.getTag(p.getUniqueId()).equals(Tags.get().getString("DefaultTag"))){
            Default.addUnsafeEnchantment(Enchantment.LUCK, 1);
        }
        nextPageMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Names.get().getString("NextPage"))));
        nextPage.setItemMeta(nextPageMeta);

        newTagMeta.setDisplayName(" ");
        newTag.setItemMeta(newTagMeta);
        Inv1.setItem(0, newTag);
        Inv1.setItem(1, newTag);
        Inv1.setItem(2, newTag);
        Inv1.setItem(3, newTag);
        Inv1.setItem(4, newTag);
        Inv1.setItem(5, newTag);
        Inv1.setItem(6, newTag);
        Inv1.setItem(7, newTag);
        Inv1.setItem(8, newTag);
        Inv1.setItem(9, newTag);
        Inv1.setItem(17, newTag);
        Inv1.setItem(18, newTag);
        Inv1.setItem(26, newTag);
        Inv1.setItem(27, newTag);
        Inv1.setItem(35, newTag);
        Inv1.setItem(36, newTag);
        Inv1.setItem(44, newTag);
        Inv1.setItem(45, newTag);
        Inv1.setItem(46, newTag);
        Inv1.setItem(47, newTag);
        Inv1.setItem(50, newTag);
        Inv1.setItem(51, newTag);
        Inv1.setItem(52, newTag);
        Inv1.setItem(53, newTag);

        Inv1.setItem(48, close);
        Inv1.setItem(49, Default);

        tagInv.put(p, Inv1);
    }
    public void tagInv2Create(Player p){
        Inventory Inv2 = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Names.get().getString("NameOfInv"))));
        ItemStack newTag = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta newTagMeta = newTag.getItemMeta();
        ItemStack close = PerfectTags.getSkull(Names.get().getString("HeadSkin"));
        ItemMeta closeMeta = close.getItemMeta();
        ItemStack close2 = PerfectTags.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGMzMDFhMTdjOTU1ODA3ZDg5ZjljNzJhMTkyMDdkMTM5M2I4YzU4YzRlNmU0MjBmNzE0ZjY5NmE4N2ZkZCJ9fX0=");
        ItemMeta closeMeta2 = close2.getItemMeta();
        ItemStack Default = new ItemStack(Material.NAME_TAG);
        ItemMeta DefaultMeta = Default.getItemMeta();
        ItemStack nextPage = PerfectTags.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjU0ZmFiYjE2NjRiOGI0ZDhkYjI4ODk0NzZjNmZlZGRiYjQ1MDVlYmE0Mjg3OGM2NTNhNWQ3OTNmNzE5YjE2In19fQ==");
        ItemMeta nextPageMeta = nextPage.getItemMeta();

        closeMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Names.get().getString("Close"))));
        close.setItemMeta(closeMeta);
        closeMeta2.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Names.get().getString("Back"))));
        close2.setItemMeta(closeMeta2);
        DefaultMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Names.get().getString("Default"))));
        DefaultMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        Default.setItemMeta(DefaultMeta);
        if(data.getTag(p.getUniqueId()).equals(Tags.get().getString("DefaultTag"))){
            Default.addUnsafeEnchantment(Enchantment.LUCK, 1);
        }
        nextPageMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Names.get().getString("NextPage"))));
        nextPage.setItemMeta(nextPageMeta);

        newTagMeta.setDisplayName(" ");
        newTag.setItemMeta(newTagMeta);
        Inv2.setItem(0, newTag);
        Inv2.setItem(1, newTag);
        Inv2.setItem(2, newTag);
        Inv2.setItem(3, newTag);
        Inv2.setItem(4, newTag);
        Inv2.setItem(5, newTag);
        Inv2.setItem(6, newTag);
        Inv2.setItem(7, newTag);
        Inv2.setItem(8, newTag);
        Inv2.setItem(9, newTag);
        Inv2.setItem(17, newTag);
        Inv2.setItem(18, newTag);
        Inv2.setItem(26, newTag);
        Inv2.setItem(27, newTag);
        Inv2.setItem(35, newTag);
        Inv2.setItem(36, newTag);
        Inv2.setItem(44, newTag);
        Inv2.setItem(45, newTag);
        Inv2.setItem(46, newTag);
        Inv2.setItem(47, newTag);
        Inv2.setItem(50, newTag);
        Inv2.setItem(51, newTag);
        Inv2.setItem(52, newTag);
        Inv2.setItem(53, newTag);
        Inv2.setItem(48, close2);
        Inv2.setItem(49, Default);

        tagInv2.put(p, Inv2);
    }
    public static ItemStack getSkull(String value){
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", value));

        try {
            Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            mtd.setAccessible(true);
            mtd.invoke(skullMeta, profile);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }

        head.setItemMeta(skullMeta);
        return head;

    }

}

