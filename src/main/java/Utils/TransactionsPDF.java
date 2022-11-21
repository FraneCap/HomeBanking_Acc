package Utils;

import java.awt.*;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.Models.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Component("/transactions/list")
public class TransactionsPDF extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {

        @SuppressWarnings("unchecked")
        List<Transaction> listTransactions = (List<Transaction>) model.get("transactions");

        /*Fuentes, tamaños y colores para cada seccion*/
        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD,20, Color.BLACK);
        Font fuenteTituloColumnas = FontFactory.getFont(FontFactory.HELVETICA_BOLD ,12,Color.BLACK);
        Font fuenteDataCeldas = FontFactory.getFont(FontFactory.COURIER ,10,Color.BLACK);

        document.setPageSize(PageSize.LETTER.rotate());
        document.setMargins(-20, -20, 30, 20);
        document.open();
        PdfPCell celda = null;

        /*Tabla Para El Titulo del PDF*/
        PdfPTable tablaTitulo = new PdfPTable(1);

        celda = new PdfPCell(new Phrase("LISTA DE TRANSACCONES", fuenteTitulo));
        celda.setBorder(0);
        celda.setBackgroundColor(new Color(154, 59, 67));
        celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        celda.setVerticalAlignment(PdfPCell.ALIGN_CENTER);
        celda.setPadding(30);

        tablaTitulo.addCell(celda);
        tablaTitulo.setSpacingAfter(30);

        /*Tabla Para Mostrar Listado de Transactions*/
        PdfPTable tablaTransactions = new PdfPTable(6);
        tablaTransactions.setWidths(new float[] {0.8f, 2f, 2f, 1.5f, 3.5f, 1.5f});

        celda = new PdfPCell(new Phrase("ID", fuenteTituloColumnas));
        celda.setBackgroundColor(Color.lightGray);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(10);
        tablaTransactions.addCell(celda);

        celda = new PdfPCell(new Phrase("TYPE", fuenteTituloColumnas));
        celda.setBackgroundColor(Color.lightGray);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(10);
        tablaTransactions.addCell(celda);

        celda = new PdfPCell(new Phrase("AMOUNT", fuenteTituloColumnas));
        celda.setBackgroundColor(Color.lightGray);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(10);
        tablaTransactions.addCell(celda);

        celda = new PdfPCell(new Phrase("DESCRIPTION", fuenteTituloColumnas));
        celda.setBackgroundColor(Color.lightGray);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(10);
        tablaTransactions.addCell(celda);

        celda = new PdfPCell(new Phrase("DATE", fuenteTituloColumnas));
        celda.setBackgroundColor(Color.lightGray);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(10);
        tablaTransactions.addCell(celda);

        celda = new PdfPCell(new Phrase("ACCOUNT", fuenteTituloColumnas));
        celda.setBackgroundColor(Color.lightGray);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(10);
        tablaTransactions.addCell(celda);


        /*Bucle For, mostrar todos los datos de los transactions*/

        for (Transaction transaction : listTransactions) {
            celda = new PdfPCell(new Phrase(String.valueOf(transaction.getId()), fuenteDataCeldas));
            celda.setPadding(5);
            tablaTransactions.addCell(celda);

            celda = new PdfPCell(new Phrase(String.valueOf(transaction.getType()), fuenteDataCeldas));
            celda.setPadding(5);
            tablaTransactions.addCell(celda);

            celda = new PdfPCell(new Phrase(String.valueOf(transaction.getAmount()), fuenteDataCeldas));
            celda.setPadding(5);
            tablaTransactions.addCell(celda);

            celda = new PdfPCell(new Phrase(transaction.getDescription(), fuenteDataCeldas));
            celda.setPadding(5);
            tablaTransactions.addCell(celda);

            celda = new PdfPCell(new Phrase(String.valueOf(transaction.getCreationDate()), fuenteDataCeldas));
            celda.setPadding(5);
            tablaTransactions.addCell(celda);

            celda = new PdfPCell(new Phrase(String.valueOf(transaction.getAccount()), fuenteDataCeldas));
            celda.setPadding(5);
            tablaTransactions.addCell(celda);

        }

		/*
		listadoClientes.forEach(cliente -> {
			tablaClientes.addCell(cliente.getId().toString());
			tablaClientes.addCell(cliente.getNombres());
			tablaClientes.addCell(cliente.getApellidos());
			tablaClientes.addCell(cliente.getTelefono());
			tablaClientes.addCell(cliente.getEmail());
			tablaClientes.addCell(cliente.getCiudad().getCiudad());
		});
		*/

        /*Anexamos las Tablas al Documento*/
        document.add(tablaTitulo);
        document.add(tablaTransactions);
    }

}
