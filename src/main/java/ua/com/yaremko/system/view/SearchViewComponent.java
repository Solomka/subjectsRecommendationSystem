package ua.com.yaremko.system.view;

import java.awt.GridLayout;
import java.util.Set;

import javax.swing.JPanel;

import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.com.yaremko.system.core.DLQuery;
import ua.com.yaremko.system.view.panel.SearchFormPanel;
import ua.com.yaremko.system.view.panel.ShowSubjectDetailsPanel;
import ua.com.yaremko.system.view.panel.ShowSubjectsPanel;

/**
 * View Component that provides students requirements input form
 * 
 * @author Solomka
 *
 */
public class SearchViewComponent extends AbstractOWLViewComponent {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchViewComponent.class);

	private ShowSubjectDetailsPanel showSubjectDetailsPanel;
	private ShowSubjectsPanel showSubjectsPanel;
	private SearchFormPanel searchFormPanel;	


	@Override
	protected void initialiseOWLView() throws Exception {
		
		setLayout(new GridLayout(0, 2));
		
		//init view panels
		showSubjectDetailsPanel = new ShowSubjectDetailsPanel(getOWLModelManager());
		showSubjectsPanel = new ShowSubjectsPanel(getOWLModelManager(), showSubjectDetailsPanel);
		searchFormPanel = new SearchFormPanel(getOWLEditorKit(), getOWLModelManager(), showSubjectsPanel);		
		
		JPanel combinedPanels = new JPanel();
		combinedPanels.setLayout(new GridLayout(2, 0));
		combinedPanels.add(showSubjectsPanel);
		combinedPanels.add(showSubjectDetailsPanel);
		
		add(combinedPanels);
		
		//add panel on the view
		add(searchFormPanel);
		
		
    	
        DLQuery dlQuery = new DLQuery(getOWLEditorKit());
        
     
        Set<OWLClass> result = dlQuery.getSubClassesSet("Предмет and кількістьКредитів value \"4.5\"^^xsd:double", true);
        
        for(OWLClass cls: result){
        Set<OWLObjectProperty> properties = cls.getObjectPropertiesInSignature();
        
        for (OWLObjectProperty prop: properties){
			System.out.println("PROPERTY: " + prop.toString());
			
		}
        }
        /*
        String [] result = dlQuery.getSubClasses("Предмет and кількістьКредитів value \"4.5\"^^xsd:double", true);
        
        for (int i = 0; i< result.length; i++) {
        	System.out.println("Entity" + result[i] + "\n");
			
		}
		*/
        
        LOGGER.info("SearchViewComponent initialized");
	}

	@Override
	protected void disposeOWLView() {
		searchFormPanel.dispose();
		showSubjectsPanel.dispose();
		showSubjectDetailsPanel.dispose();	
		
	}
	
}
