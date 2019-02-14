package Clases;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class GenerarPDF {

    private Context context;
    private File pdfFile;
    private Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    private Font fTitulo = new Font(Font.FontFamily.HELVETICA, 40, Font.BOLD);
    private Font fSubTitulo = new Font(Font.FontFamily.HELVETICA, 28, Font.BOLD);
    private Font fFecha = new Font(Font.FontFamily.HELVETICA, 20, Font.ITALIC);
    private Font fText = new Font(Font.FontFamily.HELVETICA, 20, Font.NORMAL);


    public GenerarPDF(Context context) {
        this.context = context;
    }

    /**
     * Crea la carpeta contenedora y el archivo PDF en el que se trabajará
     */
    private void createFile(){
        File carpeta = new File(Environment.getExternalStorageDirectory().toString(), "PDF");
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }
        this.pdfFile = new File(carpeta, "Presupuesto Concesionario.pdf");
    }

    /**
     * Abre el documento como escritura para poder modificarlo
     */
    public void openDocument(){
        this.createFile();
        try {
            this.document = new Document();
            this.pdfWriter = PdfWriter.getInstance(this.document, new FileOutputStream(this.pdfFile));
            this.document.addCreationDate();
            this.document.addProducer();
            this.document.setPageSize(PageSize.A4);
            this.document.open();
        }catch (Exception e){
            Log.e("openDocument", e.toString());
        }
    }

    /**
     * Cierra el documento
     */
    public void closeDocument(){
        this.document.close();
        this.pdfWriter.close();
    }

    /**
     * Añade los metadatos del PDF
     *
     * @param titulo :String
     * @param tema :String
     * @param autor :String
     */
    public void addMetaData(String titulo, String tema, String autor){
        this.document.addTitle(titulo);
        this.document.addSubject(tema);
        this.document.addAuthor(autor);
    }

    /**
     * Añade una cabecera al PDF
     *
     * @param titulo :String
     * @param subtitulo :String
     */
    public void addTtitulo(String titulo, String subtitulo){
        try {
            this.paragraph = new Paragraph();
            this.addParagraph(new Paragraph(titulo, fTitulo));
            this.addParagraph(new Paragraph(subtitulo, fSubTitulo));
            this.addParagraph(new Paragraph("Generado: " + new Date().toString(), fFecha));
            this.paragraph.setSpacingAfter(30);
            this.document.add(this.paragraph);
        }catch (Exception e){
            Log.e("addTtitulo", e.toString());
        }
    }

    /**
     *
     * @param paragraphHijo
     */
    private void addParagraph(Paragraph paragraphHijo){
        paragraphHijo.setAlignment(Element.ALIGN_CENTER);
        this.paragraph.add(paragraphHijo);
    }

    /**
     *
     * @param paragraphHijo
     */
    private void addParagraphCliente(Paragraph paragraphHijo){
        paragraphHijo.setAlignment(Element.ALIGN_LEFT);
        this.paragraph.add(paragraphHijo);
    }

    public void addDatosCliente(String nombre, String apellidos, String email, int telefono, String poblacion, String direccion){
        try {
            this.paragraph = new Paragraph();
            this.addParagraphCliente(new Paragraph("Cliente: " ));
            this.addParagraphCliente(new Paragraph("Nombre: " + nombre));
            this.addParagraphCliente(new Paragraph("Apellidos: " + apellidos));
            this.addParagraphCliente(new Paragraph("Email: " + email));
            this.addParagraphCliente(new Paragraph("Telefono: " + telefono));
            this.addParagraphCliente(new Paragraph("Población: " + poblacion));
            this.addParagraphCliente(new Paragraph("Dirección: " + direccion));

            this.paragraph.setSpacingBefore(5);
            this.paragraph.setSpacingAfter(5);
            this.document.add(this.paragraph);
        }catch (Exception e){
            Log.e("addDatosCliente", e.toString());
        }
    }

    /**
     * Muestra la ruta absoluta del archivo PDF
     *
     * @return :String
     */
    public String verPathPDF(){
        return this.pdfFile.getAbsolutePath();
    }
}
