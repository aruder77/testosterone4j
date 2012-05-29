package de.msg.xt.mdt.tdsl.scoping

import javax.inject.Inject
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.common.types.JvmFormalParameter
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations
import org.eclipse.xtext.xbase.scoping.XbaseScopeProvider
import org.eclipse.xtext.scoping.impl.SimpleScope
import org.eclipse.xtext.common.types.JvmType
import org.eclipse.xtext.common.types.JvmDeclaredType
class TDslScopeProvider extends XbaseScopeProvider {

//    @Inject extension IJvmModelAssociations associations
//    
//    override IScope createLocalVarScope(EObject context, 
//            EReference reference, IScope parent,
//            boolean includeCurrentBlock, int idx) {
//        if (context instanceof Entity) {
//            val type = (context as Entity).jvmType
//            return new SimpleScope(parent, newImmutableList(
//                EObjectDescription::create(XbaseScopeProvider::THIS, 
//                type)))
//        }
//        if(context instanceof Operation){
//                val descriptions = (context as Operation)
//                    .params.map(e | e.createIEObjectDescription())
//                return MapBasedScope::createScope(
//                        super.createLocalVarScope(context, reference, 
//                            parent, includeCurrentBlock, idx), 
//                        descriptions);    
//        }
//        return super.createLocalVarScope(context, reference, parent, 
//            includeCurrentBlock, idx)
//    }
//    
//    def createIEObjectDescription(JvmFormalParameter jvmFormalParameter)
//    {
//        EObjectDescription::^create(
//            QualifiedName::^create(jvmFormalParameter.name), 
//            jvmFormalParameter, null);
//    }
//
//    def JvmType getJvmType(Entity entity) {
//        entity.jvmElements.filter(typeof(JvmType)).head
//    }
//    
//    override JvmDeclaredType getContextType(EObject call) {
//        if (call == null)
//            return null
//        val containerClass = getContainerOfType(call, typeof(Entity));
//        if (containerClass != null)
//            return getJvmType(containerClass) as JvmDeclaredType
//        else
//            return super.getContextType(call)
//    }
}