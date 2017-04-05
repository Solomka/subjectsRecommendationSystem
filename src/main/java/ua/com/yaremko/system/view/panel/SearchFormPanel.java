package ua.com.yaremko.system.view.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
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
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import ua.com.yaremko.system.core.DLQuery;
import ua.com.yaremko.system.core.DLQueryParams;
import ua.com.yaremko.system.core.RestrictionVisitor;
import ua.com.yaremko.system.core.SubjectDTO;
import ua.com.yaremko.system.core.SubjectPropertiesConstants;

public class SearchFormPanel extends JPanel {

	// to work with ontology in protege
	private OWLModelManager modelManager;
	private OWLEditorKit owlEditorKit;

	private ShowSubjectsPanel showSubjectsPanel;

	// labels
	private JLabel infoLabel;

	// comboBoxes
	private JComboBox<String> scienceBranchBox;
	private JComboBox<String> specialityBox;
	private JComboBox<String> researchLineBox;
	private JComboBox<String> subjectTypeBox;
	private JComboBox<String> termBox;
	private JComboBox<String> creditsNumBox;

	// default comboBoxes items
	private String scBranchDefault = SubjectPropertiesConstants.SCIENCE_BRANCH_DEF;
	private String specDefault = SubjectPropertiesConstants.SPECIALITY_DEF;
	private String resLineDefault = SubjectPropertiesConstants.RESEARCH_LINE_DEF;
	private String subjTypeDefault = SubjectPropertiesConstants.SUBJECT_TYPE_DEF;
	private String termDefault = SubjectPropertiesConstants.TERM_DEF;
	private String credNumDefault = SubjectPropertiesConstants.CREDINS_NUM_DEF;

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

	private Dimension size;

	private int fontSize = 12;
	private Font font = new Font("TimesRoman", Font.PLAIN, fontSize);
	private Color bgcolor = Color.WHITE;

	public SearchFormPanel(OWLEditorKit owlEditorKit, OWLModelManager modelManager, Dimension preferredSize,
			ShowSubjectsPanel showSubjectsPanel) {
		this.modelManager = modelManager;
		this.owlEditorKit = owlEditorKit;
		this.showSubjectsPanel = showSubjectsPanel;

		setLayout(new GridLayout(0, 1));
		setBackground(bgcolor);
		// setBorder(BorderFactory.createLineBorder(Color.RED));
		setBorder(BorderFactory.createEmptyBorder(0, BORDER / 3, 0, BORDER / 3));
		if (preferredSize.width < 300)
			System.out.println("[ WARNING ] Recommended minimal width for SubjectPanel is 300");
		if (preferredSize.height < 400)
			System.out.println("[ WARNING ] Recommended minimal height for SubjectPanel is 400");
		this.size = preferredSize;
		setPreferredSize(size);

		init();
		initListeners();

		// add components to the Panel
		add(infoLabel);
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
		infoLabel.setFont(font);
		infoLabel.setHorizontalAlignment(JLabel.CENTER);

		// init comboBoxex
		scienceBranchBox = new JComboBox<String>();
		scienceBranchBox.setBackground(bgcolor);
		((JLabel) scienceBranchBox.getRenderer()).setHorizontalAlignment(SwingConstants.LEADING);
		scienceBranchBox.addItem(scBranchDefault);
		// fill scienceBranches
		fillscienceBranchBox();

		specialityBox = new JComboBox<String>();
		specialityBox.setBackground(bgcolor);
		((JLabel) specialityBox.getRenderer()).setHorizontalAlignment(SwingConstants.LEADING);
		specialityBox.addItem(specDefault);
		// disabled
		specialityBox.setEnabled(false);

		researchLineBox = new JComboBox<String>();
		researchLineBox.setBackground(bgcolor);
		((JLabel) researchLineBox.getRenderer()).setHorizontalAlignment(SwingConstants.LEADING);
		researchLineBox.addItem(resLineDefault);
		researchLineBox.setSelectedItem(resLineDefault);
		// disabled
		researchLineBox.setEnabled(false);

		subjectTypeBox = new JComboBox<String>();
		subjectTypeBox.setBackground(bgcolor);
		((JLabel) subjectTypeBox.getRenderer()).setHorizontalAlignment(SwingConstants.LEADING);
		subjectTypeBox.addItem(subjTypeDefault);
		// fill subject types
		fillSubjectTypeBox();

		termBox = new JComboBox<String>(terms);
		termBox.setBackground(bgcolor);
		((JLabel) termBox.getRenderer()).setHorizontalAlignment(SwingConstants.LEADING);

		creditsNumBox = new JComboBox<String>(creditNums);
		creditsNumBox.setBackground(bgcolor);
		((JLabel) creditsNumBox.getRenderer()).setHorizontalAlignment(SwingConstants.LEADING);

		// init search button
		searchButtonPanel = new JPanel();
		searchButtonPanel.setLayout(new BorderLayout());
		searchButtonPanel.setBackground(bgcolor);

		searchButton = new JButton("Шукати");
		// searchButton.setBackground(bgcolor);
		searchButton.setFont(font);
		searchButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		searchButton.setEnabled(false);

		searchButtonPanel.add(searchButton, BorderLayout.EAST);
	}

