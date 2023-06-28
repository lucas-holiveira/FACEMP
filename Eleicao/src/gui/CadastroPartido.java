package gui;

import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.Font;

public class CadastroPartido extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6602846808624855062L;
	private JPanel contentPane;
	private JTextField nomePartido;
	private JTextField nmrPartido;

	/**
	 * 
	 * Launch the application.
	 */

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CadastroPartido frame = new CadastroPartido();
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
	public CadastroPartido() {
		setBounds(new Rectangle(50, 10, 0, 0));
		setTitle("CADASTRO DO PARTIDO");
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		setBounds(250, 200, 389, 209);
		contentPane = new JPanel();
		contentPane.setAlignmentY(0.2f);
		contentPane.setAlignmentX(0.2f);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Nome do partido:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 11, 104, 14);
		contentPane.add(lblNewLabel);

		nomePartido = new JTextField();
		nomePartido.setBounds(10, 29, 355, 25);
		contentPane.add(nomePartido);
		nomePartido.setColumns(10);

		JLabel lblNmeroDoPartido = new JLabel("Número:");
		lblNmeroDoPartido.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNmeroDoPartido.setBounds(10, 57, 132, 14);
		contentPane.add(lblNmeroDoPartido);

		nmrPartido = new JTextField();
		nmrPartido.setColumns(10);
		nmrPartido.setBounds(10, 73, 42, 25);
		contentPane.add(nmrPartido);

		JButton cdPartido = new JButton("CADASTRAR");
		cdPartido.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					Class.forName("org.sqlite.JDBC");

					Connection con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

					String sql = "INSERT into partido (nome_partido, num_partido)" + "VALUES (?, ?)";

					PreparedStatement ps = con.prepareStatement(sql);

					ps.setString(1, nomePartido.getText());
					ps.setString(2, nmrPartido.getText());
					ps.executeUpdate();

					setVisible(false);

				} catch (SQLException | ClassNotFoundException ex) {
					ex.printStackTrace();

				}

			}
		});
		cdPartido.setBounds(20, 102, 345, 30);
		contentPane.add(cdPartido);
	}

}
