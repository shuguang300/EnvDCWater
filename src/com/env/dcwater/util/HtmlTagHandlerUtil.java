package com.env.dcwater.util;

import org.xml.sax.XMLReader;

import android.text.Editable;
import android.text.Html.TagHandler;

public class HtmlTagHandlerUtil implements TagHandler {

	@Override
	public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
		int contentIndex = 0;
		int count = 0;
		if (tag.equalsIgnoreCase("li")) {
			if (opening) {
				contentIndex = output.length();
				count++;
			} else {
				int length = output.length();
				String content = output.subSequence(contentIndex, length).toString();
				String spanStr = count + "." + content + "\n";
				output.replace(contentIndex, length, spanStr);
			}
		} else if (tag.equalsIgnoreCase("ol") && !opening) {
			output.replace(output.length() - 1, output.length(), "");
		}

	}

}
