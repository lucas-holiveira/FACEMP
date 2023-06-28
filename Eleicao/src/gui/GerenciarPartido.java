package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;

public class GerenciarPartido extends JInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8223812280711255620L;
	private JTextField nomePartido;
	private JTextField numeroPartido;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GerenciarPartido frame = new GerenciarPartido();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GerenciarPartido() {
		setTitle("GERENCIAR PARTIDO");
		setClosable(true);
		setBounds(50, 10, 742, 462);
		getContentPane().setLayout(null);

		JButton novo = new JButton("NOVO");
		novo.setVisible(false);
		novo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel tabela = (DefaultTableModel) table.getModel();
				ResultSet rs = null;

				try {
					Class.forName("org.sqlite.JDBC");

					Connection con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

					String sql = "Insert into partido (nome_partido, num_partido)" + "values (?, ?)";
					PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, nomePartido.getText());
					ps.setString(2, numeroPartido.getText());

					ps.executeUpdate();
					rs = ps.getGeneratedKeys();
					if (rs.next()) {

						int id = rs.getInt(1);

						String[] add = { String.valueOf(id), nomePartido.getText(), numeroPartido.getText() };

						tabela.addRow(add);

					}
					nomePartido.setText("");
					numeroPartido.setText("");

				} catch (SQLException | ClassNotFoundException ex) {
					ex.printStackTrace();
				}

				// fim do action

			}
		});
		novo.setBounds(216, 61, 89, 50);
		getContentPane().add(novo);

		JButton editar = new JButton("EDITAR");
		editar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// SETAR CAMPOS

				int linha = table.getSelectedRow();

				String nomelinha = table.getValueAt(linha, 1).toString();
				String numerolinha = table.getValueAt(linha, 2).toString();

				nomePartido.setText(nomelinha);
				numeroPartido.setText(numerolinha);

			}

		});
		editar.setBounds(417, 61, 89, 50);
		getContentPane().add(editar);

		JButton salvar = new JButton("SALVAR");
		salvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// inicio
				DefaultTableModel tabela = (DefaultTableModel) table.getModel();
				PreparedStatement ps = null;
				int linha = table.getSelectedRow();
				int id = Integer.parseInt(tabela.getValueAt(linha, 0).toString());

				try {

					Class.forName("org.sqlite.JDBC");
					Connection con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

					String sql = "UPDATE partido SET nome_partido = ?, num_partido = ?" + "WHERE id_partido = ?";

					ps = con.prepareStatement(sql);

					ps.setString(1, nomePartido.getText());
					ps.setString(2, numeroPartido.getText());
					ps.setInt(3, id);

					ps.executeUpdate();

					tabela.setValueAt(nomePartido.getText(), linha, 1);
					tabela.setValueAt(numeroPartido.getText(), linha, 2);

					nomePartido.setText("");
					numeroPartido.setText("");

					nomePartido.requestFocus();

				} catch (SQLException | ClassNotFoundException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}

				// fim

			}
		});
		salvar.setBounds(516, 61, 89, 50);
		getContentPane().add(salvar);

		JButton excluir = new JButton("EXCLUIR");
		excluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				PreparedStatement ps = null;
				int linha = table.getSelectedRow();

				try {

					int id = Integer.parseInt(table.getValueAt(linha, 0).toString());

					Class.forName("org.sqlite.JDBC");
					Connection con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

					String sql = "DELETE FROM partido WHERE id_partido=? ";
					ps = con.prepareStatement(sql);
					ps.setInt(1, id);
					ps.executeUpdate();

					((DefaultTableModel) table.getModel()).removeRow(linha);

					ps.close();
					con.close();

					JOptionPane.showMessageDialog(rootPane, "Excluído com Sucesso!");

				} catch (SQLException | ClassNotFoundException ex) {
					ex.printStackTrace();
				}

			}

		});
		excluir.setBounds(615, 61, 89, 50);
		getContentPane().add(excluir);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 122, 694, 283);
		getContentPane().add(scrollPane);
		// table.setSurrendersFocusOnKeystroke(true);
		// table.setFocusTraversalKeysEnabled(false);
		// table.setFocusable(false);
		// table.setUpdateSelectionOnSort(false);
		// table.setRequestFocusEnabled(false);

		table = new JTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "PARTIDO", "NUMERO"
			}
		));
		scrollPane.setViewportView(table);

		table.setDefaultEditor(Object.class, null);
		table.getTableHeader().setReorderingAllowed(false);

		JButton atualizar = new JButton("LISTAR");
		atualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

//inicio

				Statement st = null;

				DefaultTableModel tabela = (DefaultTableModel) table.getModel();

				tabela.setRowCount(0);

				try {
					Class.forName("org.sqlite.JDBC");

					Connection con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/Votacao.db");

					String sql = "SELECT id_partido, nome_partido, num_partido FROM partido";

					st = con.createStatement();
					ResultSet rs = st.executeQuery(sql);

					while (rs.next()) {

						Integer id = rs.getInt("id_partido");
						String nomePartido1 = rs.getString("nome_partido");
						String numPartido1 = rs.getString("num_partido");

						Object[] dados = { id, nomePartido1, numPartido1 };

						tabela.addRow(dados);

					}

				} catch (SQLException | ClassNotFoundException ex) {
					ex.printStackTrace();
				}

				// fim
			}
		});
		atualizar.setBounds(318, 61, 89, 50);
		getContentPane().add(atualizar);

		nomePartido = new JTextField();
		nomePartido.setColumns(10);
		nomePartido.setBounds(10, 27, 694, 23);
		getContentPane().add(nomePartido);

		JLabel lblStatus_1 = new JLabel("N° do partido:");
		lblStatus_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblStatus_1.setBounds(10, 55, 89, 14);
		getContentPane().add(lblStatus_1);

		JLabel lblNomeDoPartido = new JLabel("Nome do partido:");
		lblNomeDoPartido.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNomeDoPartido.setBounds(10, 5, 116, 19);
		getContentPane().add(lblNomeDoPartido);

		numeroPartido = new JTextField();
		numeroPartido.setColumns(10);
		numeroPartido.setBounds(10, 75, 116, 23);
		getContentPane().add(numeroPartido);

	}
}
