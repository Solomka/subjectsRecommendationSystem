package ua.com.yaremko.system.tab;

import org.protege.editor.owl.ui.OWLWorkspaceViewsTab;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tab that represents Subjects Recommendation System Plugin
 * 
 * @author Solomka
 *
 */
public class SubjectsRecommenderTab extends OWLWorkspaceViewsTab {

	private static final Logger log = LoggerFactory.getLogger(SubjectsRecommenderTab.class);

	public SubjectsRecommenderTab() {
		setToolTipText("Subjects Recommendation System");
	}

	@Override
	public void initialise() {
		super.initialise();

		log.info("Subjects Recommendation System Plugin initialized");
	}

	@Override
	public void dispose() {
		super.dispose();
		log.info("Subjects Recommendation System Plugin disposed");
	}
}
