package de.msg.xt.mdt.base;

public class Predicates {

	public static boolean all(Predicate predicate, IEvalutaionGroup group) {
		for (ControlField field : group.getFields()) {
			if (!predicate.evaluate(field)) {
				return false;
			}
		}
		return true;
	}

	public static boolean atLeastOne(Predicate predicate, IEvalutaionGroup group) {
		for (ControlField field : group.getFields()) {
			if (predicate.evaluate(field)) {
				return true;
			}
		}
		return false;
	}
}
