package com.example.demo.view.xlsx;

import com.example.demo.modelos.entidad.Factura;
import com.example.demo.modelos.entidad.ItemFactura;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component("factura/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView {


    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Factura factura = (Factura) model.get("factura");

        Sheet sheet = workbook.createSheet();
        sheet.createRow(0).createCell(0).setCellValue("Datos del cliente");
        sheet.createRow(1).createCell(0).setCellValue(factura.getCliente().toString());
        sheet.createRow(2).createCell(0).setCellValue(factura.getCliente().getEmail());
        sheet.createRow(4).createCell(0).setCellValue("Datos de la factura");
        sheet.createRow(5).createCell(0).setCellValue("Folio: " + factura.getId());
        sheet.createRow(6).createCell(0).setCellValue("Descripcion: " + factura.getDescripcion());
        sheet.createRow(7).createCell(0).setCellValue("Fecha: " + factura.getCreateAt());

        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue("Producto");
        header.createCell(1).setCellValue("Precio");
        header.createCell(2).setCellValue("Cantidad");
        header.createCell(3).setCellValue("Total");

        int rownum = 10;
        for (ItemFactura item: factura.getItems()) {
            Row fila = sheet.createRow(rownum++);
            fila.createCell(0).setCellValue(item.getProducto().getNombre());
            fila.createCell(1).setCellValue(item.getProducto().getPrecio());
            fila.createCell(2).setCellValue(item.getCantidad());
            fila.createCell(3).setCellValue(item.calcularImporte());
        }

        Row filaTotal =sheet.createRow(rownum);
        filaTotal.createCell(2).setCellValue("Total: ");
        filaTotal.createCell(3).setCellValue(factura.getTotal());
    }
}
