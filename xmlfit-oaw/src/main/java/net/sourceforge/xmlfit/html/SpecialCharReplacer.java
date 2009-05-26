package net.sourceforge.xmlfit.html;

public class SpecialCharReplacer {

	public static String createHtmlText(String text) {
		String htmltext = null;
		if (text != null) {
		    htmltext = text.replaceAll("ü", "&uuml;");
			htmltext = htmltext.replaceAll("Ü", "&Uuml;");
			htmltext = htmltext.replaceAll("ö", "&ouml;");
			htmltext = htmltext.replaceAll("Ö", "&Ouml;");
			htmltext = htmltext.replaceAll("ä", "&auml;");
			htmltext = htmltext.replaceAll("Ä", "&Auml;");
		}
		return htmltext;
	}

}
