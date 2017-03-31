package ua.com.yaremko.system.view.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.protege.editor.owl.model.OWLModelManager;

import ua.com.yaremko.system.core.SubjectDTO;
import ua.com.yaremko.system.core.utils.LineWrapTableCellRenderer;

public class ShowSubjectsPanel extends JPanel {

	private OWLModelManager modelManager;

	private ShowSubjectDetailsPanel showSubjectDetailsPanel;

	private Object[] columnsHeader = new String[] { "№", "Назва предмету", "Кафедра" };

	private JScrollPane scrollPane;

	private Dimension size;
	private Color bgcolor = Color.WHITE;

	private DefaultTableModel model;
	private JTable table;
	private JPanel tablePanel;

	private JButton detailsBtn;

	private static final int BUTTON_WIDTH = 200;
	private static final int BUTTON_HEIGHT = 35;

	private List<SubjectDTO> data;

	public ShowSubjectsPanel(OWLModelManager modelManager, Dimension preferredSize,
			ShowSubjectDetailsPanel showSubjectDetailsPanel) {
		this.modelManager = modelManager;
		this.showSubjectDetailsPanel = showSubjectDetailsPanel;

		this.size = preferredSize;

		setBackground(bgcolor);
		setPreferredSize(size);
		setLayout(new BorderLayout());

		JPanel lpanel = new JPanel();
		lpanel.setBackground(bgcolor);
		lpanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

		JLabel label = new JLabel("Рекомендовані предмети");
		lpanel.add(label);

		tablePanel = buildTablePanel();

		add(lpanel, BorderLayout.NORTH);
		add(tablePanel, BorderLayout.CENTER);
	}

	private JPanel buildTablePanel() {
		JPanel res = new JPanel(new BorderLayout());
		res.setBackground(bgcolor);

		model = new DefaultTableModel() {
			@Override
			public Class<?> getColumnClass(int column) {
				if (getRowCount() > 0) {
					Object value = getValueAt(0, column);
					if (value != null) {
						return getValueAt(0, column).getClass();
					}
				}
				return super.getColumnClass(column);
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		};

		model.setColumnIdentifiers(columnsHeader);
		table = new JTable(model);

		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(size.width / 5);
		tcm.getColumn(1).setPreferredWidth(3 * size.width / 5);
		tcm.getColumn(2).setPreferredWidth(2 * size.width / 5);

		table.getTableHeader().setPreferredSize(new Dimension(table.getSize().width, 30));
		table.getTableHeader().setBackground(Color.WHITE);
		table.setRowHeight(30);

		table.setDefaultRenderer(String.class, new LineWrapTableCellRenderer());

		JScrollPane pane = new JScrollPane(table);
		pane.setBorder(BorderFactory.createEmptyBorder());
		pane.getViewport().setBackground(bgcolor);

		JPanel btnPanel = new JPanel(new BorderLayout());
		btnPanel.setBackground(bgcolor);
		btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		detailsBtn = new JButton("Деталі");
		detailsBtn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		detailsBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(null, "Виберіть предмет!", "Помилка",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				showSubjectDetailsPanel.setSubject(data.get(selectedRow));
				showSubjectDetailsPanel.revalidate();
				showSubjectDetailsPanel.repaint();
			}

		});
		detailsBtn.setEnabled(false);
		btnPanel.add(detailsBtn, BorderLayout.EAST);

		res.add(pane, BorderLayout.CENTER);
		res.add(btnPanel, BorderLayout.SOUTH);

		return res;
	}

	public void setData(List<SubjectDTO> data) {
		int rowCount = model.getRowCount();
		for (int i = rowCount - 1; i >= 0; i--) {
			model.removeRow(i);
		}

		this.data = data;
		for (int i = 0; i < data.size(); i++) {
			model.addRow(new String[] { Integer.toString(i + 1), data.get(i).getName(), data.get(i).getFaculty() });
		}
		detailsBtn.setEnabled(true);
		tablePanel.revalidate();
		tablePanel.repaint();
	}

	public void dispose() {

	}

}
