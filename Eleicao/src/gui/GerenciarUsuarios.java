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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class GerenciarUsuarios extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2221026365237847941L;
	private JTextField nome;
	private JTextField endereco;
	private JTextField registro;
	private JTextField senha;
	private JTable table;
	private JTextField idpessoa;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GerenciarUsuarios frame = new GerenciarUsuarios();
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public GerenciarUsuarios() {
		setResizable(true);
		setClosable(true);
		setTitle("GERENCIAR USUARIOS");
		setBounds(50, 10, 1023, 680);
		getContentPane().setLayout(null);

		JComboBox<String> cbacesso = new JComboBox<>();

		JLabel lblNewLabel = new JLabel("Nome:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 57, 59, 14);
		getContentPane().add(lblNewLabel);

		nome = new JTextField();
		nome.setBounds(10, 76, 980, 25);
		getContentPane().add(nome);
		nome.setColumns(10);

		endereco = new JTextField();
		endereco.setColumns(10);
		endereco.setBounds(10, 126, 980, 25);
		getContentPane().add(endereco);

		JLabel lblEndereo = new JLabel("Endereço:");
		lblEndereo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEndereo.setBounds(10, 106, 89, 14);
		getContentPane().add(lblEndereo);

		JLabel lblLogin = new JLabel("Registro:");
		lblLogin.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblLogin.setBounds(10, 155, 79, 14);
		getContentPane().add(lblLogin);

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

					String sql = "UPDATE usuario SET nome_usuario = ?, endereco_usuario = ?, adm = ?, registro_usuario = ?, senha = ?"
							+ "WHERE id_usuario = ?";

					ps = con.prepareStatement(sql);
					ps.setString(1, nome.getText());
					ps.setString(2, endereco.getText());
					ps.setString(3, cbacesso.getSelectedItem().toString());
					ps.setString(4, registro.getText());
					ps.setString(5, senha.getText());
					ps.setInt(6, id);

					ps.executeUpdate();

					tabela.setValueAt(nome.getText(), linha, 1);
					tabela.setValueAt(endereco.getText(), linha, 2);
					tabela.setValueAt(cbacesso.getSelectedItem().toString(), linha, 3);
					tabela.setValueAt(registro.getText(), linha, 4);
					tabela.setValueAt(senha.getText(), linha, 5);

					nome.setText("");
					endereco.setText("");
					cbacesso.setSelectedItem("");
					registro.setText("");
					senha.setText("");
					idpessoa.setText("");

					nome.requestFocus();

					ps.close();
					con.close();
				} catch (SQLException | ClassNotFoundException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}

				// fim
			}
		});
		salvar.setBounds(802, 195, 89, 50);
		getContentPane().add(salvar);

		JButton editar = new JButton("EDITAR");
		editar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// SETAR CAMPOS

				int linha = table.getSelectedRow();

				String idlinha = table.getValueAt(linha, 0).toString();
				String nomelinha = table.getValueAt(linha, 1).toString();
				String enderecolinha = table.getValueAt(linha, 2).toString();
				String acessolinha = table.getValueAt(linha, 3).toString();
				String registrolinha = table.getValueAt(linha, 4).toString();
				String senhalinha = table.getValueAt(linha, 5).toString();

