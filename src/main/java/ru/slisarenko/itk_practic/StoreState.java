package ru.slisarenko.itk_practic;

import java.util.HashMap;
import java.util.Map;

public class StoreState {
    private Map<Integer, StringBuilderSnapshot> store;

    public StoreState(){
        this.store = new HashMap<>();
    }
    void saveState(StringBuilder state){
        this.store.put(store.size(), new StringBuilderSnapshot(state));
    }

    StringBuilder getState(Integer key){
        return this.store.get(key).getSnapshot();
    }

    class StringBuilderSnapshot {
        private final StringBuilder builder;

        public StringBuilderSnapshot(StringBuilder builder) {
            this.builder = new StringBuilder(builder);
        }

        StringBuilder getSnapshot() {
            return this.builder;
        }
    }

}
