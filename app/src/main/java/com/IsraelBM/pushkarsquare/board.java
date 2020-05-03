package com.IsraelBM.pushkarsquare;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class board {
    public List<String> deck;
    private cell[][] board;
    private ArrayList<int[]>[] sum_to_ten;
    private String next_card;
    private boolean full_situat;
    private int full_cells_cnt;

    private char[] card_kinds= {'c', 'd', 'h', 's'};//for kinds of playing cards
    private int[][] spcial_11 = {{0,1},{0,2},{3,1},{3,2}},
            spcial_12 = {{1,0},{2,0},{1,3},{2,3}},
            spcial_13 = {{0,0},{0,3},{3,0},{3,3}};

    //constructor
    public board()
    {
        board = new  cell[4][4];

        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                board[i][j] = new cell(i, j);

        deck = new ArrayList<>();
        for(int i = 1; i < 14; i++)
            for(int j = 0; j < 4; j++)
                deck.add("c_" + String.valueOf(i) + card_kinds[j]);
        deck.add("joker_1");
        deck.add("joker_2");

        sum_to_ten = new ArrayList[10];
        full_situat = false;
        full_cells_cnt = 0;
    }


    //getters & setters
    public cell getBoard(int i, int j) {
        return board[i][j];
    }
    public ArrayList getSum_to_ten(int i) {
        return sum_to_ten[i];
    }
    public void setSum_to_ten(ArrayList<int[]> sum_to_ten, int i) {
        this.sum_to_ten[i] = sum_to_ten;
    }
    public String getNext_card() {
        return next_card;
    }
    public int getFull_cells_cnt() {
        return full_cells_cnt;
    }
    public void setFull_cells_cnt(int full_cells_cnt) {
        this.full_cells_cnt = full_cells_cnt;
    }
    public boolean isFull_situat() {
        return full_situat;
    }
    public void setFull_situat(boolean full_situat) {
        this.full_situat = full_situat;
    }



    //take the next card
    public String getNextCard(List<String> list)
    {
        Random rand = new Random();
        int rand_idx = rand.nextInt(list.size());
        next_card = deck.get(rand_idx);
        deck.remove(rand_idx);
        return next_card;
    }

    //check if some cell is clickable
    //(for being clickable the cell has to be empty or joker. J,Q,K has spacial places)
    public boolean is_clickable(int i, int j)
    {
        String sub_nc = next_card.substring(2,4); // for check if next card is J/Q/K hwo have spacial places
        if(sub_nc.equals("11") || sub_nc.equals("12") || sub_nc.equals("13"))
        {
            if(!check_j_q_k(sub_nc,i,j))
                return false;
        }
        return board[i][j].getValue() == 0;
    }

    //check for J,Q,K if the user choose the right place for them
    public boolean check_j_q_k(String sub_nc, int i, int j)
    {
        int[][] spac;
        if(sub_nc.equals("11"))
            spac = spcial_11;
        else if(sub_nc.equals("12"))
            spac = spcial_12;
        else
            spac = spcial_13;
        for(int x = 0; x < 4; x++)
            if(i == spac[x][0] && j == spac[x][1])
                return true;
        return false;
    }

    //returns next card value (f.e - ace = 1, 2=2...j=11)
    public int get_nxtcrd_val()
    {
        if(next_card.length() > 5)//jokers
            return 0;
        if(next_card.charAt(3)-48 > 9)
            return next_card.charAt(2)-48;
        return Integer.parseInt(next_card.substring(2,4));
    }

    public boolean fill_sum_to_ten()
    {
        //fill sum_to_ten array
        //(for i = 0 to 10:  array[i] = arraylist of all cells hwo have value=i. i = 0 presents value of 10)
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
            {
                int val = board[i][j].getValue();
                if(val < 10 && val > 0)
                {
                    int[] this_i_j = {i,j};
                    if(sum_to_ten[val] == null)
                        sum_to_ten[val] = new ArrayList<int[]>();
                    sum_to_ten[val].add(this_i_j);
                }
            }
        //remove all non relevant cells
        for(int i = 1; i < 10; i++) {
            if (sum_to_ten[i] == null || sum_to_ten[10 - i] == null) {
                sum_to_ten[i] = null;
                sum_to_ten[10 - i] = null;
            }
            else if(i == 5)
                if(sum_to_ten[i].size() < 2)
                    sum_to_ten[i] = null;
        }
        //check if sum_to_ten is empty, that means the board is full and nothing to take out -> lose
        Log.d("debug", "6");
        return sumtoten_still_full();
    }

    //checs if player win (all J/Q/K in their places)
    public boolean check_if_win()
    {
        int[][] spac;
        for(int i = 11; i < 14; i++)
        {
            if(i == 11)//J case
                spac = spcial_11;
            else if(i == 12)// Q case
                spac = spcial_12;
            else//K case
                spac = spcial_13;
            for(int j = 0; j < 4; j++)
                if(board[spac[j][0]][spac[j][1]].getValue() != i)
                    return false;
        }
        return true;
    }

    //checks if player lose by he has not empty place for J/Q/K
    public boolean if_lose_by_JQK()
    {
        int[][] spac;
        int val = get_nxtcrd_val();
        if(val == 11)
            spac = spcial_11;
        else if(val == 12)
            spac = spcial_12;
        else
            spac = spcial_13;
        for(int j = 0; j < 4; j++)
            if(board[spac[j][0]][spac[j][1]].getValue() == 0)
                return false;
        return true;
    }

    //returns true if is a su_to_ten is not empty
    public boolean sumtoten_still_full()
    {
        for(int i = 1; i < 10; i++)
            if(sum_to_ten[i] != null)
                return true;
        return false;
    }

    //returns true if is a 10 in some cell in the table, false if not
    public boolean is_ten_in_sumtoten()
    {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                if(board[i][j].getValue() == 10)
                    return true;
        return false;
    }

    //returns true if is a joker in some cell in the table, false if not
    public boolean check_if_joker()
    {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                if(board[i][j].getCard().equals("joker"))
                    return true;
        return false;
    }
}
