package gui;

import java.awt.Font;
import java.awt.SystemColor;
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
import javax.swing.JTextField;

public class Votacao extends JInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7407033340520161480L;
	private JTextField txtNomePt;
	private JTextField txtNumPt;
	private int userId;

	/**
	 * 
	 */
	@SuppressWarnings({})
	public Votacao(int userId) {
		this.userId = userId;

		setClosable(true);
		setTitle("VOTAÇÃO");
		setBounds(50, 10, 499, 301);
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Escolha eleição:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 11, 109, 15);
		getContentPane().add(lblNewLabel);

		JComboBox<String> comboBoxCandidato = new JComboBox<>();
		comboBoxCandidato.setBounds(10, 83, 459, 28);
		getContentPane().add(comboBoxCandidato);

		JComboBox<String> comboBoxEleicao = new JComboBox<>();
		comboBoxEleicao.setBounds(10, 30, 459, 28);
		getContentPane().add(comboBoxEleicao);

		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

			String queryEleicoes = "SELECT titulo_eleicao, status FROM eleicao WHERE status = 'ABERTO'";
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
							String numPartido = rsCandidato.getString("num_partido");

							// Preencher os campos de texto com as informações do partido
							txtNomePt.setText(nomePartido);
							txtNumPt.setText(numPartido);
						}
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
			});
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
		}
		JLabel lblEscolhaOCandidato = new JLabel("Escolha o candidato:");
		lblEscolhaOCandidato.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEscolhaOCandidato.setBounds(10, 64, 141, 15);
		getContentPane().add(lblEscolhaOCandidato);

		JButton confirmarVoto = new JButton("CONFIRMAR");
		confirmarVoto.addActionListener(new ActionListener() {
			@SuppressWarnings("resource")
			public void actionPerformed(ActionEvent e) {
				Connection con = null;
				PreparedStatement st = null;
				ResultSet rs = null;

				try {
					Class.forName("org.sqlite.JDBC");
					con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

					int pessoaId = userId;

					// Busca ID do candidato pelo nome selecionado na combobox
					String candidatoIdSql = "SELECT id_candidato FROM candidato WHERE nome_candidato = ?";
					st = con.prepareStatement(candidatoIdSql);
					st.setString(1, comboBoxCandidato.getSelectedItem().toString());
					rs = st.executeQuery();

					int candidatoId = 0;

					// Verifica se existe uma linha de resultado antes de recuperar o valor do ID do
					// candidato
					if (rs.next()) {
						candidatoId = rs.getInt("id_candidato");
					}

					// Busca ID da eleição pelo nome selecionado na combobox
					String eleicaoIdSql = "SELECT id_eleicao FROM eleicao WHERE titulo_eleicao = ?";
					st = con.prepareStatement(eleicaoIdSql);
					st.setString(1, comboBoxEleicao.getSelectedItem().toString());
					rs = st.executeQuery();

					int eleicaoId = -1;

					// Verifica se existe uma linha de resultado antes de recuperar o valor do ID da
					// eleição
					if (rs.next()) {
						eleicaoId = rs.getInt("id_eleicao");
					}

					st = con.prepareStatement("SELECT COUNT(*) FROM voto WHERE id_usuario = ? AND id_eleicao = ?");
					st.setInt(1, pessoaId);
					st.setInt(2, eleicaoId);
					rs = st.executeQuery();

					if (rs.next()) {
						int qtdVotos = rs.getInt(1);

						if (qtdVotos > 0) {
							JOptionPane.showMessageDialog(null, "Você já votou nesta eleição");
							return;
						}
					}

					// Verifica se o usuário já votou neste candidato antes
					st = con.prepareStatement(
							"SELECT COUNT(*) FROM voto WHERE id_usuario = ? AND id_candidato = ? AND id_eleicao = ?");
					st.setInt(1, pessoaId);
					st.setInt(2, candidatoId);
					st.setInt(3, eleicaoId);
					rs = st.executeQuery();

					if (rs.next()) {
						int qtdVotos = rs.getInt(1);

						if (qtdVotos > 0) {
							JOptionPane.showMessageDialog(null, "Você já votou neste candidato nesta eleição");
							return;
						}
					}

					// Registra o voto do usuário para o candidato selecionado
					st = con.prepareStatement(
							"INSERT INTO voto (qtdVotos, id_eleicao, id_usuario, id_candidato) VALUES (?, ?, ?, ?)");

					st.setInt(1, 1); // Inserir na coluna qtdVotos, que deve ser criada na tabela voto do banco.
					st.setInt(2, eleicaoId); // id eleicao
					st.setInt(3, userId);
					st.setInt(4, candidatoId); // id candidato
					st.executeUpdate();

					JOptionPane.showMessageDialog(null, "Voto registrado com sucesso!");
				} catch (SQLException ex) {
					ex.printStackTrace();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} finally {
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}
					if (st != null) {
						try {
							st.close();
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
		});
		confirmarVoto.setFont(new Font("Tahoma", Font.PLAIN, 16));
		confirmarVoto.setBounds(10, 222, 459, 30);
		getContentPane().add(confirmarVoto);

		JLabel lblNomeDoPartido = new JLabel("Nome do partido:");
		lblNomeDoPartido.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNomeDoPartido.setBounds(10, 116, 141, 15);
		getContentPane().add(lblNomeDoPartido);

		JLabel lblNumeroDoPartido = new JLabel("Numero do partido:");
		lblNumeroDoPartido.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNumeroDoPartido.setBounds(10, 166, 141, 15);
		getContentPane().add(lblNumeroDoPartido);

		txtNomePt = new JTextField();
		txtNomePt.setEditable(false);
		txtNomePt.setBackground(SystemColor.menu);
		txtNomePt.setBounds(10, 135, 459, 28);
		getContentPane().add(txtNomePt);
		txtNomePt.setColumns(10);

		txtNumPt = new JTextField();
		txtNumPt.setEditable(false);
		txtNumPt.setBackground(SystemColor.menu);
		txtNumPt.setColumns(10);
		txtNumPt.setBounds(10, 184, 86, 28);
		getContentPane().add(txtNumPt);

	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
