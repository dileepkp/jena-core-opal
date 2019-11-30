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
package org.apache.jena.vocabulary;

import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 *
 * @author aidb
 */
public class C {
    
    /**
     * The namespace of the vocabulary as a string
     */
    public static final String uri = "http://www.semanticweb.org/yzhao30/ontologies/2015/7/c#";

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
    public static final Resource    global          = Init.global();
    public static final Resource    pointerType          = Init.pointerType();
     public static final Resource    ifElseStatement          = Init.ifElseStatement();
 
    public static final Property    hasName        = Init.hasName();
    public static final Property    hasForIncr        = Init.hasForIncr();
    public static final Property    hasCondition        = Init.hasCondition();
    public static final Property    hasType        = Init.hasType();
    public static final Property    hasBaseType        = Init.hasBaseType();
    public static final Property    hasArgumentExpr        = Init.hasArgumentExpr();
    public static final Property    referTo        = Init.referTo();
    public static final Property    hasParent        = Init.hasParent();
  
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
        public static Resource pointerType() { return resource( "pointer_type" ); }
        public static Resource global() { return resource( "Global" ); }
        
          public static Property hasParent()            { return property( "hasParent" ); }
         public static Property hasArgumentExpr()            { return property( "hasArgumentExpr" ); }
        public static Property hasBaseType()            { return property( "hasBaseType" ); }
         public static Property hasType()            { return property( "hasType" ); }
        public static Property hasName()            { return property( "hasName" ); }
        public static Property hasForIncr()            { return property( "hasForIncr" ); }
        public static Property hasCondition()            { return property( "hasCondition" ); }
        public static Property referTo()            { return property( "referTo" ); }
        public static Resource ifElseStatement()            { return property( "IfElseStatement" ); }
       
    }
    
    /**
        The same items of vocabulary, but at the Node level, parked inside a
        nested class so that there's a simple way to refer to them.
    */
    @SuppressWarnings("hiding") 
    public static final class Nodes
    {
         public static final Node Constant1        = Init.constant1().asNode();
         
        public static final Node HasName        = Init.hasName().asNode();
        public static final Node HasForIncr        = Init.hasForIncr().asNode();
        public static final Node IfElseStatement        = Init.ifElseStatement().asNode();
        public static final Node HasCondition        = Init.hasCondition().asNode();
        public static final Node HasType       = Init.hasType().asNode();
        public static final Node HasBaseType        = Init.hasBaseType().asNode();
        public static final Node PointerType        = Init.pointerType().asNode();
        public static final Node HasArgumentExpr        = Init.hasArgumentExpr().asNode();
        public static final Node ReferTo        = Init.referTo().asNode();
        public static final Node HasParent        = Init.hasParent().asNode();
        public static final Node Global        = Init.global().asNode();
    }
    
}
