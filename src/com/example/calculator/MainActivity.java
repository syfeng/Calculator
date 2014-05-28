package com.example.calculator;

import java.math.BigDecimal;
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
	static TextView t, debug;
	public Boolean dot;
	public int openBrackets, closeBrackets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dot = false;
        openBrackets = 0;
        closeBrackets = 0;
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
            debug = (TextView) rootView.findViewById(R.id.txtDebug);
            return rootView;
        }
    }
    
    // Print the button that is pressed
    public void onClickNum (View v){	
    	Button b = (Button)v;
    	// Get value of button clicked
    	String value = b.getText().toString();
    	
    	int currChar = (int)value.charAt(0);
    	
    	// Get last inputed character
    	int lastChar = (int)t.getText().charAt(t.length()-1);
    	
    	if(currChar == 40){
    		openBrackets++;
    	}else if(currChar == 41){
    		
    	}
    	    	
    	// If current value is 0 and user did not input an operator
		if(lastChar == 48 && t.length() == 1 && (currChar >= 48 || currChar == 40)){
			t.setText(value);
		}else if(printValue(lastChar, currChar)){
			t.append(value);
		}
}
    
    public Boolean printValue(int lastChar, int currChar){
    	 if(lastChar < 48 && lastChar > 41 && currChar < 48 && currChar > 41){
    		 return false;
    	 }
    	 if(currChar == 41){
    		 if(openBrackets > closeBrackets){
    			 closeBrackets++;
    			 return true;
    		 }else{
    			 return false;
    		 }
    	 }
    	 
    	 return true;
    }
    
    
    public ArrayList<String> convertToReversePolish(String s){
    	if(s == null || s == ""){
    		return null;
    	}
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
    	
    	return output;
    }
    
    public String solveReversePolish(ArrayList<String> equation){
    	Stack<String> stack = new Stack<String>();
    	String temp = null;
    	String val1, val2;
    	
    	for(int i = 0; i<equation.size();i++){
    		temp = equation.get(i).toString();
    		if(temp.equals("+") || temp.equals("-") || temp.equals("*") || temp.equals("/")){	
				val2 = stack.pop();
				val1 = stack.pop();
				stack.push(String.valueOf(operation(temp, val1, val2)));
    			
    		}else{
    			stack.push(temp);	
    		}
    	}
    	
    	if(stack.size() == 1){
    		return stack.pop();
    	}

    	return "ERROR";
    }
    
    public BigDecimal operation(String operator, String val1, String val2){
    	BigDecimal a = new BigDecimal(val1);
    	BigDecimal b = new BigDecimal(val2);
    	
    	if(operator.equals("+")){
    		return a.add(b);
    	}else if(operator.equals("-")){
    		return a.subtract(b);
    	}else if(operator.equals("*")){
    		return a.multiply(b);
    	}else if(operator.equals("/")){
    		return a.divide(b);
    	}
    	return null;
    }
    
    public void calculate(View v){  	
    	try{
	    	// Get and print value
	    	String s = t.getText().toString();
	    	
	    	ArrayList<String> equation = convertToReversePolish(s);
	    	
	    	t.setText(solveReversePolish(equation));
    	}catch(Exception ex){
    		debug.setText(ex.getMessage());
    	}finally{
    	}
    	
    }
    
    // Delete last character
    public void delete(View v){
    	String output = t.getText().toString();
    	if(output != null && output.length() > 0){
    		//t.setText(output.substring(0,output.length()-1));
    		openBrackets = 0;
    		closeBrackets = 0;
    		t.setText("0");
    	}
    }

}
