package ru.planet.wgprotect.utils;

import ru.planet.wgprotect.worldedit.*;

import java.util.Arrays;
import java.util.List;

public class Utils {

    private static List<CheckCommand> worldEditCommands = Arrays.asList(new CylCommand(), new HcylCommand(), new HsphereCommand(), new SphereCommand(), new PasteCommand(), new PyramidCommand(), new HpyramidCommand(), new UpCommand());

    public static boolean isWeCmd(String s) {
        s = s.replace("worldedit:", "");
        String[] cmds = new String[]{"set", "replace", "rep", "re", "walls", "regen", "cut", "hcyl", "cyl", "sphere", "hsphere", "pyramid", "hpyramid", "paste", "copy"};
        String[] var5 = cmds;
        int var4 = cmds.length;

        for (int var3 = 0; var3 < var4; ++var3) {
            String cmd = var5[var3];
            if (cmd.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }


    public static CheckCommand getCommand(String cmd) {
        for (CheckCommand command : worldEditCommands) {
            if (command.getName().equalsIgnoreCase(cmd)) {
                return command;
            }
        }
        return null;
    }

}
