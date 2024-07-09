package models.modules.report;

import java.io.IOException;
import java.util.Map;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import enums.ReportTimePeriods;
import models.helpers.DateHelper;
import models.helpers.PopupDialog;

public class ReportManager {
    public static void main(String[] args) {
        new ReportManager().createDailyPeriodicSales(ReportTimePeriods.TimePeriod.MONTHLY,
                DateHelper.getCurrentDateString());
    }

    // DateHelper.getCurrentDateTimeString().replace(" ", "_").replace(":", "-")

    private ReportModel model;

    public ReportManager() {
        this.model = new ReportModel();
    }

    public void createDailyPeriodicSales(ReportTimePeriods.TimePeriod timePeriod, String date) {
        try (PdfWriter writer = new PdfWriter(
                "DailyPeriodicSales_" + "replacement"
                        + ".pdf");
                PdfDocument pdfDoc = new PdfDocument(writer);) {

            // Retrieving periodic sales data from database
            Map<String, Object> periodicSalesStats = this.model.getPeriodicReportData(timePeriod,
                    DateHelper.stringToDate(date));

            // Creating new document with new size
            Document document = new Document(pdfDoc, PageSize.LETTER.rotate());
            pdfDoc.addNewPage();

            // Getting Header and Subheaders of PDF
            document.add(getHeader());
            document.add(getSubHeading(timePeriod.getPeriodString() + " Periodic Sales Report"));
            document.add(
                    getSubSubHeading(DateHelper.stringToDate(date).minusDays(timePeriod.getDays()) + " to "
                            + DateHelper.stringToDate(date)));

            document.add(new Paragraph("Food Products").setTextAlignment(TextAlignment.LEFT));
            // Creating Food Products Table
            Table foodProductSalesTable = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
            // // Creating Table Headers
            // Product Name Header
            Cell productNameHeader = new Cell().add(new Paragraph("PRODUCT NAME"));
            productNameHeader.setTextAlignment(TextAlignment.CENTER);
            productNameHeader.setBold();
            foodProductSalesTable.addHeaderCell(productNameHeader);
            // Size
            Cell sizeHeader = new Cell().add(new Paragraph("SIZE"));
            sizeHeader.setTextAlignment(TextAlignment.CENTER);
            sizeHeader.setBold();
            foodProductSalesTable.addHeaderCell(sizeHeader);
            // Quantity Sold Header
            Cell quantitySoldHeader = new Cell().add(new Paragraph("QUANTITY SOLD"));
            quantitySoldHeader.setTextAlignment(TextAlignment.CENTER);
            quantitySoldHeader.setBold();
            foodProductSalesTable.addHeaderCell(quantitySoldHeader);

            // Iterating through food
            Map<Integer, Object> iteratedMap = (Map<Integer, Object>) periodicSalesStats.get("1");
            for (Map.Entry<Integer, Object> mapEntry : iteratedMap.entrySet()) {
                Map<String, Object> foodMap = (Map<String, Object>) mapEntry.getValue();
                Cell myCell = new Cell().add(new Paragraph(String.valueOf(foodMap.get("name"))));
                myCell.setTextAlignment(TextAlignment.CENTER);
                foodProductSalesTable.addCell(myCell);
                myCell = new Cell().add(new Paragraph(String.valueOf(foodMap.get("serving_size"))));
                myCell.setTextAlignment(TextAlignment.CENTER);
                foodProductSalesTable.addCell(myCell);
                myCell = new Cell().add(new Paragraph(String.valueOf(foodMap.get("quantity"))));
                myCell.setTextAlignment(TextAlignment.CENTER);
                foodProductSalesTable.addCell(myCell);
            }
            document.add(foodProductSalesTable);

            document.add(new Paragraph("Drink Products").setTextAlignment(TextAlignment.LEFT).setMarginTop(20));
            // Creating Beverage Products Table
            Table beverageProductSalesTable = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
            // // Creating Table Headers
            // Product Name Header
            beverageProductSalesTable.addHeaderCell(productNameHeader);
            // Size
            beverageProductSalesTable.addHeaderCell(sizeHeader);
            // Quantity Sold Header
            beverageProductSalesTable.addHeaderCell(quantitySoldHeader);

            // Iterating through beverage
            iteratedMap = (Map<Integer, Object>) periodicSalesStats.get("2");
            for (Map.Entry<Integer, Object> mapEntry : iteratedMap.entrySet()) {
                Map<String, Object> beverageMap = (Map<String, Object>) mapEntry.getValue();
                Cell myCell = new Cell().add(new Paragraph(String.valueOf(beverageMap.get("name"))));
                myCell.setTextAlignment(TextAlignment.CENTER);
                beverageProductSalesTable.addCell(myCell);
                myCell = new Cell().add(new Paragraph(String.valueOf(beverageMap.get("serving_size"))));
                myCell.setTextAlignment(TextAlignment.CENTER);
                beverageProductSalesTable.addCell(myCell);
                myCell = new Cell().add(new Paragraph(String.valueOf(beverageMap.get("quantity"))));
                myCell.setTextAlignment(TextAlignment.CENTER);
                beverageProductSalesTable.addCell(myCell);
            }
            document.add(beverageProductSalesTable);

            Table transactionsTable = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
            transactionsTable.addCell(new Cell().add(new Paragraph("Successful Transactions"))
                    .setTextAlignment(TextAlignment.RIGHT));
            transactionsTable.addCell(new Cell().add(new Paragraph(String.valueOf(periodicSalesStats.get("successful")))
                    .setTextAlignment(TextAlignment.CENTER)));
            transactionsTable.addCell(new Cell().add(new Paragraph("Unsuccessful Transactions"))
                    .setTextAlignment(TextAlignment.RIGHT));
            transactionsTable
                    .addCell(new Cell().add(new Paragraph(String.valueOf(periodicSalesStats.get("unsuccessful")))
                            .setTextAlignment(TextAlignment.CENTER)));
            // Empty Cells
            transactionsTable.addCell(new Cell().add(new Paragraph(".")));
            transactionsTable.addCell(new Cell());

            transactionsTable.addCell(new Cell().add(new Paragraph("Income"))
                    .setTextAlignment(TextAlignment.RIGHT));
            transactionsTable
                    .addCell(new Cell().add(new Paragraph(String.valueOf(periodicSalesStats.get("income")))
                            .setTextAlignment(TextAlignment.CENTER)));
            transactionsTable.addCell(new Cell().add(new Paragraph("Expenses"))
                    .setTextAlignment(TextAlignment.RIGHT));
            transactionsTable
                    .addCell(new Cell().add(new Paragraph(String.valueOf(periodicSalesStats.get("expenses")))
                            .setTextAlignment(TextAlignment.CENTER)));
            transactionsTable.addCell(new Cell().add(new Paragraph("Revenue"))
                    .setTextAlignment(TextAlignment.RIGHT));
            transactionsTable
                    .addCell(new Cell().add(new Paragraph(String.valueOf(periodicSalesStats.get("revenue")))
                            .setTextAlignment(TextAlignment.CENTER)));
            document.add(transactionsTable);

            document.close();
        } catch (IOException e) {
            PopupDialog.showErrorDialog(e, getClass().getName());
            e.printStackTrace();
        }
    }

    private Paragraph getHeader() {
        Paragraph heading = new Paragraph("Zav's Kitchen and RestoBar");
        heading.setTextAlignment(TextAlignment.CENTER);
        heading.setFontSize(36);
        heading.setBold();
        heading.setMarginBottom(-20);
        return heading;
    }

    private Paragraph getSubHeading(String subHeadingText) {
        Paragraph subHeading = new Paragraph(subHeadingText);
        subHeading.setTextAlignment(TextAlignment.CENTER);
        subHeading.setFontSize(22);
        subHeading.setBold();
        return subHeading;
    }

    private Paragraph getSubSubHeading(String subSubHeadingText) {
        Paragraph subSubHeading = new Paragraph(subSubHeadingText);
        subSubHeading.setTextAlignment(TextAlignment.CENTER);
        subSubHeading.setFontSize(12);
        subSubHeading.setMarginTop(-10);
        subSubHeading.setMarginBottom(20);
        return subSubHeading;
    }
}
