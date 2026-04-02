package ru.slisarenko;

import java.util.Objects;

public class StringBuilderWithUndo {
    private StringBuilder builder;

    StringBuilderWithUndo() {
        this.builder = new StringBuilder();
    }

    public void write(Object t){
        this.builder.append(t);
    }

    public String getText(){
        return this.builder.toString();
    }

    public StringBuilderSnapshot saveState(){
        return new StringBuilderSnapshot(this.builder);
    }

    public void restoreState(StringBuilderSnapshot snapshot){
        this.builder = snapshot.getSnapshot();
    }
}
