package org.hk.tools.htmlParserTools;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

public class HtmlParserTools {
	
	
	
	public Element getValue(Source source ,String tag){
		Element element = source.getFirstElement(tag);
		return element;
	}
}
