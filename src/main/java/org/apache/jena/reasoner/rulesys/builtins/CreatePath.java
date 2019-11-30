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


public class CreatePath extends BaseBuiltin {

    /**
     * Return a name for this builtin, normally this will be the name of the 
     * functor that will be used to invoke it.
     */
    @Override
    public String getName() {
        return "createPath";
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
        
        String oldPath = Bdd.getNodeValue(getArg(0, args, context));
        String cond = Bdd.getNodeValue(getArg(1, args, context));
        Boolean eval = (Boolean)getArg(2, args, context).getLiteralValue();
        Node newPath = getArg(3, args, context);
        String funcNode = getArg(4, args, context).toString();
        
        int encodingOfCondition;
        Integer funcDetails[] = null;
        if(cond.equals("defaultCondition") && !funcNode.equals("")){
            encodingOfCondition = -1;
            funcDetails = Bdd.pmi.conditionFuncMap.get(funcNode.hashCode());
        }
        else{
            encodingOfCondition = Bdd.pmi.getEncodingOfEntity(Entity.Condition, cond.hashCode());
            funcDetails = Bdd.pmi.conditionFuncMap.get(cond.hashCode());
        }
        
        //funcDetails[1] is start bdd node of the condition of the function
        //funcDetails[2] is the no of conditions in the function + extra 1 node
        int vars[] = IntStream.rangeClosed(funcDetails[1], funcDetails[1]+funcDetails[2]).toArray();
        
        boolean[] values = new boolean[vars.length];
        if(encodingOfCondition != -1) {
             for (int i = 0; i < vars.length; i++){
            if(vars[i] < encodingOfCondition){
                values[i] = (oldPath.charAt(i)=='1');
            } else if(vars[i] == encodingOfCondition){
                values[i] = eval;
            } else if(vars[i] == encodingOfCondition +1){
                values[i] = true;
            } else if(vars[i] > encodingOfCondition +1){
                values[i] = false;
            }
        
        }
        } else{
            values[0] = true;
             for (int i = 1; i < vars.length; i++){
                 values[i] = false;
             }
        }
       
            

        
       /* if(cond.equals("defaultCondition")){//first condition of the function
            
        } else{
            
        }
                
        */
        int blockId = Entity.Condition.ordinal();
        BDD bb =  Bdd.bddFactory.getDomain(blockId).getFactory().one();
        
      /*  if(values[0])
            bb = Bdd.bddFactory.getDomain(blockId).getFactory().ithVar(vars[0]);
        else
            bb = Bdd.bddFactory.getDomain(blockId).getFactory().nithVar(vars[0]);
        */
        for (int i = 0; i < vars.length; i++){
            if(values[i])
                bb.andWith(Bdd.bddFactory.getDomain(blockId).getFactory().ithVar(vars[i]));
            else
                bb.andWith(Bdd.bddFactory.getDomain(blockId).getFactory().nithVar(vars[i]));
        }
      //  Bdd.bdd.orWith(bb);
        
      /*  if(eval) 
            bb = Bdd.bddFactory.getDomain(blockId).getFactory().ithVar(encodingOfCondition);
        else
            bb = Bdd.bddFactory.getDomain(blockId).getFactory().nithVar(encodingOfCondition);
       */ 
        
       /* List<Node> l = Util.convertList(n2, context);
        for ( Node x : l )
      
            context.add( new Triple( n0, n1, x ) );
        }
*/
       String result = Bdd.save(bb);
        // Node res = Util.makeStringNode(Arrays.toString(values) + ":" + vars[0]);
      
     // Node res = Util.makeStringNode(result);
      Node res = NodeFactory.createBlankNode(result);
      
      return env.bind(args[3], res);
    }

  
    
}
//cond is a string with 1's and 0's
