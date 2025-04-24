package org.example.utils;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.FileOutputStream;

public class ExportUtils {

    // Método para exportar a Excel con JFileChooser
    public static void exportarTablaAExcel(JTable tabla) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como Excel");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos Excel (*.xlsx)", "xlsx"));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String nombreArchivo = fileChooser.getSelectedFile().getAbsolutePath();
            if (!nombreArchivo.endsWith(".xlsx")) {
                nombreArchivo += ".xlsx";
            }

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Datos");

                // Escribir encabezados
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < tabla.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(tabla.getColumnName(i));
                }

                // Escribir datos
                for (int i = 0; i < tabla.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < tabla.getColumnCount(); j++) {
                        Cell cell = row.createCell(j);
                        Object value = tabla.getValueAt(i, j);
                        cell.setCellValue(value != null ? value.toString() : "");
                    }
                }

                // Guardar archivo
                try (FileOutputStream fileOut = new FileOutputStream(nombreArchivo)) {
                    workbook.write(fileOut);
                }

                JOptionPane.showMessageDialog(null, "Datos exportados a Excel correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al exportar a Excel: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método para exportar a PDF con JFileChooser
    public static void exportarTablaAPDF(JTable tabla) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como PDF");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos PDF (*.pdf)", "pdf"));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String nombreArchivo = fileChooser.getSelectedFile().getAbsolutePath();
            if (!nombreArchivo.endsWith(".pdf")) {
                nombreArchivo += ".pdf";
            }

            try (FileOutputStream fos = new FileOutputStream(nombreArchivo)) {
                Document document = new Document();
                PdfWriter.getInstance(document, fos);
                document.open();

                // Crear tabla PDF
                PdfPTable pdfTable = new PdfPTable(tabla.getColumnCount());
                pdfTable.setWidthPercentage(100);

                // Agregar encabezados
                for (int i = 0; i < tabla.getColumnCount(); i++) {
                    pdfTable.addCell(new Phrase(tabla.getColumnName(i), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                }

                // Agregar datos
                for (int i = 0; i < tabla.getRowCount(); i++) {
                    for (int j = 0; j < tabla.getColumnCount(); j++) {
                        Object value = tabla.getValueAt(i, j);
                        pdfTable.addCell(new Phrase(value != null ? value.toString() : ""));
                    }
                }

                document.add(pdfTable);
                document.close();

                JOptionPane.showMessageDialog(null, "Datos exportados a PDF correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al exportar a PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}