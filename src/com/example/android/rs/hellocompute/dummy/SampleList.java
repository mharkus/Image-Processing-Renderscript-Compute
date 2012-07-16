package com.example.android.rs.hellocompute.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SampleList {

    public static class SampleListItem {

        public String id;
        public String content;

        public SampleListItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    public static List<SampleListItem> ITEMS = new ArrayList<SampleListItem>();
    public static Map<String, SampleListItem> ITEM_MAP = new HashMap<String, SampleListItem>();

    static {
        addItem(new SampleListItem("1", "Colorizer"));
        addItem(new SampleListItem("2", "Monochrome"));
        addItem(new SampleListItem("3", "Rotate & Flip"));
        addItem(new SampleListItem("4", "Sepia"));
    }

    private static void addItem(SampleListItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }
}
