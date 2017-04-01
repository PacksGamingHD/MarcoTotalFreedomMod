package me.totalfreedom.totalfreedommod.command;

import me.totalfreedom.totalfreedommod.rank.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@CommandPermissions(level = Rank.OP, source = SourceType.ONLY_IN_GAME)
@CommandParameters(description = " Look into another player's inventory, optionally take items out.", usage = "/<command> <player>", aliases = "inv,insee")
public class Command_invsee extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {

        if (args.length != 1)
        {
            msg("You need to specify a player.");
            return false;
        }
        Player player = getPlayer(args[0]);
        if (player == null)
        {
            msg("This player is not online.");
            return false;
        }

        if (playerSender == player)
        {
            msg("You cannot invsee yourself.");
            return true;
        }

        //final Rank recieverRank = plugin.rm.getRank(playerSender);
        //final Rank playerRank = plugin.rm.getRank(player);
        //if (plugin.al.isAdmin(player))
        //{
        //if (playerRank.ordinal() >= recieverRank.ordinal())
        //{
        //msg("You can't spy on admins!");
        //return true;
        //}
        //}
        playerSender.closeInventory();
        Inventory playerInv = player.getInventory();
        Inventory inventory = server.createInventory(player, InventoryType.PLAYER, "Invsee: " + player.getName());
        ItemStack[] inv = playerInv.getContents();
        inventory.setContents(inv);
        playerSender.openInventory(inventory);

        return true;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event)
    {
        Inventory i = event.getInventory();
        Player player = (Player) event.getWhoClicked();

        if ((i.getTitle().contains("Invsee") || i.getType() == InventoryType.PLAYER))
        {
            if (!plugin.al.isAdmin(player))
            {
                event.setCancelled(true);
            }

        }

    }
}
