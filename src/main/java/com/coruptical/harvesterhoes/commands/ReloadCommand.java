package com.coruptical.harvesterhoes.commands;

import com.coruptical.harvesterhoes.HarvesterHoes;
import com.coruptical.harvesterhoes.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class ReloadCommand implements CommandExecutor {
    private final HarvesterHoes plugin = HarvesterHoes.getInstance();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission(plugin.getPermission())){
            commandSender.sendMessage(plugin.getPermissionMessage());
        }

        reloadConfig();

        commandSender.sendMessage(Utils.cmsg("&7[&aHarvester Hoes&7] &aSuccessfully reloaded."));

        return true;
    }

    public void reloadConfig(){
        plugin.reloadConfig();
        plugin.saveDefaultConfig();
        plugin.saveConfig();
    }
}
