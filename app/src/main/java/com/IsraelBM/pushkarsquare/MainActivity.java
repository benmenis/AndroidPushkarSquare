package com.IsraelBM.pushkarsquare;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.print.PrintAttributes;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button instructions_btn, board_btn;
    private String other_lang, eng = "English", heb = "עברית"; //store the other language - heb or eng
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        other_lang = heb; // default is english
        instructions_btn = findViewById(R.id.instructions_btn);
        board_btn = findViewById(R.id.board_btn);
        instructions_btn.setOnClickListener(new Listener());
        board_btn.setOnClickListener(new Listener());
    }



    public class Listener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            Log.d("debug", "click");
            if(v.getId() == R.id.instructions_btn)
                instr_dialog();
            else
                open_board_activity();
        }
    }

    public void open_board_activity()
    {
        Intent intent = new Intent(this, boardActivity.class);
        startActivity(intent);
    }

    public void instr_dialog()
    {
        String btn_str = "I understand!", title_str = "Instructions";
        int dialog_layout = R.layout.instructions;
        AlertDialog.Builder alertadd = new AlertDialog.Builder(this);
        if(other_lang.equals(eng))
        {
            btn_str = "הבנתי";
            title_str = "הוראות";
            dialog_layout = R.layout.instructionsh;
        }
        alertadd.setTitle(title_str);
        final View view = getLayoutInflater().inflate(dialog_layout, null);
        alertadd.setView(view);
        alertadd.setNeutralButton(btn_str, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                dlg.cancel();
            }
        });
        AlertDialog dialog = alertadd.create();
        dialog.show();

        final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        params.gravity = Gravity.CENTER_HORIZONTAL;

        positiveButton.setLayoutParams(params);
    }


    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuItem menuLanguage = menu.add(other_lang);

        menuLanguage.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                if (other_lang == heb)
                {
                    other_lang = eng;
                    set_heb();
                }
                else {
                    other_lang = heb;
                    set_eng();
                }
                invalidateOptionsMenu();
                return true;
            }
        });
        return true;
    }

    public void set_heb()
    {
        instructions_btn.setText("הוראות");
        board_btn.setText("התחל משחק");
    }

    public void set_eng()
    {
        instructions_btn.setText("INSTRUCTIONS");
        board_btn.setText("START GAME");
    }
}
