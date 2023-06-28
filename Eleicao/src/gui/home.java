package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class home extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7557402519542622184L;
	public JPanel contentPane;
	private int userId;
	public JMenu mnCadastrar = new JMenu("CADASTRAR");
	public JMenu mnGerenciar = new JMenu("GERENCIAR");
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					home frame = new home();
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
	public home(int userId) {
		this.userId = userId;

		setTitle("HOME");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(15, 15, 1700, 1000);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JDesktopPane dp = new JDesktopPane();
		dp.setBounds(0, 0, 1684, 883);
		contentPane.add(dp);
		dp.setLayout(null);

		JMenu MenuVotar = new JMenu("VOTAR");
		MenuVotar.setHorizontalTextPosition(SwingConstants.RIGHT);
		MenuVotar.setHorizontalAlignment(SwingConstants.CENTER);
		MenuVotar.setFont(new Font("Segoe UI", Font.BOLD, 18));
		MenuVotar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Votacao vt = new Votacao(userId);
				dp.add(vt);
				vt.setVisible(true);
			}
		});

		
		mnCadastrar.setIcon(new ImageIcon("C:\\Users\\Lucas\\Pictures\\cadastro.png"));
		mnCadastrar.setHorizontalTextPosition(SwingConstants.RIGHT);
		mnCadastrar.setHorizontalAlignment(SwingConstants.CENTER);
		mnCadastrar.setFont(new Font("Segoe UI", Font.BOLD, 18));
		mnCadastrar.setActionCommand("");
		menuBar.add(mnCadastrar);

		JMenuItem cadastroUsuarioMI = new JMenuItem("Usuário");
		cadastroUsuarioMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CadastroUsuario cu = new CadastroUsuario();
				dp.add(cu);
				cu.setVisible(true);
			}
		});
		mnCadastrar.add(cadastroUsuarioMI);

		JMenuItem cadastroPartidoMI = new JMenuItem("Candidato");
		cadastroPartidoMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CadastroCandidato cc = new CadastroCandidato();
				dp.add(cc);
				cc.setVisible(true);
			}
		});

		JMenuItem cadastroEleicaoMI = new JMenuItem("Eleição");
		cadastroEleicaoMI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CadastroEleicao ce = new CadastroEleicao();
				dp.add(ce);
				ce.setVisible(true);
			}
		});
		mnCadastrar.add(cadastroEleicaoMI);
		mnCadastrar.add(cadastroPartidoMI);
		MenuVotar.setIcon(new ImageIcon("C:\\Users\\Lucas\\Pictures\\elections.png"));
		menuBar.add(MenuVotar);

		JMenu MenuResultados = new JMenu("RESULTADOS");
		MenuResultados.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Resultados rsTb = new Resultados();
				dp.add(rsTb);
				rsTb.setVisible(true);
			}
		});
		MenuResultados.setFont(new Font("Segoe UI", Font.BOLD, 18));
		MenuResultados.setIcon(new ImageIcon(
				"C:\\Users\\Lucas\\Pictures\\check_document_file_internet_report_security_success_icon_127056.png"));
		menuBar.add(MenuResultados);

		
		mnGerenciar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

			}
		});
		mnGerenciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		mnGerenciar.setIcon(new ImageIcon("C:\\Users\\Lucas\\Pictures\\admin_person_user_man_2839.png"));
		mnGerenciar.setFont(new Font("Segoe UI", Font.BOLD, 18));
		mnGerenciar.setActionCommand("");
		mnGerenciar.setHorizontalTextPosition(SwingConstants.RIGHT);
		mnGerenciar.setHorizontalAlignment(SwingConstants.CENTER);
		menuBar.add(mnGerenciar);

		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Usuários");
		mntmNewMenuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GerenciarUsuarios gu = new GerenciarUsuarios();
				dp.add(gu);
				gu.setVisible(true);
			}
		});
		mnGerenciar.add(mntmNewMenuItem_4);

		JMenuItem mntmNewMenuItem_5 = new JMenuItem("Candidato");
		mntmNewMenuItem_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GerenciarCandidato gc = new GerenciarCandidato();
				dp.add(gc);
				gc.setVisible(true);
			}
		});
		mnGerenciar.add(mntmNewMenuItem_5);

		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Eleição");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GerenciarEleicao ge = new GerenciarEleicao();
				dp.add(ge);
				ge.setVisible(true);
			}
		});

		JMenuItem mntmNewMenuItem = new JMenuItem("Partido");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GerenciarPartido gp = new GerenciarPartido();
				dp.add(gp);
				gp.setVisible(true);

			}
		});
		mnGerenciar.add(mntmNewMenuItem);
		mnGerenciar.add(mntmNewMenuItem_3);

	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public home() {

	}
}
