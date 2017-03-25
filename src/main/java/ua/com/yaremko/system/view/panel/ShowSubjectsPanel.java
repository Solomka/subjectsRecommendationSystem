package ua.com.yaremko.system.view.panel;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.protege.editor.owl.model.OWLModelManager;

public class ShowSubjectsPanel extends JPanel {

	private OWLModelManager modelManager;	

	private JLabel testLabel;
	
	public ShowSubjectsPanel(OWLModelManager modelManager) {
		this.modelManager = modelManager;

		init();
		initListeners();

		add(testLabel);
		}

	private void init() {
		this.testLabel = new JLabel("Hello from ShowSubjectsPanel (Table of subjects)");
	}

	private void initListeners() {

	}

	public void dispose() {

	}

}
