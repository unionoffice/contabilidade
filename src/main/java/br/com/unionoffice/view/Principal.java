package br.com.unionoffice.view;

import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import br.com.unionoffice.model.NfEntradaTableModel;
import br.com.unionoffice.modelo.NfEntrada;
import br.com.unionoffice.util.GeraExcel;
import br.com.unionoffice.util.NfUtil;

public class Principal extends JFrame {
	JLabel lbPasta;
	JTextField tfPasta;
	JButton btProcPasta;
	JFileChooser fcPasta;
	JProgressBar pbProgresso;
	JTable tbNotas;
	JScrollPane pnNotas;
	int posX = 10;
	int posY = 20;
	List<NfEntrada> notas;
	String pasta;

	public Principal() {
		inicializarComponentes();
		definirEventos();
	}

	private void inicializarComponentes() {

		lbPasta = new JLabel("Pasta:");
		lbPasta.setBounds(posX, posY, 50, 30);

		tfPasta = new JTextField();
		tfPasta.setBounds(posX += lbPasta.getWidth(), posY, 400, 30);
		tfPasta.setEditable(false);

		btProcPasta = new JButton("Procurar");
		btProcPasta.setBounds(posX += tfPasta.getWidth() + 10, posY, 90, 30);

		fcPasta = new JFileChooser();
		// fcPasta.setCurrentDirectory(new File("P:\\NFE\\Protoloco QUALI -
		// CONTABILIDADE 2016"));
		fcPasta.setCurrentDirectory(new File("C:\\Users\\Roberto\\Desktop"));
		fcPasta.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fcPasta.setDialogTitle("Escolha a pasta dos XMLs");
		fcPasta.setApproveButtonText("Selecionar");

		posX = 10;
		posY += 40;
		pbProgresso = new JProgressBar();
		pbProgresso.setBounds(posX, posY, 550, 30);
		pbProgresso.setStringPainted(true);

		posX = 10;
		posY += 40;
		tbNotas = new JTable();
		tbNotas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pnNotas = new JScrollPane(tbNotas);
		pnNotas.setBounds(posX, posY, 550, 700);

		setLayout(null);
		add(lbPasta);
		add(tfPasta);
		add(btProcPasta);
		add(pbProgresso);
		add(pnNotas);

		pbProgresso.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (pbProgresso.getValue() == pbProgresso.getMaximum()) {
					new Thread(){
						@Override
						public void run() {
							while(notas == null){								
								continue;
							}
							tbNotas.setModel(new NfEntradaTableModel(notas));								
						}
					}.start();									
				}

			}
		});

		setTitle("Relatório Contabilidade");
		setSize(600, 900);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void definirEventos() {

		btProcPasta.addActionListener(event -> {
			int returnValue = fcPasta.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File diretorio = fcPasta.getSelectedFile();
				tfPasta.setText(diretorio.getAbsolutePath());
				File file = new File(diretorio.getAbsolutePath()+"/PROTOCOLO_ENVIO_QUALI.xls");
				file.delete();
				File arquivos[] = diretorio.listFiles();				
				pasta = diretorio.getAbsolutePath();				
				Thread thread = new Thread() {
					@Override
					public void run() {
						try {							
							notas = NfUtil.lerNotas(arquivos, pbProgresso,pasta);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				};
				thread.start();				
			}

		});
	}

}
