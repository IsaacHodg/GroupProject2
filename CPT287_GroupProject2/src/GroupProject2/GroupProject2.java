package GroupProject2;

import java.util.Scanner;
import java.util.Stack;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
 
public class GroupProject2 {
	
	//Reads in expressions from input.txt and evaluates them
    public static void main(String[] args) throws IOException, ParseException {
    	FileInputStream inputFile = new FileInputStream("input.txt");
		Scanner scanner = new Scanner(inputFile);
		
		while (scanner.hasNextLine()) {
			System.out.println(evaluate(scanner.nextLine()));
		}
    }
		
    //Evaluates expression
    public static int evaluate(String expression) {
        char[] tokens = expression.toCharArray();
 
         // Stack for numbers
        Stack<Integer> numbers = new Stack<Integer>();
 
        // Stack for Operators
        Stack<String> operators = new Stack<String>();
 
        for (int i = 0; i < tokens.length; i++) { 
        	
            // Skip whitespace
            if (tokens[i] == ' ')
                continue;
 
            // If Current token is a number, push it in the stack for numbers
            if (tokens[i] >= '0' && tokens[i] <= '9') {
            	
            	StringBuffer buf = new StringBuffer();
                 
                // When there are more than one digits in number
                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9')
                	buf.append(tokens[i++]);
                
                numbers.push(Integer.parseInt(buf.toString()));                
                i--;
            }
 
            // Push opening brace operator's stack
            else if (tokens[i] == '(')
            	operators.push(String.valueOf(tokens[i]));
  
            // Close brace
            else if (tokens[i] == ')') {
                while (!(operators.peek().equals("(")))
                	numbers.push(calculate(operators.pop(), 
                			numbers.pop(), 
                			numbers.pop()));
                operators.pop();
            }
 
            // Current token is an operator.
            else {
            	StringBuffer buf = new StringBuffer();
            	buf.append(tokens[i]);
            	if (String.valueOf(tokens[i+1]).equals("=") ||
            		String.valueOf(tokens[i+1]).equals("&") ||
            		String.valueOf(tokens[i+1]).equals("|")) { buf.append(tokens[++i]); }
            	
                // While top of 'ops' has same 
                // or greater precedence to current
                // token, which is an operator.
                // Apply operator on top of 'ops'
                // to top two elements in values stack
                while (!operators.empty() && hasPrecedence(String.valueOf(tokens[i]), operators.peek()))
                	numbers.push(calculate(operators.pop(), 
                			numbers.pop(),
                			numbers.pop()));
 
                // Push current token to operator's stack.
                operators.push(String.valueOf(buf));
            }
        }
 
        while (!operators.empty())
        	numbers.push(calculate(operators.pop(), 
        			numbers.pop(), 
        			numbers.pop()));
        
        // Returns the result
        return numbers.pop();
    }
 
    // Returns true if 'op2' has higher or same precedence as 'op1', otherwise returns false.
    public static boolean hasPrecedence(String op1, String op2) {
    	//Assign each operator a precedence value
    	int val1 = assignPrecedence(op1);
    	int val2 = assignPrecedence(op2);
        if (op2.equals("(") || op2.equals(")"))
            return false;
        //Compares precedence values
        else if (val1 >= val2)
            return false;
        else
            return true;
    }
 
    // Calculations for each operator
    public static int calculate(String op, int b, int a)
    {
        switch (op)
        {
        case "<":
            if (a < b) { return 1; }
            else { return 0; }
		case ">":
			if (a > b) { return 1; }
            else { return 0; }
        case "<=":
        	if (a <= b) { return 1; }
            else { return 0; }
        case ">=":
        	if (a >= b) { return 1; }
            else { return 0; }
        case "%":
        	return (a % b);
        case "^":
        	return (int)Math.pow(a, b);
        case "+":
            return a + b;
        case "-":
            return a - b;
        case "*":
            return a * b;
        case "/":
        	//Exception for dividing by 0
            if (b == 0) {
                throw new 
                UnsupportedOperationException("Cannot divide by zero"); 
            }
            return 0;
        case "==":
        	if (a == b) { return 1; }
            else { return 0; }
        case "!=":
        	if (a != b) { return 1; }
            else { return 0; }
		case "&&":
			if (a != 0 && b != 0) { return 1; }
            else { return 0; }
        case "||":
        	if (a != 0 || b != 0) { return 1; }
            else { return 0; }
        default:
        	return 0;
        }
    }
    
    //Assigns operators a numerical value for their precedence. Higher value means higher precedence
    public static int assignPrecedence(String op) {
    	int precedenceValue = 0;
    	switch (op) {
    	case "^":
        	return 7;
    	case "%":
        	return 6;
        case "*":
        	return 6;
        case "/":
        	return 6;
    	case "+":
    		return 5;
        case "-":
        	return 5;
    	case "<":
    		return 4;
		case ">":
			return 4;
        case "<=":
        	return 4;
        case ">=":
        	return 4;
        case "==":
        	return 3;
        case "!=":
        	return 3;
		case "&&":
			return 2;
        case "||":
        	return 1;
    	}
    	return precedenceValue;
    }
}