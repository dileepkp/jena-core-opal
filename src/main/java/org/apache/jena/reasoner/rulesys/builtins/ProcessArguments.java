/*
 * Copyright 2019 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jena.reasoner.rulesys.builtins;

import java.util.List;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.vocabulary.C;
import org.apache.jena.vocabulary.PA;
import org.apache.jena.vocabulary.RDF;
import org.nfunk.jep.JEP;

/**
 *
 * @author aidb
 */
public class ProcessArguments extends BaseBuiltin {
       /**
     * Return a name for this builtin, normally this will be the name of the 
     * functor that will be used to invoke it.
     */
    @Override
    public String getName() {
        return "processArguments";
    }
    
    /**
     * Return the expected number of arguments for this functor or 0 if the number is flexible.
     */
    @Override
    public int getArgLength() {
        return 2;
    }

    /**
     * This method is invoked when the builtin is called in a rule body.
     * @param args the array of argument values for the builtin, this is an array 
     * of Nodes, some of which may be Node_RuleVariables.
     * @param context an execution context giving access to other relevant data
     * @param length the length of the argument list, may be less than the length of the args array
     * for some rule engines
     * @return return true if the buildin predicate is deemed to have succeeded in
     * the current environment
     */
    @Override
     public void headAction(Node[] args, int length, RuleContext context) {
        try {
            checkArgs(length, context);
            BindingEnvironment env = context.getEnv();
            boolean ok = false;
            
            Node func = getArg(0, args, context);
            //Node argList = getArg(1, args, context);
           // Node varList = getArg(2, args, context);
            Node pp = getArg(1, args, context);
            
          
         //  Node prevIter = getArg(3, args, context);
           // Node prevpp = Util.getJoinSubjValue(PA.Nodes.AfterStatement, prev, PA.Nodes.Iteration, prevIter, context);
           		//(?func c:hasArgumentExpr ?y), (?y c:referTo ?var)
           
             List<Node> listOfArgs = Util.getPropValueList(func, C.Nodes.HasArgumentExpr, context);
            //List<Node> listOfVars = Util.getPropValueList(varList, RDF.item.asNode(), context);
            if(listOfArgs == null)
                return;
            Node unknown = Util.makeURINode(PA.unknown.getURI());
            Node equals = Util.makeURINode(PA.equals.getURI());
          // (?var pa:hasPointerState ?ps),
	//	(?ps pa:atProgramPoint ?pp),
	//	(?ps pa:stateValue ?stateVal),
	//	(?ps pa:stateRelation ?stateRel)
        for(Node arg : listOfArgs){
            Node varOfArg = Util.getPropValue(arg, C.Nodes.ReferTo, context);
            
                Node isGlobalVariable = Util.getJoinPropValue(varOfArg, C.Nodes.HasParent, C.Nodes.HasParent, C.Nodes.Global, context);
            if(context.contains(varOfArg, C.Nodes.HasType, C.Nodes.PointerType) ||
                    isGlobalVariable != null ){
                Node psNode = NodeFactory.createBlankNode();
                 context.add( new Triple( varOfArg, PA.Nodes.HasPointerState, psNode) );
                 context.add( new Triple( psNode, PA.Nodes.AtProgramPoint, pp) );
                 context.add( new Triple( psNode, PA.Nodes.StateValue, unknown) );
                 context.add( new Triple( psNode, PA.Nodes.StateRelation, equals) );
                  
            }
        }
            
 
            return ;
            
        } catch (Exception ex) {
            //Logger.getLogger(IfTrue_BranchBeTraversed.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            return;
        }
    }
}
