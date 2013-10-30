package de.msg.xt.mdt.tdsl.converter

import org.eclipse.xtext.xbase.conversion.XbaseValueConverterService

class TDslValueConverterService extends XbaseValueConverterService {
	
	override String toString(Object value, String lexerRule) {
		if (lexerRule.equals("ID")) {
			val index = value.toString.lastIndexOf(".")
			if (index > 0)
				return value.toString.substring(index + 1)
		} else if (lexerRule.equals("OFQN")) {
			val index = value.toString.lastIndexOf(".")
			if (index > 0) {
				val index2 = value.toString.substring(0, index).lastIndexOf(".")
				if (index2 > 0) {
					
					return "#" + value.toString.substring(index2 + 1, index) + "_#_" + value.toString.substring(index + 1)
				}
			}
		}
		return super.toString(value, lexerRule)
	}
	
}