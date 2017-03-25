package ua.com.yaremko.system.view;

import java.awt.BorderLayout;

import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.com.yaremko.system.view.panel.ShowSubjectsPanel;

/**
 * View Component that represents resulted subjects list (table)
 * 
 * @author Solomka
 *
 */
public class ShowSubjectsViewComponent extends AbstractOWLViewComponent {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShowSubjectsViewComponent.class);

	private ShowSubjectsPanel showSubjectsComponent;

	@Override
	protected void initialiseOWLView() throws Exception {
		setLayout(new BorderLayout());
		showSubjectsComponent = new ShowSubjectsPanel(getOWLModelManager());
		add(showSubjectsComponent, BorderLayout.CENTER);

		LOGGER.info("ShowSubjectsComponent initialized");

	}

	@Override
	protected void disposeOWLView() {
		showSubjectsComponent.dispose();

	}

}
