package ua.com.yaremko.system.view;

import java.awt.GridLayout;
import java.util.Set;

import javax.swing.JOptionPane;

import org.protege.editor.owl.model.cache.OWLExpressionUserCache;
import org.protege.editor.owl.model.classexpression.OWLExpressionParserException;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.protege.editor.owl.model.inference.OWLReasonerManager;
import org.protege.editor.owl.model.inference.ReasonerUtilities;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.com.yaremko.system.core.DLQuery;
import ua.com.yaremko.system.view.panel.SearchFormPanel;

/**
 * View Component that provides students requirements input form
 * 
 * @author Solomka
 *
 */
public class SearchViewComponent extends AbstractOWLViewComponent {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchViewComponent.class);

	private SearchFormPanel searchformComponent;
	// private ShowSubjectsPanel showSubjectsPanel;
	// private ShowSubjectDetailsPanel showSubjectsDetailsPanel;
	
	 private final OWLModelManagerListener listener = event -> {
	        if (event.isType(EventType.ONTOLOGY_CLASSIFIED)) {
	            //doQuery();
	        }
	    };

	    private boolean requiresRefresh = false;

	@Override
	protected void initialiseOWLView() throws Exception {
		setLayout(new GridLayout(0, 1));
		searchformComponent = new SearchFormPanel(getOWLModelManager());
		add(searchformComponent);
		
		getOWLModelManager().addListener(listener);

        addHierarchyListener(event -> {
            if (requiresRefresh && isShowing()) {
                //doQuery();
            }
        });
        
    	LOGGER.info("SearchViewComponent initialized");
    	
        DLQuery dlQuery = new DLQuery(getOWLEditorKit());
        Set<OWLClass> result = dlQuery.getSubClasses("Предмет and кількістьКредитів value \"4.5\"^^xsd:double", true);
        
        for (OWLClass entity : result) {
        	System.out.println("Entity" + entity.toString() + "/n");
			
		}
        
	
	}

	@Override
	protected void disposeOWLView() {
		searchformComponent.dispose();
	}
	
}
