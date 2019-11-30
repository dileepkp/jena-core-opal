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


import dileep.OPALFeature.ProgramMetaInformation.Entity;
import java.util.*;
import java.util.stream.IntStream;
import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import org.apache.jena.graph.* ;
import org.apache.jena.reasoner.rulesys.* ;
import org.apache.jena.vocabulary.RDF;


public class CreateOtherVariableState extends BaseBuiltin {

    /**
     * Return a name for this builtin, normally this will be the name of the 
     * functor that will be used to invoke it.
     */
    @Override
    public String getName() {
        return "createOtherVariableState";
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
     * @return 
     */
    @Override
     public boolean bodyCall(Node[] args, int length, RuleContext context) {
        checkArgs(length, context);
        BindingEnvironment env = context.getEnv();
        Node n0 = getArg(0, args, context);
        Node curr = getArg(1, args, context);
        String prev = getArg(2, args, context).toString();
        String st = getArg(3, args, context).toString();
        String pp = getArg(4, args, context).toString();
        int prevIter =  (int) getArg(5, args, context).getLiteralValue();
     
      List<Node> variables = Util.getPropValueList(n0, RDF.Nodes.item, context);
//Util.convertList(n0, context);
        variables.remove(curr);
        
        int encodingOfPrevStatement = Bdd.pmi.getEncodingOfEntity(Entity.Statement, prev.hashCode());
        if(encodingOfPrevStatement == -1)
            return true;
        BDD prevStBdd = Bdd.createBddOfEntity(Entity.Statement,encodingOfPrevStatement);
         BDD prevIterBdd  = Bdd.createBddOfEntity(Entity.iter,prevIter);
          BDD ppBdd = Bdd.load(pp);
        for(Node var: variables){
            int encodingOfVariable = Bdd.pmi.getEncodingOfEntity(Entity.Var, var.toString().hashCode());
        BDD varBdd = Bdd.createBddOfEntity(Entity.Var,encodingOfVariable);
        BDD bdd1 = varBdd.and(prevStBdd).and(prevIterBdd);
        BDD bdd2 = Bdd.bdd.restrict(bdd1);
        BDD bdd3 = bdd2.and(ppBdd);
        Bdd.bdd.orWith(bdd3);
        
        }
         
         
      
        /*String result = Bdd.save(bb);    
      
      Node res = Util.makeStringNode(result);
       return env.bind(args[2], res);
*/
        return true;
    }

    /*public void makeState(String variable, String pp, int stateValue, String stateRelation) {
        int encodingOfVariable = Bdd.pmi.getEncodingOfEntity(Entity.Var, variable.hashCode());
        BDD varBdd = Bdd.createBddOfEntity(Entity.Var,encodingOfVariable);
        BDD ppBdd = Bdd.load(pp);
        BDD valueBdd  = Bdd.createBddOfEntity(Entity.Value,stateValue);
        int encodingOfRelation = Bdd.pmi.getEncodingOfEntity(Entity.inequality, stateRelation);
        BDD stateRelationBdd  = Bdd.createBddOfEntity(Entity.inequality, encodingOfRelation);
        BDD bb = varBdd.and(ppBdd).and(valueBdd).and(stateRelationBdd);
        
        Bdd.bdd.orWith(bb);
    }*/
    
}

