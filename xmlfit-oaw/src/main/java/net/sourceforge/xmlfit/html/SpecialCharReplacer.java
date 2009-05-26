package net.sourceforge.xmlfit.html;

public class SpecialCharReplacer {

	public static String createHtmlText(String text) {
		String htmltext = null;
		if (text != null) {
		    htmltext = text.replaceAll("�", "&uuml;");
			htmltext = htmltext.replaceAll("�", "&Uuml;");
			htmltext = htmltext.replaceAll("�", "&ouml;");
			htmltext = htmltext.replaceAll("�", "&Ouml;");
			htmltext = htmltext.replaceAll("�", "&auml;");
			htmltext = htmltext.replaceAll("�", "&Auml;");
		}
		return htmltext;
	}

}
