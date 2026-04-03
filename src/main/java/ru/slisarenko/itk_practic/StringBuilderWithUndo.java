package ru.slisarenko.itk_practic;

public class StringBuilderWithUndo {
    private StringBuilder builder;
    private StoreState store;

    StringBuilderWithUndo()
    {
        this.store = new StoreState();
        this.builder = new StringBuilder();
    }

    public void write(Object t){
        this.store.saveState(this.builder);
        this.builder.append(t);
    }

    public String getText(){
        return this.builder.toString();
    }

    public void restoreStateByKey(Integer key){
        this.builder = this.store.getState(key);
    }

    public void restoreStateDefault(){
        this.builder = this.store.getState(0);
    }
}
