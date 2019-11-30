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

import dileep.OPALFeature.ProgramCodeInformation;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Statement;

/**
 *
 * @author aidb
 */
public class StaticTrace {
     public static ProgramCodeInformation pci;
     
      public static void initialize(ProgramCodeInformation pm){
          pci = pm;
      }
      
      static String getCode(Statement st){
         pci.setCStatementLocation(st);
         return pci.getCode();
      }
      
      static String getCode(Node st){
         pci.setCStatementLocation(st.toString());
         return pci.getCode();
      }
}
