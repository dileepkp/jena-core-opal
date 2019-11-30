/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.reasoner.rulesys.builtins;

import java.util.*;

import org.apache.jena.graph.* ;
import org.apache.jena.reasoner.rulesys.* ;
import org.apache.jena.vocabulary.PA;
import org.apache.jena.vocabulary.RDF;

/**
 
 */
public class CreateSlice extends BaseBuiltin {

    /**
     * Return a name for this builtin, normally this will be the name of the 
     * functor that will be used to invoke it.
     */
    @Override
    public String getName() {
        return "createSlice";
    }
    
    /**
     * Return the expected number of arguments for this functor or 0 if the number is flexible.
     */
    @Override
    public int getArgLength() {
        return 4;
    }
    
    /**
     * This method is invoked when the builtin is called in a rule head.
     * Such a use is only valid in a forward rule.
     * @param args the array of argument values for the builtin, this is an array 
     * of Nodes.
     * @param length the length of the argument list, may be less than the length of the args array
     * for some rule engines
     * @param context an execution context giving access to other relevant data
     */
    @Override
    public void headAction(Node[] args, int length, RuleContext context) {
        checkArgs(length, context);
        String sliceName = (String)getArg(0, args, context).getLiteralValue();
        int from = Integer.parseInt((String)getArg(1, args, context).getLiteralValue());
        int to = Integer.parseInt((String)getArg(2, args, context).getLiteralValue());
        Node n3 = getArg(3, args, context);
        String vars = getNodeValue(n3);
        List<Node> variables;
        if(vars.equals("all"))
            variables = null;
        else
            variables = Util.getPropValueList(n3, RDF.Nodes.item, context);
        
        Node slice = NodeFactory.createURI(sliceName);
       
        Node runningSlice = slice;
        Node first, rest;
       
        for(int i= from; i<=to ;++i){
            context.add( new Triple( runningSlice, RDF.Nodes.type, RDF.Nodes.List));
            first = NodeFactory.createBlankNode();
            //first = NodeFactory.createURI(sliceName + "-" + i);
            context.add( new Triple( runningSlice, RDF.Nodes.first, first));
            createFirstNodeInList(first,i,context,variables);
            
            if(i == to)
                context.add( new Triple( runningSlice, RDF.Nodes.rest, RDF.Nodes.nil));
            else {
                rest = NodeFactory.createBlankNode();
            context.add( new Triple( runningSlice, RDF.Nodes.rest, rest));
            runningSlice = rest;
            }
        }
        
    }

    private void createFirstNodeInList2(Node first, int time, RuleContext context, List<Node> variables) {
        context.add( new Triple( first, RDF.Nodes.type, RDF.Nodes.Bag));
        List<Node> variableStates = Util.getSubjValueJoinOnObj(PA.Nodes.AtProgramPoint,PA.Nodes.TimeStamp ,Util.makeIntNode(time), context);
        for(Node s : variableStates){
            if(variables != null){
                Node subjValue = Util.getSubjValue(PA.Nodes.HasState,s, context);
                if(!variables.contains(s))
                    continue;
            }
            context.add( new Triple( first, RDF.Nodes.li, s));
        }
    }
    
    private void createFirstNodeInList(Node first, int time, RuleContext context, List<Node> variables) {
        Node rest;
        context.add( new Triple( first, RDF.Nodes.type, RDF.Nodes.List));
        List<Node> variableStates = Util.getSubjValueJoinOnObj(PA.Nodes.AtProgramPoint,PA.Nodes.TimeStamp ,Util.makeIntNode(time), context);
        for(int i=0; i<variableStates.size(); ++i){
            Node s = variableStates.get(i);
            if(variables != null){
                Node subjValue = Util.getSubjValue(PA.Nodes.HasState,s, context);
                if(!variables.contains(s))
                    continue;
            }
            context.add( new Triple( first, RDF.Nodes.first, s));
            
             if(i == variableStates.size() - 1)
                context.add( new Triple( first, RDF.Nodes.rest, RDF.Nodes.nil));
            else {
                rest = NodeFactory.createBlankNode();
            context.add( new Triple( first, RDF.Nodes.rest, rest));
            first = rest;
            }
        }
    }
    
    
    String getNodeValue(Node arg) {
        if(arg.isLiteral())
            return arg.getLiteralValue().toString();
        else
            return arg.toString();
    }
    
}
