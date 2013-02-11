package de.msg.xt.mdt.tdsl.jvmmodel

class UtilExtensions {
	
	
	// String
	
	def toFieldName(String string) { 
		string.replace('.', '_').replace('@', '_')
	}


	
}