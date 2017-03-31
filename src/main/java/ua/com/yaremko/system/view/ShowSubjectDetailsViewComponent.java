package ua.com.yaremko.system.view;

import java.awt.BorderLayout;

import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.com.yaremko.system.view.panel.ShowSubjectDetailsPanel;

/**
 * View Component that represents resulted subject details
 * 
 * @author Solomka
 *
 */
public class ShowSubjectDetailsViewComponent extends AbstractOWLViewComponent {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShowSubjectDetailsViewComponent.class);

	private ShowSubjectDetailsPanel showSubjectDetailsComponent;

	@Override
	protected void initialiseOWLView() throws Exception {
		setLayout(new BorderLayout());
		showSubjectDetailsComponent = new ShowSubjectDetailsPanel (null);
		add(showSubjectDetailsComponent, BorderLayout.CENTER);

		LOGGER.info("ShowSubjectDetailsComponent initialized");
	}

	@Override
	protected void disposeOWLView() {
		showSubjectDetailsComponent.dispose();

	}

}
