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
import org.apache.jena.rdf.model.ResourceFactory;
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
public class GetValueOfExpression  extends BaseBuiltin  {
     /**
     * Return a name for this builtin, normally this will be the name of the 
     * functor that will be used to invoke it.
     */
    @Override
    public String getName() {
        return "getValueOfExpression";
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
        try{
        checkArgs(length, context);
        BindingEnvironment env = context.getEnv();
        boolean ok = false;
         JEP mathParser = new JEP();
        Node varList = getArg(0, args, context);
        Node prevPP = getArg(1, args, context);
        Node expression = getArg(2, args, context);
       // Node returnVal = getArg(3, args, context);
        
        String code = StaticTrace.getCode(expression);
        GetValueOfExpression.loadVariablesIntoMathParser(varList, context, prevPP, mathParser);
        mathParser.parseExpression(code);
            
            double expressionValue = (double) mathParser.getValue();
            Node newVal;
            
            if (Double.isNaN(expressionValue)) 
                newVal = Util.makeURINode(PA.constant1.getURI());
            else newVal =Util.makeDoubleNode(expressionValue);
            
            ok = env.bind(args[3], newVal);
        
       /* if (Util.isNumeric(a0)) {
            Node newVal = Util.makeIntNode( Util.getIntValue(a0) + 1 );
            ok = env.bind(args[1], newVal);
        } else if (Util.isNumeric(a1)) {
            Node newVal = Util.makeIntNode( Util.getIntValue(a1) - 1 );
            ok = env.bind(args[0], newVal);
        }*/
        return ok;
        }
        catch(Exception ex){
            
        }
        finally{
            BindingEnvironment env = context.getEnv();
            Node newVal = Util.makeURINode(PA.unknown.getURI());
            return env.bind(args[3], newVal);
        }
    }

   static void loadVariablesIntoMathParser(Node varList, RuleContext context, Node prevPP, JEP mathParser) {
        List<Node> listOfVars = Util.getPropValueList(varList, RDF.item.asNode(), context);
        for(Node var : listOfVars){
            String varName = Util.getPropValue(var, C.hasName.asNode(), context).getLiteralValue().toString();
            Node value = getValueofVariable(var,prevPP,context);
            if(value != null)
                mathParser.addVariable(varName,value.getLiteralValue());
            
        }
    }

    private static Node getValueofVariable(Node var, Node prevPP,RuleContext context) {
        //(?var pa:hasPointerState ?ps2)
	//(?ps2 pa:atProgramPoint ?prevPP)
	Node pointerState = Util.getJoinPropValue(var, PA.Nodes.HasPointerState, PA.Nodes.AtProgramPoint ,prevPP,  context);
        if(pointerState == null){
           return null; 
        }
         Node stateValue = Util.getPropValue(pointerState, PA.Nodes.StateValue , context);
           return stateValue;
        
    }
}
