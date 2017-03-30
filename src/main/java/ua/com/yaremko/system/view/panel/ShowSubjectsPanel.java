package ua.com.yaremko.system.view.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

public class ShowSubjectsPanel extends JPanel {

	private OWLModelManager modelManager;
	
	private ShowSubjectDetailsPanel showSubjectDetailsPanel;

	private Object[] columnsHeader = new String[] { "Назва предмету" };
	
	private JScrollPane scrollPane;

	private DefaultTableModel model;
	private JTable subjectsTable;
	private Set<OWLClass> subjects;
	private String[] sbjcts;
	
	private JButton detailsButton;

	
	private static final int BUTTON_HEIGHT = 20;
	private static final int BUTTON_WIDTH = 120;

	private static final int PAGINATION_BUTTON_HEIGHT = 25;
	private static final int PAGINATION_BUTTON_WIDTH = 90;

	private static final int ROW_HEIGHT = 32;

	private static final Font font = new Font("SansSerif", Font.PLAIN, 14);

	// private JLabel testLabel;

	public ShowSubjectsPanel(OWLModelManager modelManager, ShowSubjectDetailsPanel showSubjectDetailsPanel) {
		this.modelManager = modelManager;
		this.showSubjectDetailsPanel = showSubjectDetailsPanel;
		
		setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
		setLayout(new BorderLayout());

		init();
		initListeners();

		add(scrollPane, BorderLayout.CENTER);
		add(detailsButton, BorderLayout.SOUTH);
		// add(testLabel);
	}

	//fill table with the results
	public void setTableSubjects(Set<OWLClass> subjects) {
		this.subjects = subjects;
	}
	public void setTableSubjects(String [] subjects) {
		this.sbjcts = subjects;
		
		addSubjectsToTable();
	}
	
	private void addSubjectsToTable(){
		
		model.setRowCount(0);
		
		for(int i = 0; i<sbjcts.length; i++){
			model.insertRow(i, new String [] {sbjcts[i]});
		}
	}

	private void init() {
		
		int numRows = 5;
		model = new DefaultTableModel(numRows, columnsHeader.length) ;
		model.setColumnIdentifiers(columnsHeader);
		subjectsTable = new JTable(model);
		/*
		subjectsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		subjectsTable.setFont(font);
		subjectsTable.setRowHeight(ROW_HEIGHT);
		subjectsTable.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		subjectsTable.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.GRAY));

		subjectsTable.getColumnModel().getColumn(0).setMaxWidth(400);
		subjectsTable.getColumnModel().getColumn(0).setPreferredWidth(400);
		subjectsTable.getColumnModel().getColumn(1).setMaxWidth(400);
		subjectsTable.getColumnModel().getColumn(1).setPreferredWidth(400);
*/
		scrollPane = new JScrollPane(subjectsTable);
		scrollPane.setBorder(BorderFactory.createLineBorder(getBackground()));
		add(scrollPane, BorderLayout.CENTER);
		
		detailsButton = new JButton("Деталі");
		detailsButton.setPreferredSize(new Dimension(PAGINATION_BUTTON_WIDTH, PAGINATION_BUTTON_HEIGHT));

		// this.testLabel = new JLabel("Hello from ShowSubjectsPanel (Table of
		// subjects)");
	}

	private void initListeners() {
		
		detailsButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(null, "Опис предмету", "Деталі",
						JOptionPane.INFORMATION_MESSAGE);
				
				
			}
		});

	}

	private Object[][] getData(Set<OWLClass> subjects) {
		
		Object[][] array = new Object[subjects.size()][columnsHeader.length];
		
		ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
		
		int i = 0;
		for(OWLClass subject: subjects){
			array[i][0] = shortFormProvider.getShortForm(subject);
			
			Set<OWLObjectProperty> properties = subject.getObjectPropertiesInSignature();
			for (OWLObjectProperty prop: properties){
				prop.toString();
				
			}
			//array[i][1] = subject.getObjectPropertiesInSignature();			
		}
		

		return array;
	}

	public void dispose() {

	}

}
