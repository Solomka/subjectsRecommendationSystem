package ua.com.yaremko.system.core.utils;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

public class SwingUtils {
	
	public static JTextArea createTextDiv(Font font, List<String> text, int width, int padding, Color bg){
		JTextArea textArea = new JTextArea();
		setListAsText(textArea, text);
		textArea.setEditable(false);
		textArea.setFont(font);
		textArea.setColumns((width-2*padding)/font.getSize());
		textArea.setBorder(BorderFactory.createEmptyBorder(0, padding, 0, padding));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		//textArea.setBackground(bg);
		return textArea;		
	}
	
	public static JTextArea setListAsText(JTextArea area, List<String> text){
		StringBuilder sb = new StringBuilder();
		for (String s : text){
			sb.append(s + '\n');
		}
		area.setText(sb.toString());
		return area;
	}

}
