package eu.artbytefilip;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterCore extends JavaPlugin implements Listener {

    Database db = new Database("localhost", 3306, "minecraft", "root", "");

    @Override
    public void onEnable() {
        getLogger().log(Level.INFO, "{0}.onEnable()", this.getClass().getName());
        db.connectToDatabase();

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(db), this);
        Bukkit.getPluginManager().registerEvents(new BetterDrops(db), this);
        Bukkit.getPluginManager().registerEvents(new BetterGUI(), this);
        Bukkit.getPluginManager().registerEvents(new Prison(), this);

        getCommand("betterfishing").setExecutor(new BetterGUI());
        getCommand("prisonsellall").setExecutor(new Prison());

        getServer().getPluginManager().registerEvents(this, this);
    }
    
    @Override
    public void onDisable() {
        db.disconnectFromDatabase();
        getLogger().log(Level.INFO, "{0}.onDisable()", this.getClass().getName());
    }
}
