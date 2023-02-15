package org.aneras.mc.perfecttags.MySQLPackage;

import org.aneras.mc.perfecttags.PerfectTags;
import org.aneras.mc.perfecttags.configs.Tags;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UseMySQL {
    private PerfectTags plugin;
    public UseMySQL(PerfectTags plugin){
        this.plugin = plugin;
    }

    public void createTable(){
        PreparedStatement preparedStatement;
        try {
            preparedStatement = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS tags "
                    + "(NAME VARCHAR(100),UUID VARCHAR(100),TAG VARCHAR(100), PRIMARY KEY (NAME))");
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createPlayer(Player player){
        try {
            UUID uuid = player.getUniqueId();
            if(!exists(uuid)){
                PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO tags (NAME,UUID,TAG) VALUES (?,?,?)");
                ps.setString(1, player.getName());
                ps.setString(2, uuid.toString());
                ps.setString(3, Tags.get().getString("DefaultTag"));
                ps.executeUpdate();
                return;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public boolean exists(UUID uuid){
        try {
            PreparedStatement preparedStatement = plugin.SQL.getConnection().prepareStatement("SELECT * FROM tags WHERE UUID=?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet results = preparedStatement.executeQuery();
            if(results.next()){
                return true;
            }
            return false;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public void changeTag(UUID uuid, String name){
        try {
            PreparedStatement preparedStatement = plugin.SQL.getConnection().prepareStatement("UPDATE tags SET TAG=? WHERE UUID=?");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public String getTag(UUID uuid){
        try{
            PreparedStatement preparedStatement = plugin.SQL.getConnection().prepareStatement("SELECT TAG FROM tags WHERE UUID=?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet results = preparedStatement.executeQuery();
            String Tag;
            if(results.next()){
                Tag = results.getString("TAG");
                return Tag;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
