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


public class CreateVariableState1 extends BaseBuiltin {

    /**
     * Return a name for this builtin, normally this will be the name of the 
     * functor that will be used to invoke it.
     */
    @Override
    public String getName() {
        return "createVariableState1";
    }
    
    /**
     * Return the expected number of arguments for this functor or 0 if the number is flexible.
     */
    @Override
    public int getArgLength() {
        return 2;
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
        String variable = getArg(0, args, context).toString();
        String pp = getArg(1, args, context).toString();
     
        makeState(variable, pp);
      
        /*String result = Bdd.save(bb);    
      
      Node res = Util.makeStringNode(result);
       return env.bind(args[2], res);
*/
        return true;
    }

    public void makeState(String variable, String pp) {
        int encodingOfVariable = Bdd.pmi.getEncodingOfEntity(Entity.Var, variable.hashCode());
        BDD varBdd = Bdd.createBddOfEntity(Entity.Var,encodingOfVariable);
        BDD ppBdd = Bdd.load(pp);
        //BDD iterBdd  = Bdd.createBddOfEntity(Entity.iter,iter);
        BDD bb = varBdd.and(ppBdd);
        Bdd.bdd.orWith(bb);
    }
    
}
//cond is a string with 1's and 0's
