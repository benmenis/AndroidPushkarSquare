package com.IsraelBM.pushkarsquare;


import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.ArrayList;



public class boardActivity extends AppCompatActivity {
    private final int FOUR = 4;
    private LinearLayout[][] linearLayout;
    private ImageView next_card;
    private ImageView[][] cell_image;
    private ImageButton refresh;
    private board my_board;
    private boolean clicked_some, first_clicked;
    private int val_of_first_clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        my_board = new board();
        refresh = findViewById(R.id.refresh);
        next_card = findViewById(R.id.nextcard);
        cell_image = new ImageView[FOUR][FOUR];
        linearLayout = new LinearLayout[FOUR][FOUR];
        for(int i = 0; i < FOUR; i++)
            for(int j = 0; j < FOUR; j ++) {
                cell_image[i][j] = findViewById(this.getResources().getIdentifier("n_" + String.valueOf(i) + "_" + String.valueOf(j), "id", this.getPackageName()));
                linearLayout[i][j] = findViewById(this.getResources().getIdentifier("l_" + String.valueOf(i) + "_" + String.valueOf(j), "id", this.getPackageName()));
            }
        for(int i = 0; i < FOUR; i++)
            for(int j = 0; j < FOUR; j ++)
                cell_image[i][j].setOnClickListener(new Listener());

