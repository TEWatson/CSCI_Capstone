package cass.languageTool.lemma;

public class TEST_Lemmatizer implements I_Lemma{

	@Override
	public String lemmatize(String string) {
		String lemma;
		
		switch (string) {
		case "makes":
			lemma = "make";
			break;
			
		case "musical":
			lemma = "music";
			break;
		
		case "sounds":
			lemma = "sound";

		default:
			lemma = string;
			break;
		}
		
		return lemma;
	}
}