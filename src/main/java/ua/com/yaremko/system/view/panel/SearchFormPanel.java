package ua.com.yaremko.system.view.panel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerListener;

public class SearchFormPanel extends JPanel {

	private OWLModelManager modelManager;

	private JComboBox<String> scienceBranchBox;
	private JLabel scienceBranchLabal;
	private JButton searchButton;
	private String scienceBranchName;
	private String scBranchDefault = "  виберіть галузь науки  ";

	public SearchFormPanel(OWLModelManager modelManager) {
		this.modelManager = modelManager;
		recalculate();

		setLayout(new GridLayout(3, 1));
		init();
		initListeners();

		add(scienceBranchLabal);
		add(scienceBranchBox);
		add(searchButton);
	}

	private void init() {
		scienceBranchBox = new JComboBox<String>();
		scienceBranchBox.addItem(scBranchDefault);

		// add items to JComboBox

		/*
		 * Iterator iterator = Store.getGroups().iterator();
		 * 
		 * for (int i = 0; i < Store.getNumberOfGroups(); i++) { Group gr =
		 * (Group) iterator.next(); scienceBranchBox.addItem(gr.getName()); }
		 */

		scienceBranchLabal = new JLabel("ВИберіть галузь науки:   ");

		searchButton = new JButton("Ok");
		searchButton.setEnabled(false);
	}

	private void initListeners() {

		modelManager.addListener(modelListener);

		scienceBranchBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				scienceBranchName = (String) scienceBranchBox.getSelectedItem();
				searchButton.setEnabled(true);
			}

		});

		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (scienceBranchName.equals(scBranchDefault)) {
					JOptionPane.showMessageDialog(null, "Помилка! Заповніть, будь ласка, всі поля");
					return;
				}
				/*
				 * Group g = Store.getGroup((String)
				 * scienceBranchBox.getSelectedItem());
				 * g.addProduct(nameField.getText(), descField.getText(),
				 * prodField.getText(), 0,
				 * Integer.parseInt(priceField.getText()));
				 * nameField.setText(""); descField.setText("");
				 * prodField.setText(""); priceField.setText("");
				 */
				JOptionPane.showMessageDialog(null, "", "Успішна операція", JOptionPane.INFORMATION_MESSAGE);
			}

		});

	}

	private ActionListener refreshAction = e -> recalculate();

	private OWLModelManagerListener modelListener = event -> {
		if (event.getType() == EventType.ACTIVE_ONTOLOGY_CHANGED) {
			recalculate();
		}
	};

	private void recalculate() {
		int count = modelManager.getActiveOntology().getClassesInSignature().size();
		if (count == 0) {
			count = 1; // owl:Thing is always there.
		}
	}

	public void dispose() {
		modelManager.removeListener(modelListener);
	}

}
