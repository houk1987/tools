package org.hk.tools.htmlParserTools;

import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

public class HtmlParserTools {

	public Element getElement(Source source, String tag) {
		Element element = source.getFirstElement(tag);
		return element;
	}

	public List<Element> getElements(Source source, String tag) {
		List<Element> elements = source.getAllElements(tag);
		return elements;
	}

}
