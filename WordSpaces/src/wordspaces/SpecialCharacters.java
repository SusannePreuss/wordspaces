/*
 * SpezialCharacters.java
 *
 * Created on 21. April 2007, 18:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wordspaces;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author alexander
 */
public class SpecialCharacters {
    
    public static Set<Character> getSpecialChars()
	{
		HashSet<Character> result = new HashSet<Character>();
                result.add((char)10);
                result.add('.');
		result.add(',');
		result.add('!');
		result.add('@');
		result.add('#');
		result.add('$');
		result.add('%');
		result.add('^');
		result.add('&');
		result.add('*');
		result.add('(');
		result.add(')');
		result.add('-');
		result.add('_');
		result.add('=');
		result.add('+');
		result.add('[');
		result.add(']');
		result.add('{');
		result.add('}');
		result.add(';');
		result.add(':');
		result.add('"');
		result.add('\'');
		result.add('\\');
		result.add('|'); 
		result.add('<'); 
		result.add('>'); 
		result.add('/'); 
		result.add('?'); 
		result.add('`'); 
		result.add('~'); 
		return result; 
	}
    
    public static Set<Character> getEndofSentenceChars(){
        HashSet<Character> result = new HashSet<Character>();
        result.add('.');
        result.add('?');
        result.add('!');
        result.add(':');
        result.add(';');
        return result;
        
    }
}
