package us.zonix.core.symbols.commands;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import us.zonix.core.CorePlugin;
import us.zonix.core.api.request.PlayerRequest;
import us.zonix.core.profile.Profile;
import us.zonix.core.rank.Rank;
import us.zonix.core.redis.queue.Queue;
import us.zonix.core.symbols.Symbol;
import us.zonix.core.util.ItemUtil;
import us.zonix.core.util.command.BaseCommand;
import us.zonix.core.util.command.Command;
import us.zonix.core.util.command.CommandArgs;
import us.zonix.core.util.inventory.InventoryUI;

public class SymbolCommand extends BaseCommand {

    @Getter
    private InventoryUI symbolSelector = new InventoryUI("Select a Symbol", true, 2);

    public SymbolCommand() {
        this.setupSymbolInventory();
    }

    @Command(name = "symbol", aliases ={ "symbols", "icons", "prefix" }, requiresPlayer = true, rank = Rank.DEFAULT)
    public void onCommand(CommandArgs command) {

        Player player = command.getPlayer();
        player.openInventory(this.symbolSelector.getCurrentPage());

    }

    private void setupSymbolInventory() {

        int count = 0;

        for(Symbol symbol : Symbol.values()) {

            if(symbol.isOrigin()) {
                continue;
            }

            this.symbolSelector.setItem(count, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.NAME_TAG, symbol.getPrefix() + ChatColor.GRAY + " | " + ChatColor.DARK_GRAY + "(" + symbol.getRank().getColor() + symbol.getRank().getName() + "+" + ChatColor.DARK_GRAY + ")")) {
                @Override
                public void onClick(InventoryClickEvent event) {
                    Player player = (Player) event.getWhoClicked();
                    Profile profile = Profile.getByUuidIfAvailable(player.getUniqueId());

                    if(profile == null) {
                        player.closeInventory();
                        return;
                    }

                    if(!profile.getRank().isAboveOrEqual(symbol.getRank())) {
                        player.sendMessage(ChatColor.RED + "You don't have permission to use this symbol.");
                        player.sendMessage(ChatColor.RED + "Purchase access @ store.zonix.us");
                        player.closeInventory();
                        return;
                    }

                    player.closeInventory();
                    profile.setSymbol(symbol);
                    main.getRequestProcessor().sendRequestAsync(new PlayerRequest.UpdateSymbolRequest(player.getUniqueId(), symbol));
                    player.sendMessage(ChatColor.GREEN + "Your symbol has been updated to " + symbol.getPrefix() + ChatColor.GREEN + ".");
                }
            });

            count++;
        }

        this.symbolSelector.setItem(17, new InventoryUI.AbstractClickableItem(ItemUtil.createItem(Material.FIREWORK_CHARGE, ChatColor.RED + "Reset Symbol")) {
            @Override
            public void onClick(InventoryClickEvent event) {
                Player player = (Player) event.getWhoClicked();
                Profile profile = Profile.getByUuidIfAvailable(player.getUniqueId());

                if(profile == null) {
                    player.closeInventory();
                    return;
                }

                player.closeInventory();
                Symbol symbol = Symbol.getDefaultSymbolByRank(profile.getRank());
                profile.setSymbol(symbol);
                main.getRequestProcessor().sendRequestAsync(new PlayerRequest.UpdateSymbolRequest(player.getUniqueId(), symbol));
                player.sendMessage(ChatColor.GREEN + "Your symbol has been updated to " + symbol.getPrefix() + ChatColor.GREEN + ".");
            }
        });
    }
}
