package eu.artbytefilip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private Connection connection;
    private String tableName = "betterfishing";

    public PlayerJoinListener(Database db) {
        // Inicializácia pripojenia k databáze pri spustení pluginu
        this.connection = db.connection;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Získať hráča a jeho UUID
        UUID playerUUID = event.getPlayer().getUniqueId();

        // Vložiť údaje do databázy
        insertPlayerData(playerUUID);
    }

    private void insertPlayerData(UUID playerUUID) {
        try {
            // Kontrola, či hráč už existuje v tabuľke
            if (!playerExists(playerUUID)) {
                // Vytvorenie príkazu SQL pre vloženie údajov do tabuľky
                String sql = "INSERT INTO " + tableName + " (uuid, rank, level, xp_level, total_fish_caught, " +
                        "week_fish_caught, daily_fish_caught, total_longest_fish, week_longest_fish, daily_longest_fish, "
                        +
                        "total_money_earned) " +
                        "VALUES (?, 'default', 1, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0);";

                // Príprava a vykonanie príkazu
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, playerUUID.toString());
                    statement.executeUpdate();
                }

                System.out.println("Údaje boli vložené do databázy pre nového hráča s UUID: " + playerUUID);
            } else {
                System.out.println(
                        "Hráč s UUID: " + playerUUID + " už existuje v databáze. Neboli vložené žiadne nové údaje.");
            }
        } catch (SQLException e) {
            System.out.println("Nepodarilo sa vložiť údaje do databázy: " + e.getMessage());
        }
    }

    private boolean playerExists(UUID playerUUID) throws SQLException {
        // Kontrola, či hráč už existuje v tabuľke
        String sql = "SELECT 1 FROM " + tableName + " WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, playerUUID.toString());
            return statement.executeQuery().next();
        }
    }

}
