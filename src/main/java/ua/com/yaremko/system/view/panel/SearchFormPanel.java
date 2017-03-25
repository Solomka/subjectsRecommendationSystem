package ua.com.yaremko.system.view.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerListener;

import ua.com.yaremko.system.view.ShowSubjectsViewComponent;

public class SearchFormPanel extends JPanel {

	//to work with ontology in protege
	private OWLModelManager modelManager;

	//labels
	private JLabel infoLabel;
	
	/*
	private JLabel scienceBranchLabal;
	private JLabel specialityLabel;
	private JLabel researchLine;
	private JLabel subjectType;
	private JLabel term;	
	private JLabel creditsNum;
	*/
	
	//comboBoxes
	private JComboBox<String> scienceBranchBox;
	private JComboBox<String> specialityBox;
	private JComboBox<String> researchLineBox;
	private JComboBox<String> subjectTypeBox;
	private JComboBox<String> termBox;
	private JComboBox<String> creditsNumBox;
	
	//default comboBoxes items
	private String scBranchDefault = "--- виберіть галузь науки ---";
	private String specDefault = "--- виберіть спеціальність ---";
	private String resLineDefault = "--- виберіть напрям дослідження(не обов'язково) ---";
	private String subjTypeDefault = "--- виберіть тип дисципліни ---";
	private String termDefault = "--- виберіть семестр ---";
	private String credNumDaefault = "--- виберіть к-сть кредитів ---";
	
	//search button
	private JPanel searchButtonPanel;
	private JButton searchButton;
	
	// comboBoxes selected items
	private String scienceBranchName;
	
	
	private static final int BUTTON_WIDTH = 200;
	private static final int BUTTON_HEIGHT = 35;

	private static final int BORDER = 40;

	private static final Font font = new Font("SansSerif", Font.PLAIN, 14);

	public SearchFormPanel(OWLModelManager modelManager) {
		this.modelManager = modelManager;
		recalculate();

		setBorder(BorderFactory.createEmptyBorder(BORDER / 3, BORDER, BORDER / 3, BORDER));
		setLayout(new GridLayout(0, 1));
		
		init();
		initListeners();
		
		//add components to the Panel
		add(infoLabel);		
		add(new JLabel());
		add(scienceBranchBox);
		add(new JLabel());
		add(specialityBox);
		add(new JLabel());
		add(researchLineBox);
		add(new JLabel());
		add(subjectTypeBox);
		add(new JLabel());
		add(termBox);
		add(new JLabel());
		add(creditsNumBox);
		
		add(new JLabel());								
		add(searchButtonPanel);		
	}

	private void init() {
		
		//init labels
		infoLabel = new JLabel("Виберіть критерії пошуку предмету");
		infoLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		infoLabel.setHorizontalAlignment(JLabel.CENTER);
		
		// init comboBoxex
		scienceBranchBox = new JComboBox<String>();
		((JLabel)scienceBranchBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		scienceBranchBox.addItem(scBranchDefault);
		
		specialityBox = new JComboBox<String>();
		((JLabel)specialityBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		specialityBox.addItem(specDefault);
		
		researchLineBox = new JComboBox<String>();
		((JLabel)researchLineBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		researchLineBox.addItem(resLineDefault);
		
		subjectTypeBox = new JComboBox<String>();
		((JLabel)subjectTypeBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		subjectTypeBox.addItem(subjTypeDefault);
		
		termBox = new JComboBox<String>();
		((JLabel)termBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		termBox.addItem(termDefault);
		
		creditsNumBox = new JComboBox<String>();
		((JLabel)creditsNumBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		creditsNumBox.addItem(credNumDaefault);		

		// add items to each comboBox
		
		/*
		 * Iterator iterator = Store.getGroups().iterator();
		 * 
		 * for (int i = 0; i < Store.getNumberOfGroups(); i++) { Group gr =
		 * (Group) iterator.next(); scienceBranchBox.addItem(gr.getName()); }
		 */

		// init search button		
		searchButtonPanel = new JPanel();
		searchButtonPanel.setLayout(new BorderLayout());
		
		searchButton = new JButton("Шукати");		
		searchButton.setFont(font);		
		searchButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		searchButton.setEnabled(false);		
		
		searchButtonPanel.add(searchButton, BorderLayout.EAST);
	}

	private void initListeners() {

		modelManager.addListener(modelListener);

		scienceBranchBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				scienceBranchName = (String) scienceBranchBox.getSelectedItem();
				searchButton.setEnabled(true);
			}

		});

		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (scienceBranchName.equals(scBranchDefault)) {
					JOptionPane.showMessageDialog(null, "Помилка! Заповніть, будь ласка, всі поля");
					return;
					/*
					modelManager.get
					ShowSubjectsViewComponent test = new ShowSubjectsViewComponent();
					ShowSubjectsPanel ssP = test.getShowSubjectsPanel();
					ssP.testInteraction.setText("GOVNO");
					*/
				}
				/*
				 * Group g = Store.getGroup((String)
				 * scienceBranchBox.getSelectedItem());
				 * g.addProduct(nameField.getText(), descField.getText(),
				 * prodField.getText(), 0,
				 * Integer.parseInt(priceField.getText()));
				 * nameField.setText(""); descField.setText("");
				 * prodField.setText(""); priceField.setText("");
				 */
				JOptionPane.showMessageDialog(null, "", "Успішна операція", JOptionPane.INFORMATION_MESSAGE);
			}

		});

	}

	private ActionListener refreshAction = e -> recalculate();

	private OWLModelManagerListener modelListener = event -> {
		if (event.getType() == EventType.ACTIVE_ONTOLOGY_CHANGED) {
			recalculate();
		}
	};

	private void recalculate() {
		int count = modelManager.getActiveOntology().getClassesInSignature().size();
		if (count == 0) {
			count = 1; // owl:Thing is always there.
		}
	}

	public void dispose() {
		modelManager.removeListener(modelListener);
	}

}
