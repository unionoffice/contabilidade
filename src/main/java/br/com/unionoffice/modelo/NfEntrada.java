package br.com.unionoffice.modelo;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Comparator;

public class NfEntrada implements Comparable<NfEntrada> {
	private Long numero;
	private Calendar data;
	private TipoNfe tipo;
	private TipoDest tipoDest;
	private String fornecedor;
	private BigDecimal valor;
	private String chave;
	public int ordem;
	public int xml;

	public NfEntrada() {
		tipo = TipoNfe.NFE;
		tipoDest = TipoDest.FORN;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public TipoNfe getTipo() {
		return tipo;
	}

	public void setTipo(TipoNfe tipo) {
		this.tipo = tipo;
	}

	public String getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public TipoDest getTipoDest() {
		return tipoDest;
	}

	public void setTipoDest(TipoDest tipoDest) {
		this.tipoDest = tipoDest;
	}

	@Override
	public int compareTo(NfEntrada o) {
		if (this.getData().before(o.getData())) {
			return -1;
		}
		if (this.getData().after(o.getData())) {
			return 1;
		}
		return this.getFornecedor().compareTo(o.getFornecedor());
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

}
