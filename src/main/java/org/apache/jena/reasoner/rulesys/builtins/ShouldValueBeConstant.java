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

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.vocabulary.C;
import org.apache.jena.vocabulary.PA;

/**
 *
 * @author aidb
 */
public class ShouldValueBeConstant extends BaseBuiltin {
      /**
     * Return a name for this builtin, normally this will be the name of the 
     * functor that will be used to invoke it.
     */
    @Override
    public String getName() {
        return "shouldValueBeConstant";
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
        
        return ShouldValueBeConstant.shouldValueConstant(n0, n1, n2,context);
        //return true;
    }
    
    static boolean shouldValueConstant(Node st, Node iter, Node loopSetting, RuleContext context){
        boolean isStLastInLoop = false;
        Node subjValue = Util.getSubjValue(C.Nodes.HasForIncr, st, context);
        if(subjValue == null){
            Node propValue = Util.getPropValue(st, PA.Nodes.LastStatementInLoop, context);
            if(propValue != null)
                isStLastInLoop = true;
        } else
            isStLastInLoop = true;
        
        int iterValue = (int) iter.getLiteralValue();
        int loopValue = (int) loopSetting.getLiteralValue();
        
        return isStLastInLoop && iterValue >= loopValue;
    }

}
