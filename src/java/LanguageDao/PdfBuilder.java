package LanguageDao;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;




public class PdfBuilder {

    private Document document;
    private OutputStream outputStream;
    private String filename;
    private PdfPTable table;

    public PdfBuilder() throws Exception {
        this.document = new Document();
        this.filename = System.getProperty("java.io.tmpdir") + UUID.randomUUID().toString() + ".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(this.filename));
        document.open();
//        document.setMargins(0, 0, 0, 0);
        document.newPage();
    }

    public static Element buildTitle(String title, float size) throws Exception {

        Paragraph p = new Paragraph();
        p.setAlignment(Paragraph.ALIGN_CENTER);
        Font font = new Font();
        font.setSize(size);
        font.setStyle(Font.UNDERLINE);
        p.setFont(font);
        p.setSpacingBefore(size);
        p.add(title);
        PdfPTable t = new PdfPTable(1);
       // Image i = Image.getInstance(Sessions.getCurrent().getWebApp().getRealPath("images/Ict.PNG"));
//        Image i = Image.getInstance("");
//        i.setAlignment(Image.ALIGN_CENTER);
//        i.scalePercent(100);
        PdfPCell cell2 = new PdfPCell();
        cell2.setPadding(20F);
        cell2.setBottom(10F);
//        cell2.addElement(i);
//        Paragraph p2 = new Paragraph("\nCulture Learning\n\n");
//        Font font2 = new Font();
//        font2.setSize(9F);
//        font2.setStyle(Font.BOLD);
//        p2.setFont(font2);
//        cell2.addElement(p2);
        Paragraph p3 = new Paragraph("\nAddress:\n KG 82 ST Kimironko, Gasabo,Kigali, Rwanda\nBP: 5975\nTel: +250788302964\nEmail: info@ictforallinall.com");
        Font font3 = new Font();
        font3.setSize(14F);
        font3.setStyle(Font.BOLD);
        p3.setFont(font3);
        p3.setAlignment(Paragraph.ALIGN_CENTER);
        p3.setPaddingTop(0F);
        cell2.addElement(p3);
        cell2.addElement(p);
        cell2.setBorderWidth(0);
        t.addCell(cell2);
//        t.setSpacingAfter(5F);
        t.setWidthPercentage(100);
        return t;
    }

    public static Element buildSubTitle(String title, float size) throws Exception {
        Paragraph p = new Paragraph();
        Font font = new Font();
        font.setSize(size);
        font.setColor(new BaseColor(100, 100, 100));
        p.setFont(font);
        p.setSpacingBefore(size);
        p.setSpacingAfter(size);
        p.add(title);
        PdfPTable t = new PdfPTable(2);
        PdfPCell h1 = new PdfPCell();
        PdfPCell h2 = new PdfPCell();
        h1.setBackgroundColor(new BaseColor(200, 200, 255));
        h2.setBackgroundColor(new BaseColor(200, 200, 255));
        t.addCell(h1);
        t.addCell(h2);
        PdfPCell cell = new PdfPCell();
        cell.setPadding(10F);
        cell.addElement(p);
        cell.setBorderWidth(0);
        t.addCell(cell);
        Paragraph i = new Paragraph(new SimpleDateFormat("dd/mm/yyyy").format(new Date()));
        i.setAlignment(Paragraph.ALIGN_RIGHT);
        i.setFont(font);
        PdfPCell cell2 = new PdfPCell();
        cell2.addElement(i);
        cell2.setBorderWidth(0);
        t.addCell(cell2);
        t.setSpacingAfter(20F);
        t.setWidthPercentage(100);

        return t;
    }

//    public static Paragraph buildTitle(String title, float size) {
//        Paragraph p = new Paragraph();
//        p.setAlignment(Paragraph.ALIGN_CENTER);
//        Font font = new Font();
//        font.setColor(BaseColor.BLUE);
//        font.setSize(size);
//        p.setFont(font);
//        p.setSpacingAfter(10F);
//        p.add(title);
//        return p;
//    }
    public static Element buildTitle(String title) throws Exception {
        return buildTitle(title, 12F);
    }

