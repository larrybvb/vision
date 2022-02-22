package vision;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Principal {

	public static String PROPERTIES = "E:/vision/properties/config.properties";
	public static String PDFENTRADA = "PDFENTRADA";
	public static String IMAGENSALIDA = "IMAGENSALIDA";
	public static String PDFSALIDA = "PDFSALIDA";
	public static String PDFSALIDAERROR = "PDFSALIDAERROR";

	public static void main(String[] args) {
		System.out.println("==============CARGA PROPERTIES=======================");
		Properties p = new Properties();
		try {
			p.load(new FileReader(PROPERTIES));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		System.out.println("===============OBTENER IMAGENES======================");
		obtenerImagenes(p);
		System.out.println("===============OBTENER TEXTO======================");
		obtenerTexto(p);
		System.out.println("===============FIN======================");

	}
	
	public static void obtenerImagenes(Properties p) {
		PdfToImageConverter img = new PdfToImageConverter();
		File carpeta = new File(p.getProperty(PDFENTRADA));
		for (final File ficheroEntrada : carpeta.listFiles()) {
			if (ficheroEntrada.isFile()) {
				System.out.println(ficheroEntrada.getName());
				img.pdfToImagen(ficheroEntrada, p.getProperty(IMAGENSALIDA));
			}
		}
	}
	
	public static void obtenerTexto(Properties p)  {
		GoogleVisionService visionService =  new GoogleVisionService();
		File carpeta = new File(p.getProperty(IMAGENSALIDA));
		String nombrefinal ;
		for (final File ficheroEntrada : carpeta.listFiles()) {
			if (ficheroEntrada.isFile()) {
				System.out.println(ficheroEntrada.getPath());
				try {
					nombrefinal = visionService.visionService(ficheroEntrada);
					
					renombrarArchivo(ficheroEntrada.getName(), nombrefinal, p);
					
					/*File oldfile = new File(p.getProperty(PDFENTRADA)+ficheroEntrada.getName().replace("png", "pdf"));
			        File newfile = new File(p.getProperty(PDFSALIDA)+nombrefinal +".pdf");
			        if (oldfile.renameTo(newfile)) {
			            System.out.println("archivo renombrado");
			        }*/
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void renombrarArchivo (String ficheroEntrada, String nombrefinal, Properties p) {
		File oldfile = new File(p.getProperty(PDFENTRADA)+ficheroEntrada.replace("png", "pdf"));
		File newfile;
		if(nombrefinal.contains("error")) {
			newfile = new File(p.getProperty(PDFSALIDAERROR)+nombrefinal +".pdf");
		}else {
			newfile = new File(p.getProperty(PDFSALIDA)+nombrefinal +".pdf");
		}
		
        if (oldfile.renameTo(newfile)) {
            System.out.println("archivo renombrado: "+newfile.getName());
        }
		
	}

}
