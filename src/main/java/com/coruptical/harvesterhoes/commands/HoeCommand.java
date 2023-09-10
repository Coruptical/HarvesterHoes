package com.coruptical.harvesterhoes.commands;

import com.coruptical.harvesterhoes.HarvesterHoes;
import com.coruptical.harvesterhoes.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Utility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HoeCommand implements CommandExecutor, TabCompleter {
    private final Utils ut = new Utils();
    private final HarvesterHoes plugin = HarvesterHoes.getInstance();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!commandSender.hasPermission(plugin.getPermission())){
            commandSender.sendMessage(plugin.getPermissionMessage());
        }

        if (args.length == 0) {
            commandSender.sendMessage(ut.color("&7/harvesterhoes <give/giveall> <?player>"));
        }

        if (args.length > 0){
            if (args[0].equals("give")){
                if (args.length < 2){
                    commandSender.sendMessage(ut.color("&7/harvesterhoes <give> <player>"));
                }else{
                    Player toGive = Bukkit.getPlayer(args[1]);
                    if (toGive == null){
                        commandSender.sendMessage(ut.color(plugin.getPrefix()+"&cThis is not a valid player."));
                    }else{
                        ut.giveBasicHoe(toGive, 1);
                    }
                }
            } else if (args[0].equals("giveall")) {
                for (Player p : plugin.getServer().getOnlinePlayers()){
                    ut.giveBasicHoe(p, 1);
                }
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> list = new ArrayList<>();
        if(args.length == 0) return list;
        if(args.length == 1 && args[0].isEmpty()){
            list.add("give");
            list.add("giveall");
        }
        if (args.length == 1 && !args[0].isEmpty()){
            if ("give".startsWith(args[0])) list.add("give");
            if ("giveall".startsWith(args[0])) list.add("giveall");
        }
        if (args.length == 2 && args[1].isEmpty()){
            for (Player p : plugin.getServer().getOnlinePlayers()){
                list.add(p.getName());
            }
        }
        if (args.length == 2 && !args[1].isEmpty()){
            for (Player p : plugin.getServer().getOnlinePlayers()){
                if (p.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                    list.add(p.getName());
            }
        }
        return list;
    }
}
