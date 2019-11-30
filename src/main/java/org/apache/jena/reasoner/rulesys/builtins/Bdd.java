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
import dileep.OPALFeature.ProgramMetaInformation;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.rdf.model.impl.StatementImpl;
import org.apache.jena.vocabulary.PA;
import org.apache.jena.vocabulary.RDF;

/**
 *
 * @author aidb
 */
public class Bdd {
    
    public static ProgramMetaInformation pmi;
    static BDDFactory bddFactory;
    static BDD bdd;
    
    public static void buildBdd(ProgramMetaInformation pm){
        pmi = pm;
        bddFactory = BDDFactory.init(2000, 2000);
       
        long[] bddBlockSizes = pmi.getBddBlockSizes();
        int variables = pmi.getVariableCount();
        
        bddFactory.setVarNum(variables);
        for(long l: bddBlockSizes)
            bddFactory.extDomain(l);//log #variables are allocated
        
        bdd = bddFactory.zero();
    }

    static BDD createBddOfEntity(ProgramMetaInformation.Entity entity, int encodingOfStatement) {
       int blockId = entity.ordinal();
       BDD bb = bddFactory.getDomain(blockId).ithVar(encodingOfStatement);
       return bb;
    }

    static String save(BDD bb) {
        try {
            StringWriter sw = new StringWriter();
            BufferedWriter bw = new BufferedWriter(sw);
            bddFactory.save(bw, bb);
            bw.flush();
            StringBuffer sb = sw.getBuffer();
            return sb.toString();
        } catch (IOException ex) {
            Logger.getLogger(Bdd.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    static BDD load(String bddString) {
        try {
           BufferedReader br = new BufferedReader(new StringReader(bddString));
           BDD loadedBdd = bddFactory.load(br);
            return loadedBdd;
        } catch (IOException ex) {
            Logger.getLogger(Bdd.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
      static String getNodeValue(Node arg) {
        if(arg.isLiteral())
            return arg.getLiteralValue().toString();
        else
            return arg.toString();
    }

    static int getStateValue(Node arg) {
        if(arg.isLiteral())
            return (int) arg.getLiteralValue();
        else if(arg.toString().equals(PA.constant1.toString()))
                return 32767;
        return -1;
    }
    
    public static Model materializeBddInTriples(){
        int nodeNum,varNum,trueNode,falseNode,tempVal;
        HashMap<Integer, Integer> nodeToVarMap = new HashMap<>();
        Resource varNode;
        String bugFindIRI = "http://www.semanticweb.org/aidb/ontologies/BugFindingOntology#";
        Property trueProp = new PropertyImpl(bugFindIRI + "trueBddVariable");
        Property falseProp = new PropertyImpl(bugFindIRI + "falseBddVariable");
        Resource valNode;
        
        Model bddModel = ModelFactory.createDefaultModel();
        String bddString = save(bdd);
        String pattern = "^(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)$";
        
       Pattern  r = Pattern.compile(pattern,Pattern.MULTILINE);
       

      // Now create matcher object.
      Matcher m = r.matcher(bddString);
      
      while (m.find( )) {
          nodeNum = Integer.parseInt(m.group(1));
         varNum = Integer.parseInt(m.group(2));
          trueNode = Integer.parseInt(m.group(3));
          falseNode = Integer.parseInt(m.group(4));
          
          nodeToVarMap.put(nodeNum, varNum);
          
        varNode =  bddModel.createResource(bugFindIRI + varNum);
        //varNode = new ResourceImpl(bugFindIRI + varNum);
        
        tempVal = getNodeOfBddVar(nodeToVarMap,trueNode);
        valNode = bddModel.createResource(bugFindIRI + tempVal);
        bddModel.add(new StatementImpl(varNode, trueProp, valNode));
        
        tempVal = getNodeOfBddVar(nodeToVarMap,falseNode);
        valNode = bddModel.createResource(bugFindIRI + tempVal);
        bddModel.add(new StatementImpl(varNode, falseProp, valNode));
          
      }
      
      varNode = bddModel.createResource();
      Property p = bddModel.createProperty(bugFindIRI + "hasBuddyBdd");
      Literal bddLiteral = bddModel.createTypedLiteral(bddString);
       
        bddModel.add(new StatementImpl(varNode, p, bddLiteral));
        return bddModel;
    }

    private static int getNodeOfBddVar(HashMap<Integer, Integer> nodeToVarMap, int nodeNum) {
        if(nodeToVarMap.containsKey(nodeNum)){
            return (int)nodeToVarMap.get(nodeNum);
        }
        return nodeNum;
    }
    
    
}
    
      
    

