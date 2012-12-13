package de.msg.xt.mdt.tdsl.jvmmodel

import de.msg.xt.mdt.tdsl.tDsl.OperationMapping
import de.msg.xt.mdt.tdsl.tDsl.Field

class OperationMappingExtension {
	
	def field(OperationMapping mapping) {
		mapping.eContainer as Field
	}
}