    public static PdfPTable buildTable(String[][] contents) {
        PdfPTable table = new PdfPTable(contents[0].length);
        table.setWidthPercentage(95);
        int counter = 0;
        for (String[] content : contents) {
            for (String string : content) {
                PdfPCell cell = new PdfPCell();
                cell.setUseAscender(true);
                cell.setUseDescender(true);
                Paragraph p = new Paragraph();
                Font fontParagraph = new Font();
                fontParagraph.setSize(9F);
                fontParagraph.setStyle(Font.ITALIC);
                p.setPaddingTop(0);
                p.setSpacingBefore(0);
                if (counter == 0) {
                    fontParagraph.setStyle(Font.BOLD);
                }
                p.setFont(fontParagraph);
                cell.setBorderWidth(1F);
                cell.setPadding(2F);
                p.add(string);
                cell.setTop(0F);
                cell.addElement(p);
                table.addCell(cell);
            }
            counter++;
        }
        return table;
    }

    public static PdfPCell buildCell(String content, boolean header) {
        PdfPCell cell = new PdfPCell();
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        Paragraph p = new Paragraph();
        Font fontParagraph = new Font();
        fontParagraph.setSize(9F);
        fontParagraph.setStyle(Font.ITALIC);
        p.setPaddingTop(0);
        p.setSpacingBefore(0);
        if (header) {
            fontParagraph.setStyle(Font.BOLD);
        }
        p.setFont(fontParagraph);
        cell.setBorderWidth(1F);
        cell.setPadding(2F);
        p.add(content);
        cell.setTop(0F);
        cell.addElement(p);
        return cell;
    }

    public PdfPTable buildFooter() throws Exception {
        PdfPTable t = new PdfPTable(1);
        Font f = new Font();
        f.setSize(6F);
        f.setStyle(Font.BOLD);
        t.setWidthPercentage(95);
        Paragraph p = new Paragraph();
        p.setAlignment(Paragraph.ALIGN_RIGHT);
        PdfPCell c1 = new PdfPCell();
        c1.setBorderWidth(0);
//        p.setSpacingBefore(50F);
        p.setFont(f);
        SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd k:m:s 'CAT' YYYY");
        p.add("Printed On: " + dateFormat.format(new Date()) + "\nIctForAllInAll");
//        Paragraph p2 = new Paragraph();
//        p2.setAlignment(Paragraph.ALIGN_RIGHT);
//        p2.setSpacingBefore(20F);
//        p2.setFont(f);
//        p2.add("HEC System");

        c1.addElement(p);
//        c1.addElement(p2);
        t.addCell(c1);
        return t;
    }

    public void addTitle(String title) throws Exception {
        this.document.add(buildTitle(title));
    }

    public void addTable(String[][] data) throws Exception {
        this.document.add(buildTable(data));
    }
    
    public void init(int colsNum) throws Exception {
        this.table = new PdfPTable(colsNum);
        this.table.setWidthPercentage(95);
    }
    
    public void addTableHeader(String[] headers) throws Exception {
        for (String header : headers) {
            this.table.addCell(buildCell(header, true));
        }
    }
    
    public void addTableRow(String[] rows) throws Exception {
        for (String row : rows) {
            this.table.addCell(buildCell(row, false));
        }
    }
    
    public void addTableCell(String cell) throws Exception {
        this.table.addCell(buildCell(cell, false));
    }
    
    public void addTableCell(String cell, boolean header) throws Exception {
        this.table.addCell(buildCell(cell, header));
    }

    public void addSubTitle(String text) throws Exception {
        this.document.add(buildSubTitle(text, 10F));
    }

    public InputStream end() throws Exception {
        if(this.table!=null) this.document.add(this.table);
        this.document.add(buildFooter());
        this.document.close();
        return new FileInputStream(filename);
    }
    
