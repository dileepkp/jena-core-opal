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

import java.util.List;
import org.apache.jena.graph.* ;
import org.apache.jena.reasoner.rulesys.* ;
import org.apache.jena.vocabulary.RDF ;

public class NearestNeighbour extends BaseBuiltin {

    /**
     * Return a name for this builtin, normally this will be the name of the 
     * functor that will be used to invoke it.
     */
    @Override
    public String getName() {
        return "nearestNeighbour";
    }
    
    /**
     * Return the expected number of arguments for this functor or 0 if the number is flexible.
     */
    @Override
    public int getArgLength() {
        return 4;
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
        Node n3 = getArg(3, args, context);
        
        return listContains2(n0, n1, n2, n3, context);
    }
    
    /**
     * Return true if the first argument is a list which contains
     * the second argument.
     */
    protected static boolean listContains2(Node list, Node parent, Node progConditions, Node subList, RuleContext context ) {
         
         // parent condition list
         List<Node> stCondList= Util.getPropValueList(parent, progConditions, context);
        List<Node> condInPathList = Util.convertList(list, context);
        for(Node n : condInPathList){ // Conditions in path of exit statement
            
                if(stCondList.contains(n)){
                    if(n.equals(subList))
                        return true;
                    else return false;
                }
      
        }
        
         return false;
    }
}