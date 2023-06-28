package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class GerenciarCandidato extends JInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7755826393757765078L;
	@SuppressWarnings("unused")
	private JTextField candidatoNome;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GerenciarCandidato frame = new GerenciarCandidato();
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
	public GerenciarCandidato() {
		setResizable(true);
		setClosable(true);
		setTitle("GERENCIAR CANDIDATO");
		setBounds(50, 10, 945, 614);
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Candidato:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 59, 89, 14);
		getContentPane().add(lblNewLabel);

		JComboBox<String> comboBoxCandidato = new JComboBox<>();
		comboBoxCandidato.setBounds(10, 77, 909, 25);
		getContentPane().add(comboBoxCandidato);
//		try {
//			Class.forName("org.sqlite.JDBC");
//			Connection con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");
//
//			String queryCandidato = "SELECT nome_candidato FROM candidato";
//			Statement stCandidato = con.createStatement();
//			ResultSet rsCandidato = stCandidato.executeQuery(queryCandidato);
//
//		while (rsCandidato.next()) {
//				String tituloEleicao = rsCandidato.getString("titulo_eleicao");
//				candidatoNome.addItem(tituloEleicao);
//			}

		JComboBox<String> comboBoxPartido = new JComboBox<>();
		comboBoxPartido.setBounds(10, 124, 909, 25);
		getContentPane().add(comboBoxPartido);
		comboBoxPartido.removeAllItems();

		JComboBox<String> comboBoxEleicao = new JComboBox<>();
		comboBoxEleicao.setBounds(10, 30, 909, 25);
		getContentPane().add(comboBoxEleicao);

		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

			String queryEleicoes = "SELECT titulo_eleicao FROM eleicao";
			Statement stEleicoes = con.createStatement();
			ResultSet rsEleicoes = stEleicoes.executeQuery(queryEleicoes);

			while (rsEleicoes.next()) {
				String tituloEleicao = rsEleicoes.getString("titulo_eleicao");
				comboBoxEleicao.addItem(tituloEleicao);
			}

			comboBoxEleicao.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Obtem a eleição selecionada na comboBox
					String eleicaoSelecionada = (String) comboBoxEleicao.getSelectedItem();

					// Limpa a ComboBox de candidatos
					comboBoxCandidato.removeAllItems();

					// Consultar o banco de dados para obter os candidatos da eleição selecionada
					String queryCandidatos = "SELECT c.nome_candidato FROM candidato c "
							+ "INNER JOIN eleicao_candidato ec ON c.id_candidato = ec.id_candidato "
							+ "INNER JOIN eleicao e ON e.id_eleicao = ec.id_eleicao " + "WHERE e.titulo_eleicao = ?";
					try (PreparedStatement stCandidatos = con.prepareStatement(queryCandidatos)) {
						stCandidatos.setString(1, eleicaoSelecionada);
						ResultSet rsCandidatos = stCandidatos.executeQuery();

						while (rsCandidatos.next()) {
							String nomeCandidato = rsCandidatos.getString("nome_candidato");
							comboBoxCandidato.addItem(nomeCandidato);
						}
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
			});

			comboBoxCandidato.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					// Limpa a ComboBox de partidos
					comboBoxPartido.removeAllItems();
					
					
					// Obter o candidato selecionado
					String candidatoSelecionado = (String) comboBoxCandidato.getSelectedItem();

					String queryCandidato = "SELECT c.nome_candidato, p.nome_partido, p.num_partido "
							+ "FROM candidato c " + "INNER JOIN partido p ON c.id_partido = p.id_partido "
							+ "WHERE c.nome_candidato = ?";
					try (PreparedStatement stCandidato = con.prepareStatement(queryCandidato)) {
						stCandidato.setString(1, candidatoSelecionado);
						ResultSet rsCandidato = stCandidato.executeQuery();

						if (rsCandidato.next()) {
							String nomePartido = rsCandidato.getString("nome_partido");

//							 Preencher os campos de texto com as informações do partido
							comboBoxPartido.addItem(nomePartido);
						}
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
			});
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
		}

		JButton excluirCandidato = new JButton("EXCLUIR");
		excluirCandidato.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// EXCLUIR
				Connection con = null;
				PreparedStatement ps = null;
				int linha = table.getSelectedRow();

				try {

					int id = Integer.parseInt(table.getValueAt(linha, 0).toString());

					Class.forName("org.sqlite.JDBC");
					con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

					String sql = "DELETE FROM candidato WHERE id_candidato=? ";
					ps = con.prepareStatement(sql);
					ps.setInt(1, id);
					ps.executeUpdate();

					((DefaultTableModel) table.getModel()).removeRow(linha);

				

					JOptionPane.showMessageDialog(rootPane, "Excluído com Sucesso!");

				} catch (SQLException | ClassNotFoundException ex) {
					ex.printStackTrace();
				} finally {
					if (ps != null) {
						try {
							ps.close();
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}

					if (con != null) {
						try {
							con.close();
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}
				}

			}

			// fim

		});
		excluirCandidato.setBounds(830, 160, 89, 50);
		getContentPane().add(excluirCandidato);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 221, 909, 350);
		getContentPane().add(scrollPane);

		table = new JTable();
		//aqui
		table.setSurrendersFocusOnKeystroke(true);
		table.setFocusTraversalKeysEnabled(false);
		table.setFocusable(false);
		table.setUpdateSelectionOnSort(false);
		table.setRequestFocusEnabled(false);
		//aqui
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "CANDIDATO", "ELEI\u00C7\u00C3O", "PARTIDO"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(41);
		table.getColumnModel().getColumn(1).setPreferredWidth(104);
		table.getColumnModel().getColumn(2).setPreferredWidth(98);
		table.getColumnModel().getColumn(3).setPreferredWidth(121);
		scrollPane.setViewportView(table);
		//aqui
		table.setDefaultEditor(Object.class, null);
		table.getTableHeader().setReorderingAllowed(false);
		//aqui
		JButton atualizarCandidato = new JButton("LISTAR");
		atualizarCandidato.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// LISTAR
				Statement st = null;

				DefaultTableModel tabela = (DefaultTableModel) table.getModel();

				tabela.setRowCount(0);

				try {
					Class.forName("org.sqlite.JDBC");

					Connection con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

					String sql = "SELECT *  FROM candidato "
							+ "INNER JOIN eleicao ON candidato.id_eleicao = eleicao.id_eleicao "
							+ "INNER JOIN partido ON candidato.id_partido = partido.id_partido";

					st = con.createStatement();
					ResultSet rs = st.executeQuery(sql);

					while (rs.next()) {

						Integer id_candidato = rs.getInt("id_candidato");
						String nome = rs.getString("nome_candidato");
						String titulo_eleicao = rs.getString("titulo_eleicao");
						String nome_partido = rs.getString("nome_partido");

						Object[] dados = { id_candidato, nome, titulo_eleicao, nome_partido };

						tabela.addRow(dados);

					}

				} catch (SQLException | ClassNotFoundException ex) {
					ex.printStackTrace();
				}

				// fim
			}
		});
		atualizarCandidato.setBounds(731, 160, 89, 50);
		getContentPane().add(atualizarCandidato);

		JLabel lblEleio = new JLabel("Eleição:");
		lblEleio.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEleio.setBounds(10, 11, 59, 14);
		getContentPane().add(lblEleio);
//
//		Statement st2 = null;
//		ResultSet rs2 = null;
//
//		try {
//			Class.forName("org.sqlite.JDBC");
//
//			Connection con2 = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");
//
//			st2 = con2.createStatement();
//			rs2 = st2.executeQuery("select titulo_eleicao from eleicao");
//
//			while (rs2.next()) {
//				String nomeCandidato = rs2.getString("titulo_eleicao");
//				comboBoxEleicao.addItem(nomeCandidato);
//			}
//
//		} catch (SQLException | ClassNotFoundException ex) {
//			ex.printStackTrace();
//		}

		JLabel lblPartido = new JLabel("Partido:");
		lblPartido.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPartido.setBounds(10, 106, 59, 14);
		getContentPane().add(lblPartido);

		Statement st1 = null;
		ResultSet rs1 = null;

		try {
			Class.forName("org.sqlite.JDBC");

			Connection con1 = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

			st1 = con1.createStatement();
			rs1 = st1.executeQuery("select nome_partido from partido");

			while (rs1.next()) {
				String nomePartido = rs1.getString("nome_partido");
				comboBoxPartido.addItem(nomePartido);
			}

		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}

	}
}
