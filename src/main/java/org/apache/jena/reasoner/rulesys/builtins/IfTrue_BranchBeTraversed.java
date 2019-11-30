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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jena.graph.Node;
import org.apache.jena.reasoner.rulesys.BindingEnvironment;
import org.apache.jena.reasoner.rulesys.RuleContext;
import org.apache.jena.reasoner.rulesys.Util;
import org.apache.jena.vocabulary.C;
import org.apache.jena.vocabulary.PA;
import org.apache.jena.vocabulary.RDF;
import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;

/**
 *
 * @author aidb
 */
public class IfTrue_BranchBeTraversed extends BaseBuiltin{
    /**
     * Return a name for this builtin, normally this will be the name of the 
     * functor that will be used to invoke it.
     */
    @Override
    public String getName() {
        return "ifTrue_BranchBeTraversed";
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
     * @param context an execution context giving access to other relevant data
     * @param length the length of the argument list, may be less than the length of the args array
     * for some rule engines
     * @return return true if the buildin predicate is deemed to have succeeded in
     * the current environment
     */
    @Override
    public boolean bodyCall(Node[] args, int length, RuleContext context) {
        try {
            checkArgs(length, context);
            BindingEnvironment env = context.getEnv();
            boolean ok = false;
            JEP mathParser = new JEP();
            Node varList = getArg(0, args, context);
            Node prevSt = getArg(1, args, context);
            Node cond = getArg(2, args, context);
            Node iter = getArg(3, args, context);
            
            return IfTrue_BranchBeTraversed.evaluateExpression(prevSt, cond, context, iter, varList, mathParser);

            
        } catch (Exception ex) {
            //Logger.getLogger(IfTrue_BranchBeTraversed.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            return true;
        }
    }

    static boolean  evaluateExpression(Node prevSt, Node cond, RuleContext context, Node iter, Node varList, JEP mathParser) throws ParseException {
        boolean ok;
        Node interim = Util.getJoinSubjValue(PA.Nodes.AfterStatement, prevSt, PA.Nodes.HasProgramConditions, cond, context);
        Node prevPP = Util.getSubjValue(PA.Nodes.Iteration, iter, context);
        if(context.contains(prevSt,RDF.Nodes.type, C.Nodes.IfElseStatement)){
            prevSt = Util.getPropValue(prevSt,C.Nodes.HasCondition , context);
        }
        String code = StaticTrace.getCode(prevSt);
        GetValueOfExpression.loadVariablesIntoMathParser(varList, context, prevPP, mathParser);
        org.nfunk.jep.Node n1 = mathParser.parseExpression(code);
        Double result = (Double) mathParser.evaluate(n1);
        ok = result.equals(0.0d);
        return ok;
        //double expressionValue = (double) mathParser.getValue();
        //Node newVal =Util.makeDoubleNode(expressionValue);
        //ok = env.bind(args[3], newVal);
        
        /* if (Util.isNumeric(a0)) {
        Node newVal = Util.makeIntNode( Util.getIntValue(a0) + 1 );
        ok = env.bind(args[1], newVal);
        } else if (Util.isNumeric(a1)) {
        Node newVal = Util.makeIntNode( Util.getIntValue(a1) - 1 );
        ok = env.bind(args[0], newVal);
        }*/
    }
}
