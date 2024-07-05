package models.modules.report;

import java.io.IOException;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import models.helpers.DateHelper;

public class ReportManager {
    public static void main(String[] args) {
        try (PdfWriter writer = new PdfWriter("test.pdf"); PdfDocument pdfDoc = new PdfDocument(writer);) {
            createDailyPeriodicSales(DateHelper.getCurrentDateString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // DateHelper.getCurrentDateTimeString().replace(" ", "_").replace(":", "-")

    public static void createDailyPeriodicSales(String date) {
        try (PdfWriter writer = new PdfWriter(
                "DailyPeriodicSales_" + "replacement"
                        + ".pdf");
                PdfDocument pdfDoc = new PdfDocument(writer);) {
            Document document = new Document(pdfDoc, PageSize.LETTER.rotate());
            pdfDoc.addNewPage();

            document.add(getHeader());

            document.add(new Paragraph("I did my way...."));

            document.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static Paragraph getHeader() {
        Paragraph heading = new Paragraph("Zav's Kitchen and RestoBar");
        heading.setTextAlignment(TextAlignment.CENTER);
        heading.setFontSize(30);
        heading.setBold();
        return heading;
    }
}