        refresh.setOnClickListener(new Listener());
        clicked_some = false;
        first_clicked = false;
        val_of_first_clicked = 0;
        put_next_card();
    }

    public class Listener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            if(v.getId() == R.id.refresh)
            {
                for (int i = 0; i < FOUR; i++)
                    for (int j = 0; j < FOUR; j++) {
                        cell_image[i][j].setEnabled(true);
                        cell_image[i][j].setImageResource(0);
                    }
                my_board = new board();
                clicked_some = false;
                first_clicked = false;
                val_of_first_clicked = 0;
                set_black_bg_for_all();
                put_next_card();
                return;
            }
            else if(!clicked_some)
            {
                int i = getResources().getResourceEntryName(v.getId()).charAt(2)-48;
                int j = getResources().getResourceEntryName(v.getId()).charAt(4)-48;
                if(my_board.isFull_situat())
                {
                    int val = my_board.getBoard(i,j).getValue();
                    if(!first_clicked && val == 10)
                    {
                        cell_image[i][j].setImageResource(0);
                        my_board.getBoard(i,j).setValue(0);
                        my_board.getBoard(i,j).setCard("");
                        my_board.setFull_cells_cnt(my_board.getFull_cells_cnt()-1);
                        linearLayout[i][j].setBackgroundColor(Color.BLACK);
                        after_take_out();
                    }
                    else if(val < 10 && my_board.getSum_to_ten(val) != null)
                    {
                        if(!first_clicked)
                        {
                            first_clicked = true;
                            val_of_first_clicked = val;
                            cell_image[i][j].setImageResource(0);
                            set_black_bg_for_all();
                            my_board.getBoard(i,j).setValue(0);
                            my_board.getBoard(i,j).setCard("");
                            ArrayList<int[]> supple_val_to_ten = my_board.getSum_to_ten(val);
                            for(int[] cell: supple_val_to_ten)
                                if(cell[0] == i && cell[1] == j) {
                                    supple_val_to_ten.remove(supple_val_to_ten.indexOf(cell));
                                    my_board.setSum_to_ten(supple_val_to_ten, val);
                                    break;
                                }
                            supple_val_to_ten = my_board.getSum_to_ten(10-val);
                            for(int[] cell: supple_val_to_ten)
                                linearLayout[cell[0]][cell[1]].setBackgroundColor(Color.parseColor("#09d712"));
                        }
                        else
                        {
                            if(val == 10-val_of_first_clicked)
                            {
                                cell_image[i][j].setImageResource(0);
                                my_board.getBoard(i,j).setValue(0);
                                my_board.getBoard(i,j).setCard("");
                                my_board.setFull_cells_cnt(my_board.getFull_cells_cnt()-2);
                                set_black_bg_for_all();
                                ArrayList<int[]> supple_val_to_ten = my_board.getSum_to_ten(val);
                                for(int[] cell: supple_val_to_ten)
                                    if(cell[0] == i && cell[1] == j) {
                                        supple_val_to_ten.remove(supple_val_to_ten.indexOf(cell));
                                        my_board.setSum_to_ten(supple_val_to_ten, val);
                                        break;
                                    }
                                 if(my_board.getSum_to_ten(val).size() == 0 || my_board.getSum_to_ten(10-val).size() == 0 || (val == 5 && my_board.getSum_to_ten(val).size() <= 1))
                                 {
                                     my_board.setSum_to_ten(null, val);
                                     my_board.setSum_to_ten(null, 10-val);
                                 }
                                val_of_first_clicked = 0;
                                first_clicked = false;
                                after_take_out();
                            }
                        }
                    }
                }
                else if(my_board.is_clickable(i,j)) {
                    clicked_some = true;
                    set_image_and_next(i, j);
                }
            }
        }
    }


    public void set_image_and_next(int i, int j)
    {
        if(!my_board.getBoard(i,j).getCard().equals("joker"))
            my_board.setFull_cells_cnt(my_board.getFull_cells_cnt() + 1);
        cell_image[i][j].setImageResource(this.getResources().getIdentifier(my_board.getNext_card(), "drawable", this.getPackageName()));
        if(!my_board.getNext_card().equals("joker_1") && !my_board.getNext_card().equals("joker_2")) {
            my_board.getBoard(i, j).setIs_empty(false);
            my_board.getBoard(i, j).setCard("c_" + String.valueOf(i) + "_" + String.valueOf(j));
            my_board.getBoard(i,j).setValue(my_board.get_nxtcrd_val());
        }
        else
            my_board.getBoard(i, j).setCard("joker");
        if(my_board.check_if_win()) {
            do_win();
            return;
        }
        next_card.setImageResource(R.drawable.blue_back);
        if(my_board.getFull_cells_cnt() == 16)
        {
            if(!my_board.fill_sum_to_ten() && !my_board.is_ten_in_sumtoten()) {
                if (!my_board.check_if_joker())
                {
                    do_lose();
                    return;
                }
            }
            else{
                my_board.setFull_situat(true);
                hilights_removeable_cards();
                my_board.setFull_situat(true);
                clicked_some = false;
                return;
            }
        }

        new Thread(new Runnable()
        {
            public void run()
            {
                runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            put_next_card();
                            clicked_some = false;
                        }
                    });
            }
        }).start();
    }

    //pick up the next card and put in main card (the card down).
    //the function also checks if the player lose because there is not place for J/Q/K
    public void put_next_card()
    {
        next_card.setImageResource(this.getResources().getIdentifier(my_board.getNextCard(my_board.deck), "drawable", this.getPackageName()));
        if(my_board.get_nxtcrd_val() > 10 && my_board.if_lose_by_JQK())
        {
            new Thread(new Runnable()
            {
                public void run()
                {
                    SystemClock.sleep(800); // wait 1/2 sec
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            do_lose();
                        }
                    });
                }
            }).start();
        }
    }


    public void hilights_removeable_cards()
    {
        ArrayList<int[]> temp_al = new ArrayList<int[]>();
        for(int i = 0; i < 10; i++)
        {
            temp_al = my_board.getSum_to_ten(i);
            if(temp_al != null)
                for(int[] tmp: temp_al)
                {
                    linearLayout[tmp[0]][tmp[1]].setBackgroundColor(Color.parseColor("#09d712"));
                }
        }
        for(int i = 0; i < FOUR; i++)
            for(int j = 0; j < FOUR; j++)
                if(my_board.getBoard(i,j).getValue() == 10)
                    linearLayout[i][j].setBackgroundColor(Color.parseColor("#09d712"));
    }


    public void do_win()
    {
        unable_buttons_click();
        for (int i = 0; i < FOUR; i++)
        {
            linearLayout[0][i].setBackgroundColor(Color.parseColor("#09d712"));
            linearLayout[3][i].setBackgroundColor(Color.parseColor("#09d712"));
        }
        linearLayout[1][0].setBackgroundColor(Color.parseColor("#09d712"));
        linearLayout[2][0].setBackgroundColor(Color.parseColor("#09d712"));
        linearLayout[1][3].setBackgroundColor(Color.parseColor("#09d712"));
        linearLayout[2][3].setBackgroundColor(Color.parseColor("#09d712"));
        next_card.setImageResource(R.drawable.win);
    }

    public void do_lose()
    {
        unable_buttons_click();
        next_card.setImageResource(R.drawable.lose);
    }

    public void unable_buttons_click()
    {
        next_card.setEnabled(false);
        for (int i = 0; i < FOUR; i++)
            for (int j = 0; j < FOUR; j++)
                cell_image[i][j].setEnabled(false);
    }

    public void set_black_bg_for_all()
    {
        for(int i = 0; i < FOUR; i++)
            for(int j = 0; j < FOUR; j++)
                linearLayout[i][j].setBackgroundColor(Color.BLACK);
    }

    public void after_take_out()
    {
        if(my_board.sumtoten_still_full() || my_board.is_ten_in_sumtoten())
            hilights_removeable_cards();
        else {

            ///when ten is the last that player taking out, this is not calling!

            clicked_some = false;
            my_board.setFull_situat(false);
            put_next_card();
        }
    }
}
