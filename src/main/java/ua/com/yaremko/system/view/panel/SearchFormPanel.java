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

import ua.com.yaremko.system.core.DLQuery;

public class SearchFormPanel extends JPanel {

	// to work with ontology in protege
	private OWLModelManager modelManager;

	// labels
	private JLabel infoLabel;

	/*
	 * private JLabel scienceBranchLabal; private JLabel specialityLabel;
	 * private JLabel researchLine; private JLabel subjectType; private JLabel
	 * term; private JLabel creditsNum;
	 */

	// comboBoxes
	private JComboBox<String> scienceBranchBox;
	private JComboBox<String> specialityBox;
	private JComboBox<String> researchLineBox;
	private JComboBox<String> subjectTypeBox;
	private JComboBox<String> termBox;
	private JComboBox<String> creditsNumBox;

	// default comboBoxes items
	private String scBranchDefault = "--- виберіть галузь науки ---";
	private String specDefault = "--- виберіть спеціальність ---";
	private String resLineDefault = "--- виберіть напрям дослідження(не обов'язково) ---";
	private String subjTypeDefault = "--- виберіть тип дисципліни ---";
	private String termDefault = "--- виберіть семестр ---";
	private String credNumDefault = "--- виберіть к-сть кредитів ---";

	// search button
	private JPanel searchButtonPanel;
	private JButton searchButton;

	// comboBoxes selected items
	private String scienceBranchSelected;
	private String specialitySelected;
	private String researchLineSelected;
	private String subjectTypeSelected;
	private String termSelected;
	private String credNumSelected;

	// comboBox data
	private String[] terms = { termDefault, "1", "2", "2Д", "3", "4", "4Д", "5", "6", "6Д", "7", "8", "8Д" };
	private String[] creditNums = { credNumDefault, "1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5", "5.5", "6",
			"6.5", "7", "7.5", "8" };

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

		// add components to the Panel
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

		// init labels
		infoLabel = new JLabel("Виберіть критерії пошуку предмету");
		infoLabel.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		infoLabel.setHorizontalAlignment(JLabel.CENTER);

		// init comboBoxex
		scienceBranchBox = new JComboBox<String>();
		((JLabel) scienceBranchBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		scienceBranchBox.addItem(scBranchDefault);
		// fill scienceBranches
		fillscienceBranchBox();

		specialityBox = new JComboBox<String>();
		((JLabel) specialityBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		specialityBox.addItem(specDefault);
		// disabled
		specialityBox.setEnabled(false);

		researchLineBox = new JComboBox<String>();
		((JLabel) researchLineBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		researchLineBox.addItem(resLineDefault);
		// disabled
		researchLineBox.setEnabled(false);

		subjectTypeBox = new JComboBox<String>();
		((JLabel) subjectTypeBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		subjectTypeBox.addItem(subjTypeDefault);
		// fill subject types
		fillSubjectTypeBox();

		termBox = new JComboBox<String>(terms);
		((JLabel) termBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

		creditsNumBox = new JComboBox<String>(creditNums);
		((JLabel) creditsNumBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

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

		// scienceBranchBox listener
		scienceBranchBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				scienceBranchSelected = (String) scienceBranchBox.getSelectedItem();

				if (scienceBranchSelected != null && !scienceBranchSelected.equals(scBranchDefault)) {
					specialityBox.removeAllItems();
					specialityBox.addItem(specDefault);
					specialityBox.setSelectedItem(specDefault);
					specialityBox.setEnabled(true);

					researchLineBox.removeAllItems();
					researchLineBox.addItem(resLineDefault);
					researchLineBox.setSelectedItem(resLineDefault);
					researchLineBox.setEnabled(false);

					// fill scienceBranch slecialities
					fillSpecialitiesBox();

					searchButton.setEnabled(true);
				}
			}
		});

		// speciality listener
		specialityBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				specialitySelected = (String) specialityBox.getSelectedItem();

				if(specialitySelected != null && !specialitySelected.equals(specDefault)){
				// enable researchLine comboBox
				researchLineBox.setEnabled(true);

				// fill speciality researchLines
				fillResearchLineBox();
				}
			}

		});

		// researchLine listener
		researchLineBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				researchLineSelected = (String) researchLineBox.getSelectedItem();
			}

		});

		searchButton.addActionListener(new ActionListener() {

			/**
			 * validate() checkBoxesGetSelection(){ return new
			 * StudentReqestParams() UserRequestBuilder(StudentReqestParams) }
			 */

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// input validation
				if (scienceBranchSelected.equals(scBranchDefault)) {
					JOptionPane.showMessageDialog(null, "Помилка! Виберіть галузь науки");
					return;
				} else if (specialitySelected.equals(specDefault)) {
					JOptionPane.showMessageDialog(null, "Помилка! Виберіть спеціальність");
					return;
				} else if (subjectTypeSelected.equals(subjTypeDefault)) {
					JOptionPane.showMessageDialog(null, "Помилка! Виберіть тип дисципліни");
					return;
				} else if (termSelected.equals(termDefault)) {
					JOptionPane.showMessageDialog(null, "Помилка! Виберіть семестр");
					return;
				} else if (credNumSelected.equals(credNumDefault)) {
					JOptionPane.showMessageDialog(null, "Помилка! Виберіть к-сть кредитів");
					return;
				} else {
					JOptionPane.showMessageDialog(null, "", "Успішна операція", JOptionPane.INFORMATION_MESSAGE);
				}

				// TODO:exequte query to ontology and show the result

				/*
				 * modelManager.get ShowSubjectsViewComponent test = new
				 * ShowSubjectsViewComponent(); ShowSubjectsPanel ssP =
				 * test.getShowSubjectsPanel();
				 * ssP.testInteraction.setText("GOVNO");
				 */

				/*
				 * Group g = Store.getGroup((String)
				 * scienceBranchBox.getSelectedItem());
				 * g.addProduct(nameField.getText(), descField.getText(),
				 * prodField.getText(), 0,
				 * Integer.parseInt(priceField.getText()));
				 * nameField.setText(""); descField.setText("");
				 * prodField.setText(""); priceField.setText("");
				 */
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

	/*
	 * checkboxes filler
	 */

	// fill science branches
	private void fillscienceBranchBox() {
		scienceBranchBox.addItem("інформатика");
		scienceBranchBox.addItem("математика");
		scienceBranchBox.addItem("інформатика_і_кібернетика");

	}

	/*
	 * fill specialities
	 */
	private void fillSpecialitiesBox() {

		// load scienceBranch specialities from ontology by DLQueryUtility
		
			}

	/*
	 * fill researchLine
	 */
	private void fillResearchLineBox() {

		// load sleciality researchLines from ontology by DLQueryUtility
	}

	/*
	 * fill subjectType
	 */

	private void fillSubjectTypeBox() {

	}

}
