package org.aneras.mc.perfecttags.configs;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Names {

    private static File file;
    private static FileConfiguration SavePlayer;
    public static void setup(){

        file = new File(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("PerfectTags")).getDataFolder() + File.separator + "Configs", "Names.yml");

        if (!file.exists()){
            try {
                file.createNewFile();
            }catch (IOException e){

            }
        }
        SavePlayer = YamlConfiguration.loadConfiguration(file);
    }
    public static FileConfiguration get(){
        return SavePlayer;
    }
    public static void save(){
        try {
            SavePlayer.save(file);
        }catch (IOException e){
            System.out.println("File gg");
        }
    }

    public static void reload() {
        SavePlayer = YamlConfiguration.loadConfiguration(file);
    }

}
