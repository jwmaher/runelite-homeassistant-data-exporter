package haexporterplugin.utils;

import lombok.experimental.UtilityClass;
import net.runelite.api.Client;
import net.runelite.api.Prayer;
import net.runelite.api.gameval.VarbitID;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class PrayerUtils {

    public List<String> getActivePrayers(Client client)
    {
        // Deadeye and Mystic Vigour replace Eagle Eye and Mystic Might once unlocked, except in LMS.
        // Same rule as the core Prayer plugin; Client#isPrayerActive does not handle this.
        boolean inLms = client.getVarbitValue(VarbitID.BR_INGAME) != 0;
        boolean deadeye = !inLms && client.getVarbitValue(VarbitID.PRAYER_DEADEYE_UNLOCKED) != 0;
        boolean mysticVigour = !inLms && client.getVarbitValue(VarbitID.PRAYER_MYSTIC_VIGOUR_UNLOCKED) != 0;

        List<String> activePrayers = new ArrayList<>();
        for (Prayer prayer : Prayer.values())
        {
            if (client.getVarbitValue(prayer.getVarbit()) == 0)
            {
                continue;
            }

            if ((prayer == Prayer.EAGLE_EYE && deadeye)
                    || (prayer == Prayer.DEADEYE && !deadeye)
                    || (prayer == Prayer.MYSTIC_MIGHT && mysticVigour)
                    || (prayer == Prayer.MYSTIC_VIGOUR && !mysticVigour))
            {
                continue;
            }

            activePrayers.add(prayer.name());
        }
        return activePrayers;
    }
}
