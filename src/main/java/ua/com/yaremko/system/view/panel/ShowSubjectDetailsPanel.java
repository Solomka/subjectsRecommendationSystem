package ua.com.yaremko.system.view.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import ua.com.yaremko.system.core.SubjectDTO;
import ua.com.yaremko.system.core.utils.LineWrapTableCellRenderer;
import ua.com.yaremko.system.core.utils.SwingUtils;

public class ShowSubjectDetailsPanel extends JPanel {

	// recommended: new Dimension(600, 350)
	private Dimension size;

	private int fontSize = 12;
	private Font font = new Font("TimesRoman", Font.PLAIN, fontSize);
	private Color bgcolor = Color.WHITE;	
	
	private DefaultTableModel model;
	private JPanel tablePanel = new JPanel();

	private JTextArea textAreaResearch = new JTextArea();
	private JPanel centralPanel = new JPanel();

	private JTextArea textAreaPre = new JTextArea();
	private JTextArea textAreaPost = new JTextArea();
	private JPanel lowerPanel = new JPanel();	

	private SubjectDTO subject = emptySubjectDTO();

	public ShowSubjectDetailsPanel(Dimension preferredSize) {
		super();
		setLayout(new BorderLayout());
		setBackground(bgcolor);
		// setBorder(BorderFactory.createLineBorder(Color.RED));
		if (preferredSize.width < 600)
			System.out.println("[ WARNING ] Recommended minimal width for SubjectPanel is 600");
		if (preferredSize.height < 350)
			System.out.println("[ WARNING ] Recommended minimal height for SubjectPanel is 350");
		this.size = preferredSize;
		setPreferredSize(size);

		add(buildUpperPanel(), BorderLayout.NORTH);
		add(buildCentralPanel(), BorderLayout.CENTER);
		add(buildLowerPanel(), BorderLayout.SOUTH);
	}

	private JPanel buildUpperPanel() {
		tablePanel = new JPanel(new BorderLayout());
		tablePanel.setBackground(bgcolor);
		tablePanel.setPreferredSize(new Dimension(size.width, 2 * size.height / 5));

		JLabel label = new JLabel("Деталі предмету");
		JPanel lpanel = new JPanel();
		lpanel.setBackground(bgcolor);		
		lpanel.add(label);

		model = new DefaultTableModel() {
			@Override
			public Class<?> getColumnClass(int column) {
				if (getRowCount() > 0) {
					Object value = getValueAt(0, column);
					if (value != null) {
						return getValueAt(0, column).getClass();
					}
				}
				return super.getColumnClass(column);
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		};

		model.setColumnIdentifiers(new String[] { "Назва", "Кафедра", "<html><center>Тип<br>предмету</center></html>",
				"Семестр", "<html><center>Загальна<br>к-сть годин</center></html>",
				"<html><center>К-сть<br>кредитів</center></html>",
				"<html><center>К-сть годин<br>на тиждень</center></html>" });

		JTable table = new JTable(model);

		int lines = 2;
		table.setRowHeight(table.getRowHeight() * lines + 10);

		TableColumnModel tcm = table.getColumnModel();
		for (int i : new int[] { 0, 1 }) {
			tcm.getColumn(i).setPreferredWidth(2 * size.width / 9);
		}
		for (int i : new int[] { 2, 3, 4, 5, 6 }) {
			tcm.getColumn(i).setPreferredWidth(size.width / 9);
		}

		model.insertRow(0, new String[] { subject.getName(), subject.getFaculty(), subject.getType(), subject.getTerm(),
				subject.getTotalHours(), subject.getCreditsNum(), subject.getWeekHours() });
		System.out.println(subject.getName());

		// model.insertRow(0, new String[]{ "Об'єктно-орієнтоване
		// програмування", "Кафедра філософії", "залік", "4", "144", "2", "2"
		// });

		table.getTableHeader().setPreferredSize(new Dimension(table.getSize().width, 40));
		table.getTableHeader().setBackground(Color.WHITE);

		table.setDefaultRenderer(String.class, new LineWrapTableCellRenderer());

		JScrollPane pane = new JScrollPane(table);
		pane.setBorder(BorderFactory.createEmptyBorder());
		pane.getViewport().setBackground(bgcolor);

		tablePanel.add(lpanel, BorderLayout.NORTH);
		tablePanel.add(pane, BorderLayout.CENTER);

		return tablePanel;
	}

	private JPanel buildCentralPanel() {
		centralPanel = new JPanel(new BorderLayout());
		centralPanel.setBackground(bgcolor);
		centralPanel.setPreferredSize(new Dimension(size.width, 3 * size.height / 10));

		JPanel lpanel = new JPanel();
		lpanel.setBackground(bgcolor);
		JLabel label = new JLabel("Вивчає:");
		lpanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 0));
		lpanel.add(label);
	

