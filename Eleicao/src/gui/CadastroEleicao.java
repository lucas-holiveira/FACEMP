package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class CadastroEleicao extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5855250687943659970L;
	private JTextField nomeEleicao;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CadastroEleicao frame = new CadastroEleicao();
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
	public CadastroEleicao() {
		setTitle("CADASTRO DE ELEIÇÃO");
		setResizable(true);
		setClosable(true);
		setBounds(50, 10, 586, 194);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Nome da eleição:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 11, 133, 15);
		getContentPane().add(lblNewLabel);
		
		JComboBox <String>statusEleicao = new JComboBox<>();
		
		JLabel lblDataInicio = new JLabel("Status:");
		lblDataInicio.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDataInicio.setBounds(10, 59, 85, 15);
		getContentPane().add(lblDataInicio);
		
		nomeEleicao = new JTextField();
		nomeEleicao.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
		nomeEleicao.setBounds(10, 30, 542, 26);
		getContentPane().add(nomeEleicao);
		nomeEleicao.setColumns(10);
		
		JButton btnNewButton = new JButton("CADASTRAR");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					Class.forName("org.sqlite.JDBC");

					Connection con = DriverManager.getConnection("jdbc:sqlite:C://Users/Lucas/Desktop/votacao.db");

					
						String sql = "Insert into eleicao (titulo_eleicao, status)"
								+ "values (?, ?)";
						
						PreparedStatement ps = con.prepareStatement(sql);

						ps.setString(1, nomeEleicao.getText());
						ps.setString(2, statusEleicao.getSelectedItem().toString());
				
						ps.executeUpdate();
					
			
					

					nomeEleicao.setText("");
					statusEleicao.setSelectedItem(null);
					
				} catch (SQLException | ClassNotFoundException ex) {
					ex.printStackTrace();
			
			
			}
			}		
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnNewButton.setBounds(10, 115, 542, 35);
		getContentPane().add(btnNewButton);
		
		
		statusEleicao.setModel(new DefaultComboBoxModel(new String[] {"ABERTO", "FECHADO"}));
		statusEleicao.setBounds(10, 78, 296, 26);
		getContentPane().add(statusEleicao);

	}
}
