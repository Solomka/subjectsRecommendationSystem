package ua.com.yaremko.system.view.panel;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.protege.editor.owl.model.OWLModelManager;

public class ShowSubjectDetailsPanel extends JPanel {

	private OWLModelManager modelManager;

	private JLabel testLabel;

	public ShowSubjectDetailsPanel(OWLModelManager modelManager) {
		this.modelManager = modelManager;

		init();
		initListeners();

		add(testLabel);
	}

	private void init() {
		this.testLabel = new JLabel("Hello from ShowSubjectDetailsPanel (Table of subject details)");
	}

	private void initListeners() {

	}

	public void dispose() {

	}

}
