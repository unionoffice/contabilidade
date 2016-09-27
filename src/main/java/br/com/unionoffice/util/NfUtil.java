package br.com.unionoffice.util;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.swing.JProgressBar;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.com.unionoffice.modelo.NfEntrada;
import br.com.unionoffice.modelo.TipoNfe;

public class NfUtil {
	public static List<NfEntrada> lerNotas(File[] arquivos, JProgressBar progress, String pasta) throws Exception {
		SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd");
		boolean cte;
		List<NfEntrada> lista = new ArrayList<>();
		File pastaDanfe = null;

		int total = arquivos.length * 2;
		progress.setMaximum(total);
		int barra = 0;
		int i = 0;

		for (File arquivo : arquivos) {
			if (arquivo.isDirectory()) {
				pastaDanfe = arquivo;
				i++;
				barra++;
				progress.setValue(barra);
				continue;
			}
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			Document document = factory.newDocumentBuilder().parse(arquivo);
			cte = document.getDocumentElement().getElementsByTagName("CTe").item(0) != null ? true : false;
			Node numero = !cte ? document.getDocumentElement().getElementsByTagName("nNF").item(0)
					: document.getDocumentElement().getElementsByTagName("nCT").item(0);
			Node data = document.getDocumentElement().getElementsByTagName("dhEmi").item(0);
			String dataStr = data.getTextContent().substring(0, data.getTextContent().indexOf("T"));
			Calendar dataEmissao = Calendar.getInstance();
			dataEmissao.setTime(formatador.parse(dataStr));
			NodeList emitente = document.getDocumentElement().getElementsByTagName("emit");
			Element nomeEmitente = (Element) emitente.item(0);
			Node nome = nomeEmitente.getElementsByTagName("xNome").item(0);
			Node valor = !cte ? document.getDocumentElement().getElementsByTagName("vNF").item(0)
					: document.getDocumentElement().getElementsByTagName("vTPrest").item(0);
			Node chave = !cte ? document.getDocumentElement().getElementsByTagName("chNFe").item(0)
					: document.getDocumentElement().getElementsByTagName("chCTe").item(0);

			BigDecimal bigValor = new BigDecimal(valor.getTextContent());
			// ****
			NfEntrada nota = new NfEntrada();
			if (cte)
				nota.setTipo(TipoNfe.CTE);
			nota.setNumero(Long.parseLong(numero.getTextContent()));
			nota.setData(dataEmissao);
			nota.setFornecedor(nome.getTextContent());
			nota.setValor(bigValor);
			nota.setChave(chave.getTextContent());
			nota.ordem = i;
			lista.add(nota);
			Thread.sleep(500);
			i++;
			barra++;
			progress.setValue(barra);
		}
		Collections.sort(lista);
		i = 0;
		List<File> danfes = Arrays.asList(pastaDanfe.listFiles());

		for (NfEntrada nota : lista) {
			File arquivo = arquivos[nota.ordem];
			System.out.println(arquivo);
			arquivo.renameTo(new File(pasta + "/" + String.format("%02d", i + 1) + " - "
					+ nota.getFornecedor().replace("/", "").replace(" ", "_") + " - " + nota.getNumero() + ".xml"));

			try {
				File danfe = (File) danfes.stream().filter(f -> f.getName().startsWith(nota.getChave())).toArray()[0];
				danfe.renameTo(new File(danfe.getParent() + "/" + String.format("%02d", i + 1) + " - "
						+ nota.getFornecedor().replace("/", "").replace(" ", "_") + " - " + nota.getNumero() + " - R$"
						+ nota.getValor().doubleValue() + ".pdf"));
			} catch (Exception e) {

			}
			i++;
			nota.xml = i;
			barra++;
			progress.setValue(barra);
		}
		GeraExcel.expExcel(lista, pasta);
		barra++;
		progress.setValue(barra);
		return lista;
	}
}
