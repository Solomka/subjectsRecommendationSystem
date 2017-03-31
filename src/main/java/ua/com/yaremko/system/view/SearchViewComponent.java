package ua.com.yaremko.system.view;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.semanticweb.owlapi.model.IRI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.com.yaremko.system.core.SubjectPropertiesConstants;
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

	private final OWLModelManagerListener listener = event -> {
		if (event.isType(EventType.ONTOLOGY_CLASSIFIED)) {
			System.out.println("REPAINT!!!!!!!!!!!!");
			searchFormPanel.fillSubjectTypeBox();

		}
	};

	@Override
	protected void initialiseOWLView() throws Exception {

		getOWLModelManager().addListener(listener);

		setLayout(new GridLayout(0, 2));

		System.out.println("ONTOLOGY ID: " + getOWLEditorKit().getOWLModelManager().getOWLReasonerManager()
				.getCurrentReasoner().getRootOntology().getOntologyID().getOntologyIRI().get());

		IRI currOntologyIRI = getOWLEditorKit().getOWLModelManager().getOWLReasonerManager().getCurrentReasoner()
				.getRootOntology().getOntologyID().getOntologyIRI().get();

		if (currOntologyIRI != null
				&& currOntologyIRI.toString().equals(SubjectPropertiesConstants.PLUGIN_ONTOLOGY_IRI)) {
			// init view panels
			showSubjectDetailsPanel = new ShowSubjectDetailsPanel(getOWLModelManager());
			showSubjectsPanel = new ShowSubjectsPanel(getOWLModelManager(), showSubjectDetailsPanel);
			searchFormPanel = new SearchFormPanel(getOWLEditorKit(), getOWLModelManager(), showSubjectsPanel);

			JPanel combinedPanels = new JPanel();
			combinedPanels.setLayout(new GridLayout(2, 0));
			combinedPanels.add(showSubjectsPanel);
			combinedPanels.add(showSubjectDetailsPanel);

			// add panels on the view
			add(combinedPanels);
			add(searchFormPanel);

			LOGGER.info("SearchViewComponent initialized");
		} else {
			JOptionPane.showMessageDialog(null, "Виберіть онтологію Subjects!", "Помилка", JOptionPane.ERROR_MESSAGE);

			LOGGER.info("SearchViewComponent is not fully initialized ");
		}

	}

	@Override
	protected void disposeOWLView() {
		getOWLModelManager().removeListener(listener);

		searchFormPanel.dispose();
		showSubjectsPanel.dispose();
		showSubjectDetailsPanel.dispose();
	}

}
