package com.coruptical.harvesterhoes.commands;

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

public class HoeCommand implements CommandExecutor, TabCompleter {
    private final Utils ut = new Utils();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage(ut.color("&7/harvesterhoes <give/giveall> <?player> <basic/maxlevel>"));
        }

        if (args.length > 0){
            if (args[0].equals("give")){
                if (args.length < 3){
                    commandSender.sendMessage(ut.color("&7/harvesterhoes <give> <player> <basic/maxlevel>"));
                }else{
                    Player toGive = Bukkit.getPlayer(args[1]);
                    if (toGive == null){
                        commandSender.sendMessage(ut.color("&cThis is not a valid player."));
                    }else{
                        ut.giveBasicHoe(toGive, 1);
                    }
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
        return list;
    }
}
