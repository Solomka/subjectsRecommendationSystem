package ua.com.yaremko.system.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

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

	private Dimension size;

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

		this.size = new Dimension(1000, 700);
		setPreferredSize(size);

		int rightWidth = size.width / 2;
		int leftTopHeight = size.height / 2;

		setLayout(new BorderLayout());

		System.out.println("ONTOLOGY ID: " + getOWLEditorKit().getOWLModelManager().getOWLReasonerManager()
				.getCurrentReasoner().getRootOntology().getOntologyID().getOntologyIRI().get());

		IRI currOntologyIRI = getOWLEditorKit().getOWLModelManager().getOWLReasonerManager().getCurrentReasoner()
				.getRootOntology().getOntologyID().getOntologyIRI().get();

		if (currOntologyIRI != null
				&& currOntologyIRI.toString().equals(SubjectPropertiesConstants.PLUGIN_ONTOLOGY_IRI)) {
			// init view panels
			showSubjectDetailsPanel = new ShowSubjectDetailsPanel(
					new Dimension(size.width - rightWidth, size.height - leftTopHeight));
			showSubjectsPanel = new ShowSubjectsPanel(getOWLModelManager(),
					new Dimension(size.width - rightWidth, leftTopHeight), showSubjectDetailsPanel);
			searchFormPanel = new SearchFormPanel(getOWLEditorKit(), getOWLModelManager(),
					new Dimension(rightWidth, size.height), showSubjectsPanel);

			JPanel left = new JPanel(new BorderLayout());
			left.add(showSubjectsPanel, BorderLayout.CENTER);
			left.add(showSubjectDetailsPanel, BorderLayout.SOUTH);

			add(left, BorderLayout.CENTER);
			add(searchFormPanel, BorderLayout.EAST);

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
