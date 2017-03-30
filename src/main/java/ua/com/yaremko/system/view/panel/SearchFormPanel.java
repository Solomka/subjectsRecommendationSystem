package ua.com.yaremko.system.view.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLPropertyExpression;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.util.PropertyAssertionValueShortFormProvider;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import ch.qos.logback.classic.sift.SiftAction;
import ua.com.yaremko.system.core.DLQuery;
import ua.com.yaremko.system.core.DLQueryParams;
import ua.com.yaremko.system.core.RestrictionVisitor;

public class SearchFormPanel extends JPanel {

	// to work with ontology in protege
	private OWLModelManager modelManager;
	private OWLEditorKit owlEditorKit;
	
	private ShowSubjectsPanel showSubjectsPanel;

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
	private String subjTypeDefault = "--- виберіть тип предмету ---";
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

	public SearchFormPanel(OWLEditorKit owlEditorKit, OWLModelManager modelManager,
			ShowSubjectsPanel showSubjectsPanel) {
		this.modelManager = modelManager;
		this.owlEditorKit = owlEditorKit;
		this.showSubjectsPanel = showSubjectsPanel;

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
		researchLineBox.setSelectedItem(resLineDefault);
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

				if (specialitySelected != null && !specialitySelected.equals(specDefault)) {
					// enable researchLine comboBox
					researchLineBox.removeAllItems();
					researchLineBox.addItem(resLineDefault);
					researchLineBox.setSelectedItem(resLineDefault);
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

		subjectTypeBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				subjectTypeSelected = (String) subjectTypeBox.getSelectedItem();
			}
		});

		termBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				termSelected = (String) termBox.getSelectedItem();

			}
		});

		creditsNumBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				credNumSelected = (String) creditsNumBox.getSelectedItem();

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

				if (scienceBranchSelected == null || scienceBranchSelected.equals(scBranchDefault)) {
					JOptionPane.showMessageDialog(null, "Виберіть галузь науки!", "Некоректний ввід даних",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else if (specialitySelected == null || specialitySelected.equals(specDefault)) {
					JOptionPane.showMessageDialog(null, "Виберіть спеціальність!", "Некоректний ввід даних",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else if (subjectTypeSelected == null || subjectTypeSelected.equals(subjTypeDefault)) {
					JOptionPane.showMessageDialog(null, "Виберіть тип дисципліни!", "Некоректний ввід даних",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else if (termSelected == null || termSelected.equals(termDefault)) {
					JOptionPane.showMessageDialog(null, "Виберіть семестр!", "Некоректний ввід даних",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else if (credNumSelected == null || credNumSelected.equals(credNumDefault)) {
					JOptionPane.showMessageDialog(null, "Виберіть к-сть кредитів!", "Некоректний ввід даних",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					JOptionPane.showMessageDialog(null, "Успішного запису на дисципліни =)", "Операція успішна",
							JOptionPane.INFORMATION_MESSAGE);

					// exequte query to ontology and show the result

					DLQuery dlQuery = new DLQuery(owlEditorKit);
					DLQueryParams dlQueryParams = getUserSelections();
					String dlQueryRequest = dlQuery.fromDLQueryParamsToRequest(dlQueryParams);

					// test printing
					String[] recommendedSubjects = dlQuery.getSubClasses(dlQueryRequest, true);

					for (int i = 0; i < recommendedSubjects.length; i++) {
						System.out.println("Recommended subjec: " + recommendedSubjects[i]);
					}
					showSubjectsPanel.setTableSubjects(recommendedSubjects);

					/*
					 * Set<OWLClass> recommendedSubjects =
					 * dlQuery.getSubClassesSet(dlQueryRequest, true);
					 * showSubjectsPanel.setTableSubjects(recommendedSubjects);
					 */

					Set<OWLClass> recommSubjects = dlQuery.getSubClassesSet(dlQueryRequest, true);
					Set<OWLOntology> cussOntologies = new HashSet<>();
					OWLOntology currectOntology = modelManager.getOWLReasonerManager().getCurrentReasoner()
							.getRootOntology();
					cussOntologies.add(currectOntology);

					RestrictionVisitor restrictionVisitor = null;
					ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
					
					for (OWLClass c : recommSubjects) {
						restrictionVisitor = new RestrictionVisitor(Collections.singleton(currectOntology));

						for (OWLSubClassOfAxiom ax : currectOntology.getSubClassAxiomsForSubClass(c)) {
							ax.getSuperClass().accept(restrictionVisitor);
							System.out.println("SuperClass: " + ax.getSuperClass().toString());
						}

						// Our RestrictionVisitor has now collected all of the
						// properties that
						// have been restricted in existential restrictions -
						// print them out.
						
						
						List<OWLPropertyExpression> props = new ArrayList<>();
						
						for (OWLObjectPropertyExpression prop : restrictionVisitor.getRestrictedProperties()){
							props.add(prop);
						}
							
						Map<OWLDataPropertyExpression,List<String>> preferredLanguageMap = new HashMap<OWLDataPropertyExpression,List<String>>();
						PropertyAssertionValueShortFormProvider propertySHortFormProvider = new PropertyAssertionValueShortFormProvider(props, preferredLanguageMap, modelManager.getOWLOntologyManager());
						
						//get ObjectProperties
						for (OWLObjectPropertyExpression prop : restrictionVisitor.getRestrictedProperties()) {
							System.out.println("Object Property: " + propertySHortFormProvider.getShortForm((OWLEntity) prop));
						}
						//get ObjectPropertiesValues
						for (OWLClassExpression classExpr : restrictionVisitor.getRestrictedPropertiesValues()) {
							System.out.println("Object Prop Value: " + shortFormProvider.getShortForm((OWLEntity) classExpr));
						}
						
						
						//get DataProperties
						for(OWLDataPropertyExpression prop: restrictionVisitor.getDataProperties()){
							System.out.println("Data Property: " + shortFormProvider.getShortForm((OWLEntity)prop));
						}
						
						//get DataPropertiesValues
						for(OWLLiteral propValue: restrictionVisitor.getDataPropertiesValues()){
							System.out.println("Data Prop Value: " + propValue.getLiteral());
						}

					}
					/*
					 * for(OWLClass c: recommSubjects){
					 * System.out.println("SBJ: " + c.toString());
					 * 
					 * NodeSet<OWLClass> subClses =
					 * modelManager.getOWLReasonerManager().getCurrentReasoner()
					 * .getSubClasses(c, true); Set<OWLClass> classes =
					 * subClses.getFlattened();
					 * 
					 * 
					 * 
					 * Set<OWLObjectProperty> superClasses =
					 * c.getObjectPropertiesInSignature();
					 * System.out.println("OWLObjectProperty size: " +
					 * superClasses.size() ); for(OWLObjectProperty p:
					 * superClasses){ System.out.println("Object Property: " +
					 * p.toString()); }
					 * 
					 * }
					 */

					/*
					 * Set<OWLDataPropertyDomainAxiom> classAxioms =
					 * modelManager.getOWLReasonerManager().getCurrentReasoner()
					 * .getRootOntology().getAxioms(AxiomType.
					 * DATA_PROPERTY_DOMAIN);
					 * 
					 * for(OWLDataPropertyDomainAxiom axiom: classAxioms){
					 * System.out.println("Axiom Domain: " +
					 * axiom.getDomain().toString());
					 * System.out.println("Axiom : " + axiom.toString()); }
					 */

				}
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
		/*
		 * DLQuery dlQuery = new DLQuery(owlEditorKit); String[] scienceBranches
		 * = dlQuery.getSubClasses("ГалузьНауки", false);
		 * 
		 * for(int i = 0; i<scienceBranches.length; i++){
		 * scienceBranchBox.addItem(scienceBranches[i]); }
		 */

		scienceBranchBox.addItem("інформатика");
		scienceBranchBox.addItem("математика");
		scienceBranchBox.addItem("інформатика_і_кібернетика");

	}

	/*
	 * fill specialities
	 */
	private void fillSpecialitiesBox() {

		// load scienceBranch specialities from ontology by DLQuery

		DLQuery dlQuery = new DLQuery(owlEditorKit);
		String[] scienceBranchSpecialities = dlQuery.getSubClasses(scienceBranchSelected, true);
		for (int i = 0; i < scienceBranchSpecialities.length; i++) {
			specialityBox.addItem(scienceBranchSpecialities[i]);
		}

	}

	/*
	 * fill researchLine
	 */
	private void fillResearchLineBox() {

		// load sleciality researchLines from ontology by DLQuery

		DLQuery dlQuery = new DLQuery(owlEditorKit);
		String[] specialityResearchLines = dlQuery.getSubClasses(specialitySelected, true);
		for (int i = 0; i < specialityResearchLines.length; i++) {
			researchLineBox.addItem(specialityResearchLines[i]);
		}
	}

	/*
	 * fill subjectType
	 */

	private void fillSubjectTypeBox() {

		// load subjectTypes from ontology by DLQuery

		DLQuery dlQuery = new DLQuery(owlEditorKit);
		String[] subjectTypes = dlQuery.getSubClasses("ТипПредмету", true);
		for (int i = 0; i < subjectTypes.length; i++) {
			subjectTypeBox.addItem(subjectTypes[i]);
		}

	}

	// map user selections to DLQueryParams object

	private DLQueryParams getUserSelections() {
		DLQueryParams dlQueryParams = new DLQueryParams();
		dlQueryParams.setScienceBranch(scienceBranchSelected);
		dlQueryParams.setSpeciality(specialitySelected);
		dlQueryParams.setResearchLine(researchLineSelected);
		dlQueryParams.setSubjectType(subjectTypeSelected);
		dlQueryParams.setTerm(termSelected);
		dlQueryParams.setCreditsNum(credNumSelected);

		return dlQueryParams;
	}

}
