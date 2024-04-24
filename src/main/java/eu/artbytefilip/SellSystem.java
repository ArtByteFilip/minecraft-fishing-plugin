package eu.artbytefilip;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class SellSystem {

    public void SellFishAll(Player player, InventoryClickEvent e) {
        PlayerInventory inventory = player.getInventory();
        Inventory plguinInventory = e.getInventory();
        
        double coins = 0.0D;

        // Prejdite všetky položky v inventári hráča
        for (ItemStack item : inventory.getContents()) {
            if (item == null || item.getType().isAir()) {
                continue; // Preskočte prázdne miesta
            }

            // Overte, či položka obsahuje lore
            ItemMeta meta = item.getItemMeta();
            if (meta == null || !meta.hasLore()) {
                continue; // Preskočte položky bez lore
            }

            int amount = item.getAmount();

            // Získajte všetky riadky lore
            for (String loreLine : meta.getLore()) {
                // Overte, či lore obsahuje "Measures:"
                if (loreLine.contains("Measures:")) {
                    // Získajte hodnotu merania z lore
                    String[] parts = loreLine.split("Measures:");

                    if (parts.length >= 2) {

                        try {
                            // Skúste získať hodnotu merania ako celé číslo
                            String numericValue = parts[1].replaceAll("[^0-9.]", "");
                            double measurement = Double.parseDouble(numericValue.trim());

                            coins = coins + ((measurement / 10) * amount);

                            item.setAmount(0);
                        } catch (NumberFormatException err) {
                            // Ak sa nepodarí analyzovať hodnotu merania, preskočte to
                            System.out.println("Error: " + err);
                        }
                    }
                }
            }
        }

        for (ItemStack item : plguinInventory.getContents()) {
            if (item == null || item.getType().isAir()) {
                continue; // Preskočte prázdne miesta
            }

            // Overte, či položka obsahuje lore
            ItemMeta meta = item.getItemMeta();
            if (meta == null || !meta.hasLore()) {
                continue; // Preskočte položky bez lore
            }

            int amount = item.getAmount();

            // Získajte všetky riadky lore
            for (String loreLine : meta.getLore()) {
                // Overte, či lore obsahuje "Measures:"
                if (loreLine.contains("Measures:")) {
                    // Získajte hodnotu merania z lore
                    String[] parts = loreLine.split("Measures:");

                    if (parts.length >= 2) {

                        try {
                            // Skúste získať hodnotu merania ako celé číslo
                            String numericValue = parts[1].replaceAll("[^0-9.]", "");
                            double measurement = Double.parseDouble(numericValue.trim());

                            coins = coins + ((measurement / 10) * amount);

                            item.setAmount(0);
                        } catch (NumberFormatException err) {
                            // Ak sa nepodarí analyzovať hodnotu merania, preskočte to
                            System.out.println("Error: " + err);
                        }
                    }
                }
            }
        }


        if (coins != 0.0D) {
            player.sendMessage("You earned: " + coins + " $");
        }
    }

    public void SellFishSelectedOnly(Player player, InventoryClickEvent e) {
        Inventory inventory = e.getInventory();
        double coins = 0.0D;

        for (ItemStack item : inventory.getContents()) {
            if (item == null || item.getType().isAir()) {
                continue;
            }

            ItemMeta meta = item.getItemMeta();
            if (meta == null || !meta.hasLore()) {
                continue;
            }

            int amount = item.getAmount();

            for (String loreLine : meta.getLore()) {
                if (loreLine.contains("Measures:")) {
                    String[] parts = loreLine.split("Measures:");
                    if (parts.length >= 2) {
                        try {
                            String numericValue = parts[1].replaceAll("[^0-9.]", "");
                            double measurement = Double.parseDouble(numericValue.trim());

                            coins = coins + ((measurement / 10) * amount);

                            item.setAmount(0);
                        } catch (NumberFormatException err) {
                            System.out.println("Error: " + err);
                        }
                    }
                }
            }
        }

        if (coins != 0.0D) {
            player.sendMessage("You earned: " + coins + " $");
        }
    }
}