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
 * For each element in the RDF list (first argument) it asserts 
 * triples with that as the subject and predicate and object given by arguments
 * two and three. A strange and hacky function, only usable in the head of
 * forward rules.
 */
public class CreateOtherVariableStateTriples extends BaseBuiltin {

    /**
     * Return a name for this builtin, normally this will be the name of the 
     * functor that will be used to invoke it.
     */
    @Override
    public String getName() {
        return "createOtherVariableStateTriples";
    }
    
    /**
     * Return the expected number of arguments for this functor or 0 if the number is flexible.
     */
    @Override
    public int getArgLength() {
        return 6;
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
        BindingEnvironment env = context.getEnv();
        Node n0 = getArg(0, args, context);
        Node curr = getArg(1, args, context);
        Node prev = getArg(2, args, context);
        String st = getArg(3, args, context).toString();
        Node pp = getArg(4, args, context);
        //int prevIter =  (int) getArg(5, args, context).getLiteralValue();
        Node prevIter =  getArg(5, args, context);
     
      List<Node> variables = Util.getPropValueList(n0, RDF.Nodes.item, context);
//Util.convertList(n0, context);
        variables.remove(curr);
        
        Node prevpp = Util.getJoinSubjValue(PA.Nodes.AfterStatement, prev, PA.Nodes.Iteration, prevIter, context);
  //      List<Node>l = Util.convertList(n0, context);
        for ( Node var : variables )
        {
          Node ps = Util.getJoinPropValue(var,PA.Nodes.HasPointerState,PA.Nodes.AtProgramPoint,prevpp, context);
          if(ps != null)
          context.add( new Triple( ps, PA.Nodes.AtProgramPoint,pp ) );
        }

    }
    
}
