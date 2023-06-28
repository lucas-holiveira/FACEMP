package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class CadastroCandidato extends JInternalFrame {
	public JComboBox<String> comboBoxPartido = new JComboBox<>();
	public String partido = null;

	public void adicionarPartido(String partido) {
		comboBoxPartido.addItem(partido); // Adiciona o partido à comboBoxPartido
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2239225024414608372L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CadastroCandidato frame = new CadastroCandidato();
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
	public CadastroCandidato() {

		setClosable(true);
		setTitle("CADASTRO DE CANDIDATO");
		setBounds(50, 10, 492, 260);
		getContentPane().setLayout(null);

		JLabel lblEleio = new JLabel("Eleição:");
		lblEleio.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEleio.setBounds(6, 5, 53, 15);
		getContentPane().add(lblEleio);

		JLabel lblPartido = new JLabel("Partido:");
		lblPartido.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPartido.setBounds(6, 100, 83, 23);
		getContentPane().add(lblPartido);

		JComboBox<String> eleicaoCandidato = new JComboBox<>();
		eleicaoCandidato.setBounds(6, 25, 452, 25);
		getContentPane().add(eleicaoCandidato);

		Statement st = null;
		ResultSet rs = null;

		try {
			Class.forName("org.sqlite.JDBC");

			Connection con1 = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

			st = con1.createStatement();
			rs = st.executeQuery("select titulo_eleicao from eleicao");

			while (rs.next()) {
				String nomeEleicao = rs.getString("titulo_eleicao");
				eleicaoCandidato.addItem(nomeEleicao);
			}

		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		JLabel lblNome = new JLabel("Nome:");
		lblNome.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNome.setBounds(6, 54, 83, 20);
		getContentPane().add(lblNome);

		JComboBox<String> nomeCandidato = new JComboBox<>();
		nomeCandidato.setBounds(6, 74, 452, 25);
		getContentPane().add(nomeCandidato);
		Statement st1 = null;
		ResultSet rs1 = null;

		try {
			Class.forName("org.sqlite.JDBC");

			Connection con1 = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

			st1 = con1.createStatement();
			rs1 = st1.executeQuery("select nome_usuario from usuario");

			while (rs1.next()) {
				String nomeUsuario = rs1.getString("nome_usuario");
				nomeCandidato.addItem(nomeUsuario);
			}

		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		comboBoxPartido.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {

				Statement st1 = null;
				ResultSet rs1 = null;

				try {
					Class.forName("org.sqlite.JDBC");

					Connection con1 = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

					st1 = con1.createStatement();
					rs1 = st1.executeQuery("select nome_usuario from usuario");

					while (rs1.next()) {
						String nomeUsuario = rs1.getString("nome_usuario");
						nomeCandidato.addItem(nomeUsuario);
					}

				} catch (SQLException | ClassNotFoundException ex) {
					ex.printStackTrace();
				}

			}
		});

		comboBoxPartido.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

			}
		});
		comboBoxPartido.setBounds(6, 126, 341, 25);
		getContentPane().add(comboBoxPartido);
		Statement st11 = null;
		ResultSet rs11 = null;

		try {
			Class.forName("org.sqlite.JDBC");

			Connection con1 = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

			st11 = con1.createStatement();
			rs11 = st11.executeQuery("select num_partido, nome_partido from partido");

			while (rs11.next()) {
				String nomePartido = "(" + rs11.getString("num_partido") + ") " + rs11.getString("nome_partido");
				comboBoxPartido.addItem(nomePartido);
			}

		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		JButton novoPartido = new JButton("NOVO");
		novoPartido.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CadastroPartido cp = new CadastroPartido();
				cp.setVisible(true);

			}
		});

		JButton cdCandidato = new JButton("CADASTRAR");
		cdCandidato.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nomeCandidatoSelecionado = (String) nomeCandidato.getSelectedItem();

				int idEleicaoSelecionada = eleicaoCandidato.getSelectedIndex() + 1;

				int idnomeCandidatoSelecionado = nomeCandidato.getSelectedIndex() + 1;

				int idPartidoSelecionado = comboBoxPartido.getSelectedIndex() + 1;

				Connection con = null;

				try {
					Class.forName("org.sqlite.JDBC");
					con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

					// Verifica se o candidato ja esta cadastrado em uma eleição
					String sqlVerificarCandidato = "SELECT id_candidato FROM eleicao_candidato WHERE id_candidato = ?";
					try (PreparedStatement statementVerificarCandidato = con.prepareStatement(sqlVerificarCandidato)) {
						statementVerificarCandidato.setInt(1, idnomeCandidatoSelecionado);
						try (ResultSet rsCandidato = statementVerificarCandidato.executeQuery()) {
							if (rsCandidato.next()) {
								JOptionPane.showMessageDialog(null, "O candidato já está cadastrado em uma eleição.");

							}
						}
					}

					// Inseri os dados na tabela candidato
					String sqlInserirCandidato = "INSERT INTO candidato (nome_candidato, id_eleicao, id_usuario, id_partido) VALUES (?, ?, ?, ?)";
					try (PreparedStatement statementInserirCandidato = con.prepareStatement(sqlInserirCandidato,
							Statement.RETURN_GENERATED_KEYS)) {
						statementInserirCandidato.setString(1, nomeCandidatoSelecionado);
						statementInserirCandidato.setInt(2, idEleicaoSelecionada);
						statementInserirCandidato.setInt(3, idnomeCandidatoSelecionado);
						statementInserirCandidato.setInt(4, idPartidoSelecionado);
						statementInserirCandidato.executeUpdate();

						// Recupera o id gerado automaticamente na tabela candidato
						int candidatoId = -1;
						try (ResultSet generatedKeys = statementInserirCandidato.getGeneratedKeys()) {
							if (generatedKeys.next()) {
								candidatoId = generatedKeys.getInt(1);
							}
						}

						// Inserir os dados na tabela eleicao_candidato
						String sqlInserirEleicaoCandidato = "INSERT INTO eleicao_candidato (id_eleicao, id_candidato) VALUES (?, ?)";
						try (PreparedStatement statementInserirEleicaoCandidato = con
								.prepareStatement(sqlInserirEleicaoCandidato)) {
							statementInserirEleicaoCandidato.setInt(1, idEleicaoSelecionada);
							statementInserirEleicaoCandidato.setInt(2, candidatoId);
							statementInserirEleicaoCandidato.executeUpdate();
						}
					}

					con.close();

					JOptionPane.showMessageDialog(null, "Candidato cadastrado com sucesso!");

				} catch (ClassNotFoundException | SQLException ex) {

					//ex.printStackTrace();
					//JOptionPane.showMessageDialog(null, "Erro ao cadastrar candidato: " + ex.getMessage());
				} finally {

					if (con != null) {
						try {
							con.close();
						} catch (SQLException ex) {
							//ex.printStackTrace();
						}
					}
				}

			}

		});
		cdCandidato.setBounds(6, 162, 452, 47);
		getContentPane().add(cdCandidato);

		novoPartido.setBounds(357, 126, 101, 25);
		getContentPane().add(novoPartido);

	}
}