	private void initListeners() {

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

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// input validation

				if (scienceBranchSelected == null || scienceBranchSelected.equals(scBranchDefault)) {
					JOptionPane.showMessageDialog(null, "Виберіть галузь науки!", "Некоректний ввід даних",
							JOptionPane.ERROR_MESSAGE);
					return;
				} /*else if (specialitySelected == null || specialitySelected.equals(specDefault)) {
					JOptionPane.showMessageDialog(null, "Виберіть спеціальність!", "Некоректний ввід даних",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else if (subjectTypeSelected == null || subjectTypeSelected.equals(subjTypeDefault)) {
					JOptionPane.showMessageDialog(null, "Виберіть тип предмету!", "Некоректний ввід даних",
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
				} */else {
					

					// exequte query to ontology and show the result

					DLQuery dlQuery = new DLQuery(owlEditorKit);
					DLQueryParams dlQueryParams = getUserSelections();
					String dlQueryRequest = dlQuery.fromDLQueryParamsToRequest(dlQueryParams);

					Set<OWLClass> recommSubjects = dlQuery.getSubClassesSet(dlQueryRequest, true);
					OWLOntology currectOntology = modelManager.getOWLReasonerManager().getCurrentReasoner()
							.getRootOntology();

					RestrictionVisitor restrictionVisitor = null;
					ShortFormProvider shortFormProvider = new SimpleShortFormProvider();

					List<SubjectDTO> recommendedSubjects = new ArrayList<>();
					SubjectDTO subject;

					// fill List <SubjectDTO> recommendedSubjects
					for (OWLClass c : recommSubjects) {

						subject = new SubjectDTO();

						restrictionVisitor = new RestrictionVisitor(Collections.singleton(currectOntology));

						// get short subject name
						String shortSubjectName = shortFormProvider.getShortForm(c);

						for (OWLSubClassOfAxiom ax : currectOntology.getSubClassAxiomsForSubClass(c)) {
							ax.getSuperClass().accept(restrictionVisitor);
							System.out.println("SuperClass: " + ax.getSuperClass().toString());
						}

						// print Object Properies
						Map<String, String> objectProperties = restrictionVisitor.getObjectProps();
						for (String key : objectProperties.keySet()) {
							System.out.println("ObjectProp: " + key + " Value: " + objectProperties.get(key));

							if (key.equals(SubjectPropertiesConstants.BELONGS_TO_FACULTY)) {
								subject.setFaculty(objectProperties.get(key));
							} else if (key.equals(SubjectPropertiesConstants.SUBJECT_TYPE)) {
								subject.setType(objectProperties.get(key));
							}

						}

						// print Data Properties
						Map<String, String> dataProperties = restrictionVisitor.getDataProps();
						for (String key : dataProperties.keySet()) {
							System.out.println("DataProp: " + key + " Value: " + dataProperties.get(key));

							if (key.equals(SubjectPropertiesConstants.TERM)) {
								subject.setTerm(dataProperties.get(key));
							} else if (key.equals(SubjectPropertiesConstants.CREDITS_NUM)) {
								subject.setCreditsNum(dataProperties.get(key));
							} else if (key.equals(SubjectPropertiesConstants.WEEK_HOURS_NUM)) {
								subject.setWeekHours(dataProperties.get(key));
							} else if (key.equals(SubjectPropertiesConstants.TOTAL_HOURS_NUM)) {
								subject.setTotalHours(dataProperties.get(key));
							}
						}

						// print subjectResearchLines

						List<String> subjectResearchLines = restrictionVisitor.getSubjectResearchLines();
						System.out.println("STUDY SIZE: " + restrictionVisitor.getSubjectResearchLines().size());
						for (String subjectResearchLine : restrictionVisitor.getSubjectResearchLines()) {
							System.out.println("Вивчає: " + subjectResearchLine);
						}
						subject.setSubjectResearchLines(subjectResearchLines);

						// print suject's preSubjects
						List<String> preSubjects = restrictionVisitor.getPreSubjects();
						System.out.println("PreSubjects SIZE: " + restrictionVisitor.getPreSubjects().size());
						for (String preSubject : restrictionVisitor.getPreSubjects()) {
							System.out.println("PreSubject: " + preSubject);
						}
						subject.setPreSubjects(preSubjects);

						// print subject's postSubjects
						List<String> postSubjects = restrictionVisitor.getPostSubjects();
						System.out.println("PostSubjects SIZE: " + restrictionVisitor.getPostSubjects().size());
						for (String postSubject : restrictionVisitor.getPostSubjects()) {
							System.out.println("PostSubject: " + postSubject);
						}
						subject.setPostSubjects(postSubjects);

						// fill SubjectDTO
						subject.setName(shortSubjectName);
						System.out.println("Recommended subject: " + subject.toString());
						System.out.println("PreSubjects: " + subject.getPreSubjects().size());
						System.out.println("PostSubjects: " + subject.getPostSubjects().size());
						recommendedSubjects.add(subject);
					}
					System.out.println("RECOMMENDED SUBJECTS SIZE: " + recommendedSubjects.size());
					
					//check if rec system provides recommendations acc to user's restrictions
					if(recommendedSubjects.size() == 1 && recommendedSubjects.get(0).getName() == "Nothing"){
						/*
						JOptionPane.showMessageDialog(null, "Змініть вимоги пошуку!", "Немає відповідних рекомендацій!",
								JOptionPane.ERROR_MESSAGE);*/
						JOptionPane.showMessageDialog(null, "Показати рекомендації без врахування:\n типу предмету, к-сті кредитів, семестру ?", "Немає відповідних рекомендацій!",
								JOptionPane.OK_CANCEL_OPTION);
						
						}
					else{	
						JOptionPane.showMessageDialog(null, "Успішного запису на дисципліни =)", "Операція успішна",
								JOptionPane.INFORMATION_MESSAGE);
					showSubjectsPanel.setData(recommendedSubjects);
					showSubjectsPanel.revalidate();
					showSubjectsPanel.repaint();
						}

				}
			}

		});

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
	 * fill subjectType // load subjectTypes from ontology by DLQuery
	 */

	public void fillSubjectTypeBox() {
		subjectTypeBox.removeAllItems();
		subjectTypeBox.addItem(subjTypeDefault);
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

	public void dispose() {

	}

}
