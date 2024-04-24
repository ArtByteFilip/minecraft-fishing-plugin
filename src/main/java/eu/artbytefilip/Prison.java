package eu.artbytefilip;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class Prison implements Listener, CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PrisonSellAll(player);
        } else {
            sender.sendMessage("This command can by executed only by player.");
        }
        return true;
    }

    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent e) {

        Player player = e.getPlayer();
        GameMode playerGamemode = player.getGameMode();
        Block block = e.getBlock();

        if (playerGamemode == GameMode.CREATIVE) {
            return;
        }

        if (block.getType() == Material.SANDSTONE) {
            e.setCancelled(true);

            player.getInventory().addItem(createBlock(block, player));

        }
    }

    @EventHandler
    public void onPlayerBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        GameMode playerGamemode = player.getGameMode();
        ItemStack itemInHandMain = player.getInventory().getItemInMainHand();
        ItemStack itemInHandOff = player.getInventory().getItemInOffHand();

        if (playerGamemode == GameMode.CREATIVE) {
            return;
        }

        ItemMeta metaMain = itemInHandMain.getItemMeta();
        ItemMeta metaOff = itemInHandOff.getItemMeta();

        if (hasMinedByLore(metaMain) && itemInHandMain.getType() == Material.SANDSTONE) {
            e.setCancelled(true);
            return;
        }

        if (hasMinedByLore(metaOff) && itemInHandOff.getType() == Material.SANDSTONE) {
            e.setCancelled(true);
        }
    }

    private boolean hasMinedByLore(ItemMeta itemMeta) {
        if (itemMeta == null || !itemMeta.hasLore()) {
            return false;
        }

        for (String loreLine : itemMeta.getLore()) {
            if (loreLine.contains("Mined by:")) {
                return true;
            }
        }
        return false;
    }

    private ItemStack createBlock(Block block, Player player) {
        ItemStack item = new ItemStack(block.getType());
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("Prison Sandstone");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "Mined by: " + ChatColor.WHITE + player.getName());
        meta.setLore(lore);

        meta.setCustomModelData(12);

        item.setItemMeta(meta);
        return item;
    }

    public void PrisonSellAll(Player player) {
        PlayerInventory inventory = player.getInventory();
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
                if (loreLine.contains("Mined by:")) {
                    try {
                        coins = coins + (5 * amount);
                        item.setAmount(0);
                    } catch (NumberFormatException err) {
                        System.out.println("Error: " + err);
                    }
                }
            }
        }

        if (coins != 0.0D) {
            player.sendMessage("You earned: " + coins + " $");
        }
    }

}
