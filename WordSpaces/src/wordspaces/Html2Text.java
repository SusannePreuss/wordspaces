/*
 * InternetFetcher.java
 *
 * Created on 9. Juni 2007, 16:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wordspaces;

import java.io.*;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;

public class Html2Text extends HTMLEditorKit.ParserCallback {
 StringBuffer s;

 public Html2Text() {}

 public void parse(Reader in) throws IOException {
   s = new StringBuffer();
   ParserDelegator delegator = new ParserDelegator();
   delegator.parse(in, this, true);
   }

 public void handleText(char[] text, int pos) {
   s.append(text);
   }

 public String getText() {
   return s.toString();
   }

}
