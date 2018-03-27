/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();

    String wordFragment="",currentWord="";
    TextView text , label;
    Button bChallenge , bRestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        try{
            InputStream dictionaryWords = getAssets().open("words.txt");
            dictionary = new FastDictionary(dictionaryWords);

        }catch(IOException e ) {
            e.printStackTrace();
        }
        text=(TextView) findViewById(R.id.ghostText);
        label=(TextView) findViewById(R.id.gameStatus);
        bChallenge=(Button) findViewById(R.id.button1);
        bRestart=(Button) findViewById(R.id.button2);

        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        text.setText("");
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        //TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
       // userTurn = true;

        //label.setText(USER_TURN);
        if(wordFragment.length()>=4 && dictionary.isWord(wordFragment))
        {
            label.setText(" Computer win  ");
        }
        else
        {
            String wordFromDictionary =dictionary.getGoodWordStartingWith(wordFragment);
            if(wordFromDictionary!=null)
            {
                wordFragment=wordFragment.concat(wordFromDictionary.substring(wordFragment.length(),wordFragment.length()+1));
                text.setText(wordFragment);
                currentWord=wordFragment;
                userTurn=true;
            }
            else
            {
                label.setText("No word can be formed from this prefix");
                text.setText("Match draw");
            }
        }

    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode >=29 && keyCode <=54){
            char c=(char)event.getUnicodeChar();
            wordFragment = (String)text.getText();
            wordFragment = currentWord + Character.toString(c);
            text.setText(wordFragment);
            computerTurn();
            return true;


        }
        return super.onKeyUp(keyCode, event);
    }
    public void reset(View v ){
        userTurn = random.nextBoolean();
        wordFragment="";
        currentWord="";
        text.setText("");
        if(userTurn){
            label.setText("USER_TURN");
        }
        else {
            label.setText("COMPUTER_TURN");
            computerTurn();
        }

    }
    public void challenge(View v ){
        wordFragment=(String)text.getText();

        if(dictionary.isWord(wordFragment)){
            label.setText("USER WIN");
        }
        else {
            label.setText("COMPUTER WIN");
        }






    }
}
