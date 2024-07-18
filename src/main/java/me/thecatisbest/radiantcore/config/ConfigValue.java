package me.thecatisbest.radiantcore.config;

import java.util.Arrays;
import java.util.List;

public class ConfigValue {

    public static String COMMANDS = "say hi;say I'm %player%";
    public static Boolean SWAP = false;

    public static String MAGIC_MUSHROOM_SOUP_NAME = "&6魔菇湯";
    public static List<String> MAGIC_MUSHROOM_SOUP_LORE = Arrays.asList(
            "&f聽說喝了可以有&b飛上天&f的感覺, 真的假的?",
            "&f可以讓玩家飛行&610 分鐘&f, 離開伺服器將暫停效果",
            "",
            "&e點擊右鍵使用！"
    );
    public static Integer MAGIC_MUSHROOM_SOUP_DURATION = 600;
    public static String  MAGIC_MUSHROOM_SOUP_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjI2ODMyODZhOTEyZWVkYzIyNDk3NGIyZDRhZmI3ZmE1NDViMTNhZTYxZDE5ZjNjZGY5OTBlNTFhNTA1YWVmMSJ9fX0=";

    public static String SUPER_MAGIC_MUSHROOM_SOUP_NAME = "&6超級魔菇湯";
    public static List<String> SUPER_MAGIC_MUSHROOM_SOUP_LORE = Arrays.asList(
            "&f聽說喝了可以有&b飛上天&f的感覺, 效果比&6魔菇湯&f還要好!",
            "&f可以讓玩家飛行&660 分鐘&f, 離開伺服器將暫停效果",
            "",
            "&e點擊右鍵使用！"
    );
    public static Integer SUPER_MAGIC_MUSHROOM_SOUP_DURATION = 3600;
    public static String SUPER_MAGIC_MUSHROOM_SOUP_TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTFlNWQwYzIzZWMxYTFmODEzYzBjNjNmMTEyZTU1YjdiMWM4N2ZlY2QzMjY5YzBmZGJjZTk2ZDAzYjU1OGMwOCJ9fX0=";

    public static String BUILDERS_WAND_NAME = "&6建造者魔杖";
    public static List<String> BUILDERS_WAND_LORE = Arrays.asList(
            "&f建築愛好者玩家必看",
            "&b點擊右鍵&f方塊的面以延伸所有連接的方塊"
    );
}
