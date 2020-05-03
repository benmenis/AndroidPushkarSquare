package com.IsraelBM.pushkarsquare;

public class cell {
    private boolean is_empty;
    private String card;
    private int value, i, j;

    public cell(int i, int j)
    {
        is_empty = true;
        card = "";
        value = 0;
        this.i = i;
        this.j = j;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public boolean isIs_empty() {
        return is_empty;
    }

    public void setIs_empty(boolean is_empty) {
        this.is_empty = is_empty;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
