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
import net.sf.javabdd.BDD;

import org.apache.jena.graph.* ;
import org.apache.jena.reasoner.rulesys.* ;


public class CreateProgramPoint extends BaseBuiltin {

    /**
     * Return a name for this builtin, normally this will be the name of the 
     * functor that will be used to invoke it.
     */
    @Override
    public String getName() {
        return "createProgramPoint";
    }
    
    /**
     * Return the expected number of arguments for this functor or 0 if the number is flexible.
     */
    @Override
    public int getArgLength() {
        return 5;
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
        String st = getArg(0, args, context).toString();
        String cond = getArg(1, args, context).toString();
        int iter =  (int) getArg(2, args, context).getLiteralValue();
        Node baseConditions =  getArg(3, args, context);//.getLiteralValue().toString();
        //Node pp = getArg(4, args, context);
        /* List<Node> l = Util.convertList(n2, context);
        for ( Node x : l )
        {
        context.add( new Triple( n0, n1, x ) );
        }
         */
        int encodingOfStatement = Bdd.pmi.getEncodingOfEntity(Entity.Statement, st.hashCode());
        BDD stBdd = Bdd.createBddOfEntity(Entity.Statement,encodingOfStatement);
        BDD condBdd = Bdd.load(cond);
        BDD iterBdd  = Bdd.createBddOfEntity(Entity.iter,iter);
        BDD bb = stBdd.and(condBdd).and(iterBdd);
        //Bdd.bdd.orWith(bb);
        String result = Bdd.save(bb);    
        Node res = NodeFactory.createBlankNode(result);
       return env.bind(args[4], res);
    }
    
}
