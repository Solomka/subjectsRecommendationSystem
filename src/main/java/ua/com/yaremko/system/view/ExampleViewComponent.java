package ua.com.yaremko.system.view;

import java.awt.BorderLayout;

import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleViewComponent extends AbstractOWLViewComponent {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleViewComponent.class);
	//private static final Logger LOGGER = Logger.getLogger(ExampleViewComponent.class.getName());

	// total number of ontology classes
	private Metrics metricsComponent;

	/*
	 * work as with Frame
	 */
	@Override
	protected void initialiseOWLView() throws Exception {
		setLayout(new BorderLayout());
		metricsComponent = new Metrics(getOWLModelManager());
		add(metricsComponent, BorderLayout.CENTER);
		LOGGER.info("Example View Component initialized");
	}

	@Override
	protected void disposeOWLView() {
		metricsComponent.dispose();
	}
}
