package ua.com.yaremko.system.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	@Override
	protected void initialiseOWLView() throws Exception {
		setLayout(new GridLayout(0, 1));
		searchformComponent = new SearchFormPanel(getOWLModelManager());
		add(searchformComponent);

		LOGGER.info("SearchViewComponent initialized");
	}

	@Override
	protected void disposeOWLView() {
		searchformComponent.dispose();
	}

}
