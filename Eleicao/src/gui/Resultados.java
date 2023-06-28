package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class Resultados extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3206567689351485344L;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Resultados frame = new Resultados();
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
	public Resultados() {
		setClosable(true);
		setTitle("RESULTADOS");
		setBounds(50, 10, 642, 441);
		getContentPane().setLayout(null);

		JTextPane txtQtdVotos = new JTextPane();

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 126, 609, 274);
		getContentPane().add(scrollPane);

		table = new JTable();
		table.setFocusTraversalKeysEnabled(false);
		table.setFocusable(false);
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Id", "Candidato", "Votos" }));
		DefaultTableModel tabela = (DefaultTableModel) table.getModel();
		tabela.setRowCount(0);
		table.setSurrendersFocusOnKeystroke(true);
		table.getColumnModel().getColumn(0).setPreferredWidth(34);
		table.getColumnModel().getColumn(1).setPreferredWidth(204);
		table.getColumnModel().getColumn(1).setMinWidth(30);
		table.getColumnModel().getColumn(2).setPreferredWidth(67);
		scrollPane.setViewportView(table);

		table.getTableHeader().setReorderingAllowed(false);
		table.setDefaultEditor(Object.class, null);

		JTextPane txtStatus = new JTextPane();

		JLabel lblNewLabel = new JLabel("Eleição:");
		lblNewLabel.setBounds(10, 11, 117, 15);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		getContentPane().add(lblNewLabel);

		JComboBox<String> comboBoxEleicao = new JComboBox<>();
		comboBoxEleicao.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				Connection con = null;
				PreparedStatement pst = null;
				ResultSet rs = null;

				try {

					Class.forName("org.sqlite.JDBC");
					con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

					// Busca ID da eleição pelo nome selecionado na combobox

					String eleicaoIdSql = "SELECT id_eleicao FROM eleicao WHERE titulo_eleicao = ?";
					pst = con.prepareStatement(eleicaoIdSql);
					pst.setString(1, comboBoxEleicao.getSelectedItem().toString());
					rs = pst.executeQuery();

					int eleicaoId = 0;
					if (rs.next()) {
						eleicaoId = rs.getInt("id_eleicao");
					}

					// Busca qnt de total votos da eleicao selecionada

					String qntVotosTotalSQL = "SELECT COUNT(id_voto) FROM voto WHERE id_eleicao = ?";
					pst = con.prepareStatement(qntVotosTotalSQL);
					pst.setInt(1, eleicaoId);
					rs = pst.executeQuery();

					String qntVotosTotal = null;
					if (rs.next()) {
						qntVotosTotal = rs.getString("COUNT(id_voto)");
					}

					// Busca status da eleicao selecionada

					String StatusSQL = "SELECT status FROM eleicao WHERE id_eleicao = ?";
					pst = con.prepareStatement(StatusSQL);
					pst.setInt(1, eleicaoId);
					rs = pst.executeQuery();

					String status = null;
					if (rs.next()) {
						status = rs.getString("status");
					}

					// Mostra status
					txtStatus.setText(status);

					// Mostra qnt total de votos
					txtQtdVotos.setText(qntVotosTotal);

					// Alimenta a tabela
					DefaultTableModel tabela = (DefaultTableModel) table.getModel();
					PreparedStatement p = con.prepareStatement(
							"SELECT id_candidato, nome_candidato from candidato where id_eleicao = ?");
					p.setInt(1, eleicaoId);
					p.execute();
					ResultSet r = p.executeQuery();
					tabela.setRowCount(0);

					while (r.next()) {

						try {
							Integer idCandidato = r.getInt("id_candidato");
							String nomeCandidato = r.getString("nome_candidato");

							String qntVotosCandidatoSQL = "SELECT COUNT(id_voto) FROM voto "
									+ "WHERE id_candidato = ? AND id_eleicao = ?";
							PreparedStatement p2 = con.prepareStatement(qntVotosCandidatoSQL);
							p2.setInt(1, idCandidato);
							p2.setInt(2, eleicaoId);
							ResultSet rs1 = p2.executeQuery();

							String qntVotosCandidato = null;

							qntVotosCandidato = rs1.getString("COUNT(id_voto)");
							Object[] dados = { idCandidato, nomeCandidato, qntVotosCandidato };
							tabela.addRow(dados);
						} catch (Exception e1) {
							e1.printStackTrace();

						}

					}

				} catch (SQLException | ClassNotFoundException ex) {
					ex.printStackTrace();
				}

				// fim
			}
		});
		comboBoxEleicao.setBounds(10, 29, 609, 30);
		getContentPane().add(comboBoxEleicao);
		Statement st1 = null;
		ResultSet rs1 = null;

		try {
			Class.forName("org.sqlite.JDBC");

			Connection con1 = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

			st1 = con1.createStatement();
			rs1 = st1.executeQuery("select titulo_eleicao from eleicao");

			while (rs1.next()) {
				String nomeEleicao = rs1.getString("titulo_eleicao");
				comboBoxEleicao.addItem(nomeEleicao);
			}

		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		JLabel lblStatusDaEleio = new JLabel("Status da eleição:");
		lblStatusDaEleio.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblStatusDaEleio.setBounds(10, 74, 107, 15);
		getContentPane().add(lblStatusDaEleio);

		JLabel lblTotalDeVotos = new JLabel("Total de votos da eleição:");
		lblTotalDeVotos.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTotalDeVotos.setBounds(10, 100, 156, 15);
		getContentPane().add(lblTotalDeVotos);

		txtStatus.setBackground(SystemColor.menu);
		txtStatus.setCaretColor(new Color(0, 0, 0));
		txtStatus.setEditable(false);
		txtStatus.setBounds(124, 70, 107, 19);
		getContentPane().add(txtStatus);

		txtQtdVotos.setBackground(SystemColor.menu);
		txtQtdVotos.setEditable(false);
		txtQtdVotos.setBounds(168, 95, 124, 20);
		getContentPane().add(txtQtdVotos);

	}
}
