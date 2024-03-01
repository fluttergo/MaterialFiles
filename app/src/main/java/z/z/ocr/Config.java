package z.z.ocr;

import com.googlecode.tesseract.android.TessBaseAPI;

public class Config {

	public static final int TESS_ENGINE = TessBaseAPI.OEM_LSTM_ONLY;

	public static final String TESS_LANG_ENG = "eng";//english
	public static final String TESS_LANG_CHI_SIM = "chi_sim";//chinese

	public static final String IMAGE_NAME = "sample.jpg";
}
