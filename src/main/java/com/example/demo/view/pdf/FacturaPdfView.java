package com.example.demo.view.pdf;

import com.example.demo.modelos.entidad.Factura;
import com.example.demo.modelos.entidad.ItemFactura;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Map;

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {


    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Factura factura = (Factura) model.get("factura");

        PdfPTable table = new PdfPTable(1);
        table.setSpacingAfter(20);

        PdfPCell cell;
        cell = new PdfPCell(new Phrase("Datos del Cliente"));
        cell.setBackgroundColor(new Color(143, 217, 255));
        cell.setPadding(8f);
        table.addCell(cell);
        table.addCell(factura.getCliente().toString()); //toString retorna el nombre + apellido
        table.addCell(factura.getCliente().getEmail());

        PdfPTable table1 = new PdfPTable(1);
        table1.setSpacingAfter(20);
        cell = new PdfPCell(new Phrase("Datos de la factura"));
        cell.setBackgroundColor(new Color(49, 255, 131));
        cell.setPadding(8f);
        table1.addCell(cell);
        table1.addCell("Folio: " + factura.getId());
        table1.addCell("Descripcion: " + factura.getDescripcion());
        table1.addCell("Fecha: " + factura.getCreateAt());

        PdfPTable table2 = new PdfPTable(4);
        table2.setSpacingAfter(20);
        table2.setWidths(new float[] {2.5f, 1, 1, 1});
        table2.addCell("Producto");
        table2.addCell("Precio");
        table2.addCell("Cantidad");
        table2.addCell("Total");

        for (ItemFactura item: factura.getItems()) {
            table2.addCell(item.getProducto().getNombre());
            table2.addCell(item.getProducto().getPrecio().toString());
            table2.addCell(item.getCantidad().toString());
            table2.addCell(item.calcularImporte().toString());
        }

        cell = new PdfPCell(new Phrase("Total: "));
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        table2.addCell(cell);
        table2.addCell(factura.getTotal().toString());

        document.add(table);
        document.add(table1);
        document.add(table2);
    }
}
