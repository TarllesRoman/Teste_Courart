package com.courart.backend.api.utils;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PdfUtils {

	public static void mkTabelaHead(PdfPTable table, String... headers) {
		Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		
		PdfPCell hcell;
		for(String h : headers) {
	        hcell = new PdfPCell(new Phrase(h, headFont));
	        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(hcell);
		}
	}
	
	public static void addTabelaCell(PdfPTable table, String... values) {
		PdfPCell cell;
		for(String v : values) {
			cell = new PdfPCell(new Phrase(v));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
		}
	}
	
}
