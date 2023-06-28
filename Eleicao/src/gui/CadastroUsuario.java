package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class CadastroUsuario extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8800523081293090410L;
	private JPanel contentPane;
	private JTextField nomeUsuario;

	private JTextField enderecoUsuario;
	@SuppressWarnings("unused")
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField registroUsuario;
	private JPasswordField senhaUsuario;
	@SuppressWarnings("unused")
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CadastroUsuario frame = new CadastroUsuario();
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
	public CadastroUsuario() {
		JComboBox<String> admUsuario = new JComboBox<>();

		setTitle("CADASTRO DE USUARIO");
		setClosable(true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(50, 10, 533, 321);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Nome:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 11, 71, 15);
		contentPane.add(lblNewLabel);

		JLabel lblEndereo = new JLabel("Endereço:");
		lblEndereo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEndereo.setBounds(10, 58, 83, 15);
		contentPane.add(lblEndereo);

		nomeUsuario = new JTextField();
		nomeUsuario.setBounds(10, 31, 498, 25);
		contentPane.add(nomeUsuario);
		nomeUsuario.setColumns(10);

		enderecoUsuario = new JTextField();
		enderecoUsuario.setColumns(10);
		enderecoUsuario.setBounds(10, 78, 498, 25);
		contentPane.add(enderecoUsuario);

		JLabel lblLogin = new JLabel("Registro:");
		lblLogin.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblLogin.setBounds(10, 138, 83, 15);
		contentPane.add(lblLogin);

		registroUsuario = new JTextField();
		registroUsuario.setColumns(10);
		registroUsuario.setBounds(10, 154, 193, 25);
		contentPane.add(registroUsuario);

		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSenha.setBounds(10, 182, 65, 15);
		contentPane.add(lblSenha);

		senhaUsuario = new JPasswordField();
		senhaUsuario.setBounds(10, 200, 193, 25);
		contentPane.add(senhaUsuario);

		JButton inserirUser = new JButton("CADASTRAR");
		inserirUser.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {

				try {
					Class.forName("org.sqlite.JDBC");

					Connection con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

					String sql = "Insert into usuario (nome_usuario, endereco_usuario, adm, registro_usuario, senha)"
							+ "values (?, ?, ?, ?, ?)";

					PreparedStatement ps = con.prepareStatement(sql);

					ps.setString(1, nomeUsuario.getText());
					ps.setString(2, enderecoUsuario.getText());
					ps.setString(3, admUsuario.getSelectedItem().toString());
					ps.setString(4, registroUsuario.getText());
					ps.setString(5, senhaUsuario.getText());
					ps.executeUpdate();

					nomeUsuario.setText("");
					enderecoUsuario.setText("");
					senhaUsuario.setText("");
					admUsuario.setSelectedItem("");

				} catch (SQLException | ClassNotFoundException ex) {
					ex.printStackTrace();
				}

				JOptionPane.showMessageDialog(inserirUser, "Seu N° de registro é: " + registroUsuario.getText());
			}
		});
		inserirUser.setBounds(10, 232, 498, 47);
		contentPane.add(inserirUser);

		admUsuario.setModel(new DefaultComboBoxModel(new String[] { "Administrador", "Comum" }));
		admUsuario.setBounds(10, 109, 193, 24);
		contentPane.add(admUsuario);

	}
}
