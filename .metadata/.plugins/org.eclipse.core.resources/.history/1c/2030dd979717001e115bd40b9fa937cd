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

import javax.swing.ButtonGroup;
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
import java.awt.Font;

public class GerenciarEleicao extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5510264851989117853L;
	private JTable table;
	@SuppressWarnings("unused")
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField eleicao;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GerenciarEleicao frame = new GerenciarEleicao();
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public GerenciarEleicao() {
		setTitle("GERENCIAR ELEIÇÃO");
		setClosable(true);
		setBounds(50, 10, 947, 439);
		getContentPane().setLayout(null);
		JComboBox<String> cbstatus = new JComboBox<>();

		JLabel lblId = new JLabel("Eleição:");
		lblId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblId.setBounds(10, 11, 89, 14);
		getContentPane().add(lblId);

		JButton novo = new JButton("NOVO");
		novo.setVisible(false);
		novo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel tabela = (DefaultTableModel) table.getModel();

				Connection con = null;
				ResultSet rs = null;
				PreparedStatement ps = null;

				try {
					Class.forName("org.sqlite.JDBC");

					con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

					String sql = "Insert into eleicao (titulo_eleicao, status)" + "values (?, ?)";
					ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, eleicao.getText());
					ps.setString(2, cbstatus.getSelectedItem().toString());

					ps.executeUpdate();
					rs = ps.getGeneratedKeys();
					if (rs.next()) {

						int id = rs.getInt(1);

						String[] add = { String.valueOf(id), eleicao.getText(), cbstatus.getSelectedItem().toString() };

						tabela.addRow(add);

					}
					eleicao.setText("");
					cbstatus.setSelectedItem("");

				} catch (SQLException | ClassNotFoundException ex) {
					ex.printStackTrace();
				} finally {
					if (rs != null) {
						try {
							rs.close();
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
					if (ps != null) {
						try {
							ps.close();
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}

				}

				// fim do action

			}
		});
		novo.setBounds(433, 57, 89, 50);
		getContentPane().add(novo);

		JButton editar = new JButton("EDITAR");
		editar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// SETAR CAMPOS

				int linha = table.getSelectedRow();

				String eleicaolinha = table.getValueAt(linha, 1).toString();
				String statuslinha = table.getValueAt(linha, 2).toString();

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

				eleicao.setText(eleicaolinha);
				cbstatus.setSelectedItem(statuslinha);

			}

		});
		editar.setBounds(632, 57, 89, 50);
		getContentPane().add(editar);

		JButton salvar = new JButton("SALVAR");
		salvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// inicio
				DefaultTableModel tabela = (DefaultTableModel) table.getModel();
				Connection con = null;
				PreparedStatement ps = null;

				int linha = table.getSelectedRow();
				int id = Integer.parseInt(tabela.getValueAt(linha, 0).toString());

				try {

					Class.forName("org.sqlite.JDBC");
					con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

					String sql = "UPDATE eleicao SET titulo_eleicao = ?, status = ?" + "WHERE id_eleicao = ?";

					ps = con.prepareStatement(sql);

					ps.setString(1, eleicao.getText());
					ps.setString(2, cbstatus.getSelectedItem().toString());
					ps.setInt(3, id);

					ps.executeUpdate();

					tabela.setValueAt(eleicao.getText(), linha, 1);
					tabela.setValueAt(cbstatus.getSelectedItem().toString(), linha, 2);

					eleicao.setText("");
					cbstatus.setSelectedItem("");

					eleicao.requestFocus();

				} catch (SQLException | ClassNotFoundException ex) {
					ex.printStackTrace();
				} finally {
					if (con != null) {
						try {
							con.close();
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}
					if (ps != null) {
						try {
							ps.close();
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}

				}

				// fim
			}
		});
		salvar.setBounds(731, 57, 89, 50);
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

					String sql = "DELETE FROM eleicao WHERE id_eleicao=? ";
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
		excluir.setBounds(830, 57, 89, 50);
		getContentPane().add(excluir);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 118, 909, 280);
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
				"ID", "ELEI\u00C7\u00C3O", "STATUS"
			}
		));
		scrollPane.setViewportView(table);

		table.setDefaultEditor(Object.class, null);
		table.getTableHeader().setReorderingAllowed(false);

		JButton atualizar = new JButton("LISTAR");
		atualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// inicio

				Statement st = null;
				ResultSet rs = null;
				Connection con = null;

				DefaultTableModel tabela = (DefaultTableModel) table.getModel();

				tabela.setRowCount(0);

				try {
					Class.forName("org.sqlite.JDBC");

					con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/Votacao.db");

					String sql = "SELECT id_eleicao, titulo_eleicao, status FROM eleicao";

					st = con.createStatement();
					rs = st.executeQuery(sql);

					while (rs.next()) {

						Integer id = rs.getInt("id_eleicao");
						String eleicao = rs.getString("titulo_eleicao");
						String status = rs.getString("status");

						Object[] dados = { id, eleicao, status };

						tabela.addRow(dados);

					}

				} catch (SQLException | ClassNotFoundException ex) {
					ex.printStackTrace();
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

				// fim
			}
		});
		atualizar.setBounds(533, 57, 89, 50);
		getContentPane().add(atualizar);

		eleicao = new JTextField();
		eleicao.setColumns(10);
		eleicao.setBounds(10, 27, 909, 23);
		getContentPane().add(eleicao);

		JLabel lblStatus_1 = new JLabel("Status:");
		lblStatus_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblStatus_1.setBounds(10, 57, 89, 14);
		getContentPane().add(lblStatus_1);

		cbstatus.setModel(new DefaultComboBoxModel(new String[] { "ABERTO", "FECHADO" }));
		cbstatus.setBounds(10, 75, 296, 23);
		getContentPane().add(cbstatus);

	}
}
