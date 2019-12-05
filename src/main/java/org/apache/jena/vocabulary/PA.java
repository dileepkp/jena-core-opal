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

package org.apache.jena.vocabulary;

import org.apache.jena.datatypes.RDFDatatype ;
import org.apache.jena.datatypes.xsd.impl.RDFLangString ;
import org.apache.jena.datatypes.xsd.impl.RDFhtml ;
import org.apache.jena.datatypes.xsd.impl.XMLLiteralType ;
import org.apache.jena.graph.Node ;
import org.apache.jena.rdf.model.Property ;
import org.apache.jena.rdf.model.Resource ;
import org.apache.jena.rdf.model.ResourceFactory ;
import static org.apache.jena.vocabulary.RDF.resource;

/**
    The standard RDF vocabulary.
*/

public class PA{

    /**
     * The namespace of the vocabulary as a string
     */
    public static final String uri = "http://www.semanticweb.org/aidb/ontologies/BugFindingOntology#";

    /** returns the URI for this schema
        @return the URI for this schema
    */
    public static String getURI()
        { return uri; }

    protected static final Resource resource( String local )
        { return ResourceFactory.createResource( uri + local ); }

    protected static final Property property( String local )
        { return ResourceFactory.createProperty( uri, local ); }

    public static Property li( int i )
        { return property( "_" + i ); }

    public static final Resource    constant1          = Init.constant1();
    public static final Resource    unknown          = Init.unknown();
    public static final Resource    equals          = Init.equals();
 
   // public static final Property    hasPointerState        = Init.hasPointerState();
    public static final Property    atProgramPoint        = Init.atProgramPoint();
    public static final Property    lastStatementInLoop        = Init.lastStatementInLoop();
    public static final Property    stateValue        = Init.stateValue();
    public static final Property    stateRelation        = Init.stateRelation();
      public static final Property    afterStatement        = Init.afterStatement();
        public static final Property    iteration        = Init.iteration();
          public static final Property    hasProgramConditions        = Init.hasProgramConditions();
  
    /** RDF constants are used during Jena initialization.
     * <p>
     * If that initialization is triggered by touching the RDF class,
     * then the constants are null.
     * <p>
     * So for these cases, call this helper class: Init.function()   
     */
    public static class Init {
        // JENA-1294
        // Version that calculate the constant when called. 
        public static Resource constant1() { return resource( "constant1" ); }
        public static Resource unknown() { return resource( "unknown" ); }
       
         public static Resource equals() { return resource( "equals" ); }
       
       // public static Property hasPointerState()            { return property( "hasPointerState" ); }
        public static Property hasState()            { return property( "hasState" ); }
        public static Property atProgramPoint()            { return property( "atProgramPoint" ); }
        public static Property stateValue()            { return property( "stateValue" ); }
        public static Property stateRelation()            { return property( "stateRelation" ); }
        public static Property afterStatement()            { return property( "afterStatement" ); }
        public static Property iteration()            { return property( "iteration" ); }
        public static Property timeStamp()            { return property( "timeStamp" ); }
        public static Property lastStatementInLoop()            { return property( "lastStatementInLoop" ); }
          public static Property hasProgramConditions()            { return property( "hasProgramConditions" ); }
        
    }
    
    /**
        The same items of vocabulary, but at the Node level, parked inside a
        nested class so that there's a simple way to refer to them.
    */
    @SuppressWarnings("hiding") 
    public static final class Nodes
    {
         public static final Node Constant1        = Init.constant1().asNode();
         public static final Node unknown        = Init.unknown().asNode();
         public static final Node equals        = Init.equals().asNode();
         
        public static final Node HasState        = Init.hasState().asNode();
        //public static final Node HasState        = Init.hasState().asNode();
        public static final Node AtProgramPoint        = Init.atProgramPoint().asNode();
        public static final Node StateValue        = Init.stateValue().asNode();
        public static final Node StateRelation        = Init.stateRelation().asNode();
        public static final Node AfterStatement        = Init.afterStatement().asNode();
        public static final Node Iteration        = Init.iteration().asNode();
        public static final Node TimeStamp        = Init.timeStamp().asNode();
        
        public static final Node LastStatementInLoop        = Init.lastStatementInLoop().asNode();
           public static final Node HasProgramConditions        = Init.hasProgramConditions().asNode();
       
    }
}
