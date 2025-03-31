package org.weebeler.villageCraft.Villagers;

import org.weebeler.villageCraft.Items.Backend.GenericItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FlagProfile {
    public HashMap<Flag, ArrayList<GenericItem>> map;

    public FlagProfile() {
        map = new HashMap<>();
    }

    public void remove(GenericItem i) {
        for (Map.Entry<Flag, ArrayList<GenericItem>> e : map.entrySet()) {
            if (i.flags.contains(e.getKey())) {
                e.getValue().remove(i);
                removeEmpties();
            }
        }
    }

    public void add(GenericItem i) {
        for (Flag f : i.flags) {
            if (map.containsKey(f)) {
                map.get(f).add(i);
            } else {
                map.put(f, new ArrayList<>());
                map.get(f).add(i);
            }
        }
    }

    public void removeEmpties() {
        ArrayList<Flag> forRemoval = new ArrayList<>();
        for (Map.Entry<Flag, ArrayList<GenericItem>> e : map.entrySet()) {
            if (e.getValue().isEmpty()) {
                forRemoval.add(e.getKey());
            }
        }
        for (Flag f : forRemoval) {
            map.remove(f);
        }
    }
}