    public void run() throws Exception {
        if(this.table!=null) this.document.add(this.table);
        this.document.add(buildFooter());
        this.document.close();
        Process process = Runtime.getRuntime().exec("\"C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe\" \"" + this.filename + "\"");
    }
    
    public void generateCertificate(String names, String course, String marks, String date, String email) throws Exception {
        String htmlData = "<div>"
                + "<div style='margin: auto; max-width: 500px;background: #6747C7;padding: 40px; border-radius: 20px;'>"
                + "<h1 style='color: #FFFFFF;text-align: center;'>Icyemezo cyo kurangiza</h1>"
                + "<p style='color: #FFFFFF; font-size: 16px;'>Nshuti "+names+"<br/><br/>Ibi nugutangaza ko urangije ibazwa wakoraga kurubuga rwacu, Kandi wakiriye icyemezo kirangiye! Ushobora kugisanga kumugereka<br/><br/>Murakoze!</p>"
                + "</div>"
                + "</div>";
        String fileName;
        Document document = new Document();
        fileName = System.getProperty("java.io.tmpdir")+UUID.randomUUID().toString()+".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(fileName));
        document.open();
        document.setPageSize(new Rectangle(650, 481));
        document.setMargins(0, 0, 0, 0);
        document.newPage();
        //Image background = Image.getInstance(Sessions.getCurrent().getWebApp().getRealPath("images/background.jpg"));
         Image background = Image.getInstance("");
        background.setAbsolutePosition(0, 0);
        document.add(background);
        Paragraph p1 = new Paragraph();
        Font font1 = new Font();
        font1.setSize(25);
        font1.setColor(new BaseColor(193, 150, 67));
        font1.setStyle(Font.BOLD);
        p1.setFont(font1);
        p1.setAlignment(Paragraph.ALIGN_CENTER);
        p1.add("Cyo Kurangiza");
        p1.setSpacingBefore(140);
        
        Paragraph p2 = new Paragraph();
        Font font2 = new Font();
        font2.setSize(14);
        font2.setColor(new BaseColor(193, 150, 67));
        p2.setFont(font2);
        p2.setAlignment(Paragraph.ALIGN_CENTER);
        p2.add("Ibi ni ukwemeza ko ");
        p2.setSpacingBefore(40);
        
        Paragraph p3 = new Paragraph();
        Font font3 = new Font();
        font3.setSize(25);
        font3.setColor(new BaseColor(193, 150, 67));
        font3.setStyle(Font.BOLD);
        p3.setFont(font3);
        p3.setAlignment(Paragraph.ALIGN_CENTER);
        p3.add(names);
        p3.setSpacingBefore(20);
        
        Paragraph p4 = new Paragraph();
        Font font4 = new Font();
        font4.setSize(14);
        font4.setColor(new BaseColor(193, 150, 67));
        p4.setFont(font4);
        p4.setAlignment(Paragraph.ALIGN_CENTER);
        p4.add("yarangije isomo ry' "+course+" afite amanota"+marks+"\n\n"+date);
        p4.setSpacingBefore(20);
        
        document.add(p1);
        document.add(p2);
        document.add(p3);
        document.add(p4);
        document.close();
        (new NotificationDao()).sendEmail(email, "Icyemezo cyo kurangiza", htmlData, fileName);
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public PdfPTable getTable() {
        return table;
    }

    public void setTable(PdfPTable table) {
        this.table = table;
    }
    
    void copy(InputStream source, OutputStream target) throws IOException {
        byte[] buf = new byte[8192];
        int length;
        while ((length = source.read(buf)) > 0) {
            target.write(buf, 0, length);
        }
    }
    public void download(String filename) throws IOException {
        
        try{
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.responseReset();
            ec.setResponseContentType("application/data");
            ec.setResponseHeader("Content-Disposition", "attachment;filename="+filename );
           InputStream input=end();
            
            OutputStream output=ec.getResponseOutputStream();
            copy(input, output);
              fc.responseComplete();

        }catch(Exception e){
            e.printStackTrace();
            e.getMessage();
        }
    
   
       
    }
    
}

