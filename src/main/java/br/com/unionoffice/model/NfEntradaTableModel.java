package br.com.unionoffice.model;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import br.com.unionoffice.modelo.NfEntrada;

public class NfEntradaTableModel extends AbstractTableModel {
	private List<NfEntrada> lista;
	public static final String[] COLUNAS = {"DATA", "Nº", "TIPO", "EMITENTE", "VALOR","XML" };

	public NfEntradaTableModel(List<NfEntrada> lista) {
		this.lista = lista;
	}

	@Override
	public int getRowCount() {
		return lista.size();
	}

	@Override
	public int getColumnCount() {
		return COLUNAS.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		NfEntrada nota = lista.get(rowIndex);
		switch (columnIndex) {
		case 0:
			SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
			return formatador.format(nota.getData().getTime());
		case 1:
			return nota.getNumero();
		case 2:
			return nota.getTipo();
		case 3:
			return nota.getFornecedor();
		case 4:
			return nota.getValor();
		case 5:
			return nota.xml;
		default:
			return null;
		}
	}

	@Override
	public String getColumnName(int column) {
		return COLUNAS[column];
	}

}
