package br.com.unionoffice.util;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFRegionUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;

import br.com.unionoffice.modelo.NfEntrada;

public class GeraExcel {
	public static void expExcel(List<NfEntrada> notas, String diretorio) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheetEntrada = workbook.createSheet("Entrada");
		sheetEntrada.setDefaultRowHeight((short) 400);
		FileOutputStream fos = null;
		String nomeArquivo = diretorio + "/PROTOCOLO_ENVIO_QUALI.xls";

		// FONTE
		HSSFFont fonte = workbook.createFont();
		fonte.setFontHeightInPoints((short) 11);
		fonte.setFontName("Arial");
		fonte.setBold(true);

		// ESTILO
		CellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(fonte);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);

		// FONTE NOTA
		HSSFFont fonteNota = workbook.createFont();
		fonte.setFontHeightInPoints((short) 11);
		fonte.setFontName("Arial");

		// ESTILO NOTA
		CellStyle styleNota = workbook.createCellStyle();
		styleNota.setAlignment(CellStyle.ALIGN_CENTER);
		styleNota.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		styleNota.setFont(fonteNota);
		styleNota.setBorderBottom(CellStyle.BORDER_THIN);
		styleNota.setBorderTop(CellStyle.BORDER_THIN);
		styleNota.setBorderRight(CellStyle.BORDER_THIN);
		styleNota.setBorderLeft(CellStyle.BORDER_THIN);

		int posLinha = 0;
		// TITULO
		Cell celula = sheetEntrada.createRow(posLinha++).createCell(0);
		celula.setCellValue("UNION NEGÓCIOS MOBILIÁRIOS LTDA CNPJ: 10.604.875/0001-56");
		CellRangeAddress titulo = new CellRangeAddress(0, 0, 0, 6);
		sheetEntrada.addMergedRegion(titulo);
		celula.setCellStyle(style);
		// Sets the borders to the merged cell
		HSSFRegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, titulo, sheetEntrada, workbook);
		HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, titulo, sheetEntrada, workbook);
		HSSFRegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, titulo, sheetEntrada, workbook);
		HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, titulo, sheetEntrada, workbook);

		posLinha++;

		// SUBTITULO
		Cell celulaSub = sheetEntrada.createRow(posLinha++).createCell(0);
		celulaSub.setCellValue("PROTOCOLO DE ENVIO DE DOCUMENTOS (NF ENTRADA): "
				+ new SimpleDateFormat("MMMM/yyyy").format(notas.get(0).getData().getTime()));
		CellRangeAddress subTitulo = new CellRangeAddress(2, 2, 0, 6);
		sheetEntrada.addMergedRegion(subTitulo);
		celulaSub.setCellStyle(styleNota);
		// Sets the borders to the merged cell
		HSSFRegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, subTitulo, sheetEntrada, workbook);
		HSSFRegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, subTitulo, sheetEntrada, workbook);
		HSSFRegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, subTitulo, sheetEntrada, workbook);
		HSSFRegionUtil.setBorderBottom(CellStyle.BORDER_THIN, subTitulo, sheetEntrada, workbook);

		posLinha++;

		// CABEÇALHO DAS CÉLULAS
		HSSFRow linha = sheetEntrada.createRow(posLinha++);
		linha.createCell(0).setCellValue("DATA DOC.");
		linha.createCell(1).setCellValue("Nº DOC.");
		linha.createCell(2).setCellValue("TIPO DOC.");
		linha.createCell(3).setCellValue("TIPO EM.");
		linha.createCell(4).setCellValue("EMITENTE");
		linha.createCell(5).setCellValue("VALOR");
		linha.createCell(6).setCellValue("XML");

		for (int i = 0; i < 7; i++) {
			linha.getCell(i).setCellStyle(style);
			sheetEntrada.autoSizeColumn(i);
		}
		CellStyle estiloNumber = workbook.createCellStyle();
		int ordem = 1;
		for (NfEntrada nota : notas) {
			HSSFRow linhaNota = sheetEntrada.createRow(posLinha++);
			linhaNota.setHeight((short) 400);
			linhaNota.createCell(0).setCellValue(new SimpleDateFormat("dd/MM/yyyy").format(nota.getData().getTime()));
			linhaNota.getCell(0).setCellStyle(styleNota);
			linhaNota.createCell(1).setCellValue(nota.getNumero());
			linhaNota.getCell(1).setCellStyle(styleNota);
			linhaNota.createCell(2).setCellValue(nota.getTipo().toString());
			linhaNota.getCell(2).setCellStyle(styleNota);
			linhaNota.createCell(3).setCellValue(nota.getTipoDest().toString());
			linhaNota.getCell(3).setCellStyle(styleNota);
			linhaNota.createCell(4).setCellValue(nota.getFornecedor());
			linhaNota.getCell(4).setCellStyle(styleNota);
			linhaNota.createCell(5).setCellValue(nota.getValor().doubleValue());
			estiloNumber.cloneStyleFrom(styleNota);
			linhaNota.getCell(5).setCellStyle(estiloNumber);
			linhaNota.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
			linhaNota.createCell(6).setCellValue(ordem++);
			linhaNota.getCell(6).setCellStyle(styleNota);
		}

		for (int i = 0; i < 7; i++) {
			sheetEntrada.autoSizeColumn(i);
		}

		HSSFRow linhaTotal = sheetEntrada.createRow(++posLinha);
		linhaTotal.createCell(4).setCellValue("Total:");
		linhaTotal.getCell(4).setCellStyle(style);
		linhaTotal.createCell(5).setCellFormula("SUM(F5:F" + (posLinha - 1) + ")");
		linhaTotal.getCell(5).setCellStyle(estiloNumber);

		try {
			File planilha;
			fos = new FileOutputStream(planilha = new File(nomeArquivo));
			workbook.write(fos);
			fos.flush();
			fos.close();
			Desktop.getDesktop().open(planilha);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}
