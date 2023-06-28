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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

public class TelaLogin extends JFrame {
	private static final long serialVersionUID = 2925971969788649972L;

	private JPanel contentPane;
	private JTextField login;
	private JPasswordField senha;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaLogin frame = new TelaLogin();
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
	public TelaLogin() {
		setTitle("TELA DE LOGIN");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 347, 206);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Login:");
		lblNewLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel.setBounds(107, 11, 63, 16);
		contentPane.add(lblNewLabel);

		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setHorizontalTextPosition(SwingConstants.CENTER);
		lblSenha.setHorizontalAlignment(SwingConstants.LEFT);
		lblSenha.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblSenha.setBounds(107, 69, 63, 16);
		contentPane.add(lblSenha);

		JLabel lblNewLabel_2 = new JLabel();
		lblNewLabel_2
				.setIcon(new ImageIcon("C:\\Users\\Lucas\\Documents\\tamp\\ws-eclips\\Login\\src\\imagens\\Lock.png"));
		lblNewLabel_2.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(10, 26, 86, 94);
		contentPane.add(lblNewLabel_2);

		login = new JTextField();
		login.setBounds(107, 36, 214, 29);
		contentPane.add(login);
		login.setColumns(10);

		JButton btnNewButton = new JButton("Entrar");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnNewButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {

				String txtNome;
				Connection conn = null;
				PreparedStatement p = null;
				ResultSet r = null;

				try {
					Class.forName("org.sqlite.JDBC");
					conn = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");
					p = conn.prepareStatement("select * from usuario where registro_usuario = ? and senha = ?");
					p.setString(1, login.getText());
					p.setString(2, senha.getText());
					r = p.executeQuery();

					if (r.next()) {
						String perfil = r.getString(4);

						if (perfil.equals("Administrador")) {
							int id = r.getInt("id_usuario");
							txtNome = (r.getString("nome_usuario"));
							home hm2 = new home(id);
							hm2.setVisible(true);
							setVisible(false);
							conn.close();
							JOptionPane.showMessageDialog(null, "Login efetuado com sucesso!\n" + "Acesso: " + perfil
									+ "\nSeja Bem-vindo, " + txtNome + "!");
						} else {
							int id = r.getInt("id_usuario");
							txtNome = (r.getString("nome_usuario"));
							home hm2 = new home(id);
							hm2.setVisible(true);
							hm2.mnGerenciar.setVisible(false);
							hm2.mnCadastrar.setVisible(false);
							setVisible(false);
							JOptionPane.showMessageDialog(null, "Login efetuado com sucesso!\n" + "Acesso: " + perfil
									+ "\nSeja Bem-vindo, " + txtNome + "!");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Login ou senha incorretos!");
					}

				} catch (ClassNotFoundException | SQLException ex) {
					ex.printStackTrace();
				} finally {
					if (r != null) {
						try {
							r.close();
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}
					if (p != null) {
						try {
							p.close();
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}

					if (conn != null) {
						try {
							conn.close();
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		});

		btnNewButton.setBounds(107, 128, 214, 29);
		contentPane.add(btnNewButton);

		senha = new JPasswordField();
		senha.setColumns(10);
		senha.setBounds(107, 88, 214, 29);
		contentPane.add(senha);
	}
}
