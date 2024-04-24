package eu.artbytefilip;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;

public class BetterGUI implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            openMainMenu((Player) sender);
        } else {
            sender.sendMessage("This command can by executed only by player.");
        }
        return true;
    }

    @EventHandler
    public void clickEvent(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        String currentTitle = e.getView().getTitle();

        SellSystem sellSystem = new SellSystem();

        if (currentTitle.equalsIgnoreCase(ChatColor.BLUE + "Better Fishing - Main Menu") ||
                currentTitle.equalsIgnoreCase(ChatColor.DARK_GREEN + "Better Fishing - Fishing Shop")) {

            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem == null || clickedItem.getType() == Material.AIR) {
                return;
            } else {
                switch (clickedItem.getType()) {
                    case TNT:
                        player.closeInventory();
                        player.setHealth(0.0);
                        player.sendMessage("You just killed yourself.");
                        break;
                    case CHEST_MINECART:
                        player.closeInventory();
                        openShopMenu(player);
                        break;
                    case GOLD_INGOT:
                        sellSystem.SellFishSelectedOnly(player, e);
                        break;
                    case NETHERITE_INGOT:
                        sellSystem.SellFishAll(player, e);
                        break;
                    case BARRIER:
                        player.closeInventory();
                        openMainMenu(player);
                        break;
                    default:
                        break;
                }

                e.setCancelled(true);
            }

        }

        if (currentTitle.equalsIgnoreCase(ChatColor.DARK_GREEN + "Better Fishing - Fishing Shop")) {

            ItemStack clickedItem = e.getCurrentItem();

            if (clickedItem == null || clickedItem.getType() == Material.AIR) {
                return;
            } else {

                ItemMeta meta = clickedItem.getItemMeta();
                if (meta == null || !meta.hasLore()) {
                    return;
                }

                for (String loreLine : meta.getLore()) {
                    if (loreLine.contains("Measures:")) {
                        String[] parts = loreLine.split("Measures:");
                        if (parts.length >= 2) {
                            try {
                                Inventory pluginInventory = e.getInventory();
                                Inventory playerInventory = player.getInventory();

                                if (playerInventory.contains(clickedItem)) {
                                    playerInventory.removeItem(clickedItem);
                                    pluginInventory.addItem(clickedItem);
                                } else if (pluginInventory.contains(clickedItem)) {
                                    pluginInventory.removeItem(clickedItem);
                                    playerInventory.addItem(clickedItem);
                                }
                            } catch (NumberFormatException err) {
                                System.out.println("Error: " + err);
                            }
                        }
                    }
                }
            }
        }
    }

    public void openShopMenu(Player player) {
        Inventory shopMenu = Bukkit.createInventory(player, 45, ChatColor.DARK_GREEN + "Better Fishing - Fishing Shop");

        ItemStack sellAll = new ItemStack(Material.NETHERITE_INGOT);
        ItemMeta sellAll_meta = sellAll.getItemMeta();
        sellAll_meta.setDisplayName(ChatColor.GREEN + "SELL ALL");
        sellAll.setItemMeta(sellAll_meta);

        ItemStack sellSelected = new ItemStack(Material.GOLD_INGOT);
        ItemMeta sellSelected_meta = sellSelected.getItemMeta();
        sellSelected_meta.setDisplayName(ChatColor.GREEN + "SELL SELECTED ONLY");
        sellSelected.setItemMeta(sellSelected_meta);

        ItemStack backToHome = new ItemStack(Material.BARRIER);
        ItemMeta backToHome_meta = backToHome.getItemMeta();
        backToHome_meta.setDisplayName(ChatColor.RED + "BACK");
        backToHome.setItemMeta(backToHome_meta);

        ItemStack empty = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta empty_meta = empty.getItemMeta();
        empty_meta.setDisplayName(ChatColor.DARK_GRAY + "#");
        empty.setItemMeta(empty_meta);

        for (int i = 0; i < 45; i++) {
            switch (i) {
                case 39:
                    shopMenu.setItem(i, sellSelected);
                    break;
                case 41:
                    shopMenu.setItem(i, sellAll);
                    break;
                case 36:
                    shopMenu.setItem(i, backToHome);
                    break;
                case 37:
                case 38:
                case 40:
                case 42:
                case 43:
                case 44:
                    shopMenu.setItem(i, empty);
                    break;

                default:
                    break;
            }
        }

        player.openInventory(shopMenu);
    }

    public void openMainMenu(Player player) {
        Inventory mainMenu = Bukkit.createInventory(player, 27, ChatColor.BLUE + "Better Fishing - Main Menu");

        ItemStack lootChances = new ItemStack(Material.COD);
        ItemMeta lootChances_meta = lootChances.getItemMeta();
        lootChances_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&lLoot Chances"));
        List<String> lootChancesLore = new ArrayList<>();
        lootChancesLore.add("----------------------------------");
        lootChancesLore.add("• " + ChatColor.GRAY + "Common fish: 50% - 0cm to 1750cm");
        lootChancesLore.add("• " + ChatColor.DARK_GRAY + "Uncommon fish: 35% - 1750cm to 2975cm");
        lootChancesLore.add("• " + ChatColor.GREEN + "Rare fish: 10% - 2975cm to 3325cm");
        lootChancesLore.add("• " + ChatColor.RED + "Legendary fish: 4% - 3325cm to 3465cm");
        lootChancesLore.add("• " + ChatColor.LIGHT_PURPLE + "Mythic fish: 1% - 3465cm to 3500cm");
        lootChancesLore.add("----------------------------------");
        lootChances_meta.setLore(lootChancesLore);
        lootChances.setItemMeta(lootChances_meta);

        ItemStack fishingSkill = new ItemStack(Material.FISHING_ROD);
        ItemMeta fishingSkill_meta = fishingSkill.getItemMeta();
        fishingSkill_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lFishing Skill"));
        List<String> fishingSkillLore = new ArrayList<>();
        fishingSkillLore.add("--------------------------");
        fishingSkillLore.add("• " + ChatColor.AQUA + "YOUR CURRENT LEVEL: " + ChatColor.WHITE + "11");
        fishingSkillLore.add("• " + ChatColor.AQUA + "LONGEST FISH CAUGHT: " + ChatColor.WHITE + "3754,25 cm");
        fishingSkillLore.add("• " + ChatColor.AQUA + "TOTAL FISH SOLD: " + ChatColor.WHITE + "213");
        fishingSkillLore.add("--------------------------");
        fishingSkill_meta.setLore(fishingSkillLore);
        fishingSkill.setItemMeta(fishingSkill_meta);

        ItemStack fishingUpgrades = new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK);
        ItemMeta fishingUpgrades_meta = fishingUpgrades.getItemMeta();
        fishingUpgrades_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&2&lFishing Upgrades"));
        List<String> fishingUpgradesLore = new ArrayList<>();
        fishingUpgradesLore.add("");
        fishingUpgradesLore.add(ChatColor.WHITE + "Here you can improve your fishing skills.");
        fishingUpgrades_meta.setLore(fishingUpgradesLore);
        fishingUpgrades.setItemMeta(fishingUpgrades_meta);

        ItemStack fishingShop = new ItemStack(Material.CHEST_MINECART);
        ItemMeta fishingShop_meta = fishingShop.getItemMeta();
        fishingShop_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lFishing Shop"));
        List<String> fishingShopLore = new ArrayList<>();
        fishingShopLore.add("");
        fishingShopLore.add(ChatColor.WHITE + "Here you can sell everything you catch from the water.");
        fishingShop_meta.setLore(fishingShopLore);
        fishingShop.setItemMeta(fishingShop_meta);

        ItemStack suicide = new ItemStack(Material.TNT);
        ItemMeta suicide_meta = suicide.getItemMeta();
        suicide_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lSuicide"));
        List<String> suicideLore = new ArrayList<>();
        suicideLore.add("");
        suicideLore.add(ChatColor.WHITE + "You can commit tactical suicide.");
        suicide_meta.setLore(suicideLore);
        suicide.setItemMeta(suicide_meta);

        ItemStack empty = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta empty_meta = empty.getItemMeta();
        empty_meta.setDisplayName(ChatColor.DARK_GRAY + "#");
        empty.setItemMeta(empty_meta);

        for (int i = 0; i < 27; i++) {
            switch (i) {
                case 10:
                    mainMenu.setItem(i, fishingSkill);
                    break;
                case 11:
                    mainMenu.setItem(i, fishingUpgrades);
                    break;
                case 12:
                    mainMenu.setItem(i, lootChances);
                    break;
                case 13:
                    mainMenu.setItem(i, fishingShop);
                    break;
                case 16:
                    mainMenu.setItem(i, suicide);
                    break;

                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                    mainMenu.setItem(i, empty);
                    break;

                default:
                    break;
            }
        }

        player.openInventory(mainMenu);
    }

}
