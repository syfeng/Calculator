package com.example.calculator;

import java.util.ArrayList;
import java.util.Stack;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity {
	static TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            t = (TextView) rootView.findViewById(R.id.txtOutput);
            return rootView;
        }
    }
    
    // Print the button that is pressed
    public void onClickNum (View v){	
    	Button b = (Button)v;
    	
    	// Get and print value
    	String value = b.getText().toString();
    	t.append(value);
    }
    
    public class OperatorRank{
    	public char op;
    	public int rank;
    	
    	public OperatorRank(char op,int rank){
    		this.op = op;
    		this.rank = rank;
    	}
    }
    
    public String convertToReversePolish(String s){
    	ArrayList<String> output = new ArrayList<String>();
    	Stack<String> operators = new Stack<String>();
    	Character prev = null, curr = null;
    	int start = 0;    	
    	
    	for(int i=0;i < s.length();i++){
    		curr = s.charAt(i);
    		
    		if(curr == '+' || curr == '-' || curr == '*' || curr == '/'){

    			output.add(s.substring(start, i));
    			start = i+1;
    			
    			if(operators.empty()){ // Stack is empty, push operator
    				operators.push(curr.toString());
    				prev = curr;
    			}else{ //Compare rank ASCII + and - Odd, * and / Even
    				if((curr % 2) - (prev % 2) > 0){ //TopStack has higher precedence over current operator
    					// Pop the stack
    					while(!operators.empty()){
	    					output.add(operators.pop());
	    				}
    				}
    				operators.push(curr.toString());
    				prev=curr;
    			}
    		}else if(i == s.length()-1){
    			output.add(s.substring(start,s.length()));
    		}
    	}
    	
    	while(!operators.empty()){
			output.add(operators.pop());
		}
    	
    	return output.toString();
    }
    
    public void calculate(View v){  	
    	// Get and print value
    	String s = t.getText().toString();
    	
    	String output = convertToReversePolish(s);
    	
    	t.setText(output);
    	
    }
    
    // Delete last character
    public void delete(View v){
    	String output = t.getText().toString();
    	if(output != null && output.length() > 0){
    		//t.setText(output.substring(0,output.length()-1));
    		t.setText("");
    	}
    }

}
