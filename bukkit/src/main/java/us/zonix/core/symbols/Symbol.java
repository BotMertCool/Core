package us.zonix.core.symbols;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import us.zonix.core.rank.Rank;

@AllArgsConstructor
@Getter
public enum Symbol {

    SYMBOL_1(1, ChatColor.WHITE + "❖", Rank.SILVER),
    SYMBOL_2(2, ChatColor.WHITE + "✤", Rank.SILVER),
    SYMBOL_3(3, ChatColor.YELLOW + "✯", Rank.GOLD),
    SYMBOL_4(4, ChatColor.YELLOW + "✪", Rank.GOLD),
    SYMBOL_5(5, ChatColor.YELLOW + "❂", Rank.GOLD),
    SYMBOL_6(6, ChatColor.AQUA + "❇", Rank.PLATINUM),
    SYMBOL_7(7, ChatColor.AQUA + "➤", Rank.PLATINUM),
    SYMBOL_8(8, ChatColor.AQUA + "❁", Rank.PLATINUM),
    SYMBOL_9(9, ChatColor.GREEN + "✵", Rank.EMERALD),
    SYMBOL_10(10, ChatColor.GREEN + "✔", Rank.EMERALD),
    SYMBOL_11(11, ChatColor.GREEN + "✖", Rank.EMERALD),
    SYMBOL_12(12, ChatColor.GOLD + "❊", Rank.ZONIX),
    SYMBOL_13(13, ChatColor.GOLD + "❤", Rank.ZONIX),
    SYMBOL_14(14, ChatColor.GOLD + "☯", Rank.ZONIX),
    SYMBOL_15(15, ChatColor.GOLD + "☣", Rank.ZONIX),
    SYMBOL_16(16, ChatColor.GOLD + "☢", Rank.ZONIX);

    private final int id;
    private final String prefix;
    private final Rank rank;

    public static Symbol getSymbolOrDefault(String symbolName) {
        Symbol symbol;

        try {
            symbol = Symbol.valueOf(symbolName.toUpperCase());
        }
        catch (Exception e) {
            System.out.println("error");
            symbol = Symbol.SYMBOL_1;
        }

        return symbol;
    }

    public static Symbol getDefaultSymbolByRank(Rank rank) {
        if(rank == Rank.SILVER) {
            return SYMBOL_1;
        } else if(rank == Rank.GOLD) {
            return SYMBOL_3;
        } else if(rank == Rank.PLATINUM) {
            return SYMBOL_6;
        } else if(rank == Rank.EMERALD) {
            return SYMBOL_9;
        } else if(rank == Rank.ZONIX) {
            return SYMBOL_12;
        }

        return SYMBOL_1;
    }
}