//					Class.forName("org.sqlite.JDBC");
//
//					Connection con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/Prova Anderson/eleicao/DB/Votacao.db");
//
//					String sql = "SELECT (nome, endereco, acesso, registro, senha) FROM pessoa WHERE id_pessoa = ?";
//					
//					PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//					
//					ps.setInt(1, id);	
//					rs = ps.executeQuery();

				idpessoa.setText(idlinha);
				nome.setText(nomelinha);
				endereco.setText(enderecolinha);
				cbacesso.setSelectedItem(acessolinha);
				registro.setText(registrolinha);
				senha.setText(senhalinha);

			}
		});
		editar.setBounds(703, 195, 89, 50);
		getContentPane().add(editar);

		JButton novo = new JButton("NOVO");
		novo.setVisible(false);
		novo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// NOVO
				DefaultTableModel tabela = (DefaultTableModel) table.getModel();
				ResultSet rs = null;

				try {
					Class.forName("org.sqlite.JDBC");

					Connection con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

					String sql = "Insert into usuario (nome_usuario, endereco_usuario, adm, registro_usuario, senha)"
							+ "values (?, ?, ?, ?, ?)";
					PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, nome.getText());
					ps.setString(2, endereco.getText());
					ps.setString(3, cbacesso.getSelectedItem().toString());
					ps.setString(4, registro.getText());
					ps.setString(5, senha.getText());

					ps.executeUpdate();
					rs = ps.getGeneratedKeys();
					if (rs.next()) {

						int id = rs.getInt(1);

						String[] add = { String.valueOf(id), nome.getText(), endereco.getText(),
								cbacesso.getSelectedItem().toString(), registro.getText(), senha.getText(), };

						tabela.addRow(add);

					}
					nome.setText("");
					endereco.setText("");
					cbacesso.setSelectedItem("");
					registro.setText("");
					senha.setText("");
					idpessoa.setText("");

				} catch (SQLException | ClassNotFoundException ex) {
					ex.printStackTrace();
				}

				// fim do action
			}
		});
		novo.setBounds(453, 195, 89, 50);
		getContentPane().add(novo);

		JButton excluir = new JButton("EXCLUIR");
		excluir.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// EXCLUIR

				PreparedStatement ps = null;
				int linha = table.getSelectedRow();

				try {

					int id = Integer.parseInt(table.getValueAt(linha, 0).toString());

					Class.forName("org.sqlite.JDBC");
					Connection con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

					String sql = "DELETE FROM usuario WHERE id_usuario=? ";
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

			// fim

		});
		excluir.setBounds(901, 195, 89, 50);
		getContentPane().add(excluir);

		registro = new JTextField();
		registro.setColumns(10);
		registro.setBounds(10, 174, 170, 25);
		getContentPane().add(registro);

		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSenha.setBounds(10, 203, 59, 14);
		getContentPane().add(lblSenha);

		senha = new JTextField();
		senha.setColumns(10);
		senha.setBounds(10, 220, 170, 25);
		getContentPane().add(senha);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 254, 980, 386);
		getContentPane().add(scrollPane);

		table = new JTable();

		table.setSurrendersFocusOnKeystroke(true);
		table.setFocusTraversalKeysEnabled(false);
		table.setFocusable(false);
		table.setUpdateSelectionOnSort(false);
		table.setRequestFocusEnabled(false);

		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "NOME", "ENDERE\u00C7O", "ACESSO", "REGISTRO", "SENHA"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(25);
		table.getColumnModel().getColumn(1).setPreferredWidth(258);
		table.getColumnModel().getColumn(2).setPreferredWidth(186);
		table.getColumnModel().getColumn(3).setPreferredWidth(103);
		scrollPane.setViewportView(table);

		table.setDefaultEditor(Object.class, null);
		table.getTableHeader().setReorderingAllowed(false);

		JButton btnNewButton = new JButton("LISTAR");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// LISTAR
				Statement st = null;

				DefaultTableModel tabela = (DefaultTableModel) table.getModel();

				tabela.setRowCount(0);

				try {
					Class.forName("org.sqlite.JDBC");

					Connection con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

					String sql = "SELECT * FROM usuario";

					st = con.createStatement();
					ResultSet rs = st.executeQuery(sql);

					while (rs.next()) {

						Integer id_usuario = rs.getInt("id_usuario");
						String nome = rs.getString("nome_usuario");
						String endereco = rs.getString("endereco_usuario");
						String acesso = rs.getString("adm");
						String registro = rs.getString("registro_usuario");
						Integer senha = rs.getInt("senha");

						Object[] dados = { id_usuario, nome, endereco, acesso, registro, senha };

						tabela.addRow(dados);

					}

				} catch (SQLException | ClassNotFoundException ex) {
					ex.printStackTrace();
				}

				// fim
			}
		});
		btnNewButton.setBounds(604, 195, 89, 50);
		getContentPane().add(btnNewButton);

		JLabel lblId = new JLabel("ID:");
		lblId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblId.setBounds(10, 11, 59, 14);
		getContentPane().add(lblId);

		idpessoa = new JTextField();
		idpessoa.setEnabled(false);
		idpessoa.setColumns(10);
		idpessoa.setBounds(10, 30, 59, 25);
		getContentPane().add(idpessoa);

		cbacesso.setModel(new DefaultComboBoxModel(new String[] { "Administrador", "Comum" }));
		cbacesso.setBounds(79, 31, 271, 25);
		getContentPane().add(cbacesso);

		JLabel lblNewLabel_1 = new JLabel("Tipo:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(79, 9, 59, 19);
		getContentPane().add(lblNewLabel_1);

	}
}