		JPanel description = new JPanel();
		description.setBackground(bgcolor);
		Dimension size = new Dimension(this.size.width - 100, 2 * this.size.height / 5);
		textAreaResearch = SwingUtils.createTextDiv(font, subject.getSubjectResearchLines(), size.width, 0, bgcolor);

		description.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
		description.add(textAreaResearch);

		centralPanel.add(lpanel, BorderLayout.WEST);
		centralPanel.add(description, BorderLayout.CENTER);
		return centralPanel;
	}

	private JPanel buildLowerPanel() {
		lowerPanel = new JPanel(new GridLayout(1, 2));
		lowerPanel.setPreferredSize(new Dimension(size.width, 3 * size.height / 10));
		lowerPanel.setBackground(bgcolor);

		int padding = 10;

		JPanel left = new JPanel(new BorderLayout());
		left.setBackground(bgcolor);
		JPanel lpanel = new JPanel();
		lpanel.setBackground(bgcolor);
		JLabel label = new JLabel("Базові предмети");
		lpanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
		lpanel.add(label);
		// JTextArea textArea = SwingUtils.createTextDiv(font, "Процедурне
		// програмування", size.width/2, padding, bgcolor);
		textAreaPre = SwingUtils.createTextDiv(font, subject.getPreSubjects(), size.width / 2, padding, bgcolor);
		left.add(lpanel, BorderLayout.NORTH);
		left.add(textAreaPre, BorderLayout.CENTER);

		JPanel right = new JPanel(new BorderLayout());
		left.setBackground(bgcolor);
		JPanel lpanel2 = new JPanel();
		lpanel2.setBackground(bgcolor);
		JLabel label2 = new JLabel("Предмети-продовження");
		lpanel2.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
		lpanel2.add(label2);
		// JTextArea textArea2 = SwingUtils.createTextDiv(font, "Методи
		// об'єктно-орієнтованого програмування", size.width/2, padding,
		// bgcolor);
		textAreaPost = SwingUtils.createTextDiv(font, subject.getPostSubjects(), size.width / 2, padding, bgcolor);
		right.add(lpanel2, BorderLayout.NORTH);
		right.add(textAreaPost, BorderLayout.CENTER);

		lowerPanel.add(left);
		lowerPanel.add(right);
		return lowerPanel;
	}

	public void setSubject(SubjectDTO subjectDTO) {
		this.subject = subjectDTO;
		model.removeRow(0);
		model.insertRow(0, new String[] { subject.getName(), subject.getFaculty(), subject.getType(), subject.getTerm(),
				subject.getTotalHours(), subject.getCreditsNum(), subject.getWeekHours() });
		System.out.println(subject.getName());
		tablePanel.revalidate();
		tablePanel.repaint();

		SwingUtils.setListAsText(textAreaResearch, subject.getSubjectResearchLines());
		centralPanel.revalidate();
		centralPanel.repaint();

		if(subject.getPreSubjects().size() != 0){
			SwingUtils.setListAsText(textAreaPre, subject.getPreSubjects());
			
		}
		if(subject.getPostSubjects().size() != 0){
			SwingUtils.setListAsText(textAreaPost, subject.getPostSubjects());			
		}		
		
		lowerPanel.revalidate();
		lowerPanel.repaint();
	}

	private SubjectDTO emptySubjectDTO() {
		SubjectDTO s = new SubjectDTO();
		s.setName("");
		s.setFaculty("");
		s.setType("");
		s.setTerm("");
		s.setWeekHours("");
		s.setCreditsNum("");
		s.setTotalHours("");
		s.setSubjectResearchLines(new ArrayList<String>(Arrays.asList(new String[] { "", "", "" })));
		s.setPreSubjects(new ArrayList<String>(Arrays.asList(new String[] { "", "" })));
		s.setPostSubjects(new ArrayList<String>(Arrays.asList(new String[] { "", "" })));
		return s;
	}

	public void dispose() {

	}

}
