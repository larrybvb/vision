package vision;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

public class GoogleVisionService {

	public String visionService(File fileName) throws IOException {

		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

			// Reads the image file into memory
			Path path = Paths.get(fileName.getPath());
			byte[] data = Files.readAllBytes(path);
			// byte[] data = file.getBytes();
			ByteString imgBytes = ByteString.copyFrom(data);

			// Builds the image annotation request
			List<AnnotateImageRequest> requests = new ArrayList<>();
			Image img = Image.newBuilder().setContent(imgBytes).build();
			// Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
			Feature feat2 = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
			AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat2).setImage(img).build();
			// AnnotateImageRequest request =
			// AnnotateImageRequest.newBuilder().addFeatures(feat).addFeatures(feat2).setImage(img).build();
			requests.add(request);

			// Performs label detection on the image file
			BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();

			AnnotateImageResponse res = responses.get(0);

			String tit = extraerTIT(res.getTextAnnotationsList().get(0).getDescription());
			System.out.println("tit: " + tit);
			String nombreFinal = formatearTIT(tit);
			System.out.println("tit formateado: " + nombreFinal);

			return nombreFinal;
		}

		// return ;
	}

	private String formatearTIT(String tit) {
		String anio = tit.split("-")[0];
		String codigo;
		if (tit.split("-").length > 1) {
			codigo = String.format("%08d", Integer.parseInt(tit.split("-")[1]));
		} else {
			codigo = "error";
		}

		return anio + "-" + codigo;
	}

	private String extraerTIT(String descripcion) {
		// System.out.println("DESCRIPCION: "+ descripcion);
		String texto;
		if (descripcion.split("TIT.").length > 1) {
			texto = descripcion.split("TIT.")[1];
		} else {
			texto = descripcion.split("T. ")[1];
		}
		return texto.substring(1, 20).replaceAll("[A-Za-z*:.#]", "").trim();
	}

}