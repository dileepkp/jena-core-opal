/*
 * Copyright 2018 The Apache Software Foundation.
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

import java.util.Iterator;
import java.util.List;
import org.apache.jena.graph.* ;
import org.apache.jena.reasoner.rulesys.* ;
import org.apache.jena.vocabulary.PA;
import org.apache.jena.vocabulary.RDF ;

public class StateEqual extends BaseBuiltin {

    /**
     * Return a name for this builtin, normally this will be the name of the 
     * functor that will be used to invoke it.
     */
    @Override
    public String getName() {
        return "stateEqual";
    }
    
    /**
     * Return the expected number of arguments for this functor or 0 if the number is flexible.
     */
    @Override
    public int getArgLength() {
        return 3;
    }

    /**
     * This method is invoked when the builtin is called in a rule body.
     * @param args the array of argument values for the builtin, this is an array 
     * of Nodes, some of which may be Node_RuleVariables.
     * @param length the length of the argument list, may be less than the length of the args array
     * for some rule engines
     * @param context an execution context giving access to other relevant data
     * @return return true if the builtin predicate is deemed to have succeeded in
     * the current environment
     */
    @Override
    public boolean bodyCall(Node[] args, int length, RuleContext context) {
        checkArgs(length, context);
        Node n0 = getArg(0, args, context);
        Node n1 = getArg(1, args, context);
        Node n2 = getArg(2, args, context);
        //Node n3 = getArg(3, args, context);
        
        return stateEqual(n0, n1, n2, context);
       //return false;
    }

    /**
     * Return true if the states of the variables are same in 
     * both the program points
     */
    protected static boolean stateEqual(Node varList, Node pp1, Node pp2, RuleContext context) {
       for (Iterator<Triple> ni = context.find(varList, RDF.Nodes.item, null); ni.hasNext(); ) {
            Node var = ni.next().getObject();
            Node pointerState = Util.getJoinPropValue(var, PA.Nodes.HasState, PA.Nodes.AtProgramPoint ,pp1,  context);
            Node pointerState2 = Util.getJoinPropValue(var, PA.Nodes.HasState, PA.Nodes.AtProgramPoint ,pp2,  context);
           
            if((pointerState == null && pointerState2 != null) || (pointerState != null && pointerState2 == null))
                return false;
            //If both the pointerStates are null, it is as good as they are same.
            if((pointerState == null && pointerState2 == null) || pointerState.sameValueAs(pointerState2))
                continue;
            Node stateValue = Util.getPropValue(pointerState, PA.Nodes.StateValue , context);
            Node stateValue2 = Util.getPropValue(pointerState2, PA.Nodes.StateValue , context);
            Node stateRelation = Util.getPropValue(pointerState, PA.Nodes.StateRelation , context);
            Node stateRelation2 = Util.getPropValue(pointerState2, PA.Nodes.StateRelation , context);
           /* if(stateValue != null && stateValue2 != null){
                if(stateValue.getLocalName().equals("constant1") || stateValue2.getLocalName().equals("constant1"))
                    continue;
            }
*/
            if(stateValue != null && stateValue2 != null && stateRelation != null && stateRelation2 != null)
           if(!stateValue.sameValueAs(stateValue2) || !stateRelation.sameValueAs(stateRelation2))
               return false;
        }
       return true;
    }
    
    
   }