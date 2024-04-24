package eu.artbytefilip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BetterDrops implements Listener {

    private Connection connection;

    public BetterDrops(Database db) {
        this.connection = db.connection;
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent e) {

        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();
        FishHook hook = e.getHook();
        Entity entityOfCatchedFish = e.getCaught();

        if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH && entityOfCatchedFish != null) {
            e.setCancelled(true);
            hook.remove();

            Fish catchedFish = catchFish();

            updateFishStatistics(playerUUID);
            updateLongestFishStatistics(playerUUID, catchedFish.size);
            updateXpStatistics(playerUUID, catchedFish.getExp());

            player.giveExp(catchedFish.getExp());
            player.getInventory().addItem(createFish(catchedFish, player));
            player.sendMessage(ChatColor.WHITE + "Gratulujem! Chytil si rybu: " + catchedFish.getName());
        }

    }

    private void updateXpStatistics(UUID playerUUID, int xpLevel) {
        try {
            String sql = "UPDATE betterfishing SET " +
                    "xp_level = xp_level + ? " +
                    "WHERE uuid = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, xpLevel); 
                statement.setString(2, playerUUID.toString());
                statement.executeUpdate();
            }

            System.out.println(
                    "Hráč s UUID: " + playerUUID + " chytil rybu. Aktualizovala sa štatistika o xp_level.");
        } catch (SQLException err) {
            System.out.println("Nepodarilo sa aktualizovať štatistiku o xp_level: " + err.getMessage());
        }
    }

    private void updateFishStatistics(UUID playerUUID) {
        try {
            // Vytvorenie príkazu SQL pre aktualizáciu štatistík o chytených rýbach
            String sql = "UPDATE betterfishing SET " +
                    "total_fish_caught = total_fish_caught + 1, " +
                    "week_fish_caught = week_fish_caught + 1, " +
                    "daily_fish_caught = daily_fish_caught + 1 " +
                    "WHERE uuid = ?";

            // Príprava a vykonanie príkazu
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, playerUUID.toString());
                statement.executeUpdate();
            }

            System.out.println(
                    "Hráč s UUID: " + playerUUID + " chytil rybu. Aktualizovala sa štatistika o chytených rýb.");
        } catch (SQLException err) {
            System.out.println("Nepodarilo sa aktualizovať štatistiku o chytených rýbach: " + err.getMessage());
        }
    }

    private void updateLongestFishStatistics(UUID playerUUID, double catchedFishSize) {
        try {
            // Vytvorenie príkazu SQL pre aktualizáciu štatistík o najdlhších chytených
            // rýbach
            String sql = "UPDATE betterfishing SET " +
                    "total_longest_fish = GREATEST(total_longest_fish, ?), " +
                    "week_longest_fish = GREATEST(week_longest_fish, ?), " +
                    "daily_longest_fish = GREATEST(daily_longest_fish, ?) " +
                    "WHERE uuid = ?;";

            // Príprava a vykonanie príkazu
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDouble(1, catchedFishSize);
                statement.setDouble(2, catchedFishSize);
                statement.setDouble(3, catchedFishSize);
                statement.setString(4, playerUUID.toString());
                statement.executeUpdate();
            }

            System.out.println("Hráč s UUID: " + playerUUID + " chytil rybu s veľkosťou: " + catchedFishSize
                    + ". Aktualizovala sa štatistika o najdlhších chytených rýb.");
        } catch (SQLException err) {
            System.out.println(
                    "Nepodarilo sa aktualizovať štatistiku o najdlhších chytených rýbach: " + err.getMessage());
        }
    }

    public Fish catchFish() {
        double randomSize = getRandomSize();

        // Zoznam rýb s priradenými percentuálnymi hodnotami
        List<Fish> availableFish = new ArrayList<>();
        availableFish.add(new Fish("Salmon", Material.SALMON, randomSize, (int) (randomSize / 5), 1));
        availableFish.add(new Fish("Trout", Material.SALMON, randomSize, (int) (randomSize / 5), 1));
        availableFish.add(new Fish("Tuna", Material.SALMON, randomSize, (int) (randomSize / 5), 1));
        availableFish.add(new Fish("Cod", Material.SALMON, randomSize, (int) (randomSize / 5), 1));
        availableFish.add(new Fish("Bass", Material.SALMON, randomSize, (int) (randomSize / 5), 1));
        availableFish.add(new Fish("Catfish", Material.SALMON, randomSize, (int) (randomSize / 5), 1));
        availableFish.add(new Fish("Swordfish", Material.SALMON, randomSize, (int) (randomSize / 5), 1));
        availableFish.add(new Fish("Haddock", Material.SALMON, randomSize, (int) (randomSize / 5), 1));
        availableFish.add(new Fish("Mackerel", Material.SALMON, randomSize, (int) (randomSize / 5), 1));
        availableFish.add(new Fish("Carp", Material.SALMON, randomSize, (int) (randomSize / 5), 1));
        // Ďalšie rýby...

        // Generovanie náhodného čísla od 0 do 100
        Random random = new Random();
        int randomNumber = random.nextInt(101);

        // Náhodný výber ryby podľa percentuálneho rozdelenia
        int cumulativePercentage = 0;
        for (Fish fish : availableFish) {
            cumulativePercentage += fishPercentage(fish); // získanie percentuálneho podielu ryby
            if (randomNumber <= cumulativePercentage) {
                return fish; // vrátenie vybranej ryby
            }
        }

        // Ak sa nedosiahne výber ryby, vráti sa náhodná ryba (tu môžete prispôsobiť
        // podľa potreby)
        return availableFish.get(random.nextInt(availableFish.size()));
    }

    private double getRandomSize() {
        Random random = new Random();
        double randomSize = 0.1 + (3500 - 0.1) * random.nextDouble();
        return Math.round(randomSize * 100.0) / 100.0;
    }

    private int fishPercentage(Fish fish) {
        switch (fish.getName()) {
            case "Specialna ryba":
                return 0; // počet percent

            default:
                return 0; // Default hodnota, ak sa nezhoduje s žiadnou konkrétnou rýbou
        }
    }

    public static class Fish {
        private String name;
        private Material material;
        private double size;
        private int exp;
        private int modelData;

        public Fish(String name, Material material, double size, int exp, int modelData) {
            this.name = name;
            this.material = material;
            this.size = size;
            this.exp = exp;
            this.modelData = modelData;
        }

        public String getName() {
            return name;
        }

        public Material getMaterial() {
            return material;
        }

        public double getSize() {
            return size;
        }

        public int getExp() {
            return exp;
        }

        public int getModelData() {
            return modelData;
        }
    }

    private String getCategoryBySize(double size) {
        if (size >= 0 && size < 1750) {
            return "&7COMMON";
        } else if (size >= 1750 && size < 2975) {
            return "&8UNCOMMON";
        } else if (size >= 2975 && size < 3325) {
            return "&aRARE";
        } else if (size >= 3325 && size < 3465) {
            return "&4LEGENDARY";
        } else if (size >= 3465 && size <= 3500 + size) {
            return "&dMYTHIC";
        } else {
            return "UNKNOWN";
        }
    }

    private ChatColor getColorBySize(double size) {
        if (size >= 0 && size < 1750) {
            return ChatColor.GRAY;
        } else if (size >= 1750 && size < 2975) {
            return ChatColor.DARK_GRAY;
        } else if (size >= 2975 && size < 3325) {
            return ChatColor.GREEN;
        } else if (size >= 3325 && size < 3465) {
            return ChatColor.RED;
        } else if (size >= 3465 && size <= 3500 + size) {
            return ChatColor.LIGHT_PURPLE;
        } else {
            return ChatColor.GRAY;
        }
    }

    private ItemStack createFish(Fish fish, Player player) {
        ItemStack item = new ItemStack(fish.getMaterial());
        ItemMeta meta = item.getItemMeta();

        // Nastavenie názvu
        meta.setDisplayName(getColorBySize(fish.getSize()) + fish.getName());

        // Nastavenie lore (popisu)
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "Caught by: " + ChatColor.WHITE + player.getName());
        lore.add(ChatColor.AQUA + "Measures: " + ChatColor.WHITE + fish.getSize() + "cm");
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&l" + getCategoryBySize(fish.getSize())));
        meta.setLore(lore);

        // Nastavenie custom modelu
        meta.setCustomModelData(fish.modelData);

        item.setItemMeta(meta);
        return item;
    }
}
