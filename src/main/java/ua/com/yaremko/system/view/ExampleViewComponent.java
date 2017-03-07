package ua.com.yaremko.system.view;

import java.awt.BorderLayout;

import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleViewComponent extends AbstractOWLViewComponent {

	private static final Logger log = LoggerFactory.getLogger(ExampleViewComponent.class);

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
		log.info("Example View Component initialized");
	}

	@Override
	protected void disposeOWLView() {
		metricsComponent.dispose();
	}
}