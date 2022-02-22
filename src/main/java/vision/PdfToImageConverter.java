package vision;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PdfToImageConverter {
	
	public boolean pdfToImagen (File sourceFile, String imagensalida ) {
		try {
			File destinationFile = new File(imagensalida);

			if (!destinationFile.exists()) {
				destinationFile.mkdir();
				System.out.println("ruta creada -> " + destinationFile.getAbsolutePath());
			}

			if (sourceFile.exists()) {
				PDDocument document = PDDocument.load(sourceFile);
				PDFRenderer pdfRenderer = new PDFRenderer(document);

				String fileName = sourceFile.getName().replace(".pdf", "");

				BufferedImage bim = pdfRenderer.renderImage(0);
				String destDir = imagensalida + fileName + ".png";
				ImageIO.write(bim, "png", new File(destDir));

				document.close();

				System.out.println("Imagen guardada en -> " + destinationFile.getAbsolutePath());
				return true;
			} else {
				System.err.println(sourceFile.getName() + " Fichero no existe");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(sourceFile.getName() + "-" +e.getMessage());
			return false;
		}
	}

}
