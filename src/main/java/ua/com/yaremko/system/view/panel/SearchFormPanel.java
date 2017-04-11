package ua.com.yaremko.system.view.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import ua.com.yaremko.system.core.DLQuery;
import ua.com.yaremko.system.core.RestrictionVisitor;
import ua.com.yaremko.system.core.SubjectPropertiesConstants;
import ua.com.yaremko.system.core.converter.DLQueryParamsRequestStringConverter;
import ua.com.yaremko.system.core.converter.OWLClassSubjectDTOConverter;
import ua.com.yaremko.system.core.entity.DLQueryParams;
import ua.com.yaremko.system.core.entity.Relaxations;
import ua.com.yaremko.system.core.entity.SubjectDTO;

public class SearchFormPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// to work with ontology in protege
	private OWLModelManager modelManager;
	private OWLEditorKit owlEditorKit;
	
	//Query processing
	private OWLDataFactory factory;
	private DLQuery dlQuery;
	//restrictions relaxation
	private List<Relaxations> appliedRelax;
	
	OWLOntology currectOntology;
	OWLClassSubjectDTOConverter subjectDTOConverter;
	

	//----------- Swing -----------------
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
		
		factory = modelManager.getOWLDataFactory();
		dlQuery = new DLQuery(owlEditorKit);
		appliedRelax = new ArrayList<>();
		
		currectOntology = modelManager.getOWLReasonerManager().getCurrentReasoner()
				.getRootOntology();
		
		setLayout(new GridLayout(0, 1));
		//setBackground(bgcolor);		
		setBorder(BorderFactory.createEmptyBorder(0, BORDER / 3, 0, BORDER / 3));
		//setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
		//scienceBranchBox.setBackground(bgcolor);
		((JLabel) scienceBranchBox.getRenderer()).setHorizontalAlignment(SwingConstants.LEADING);
		scienceBranchBox.addItem(scBranchDefault);
		// fill scienceBranches
		fillscienceBranchBox();

		specialityBox = new JComboBox<String>();
		//specialityBox.setBackground(bgcolor);
		((JLabel) specialityBox.getRenderer()).setHorizontalAlignment(SwingConstants.LEADING);
		specialityBox.addItem(specDefault);
		// disabled
		specialityBox.setEnabled(false);

		researchLineBox = new JComboBox<String>();
		//researchLineBox.setBackground(bgcolor);
		((JLabel) researchLineBox.getRenderer()).setHorizontalAlignment(SwingConstants.LEADING);
		researchLineBox.addItem(resLineDefault);
		researchLineBox.setSelectedItem(resLineDefault);
		// disabled
		researchLineBox.setEnabled(false);

		subjectTypeBox = new JComboBox<String>();
		//subjectTypeBox.setBackground(bgcolor);
		((JLabel) subjectTypeBox.getRenderer()).setHorizontalAlignment(SwingConstants.LEADING);
		subjectTypeBox.addItem(subjTypeDefault);
		// fill subject types
		fillSubjectTypeBox();

		termBox = new JComboBox<String>(terms);
		//termBox.setBackground(bgcolor);
		((JLabel) termBox.getRenderer()).setHorizontalAlignment(SwingConstants.LEADING);

		creditsNumBox = new JComboBox<String>(creditNums);
		//creditsNumBox.setBackground(bgcolor);
		((JLabel) creditsNumBox.getRenderer()).setHorizontalAlignment(SwingConstants.LEADING);

		// init search button
		searchButtonPanel = new JPanel();
		searchButtonPanel.setLayout(new BorderLayout());
		//searchButtonPanel.setBackground(bgcolor);

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
				
				specialityBox.removeAllItems();
				specialityBox.addItem(specDefault);
				specialityBox.setSelectedItem(specDefault);
				specialityBox.setEnabled(false);

				researchLineBox.removeAllItems();
				researchLineBox.addItem(resLineDefault);
				researchLineBox.setSelectedItem(resLineDefault);
				researchLineBox.setEnabled(false);

				if (scienceBranchSelected != null && !scienceBranchSelected.equals(scBranchDefault)) {
					// fill scienceBranch slecialities
					
					fillSpecialitiesBox();
					specialityBox.setEnabled(true);

					searchButton.setEnabled(true);
				}
			}
		});

		// speciality listener
		specialityBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				specialitySelected = (String) specialityBox.getSelectedItem();

				// enable researchLine comboBox
				researchLineBox.removeAllItems();
				researchLineBox.addItem(resLineDefault);
				researchLineBox.setSelectedItem(resLineDefault);
				researchLineBox.setEnabled(false);
				
				if (specialitySelected != null && !specialitySelected.equals(specDefault)) {					
					
					// fill speciality researchLines
					fillResearchLineBox();
					researchLineBox.setEnabled(true);
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
				if (!searchPanelValidate()) {
					return;
				}else {							
					
					DLQueryParams dlQueryParams = getUserSelections();
					String dlQueryRequest = DLQueryParamsRequestStringConverter.fromDLQueryParamsToRequest(dlQueryParams);
					Set<OWLClass> recommSubjects = dlQuery.getSubClassesSet(dlQueryRequest, true);
					
					//check if empty
					if(recommSubjects.size() == 1 && recommSubjects.contains(factory.getOWLClass(IRI.create(SubjectPropertiesConstants.NOTHING_CLASS_IRI))) ){
						
						recommSubjects = processSubjectsQueryWithRelaxation(dlQueryParams, recommSubjects);
						
						StringBuilder restrRelaxationBuilder = new StringBuilder();
						
						for(int i = 0; i< appliedRelax.size(); i++){
							restrRelaxationBuilder.append(appliedRelax.get(i).getValue());
							if(i != appliedRelax.size() - 1){
								restrRelaxationBuilder.append(", ");
							}else{
								restrRelaxationBuilder.append(" ?");
							}
							}
						
						int result = JOptionPane.showConfirmDialog((Component) null, "Показати рекомендації без врахування критеріїв: \n" + restrRelaxationBuilder.toString() ,
								"Немає відповідних рекомендацій!", JOptionPane.YES_NO_OPTION);
						
						if(result == JOptionPane.NO_OPTION){
							JOptionPane.showMessageDialog((Component) null, "Немає відповідних рекомендацій! \n Змініть критерії пошуку!", "Увага!", JOptionPane.INFORMATION_MESSAGE);
						}else if(result == JOptionPane.CLOSED_OPTION){
							JOptionPane.showMessageDialog((Component) null,
									"Немає відповідних рекомендацій! \n Змініть критерії пошуку!", "Увага!", JOptionPane.INFORMATION_MESSAGE);
						}else if ( result == JOptionPane.YES_OPTION){
															
							// get recommended subjects
							List<SubjectDTO> recommendedSubjects = getRecommendedSubjectDTOsList(recommSubjects);		
							
							System.out.println("RECOMMENDED SUBJECTS SIZE: " + recommendedSubjects.size());
						
								JOptionPane.showMessageDialog(null, "Успішного запису на дисципліни =)", "Операція успішна",
										JOptionPane.INFORMATION_MESSAGE);
							showSubjectsPanel.setData(recommendedSubjects);
							showSubjectsPanel.revalidate();
							showSubjectsPanel.repaint();								
							
						}
						
					}else{												
						// get recommended subjects
						List<SubjectDTO> recommendedSubjects = getRecommendedSubjectDTOsList(recommSubjects);		
						
						System.out.println("RECOMMENDED SUBJECTS SIZE: " + recommendedSubjects.size());
					
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
	
	
	private Set<OWLClass> processSubjectsQueryWithRelaxation(DLQueryParams dlQueryParams, Set<OWLClass> recommSubjects){
		
		DLQueryParams dlQueryParamsParameters = dlQueryParams;
		String dlQueryRequest = DLQueryParamsRequestStringConverter.fromDLQueryParamsToRequest(dlQueryParams);
		Set<OWLClass> recommenderSubjects = recommSubjects;		
		
		appliedRelax = new ArrayList<>();		
		String subjectType = null; 					
		
		
			while(recommenderSubjects.size() == 1 && recommenderSubjects.contains(factory.getOWLClass(IRI.create(SubjectPropertiesConstants.NOTHING_CLASS_IRI)))){
				
				if(dlQueryParamsParameters.getSubjectType() != null ){
					subjectType = dlQueryParamsParameters.getSubjectType();
					dlQueryParamsParameters.setSubjectType(null);
					appliedRelax.add(Relaxations.SUBJECT_TYPE);								
					
				}else if(dlQueryParamsParameters.getResearchLine() != null && appliedRelax.contains(Relaxations.SUBJECT_TYPE)){
					dlQueryParamsParameters.setSubjectType(subjectType);			
					appliedRelax.remove(Relaxations.SUBJECT_TYPE);
					
					dlQueryParamsParameters.setResearchLine(null);
					appliedRelax.add(Relaxations.RESEARCH_LINE);						
					
				}else if(dlQueryParamsParameters.getResearchLine() != null && !appliedRelax.contains(Relaxations.SUBJECT_TYPE)){
					dlQueryParamsParameters.setResearchLine(null);
					appliedRelax.add(Relaxations.RESEARCH_LINE);								
					
				} else if( dlQueryParamsParameters.getSpeciality() != null && appliedRelax.contains(Relaxations.SUBJECT_TYPE) && appliedRelax.contains(Relaxations.RESEARCH_LINE)){
					dlQueryParamsParameters.setSpeciality(null);
					appliedRelax.add(Relaxations.SPECIALITY);								
					
				}else if(dlQueryParamsParameters.getTerm() != null && appliedRelax.contains(Relaxations.SUBJECT_TYPE) && appliedRelax.contains(Relaxations.RESEARCH_LINE) && appliedRelax.contains(Relaxations.SPECIALITY)){
					dlQueryParamsParameters.setTerm(null);
					appliedRelax.add(Relaxations.TERM);								
					
				}else if(dlQueryParamsParameters.getCreditsNum() != null && appliedRelax.contains(Relaxations.SUBJECT_TYPE) && appliedRelax.contains(Relaxations.RESEARCH_LINE) && appliedRelax.contains(Relaxations.SPECIALITY) && appliedRelax.contains(Relaxations.TERM)){
					dlQueryParamsParameters.setCreditsNum(null);
					appliedRelax.add(Relaxations.TERM);							
					
				}else{
					JOptionPane.showMessageDialog(null, "Немає відповідних рекомендацій! \n Змініть критерії пошуку!", "Увага!",
							JOptionPane.ERROR_MESSAGE);
					break;
				}
				
				dlQueryRequest = DLQueryParamsRequestStringConverter.fromDLQueryParamsToRequest(dlQueryParams);
				System.out.println("Relaxed query: " + dlQueryRequest);
				recommenderSubjects = dlQuery.getSubClassesSet(dlQueryRequest, true);
				
					}
			return recommenderSubjects;
		}
	
	private List<SubjectDTO> getRecommendedSubjectDTOsList(Set<OWLClass>recommSubjects){
		
		List<SubjectDTO> recommendedSubjects = new ArrayList<>();
		SubjectDTO subject;

		// fill List <SubjectDTO> recommendedSubjects
		for (OWLClass c : recommSubjects) {
			subjectDTOConverter = new OWLClassSubjectDTOConverter(currectOntology);	
			subject = subjectDTOConverter.fromOWLClassToSubjectDTO(c);	
			System.out.println("Recommended subject: " + subject.toString());
			System.out.println("PreSubjects: " + subject.getPreSubjects().size());
			System.out.println("PostSubjects: " + subject.getPostSubjects().size());
			recommendedSubjects.add(subject);
		}
		
		Collections.sort(recommendedSubjects);
		//Collections.sort(recommendedSubjects, SubjectDTO.SORT_BY_NAME);
		
		return recommendedSubjects;
		
	}
		
	private boolean searchPanelValidate(){
		
		if (scienceBranchSelected == null || scienceBranchSelected.equals(scBranchDefault)) {
			JOptionPane.showMessageDialog(null, "Виберіть галузь науки!", "Некоректний ввід даних",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} /*else if (specialitySelected == null || specialitySelected.equals(specDefault)) {
			JOptionPane.showMessageDialog(null, "Виберіть спеціальність!", "Некоректний ввід даних",
					JOptionPane.ERROR_MESSAGE);
			return;
		} else if (subjectTypeSelected == null || subjectTypeSelected.equals(subjTypeDefault)) {
			JOptionPane.showMessageDialog(null, "Виберіть тип предмету!", "Некоректний ввід даних",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (termSelected == null || termSelected.equals(termDefault)) {
			JOptionPane.showMessageDialog(null, "Виберіть семестр!", "Некоректний ввід даних",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (credNumSelected == null || credNumSelected.equals(credNumDefault)) {
			JOptionPane.showMessageDialog(null, "Виберіть к-сть кредитів!", "Некоректний ввід даних",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} */
		
		return true;		
	}
	
	public void dispose() {

	}

}
