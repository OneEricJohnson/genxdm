/**
 * Copyright (c) 2011 TIBCO Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.genxdm.bridgetest.axes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.genxdm.Feature;
import org.genxdm.base.Model;
import org.genxdm.base.ProcessingContext;
import org.genxdm.base.io.FragmentBuilder;
import org.genxdm.bridgetest.TestBase;
import org.junit.Test;

public abstract class AxisNodeNavigatorBase<N>
    extends TestBase<N>
{

    @Test
    public void attributes()
    {
        ProcessingContext<N> context = newProcessingContext();
        
        boolean doInheritedAttributeTests = context.isSupported(Feature.ATTRIBUTE_AXIS_INHERIT);
        
        FragmentBuilder<N> builder = context.newFragmentBuilder();
        N doc = createComplexTestDocument(builder);
        
        assertNotNull(doc);
        Model<N> model = context.getModel();
        assertNotNull(model);
        
        ArrayList<N> attributes = new ArrayList<N>(); 
        
        // document nodes have no attributes, inherited or otherwise
        Iterable<N> atts = model.getAttributeAxis(doc, false);
        assertNotNull(atts);
        iterableToList(atts, attributes);
        assertEquals(0, attributes.size());
        
        if (doInheritedAttributeTests)
        {
            atts = model.getAttributeAxis(doc, true);
            assertNotNull(atts);
            iterableToList(atts, attributes);
            assertEquals(0, attributes.size());
        }
        
        // (some) element nodes do have attributes.
        N de = model.getFirstChildElement(doc);
        assertNotNull(de);
        // the document element has three attributes, one of which is xml:lang
        atts = model.getAttributeAxis(de, false);
        assertNotNull(atts);
        iterableToList(atts, attributes);
        assertEquals(3, attributes.size());

        if (doInheritedAttributeTests)
        {
            atts = model.getAttributeAxis(de, true);
            assertNotNull(atts);
            iterableToList(atts, attributes);
            assertEquals(3, attributes.size());
        }
        
        N n = model.getFirstChildElement(de); // path element; 1 attribute
        assertNotNull(n);
        atts = model.getAttributeAxis(n, false);
        assertNotNull(atts);
        iterableToList(atts, attributes);
        assertEquals(1, attributes.size());
        
        if (doInheritedAttributeTests)
        {
            // there are *two* attributes if we inherit.
            atts = model.getAttributeAxis(n, true);
            assertNotNull(atts);
            iterableToList(atts, attributes);
            assertEquals(2, attributes.size());
        }
        
        n = attributes.get(0); // first attribute
        atts = model.getAttributeAxis(n, false);
        assertNotNull(atts);
        iterableToList(atts, attributes);
        assertEquals(0, attributes.size()); // attributes ain't got attributes.

        if (doInheritedAttributeTests)
        {
            atts = model.getAttributeAxis(n, true);
            assertNotNull(atts);
            iterableToList(atts, attributes);
            assertEquals(0, attributes.size()); // not even inherited ones.
        }

        // nstest element has no attributes, but does inherit the same as before.
        n = model.getPreviousSibling(model.getLastChild(de)); // nstest element
        atts = model.getAttributeAxis(n, false);
        assertNotNull(atts);
        iterableToList(atts, attributes);
        assertEquals(0, attributes.size());

        if (doInheritedAttributeTests)
        {
            atts = model.getAttributeAxis(n, true);
            assertNotNull(atts);
            iterableToList(atts, attributes);
            assertEquals(1, attributes.size());
        }
        
        N ns = getNamespaceNode(model, n, "gue");
        assertNotNull(ns);
        atts = model.getAttributeAxis(ns, false);
        assertNotNull(atts);
        iterableToList(atts, attributes);
        assertEquals(0, attributes.size()); // namespaces don't got attributes, neither

        if (doInheritedAttributeTests)
        {
            atts = model.getAttributeAxis(ns, true);
            assertNotNull(atts);
            iterableToList(atts, attributes);
            assertEquals(0, attributes.size()); // nope, not even inherited ones
        }
        
        n = model.getLastChild(model.getFirstChildElement(n)); // text node: no atts
        assertNotNull(n);
        atts = model.getAttributeAxis(n, false);
        assertNotNull(atts);
        iterableToList(atts, attributes);
        assertEquals(0, attributes.size());
        if (doInheritedAttributeTests)
        {
            atts = model.getAttributeAxis(n, true);
            assertNotNull(atts);
            iterableToList(atts, attributes);
            assertEquals(0, attributes.size());
        }
        
        n = model.getPreviousSibling(n); // pi; no atts
        assertNotNull(n);
        atts = model.getAttributeAxis(n, false);
        assertNotNull(atts);
        iterableToList(atts, attributes);
        assertEquals(0, attributes.size());
        if (doInheritedAttributeTests)
        {
            atts = model.getAttributeAxis(n, true);
            assertNotNull(atts);
            iterableToList(atts, attributes);
            assertEquals(0, attributes.size());
        }

        n = model.getPreviousSibling(model.getPreviousSibling(n)); // comment; no atts
        assertNotNull(n);
        atts = model.getAttributeAxis(n, false);
        assertNotNull(atts);
        iterableToList(atts, attributes);
        assertEquals(0, attributes.size());
        if (doInheritedAttributeTests)
        {
            atts = model.getAttributeAxis(n, true);
            assertNotNull(atts);
            iterableToList(atts, attributes);
            assertEquals(0, attributes.size());
        }
    }
    
    @Test
    public void namespaces()
    {
        ProcessingContext<N> context = newProcessingContext();
        if (context.isSupported(Feature.NAMESPACE_AXIS))
        {
            FragmentBuilder<N> builder = context.newFragmentBuilder();
            N doc = createComplexTestDocument(builder);
            
            assertNotNull(doc);
            Model<N> model = context.getModel();
            assertNotNull(model);

            ArrayList<N> domains = new ArrayList<N>();
            
            Iterable<N> namespaces = model.getNamespaceAxis(doc, false);
            assertNotNull(namespaces);
            iterableToList(namespaces, domains);
            assertEquals(0, domains.size());
            
            namespaces = model.getNamespaceAxis(doc, true);
            assertNotNull(namespaces);
            iterableToList(namespaces, domains);
            assertEquals(0, domains.size());
            
            N e = model.getFirstChildElement(doc);
            // an element node has a namespace axis; this one's empty,
            // except for the inherited namespace.
            
            namespaces = model.getNamespaceAxis(e, false);
            assertNotNull(namespaces);
            iterableToList(namespaces, domains);
            assertEquals(0, domains.size());
            
            namespaces = model.getNamespaceAxis(e, true);
            assertNotNull(namespaces);
            iterableToList(namespaces, domains);
            assertEquals(1, domains.size());
            
            N n = model.getAttribute(e, "", "name");
            namespaces = model.getNamespaceAxis(n, false);
            assertNotNull(namespaces);
            iterableToList(namespaces, domains);
            assertEquals(0, domains.size());
            
            namespaces = model.getNamespaceAxis(n, true);
            assertNotNull(namespaces);
            iterableToList(namespaces, domains);
            assertEquals(0, domains.size());
            
            n = model.getLastChild(e);
            e = model.getPreviousSibling(n);
            namespaces = model.getNamespaceAxis(e, false);
            assertNotNull(namespaces);
            iterableToList(namespaces, domains);
//for (N ns : namespaces) { System.out.println(model.getLocalName(ns)); System.out.println(model.getStringValue(ns));}
            assertEquals(2, domains.size());
            
            namespaces = model.getNamespaceAxis(e, true);
            assertNotNull(namespaces);
            iterableToList(namespaces, domains);
            assertEquals(3, domains.size());
            
            n = getNamespaceNode(model, e, "gue");
            namespaces = model.getNamespaceAxis(e, false);
            assertNotNull(namespaces);
            iterableToList(namespaces, domains);
            assertEquals(0, domains.size());
            
            namespaces = model.getNamespaceAxis(e, true);
            assertNotNull(namespaces);
            iterableToList(namespaces, domains);
            assertEquals(0, domains.size());
            
            e = model.getFirstChildElement(e);
            // this one also has namespaces. 2+2
            namespaces = model.getNamespaceAxis(e, false);
            iterableToList(namespaces, domains);
            assertEquals(2, domains.size());
            
            namespaces = model.getNamespaceAxis(e, true);
            iterableToList(namespaces, domains);
            assertEquals(4, domains.size());
            
            // get the last child.  that's a text node.
            n = model.getLastChild(e);
            namespaces = model.getNamespaceAxis(n, false);
            assertNotNull(namespaces);
            iterableToList(namespaces, domains);
            assertEquals(0, domains.size());
            
            namespaces = model.getNamespaceAxis(n, true);
            assertNotNull(namespaces);
            iterableToList(namespaces, domains);
            assertEquals(0, domains.size());
            
            // get the previous sibling. it's a processing instruction
            n = model.getPreviousSibling(n);
            namespaces = model.getNamespaceAxis(n, false);
            assertNotNull(namespaces);
            iterableToList(namespaces, domains);
            assertEquals(0, domains.size());
            
            namespaces = model.getNamespaceAxis(n, true);
            assertNotNull(namespaces);
            iterableToList(namespaces, domains);
            assertEquals(0, domains.size());
            
            // get the previous sibling of the previous sibling. that's a comment
            n = model.getPreviousSibling(model.getPreviousSibling(n));
            namespaces = model.getNamespaceAxis(n, false);
            assertNotNull(namespaces);
            iterableToList(namespaces, domains);
            assertEquals(0, domains.size());
            
            namespaces = model.getNamespaceAxis(n, true);
            assertNotNull(namespaces);
            iterableToList(namespaces, domains);
            assertEquals(0, domains.size());
            
            // get the firstchildelement.  0+4
            n = model.getFirstChildElement(e);
            namespaces = model.getNamespaceAxis(n, false);
            iterableToList(namespaces, domains);
            assertEquals(0, domains.size());
            
            namespaces = model.getNamespaceAxis(n, true);
            iterableToList(namespaces, domains);
            assertEquals(4, domains.size());
            // TODO: doc, doc element (inherited only). check docelement att
            // nstest element and its children. namespaces, text, comment, pi
        }
    }
    
    @Test
    public void ancestors()
    {
        ProcessingContext<N> context = newProcessingContext();
        FragmentBuilder<N> builder = context.newFragmentBuilder();
        N doc = createSimpleAllKindsDocument(builder);
        
        assertNotNull(doc);
        Model<N> model = context.getModel();
        assertNotNull(model);
        
        ArrayList<N> olds = new ArrayList<N>();
        
        Iterable<N> ancestors = model.getAncestorAxis(doc);
        assertNotNull(ancestors);
        iterableToList(ancestors, olds);
        assertEquals(0, olds.size()); // documents have no ancestors.
        
        ancestors = model.getAncestorOrSelfAxis(doc);
        assertNotNull(ancestors);
        iterableToList(ancestors, olds);
        assertEquals(1, olds.size());
        
        N de = model.getFirstChildElement(doc);
        assertNotNull(de);
        
        ancestors = model.getAncestorAxis(de);
        assertNotNull(ancestors);
        iterableToList(ancestors, olds);
        assertEquals(1, olds.size());
        
        ancestors = model.getAncestorOrSelfAxis(de);
        assertNotNull(ancestors);
        iterableToList(ancestors, olds);
        assertEquals(2, olds.size());
        
        N n = model.getAttribute(de, "", "att");
        ancestors = model.getAncestorAxis(n);
        assertNotNull(ancestors);
        iterableToList(ancestors, olds);
        assertEquals(2, olds.size());
        
        ancestors = model.getAncestorOrSelfAxis(n);
        assertNotNull(ancestors);
        iterableToList(ancestors, olds);
        assertEquals(3, olds.size());
        
        n = getNamespaceNode(model, de, "ns");
        ancestors = model.getAncestorAxis(n);
        assertNotNull(ancestors);
        iterableToList(ancestors, olds);
        assertEquals(2, olds.size());
        
        ancestors = model.getAncestorOrSelfAxis(n);
        assertNotNull(ancestors);
        iterableToList(ancestors, olds);
        assertEquals(3, olds.size());
        
        n = model.getFirstChild(de); //comment
        ancestors = model.getAncestorAxis(n);
        assertNotNull(ancestors);
        iterableToList(ancestors, olds);
        assertEquals(2, olds.size());
        
        ancestors = model.getAncestorOrSelfAxis(n);
        assertNotNull(ancestors);
        iterableToList(ancestors, olds);
        assertEquals(3, olds.size());
        
        n = model.getNextSibling(n); // text
        ancestors = model.getAncestorAxis(n);
        assertNotNull(ancestors);
        iterableToList(ancestors, olds);
        assertEquals(2, olds.size());
        
        ancestors = model.getAncestorOrSelfAxis(n);
        assertNotNull(ancestors);
        iterableToList(ancestors, olds);
        assertEquals(3, olds.size());
        
        n = model.getNextSibling(n);
        ancestors = model.getAncestorAxis(n);
        assertNotNull(ancestors);
        iterableToList(ancestors, olds);
        assertEquals(2, olds.size());
        
        ancestors = model.getAncestorOrSelfAxis(n);
        assertNotNull(ancestors);
        iterableToList(ancestors, olds);
        assertEquals(3, olds.size());
    }
    
    @Test
    public void descendants()
    {
        ProcessingContext<N> context = newProcessingContext();
        FragmentBuilder<N> builder = context.newFragmentBuilder();
        N doc = createComplexTestDocument(builder);
        
        assertNotNull(doc);
        Model<N> model = context.getModel();
        assertNotNull(model);
        
        ArrayList<N> spawn = new ArrayList<N>();
        // TODO
        // no attributes, no namespaces
    }
    
    @Test
    public void children()
    {
        ProcessingContext<N> context = newProcessingContext();
        FragmentBuilder<N> builder = context.newFragmentBuilder();
        N doc = createComplexTestDocument(builder);
        
        assertNotNull(doc);
        Model<N> model = context.getModel();
        assertNotNull(model);

        ArrayList<N> spawn = new ArrayList<N>();
        // TODO
        // no attributes, no namespaces
    }
    
    @Test
    public void childElements()
    {
        ProcessingContext<N> context = newProcessingContext();
        FragmentBuilder<N> builder = context.newFragmentBuilder();
        N doc = createComplexTestDocument(builder);
        
        assertNotNull(doc);
        Model<N> model = context.getModel();
        assertNotNull(model);

        ArrayList<N> spawn = new ArrayList<N>();
        // TODO
        // no attributes, no namespaces
        // no comments, no text, no pis
    }
    
    @Test
    public void childElementsByName()
    {
        ProcessingContext<N> context = newProcessingContext();
        FragmentBuilder<N> builder = context.newFragmentBuilder();
        N doc = createComplexTestDocument(builder);
        
        assertNotNull(doc);
        Model<N> model = context.getModel();
        assertNotNull(model);

        ArrayList<N> spawn = new ArrayList<N>();
        // TODO
    }
    
    @Test
    public void followingSiblings()
    {
        ProcessingContext<N> context = newProcessingContext();
        FragmentBuilder<N> builder = context.newFragmentBuilder();
        N doc = createComplexTestDocument(builder);
        
        assertNotNull(doc);
        Model<N> model = context.getModel();
        assertNotNull(model);
        
        ArrayList<N> pests = new ArrayList<N>();
        // TODO
        // no attributes, no namespaces
    }
    
    @Test
    public void precedingSiblings()
    {
        ProcessingContext<N> context = newProcessingContext();
        FragmentBuilder<N> builder = context.newFragmentBuilder();
        N doc = createComplexTestDocument(builder);
        
        assertNotNull(doc);
        Model<N> model = context.getModel();
        assertNotNull(model);
        
        ArrayList<N> elders = new ArrayList<N>();
        // TODO
        // no attributes, no namespaces
    }

    @Test
    public void following()
    {
        ProcessingContext<N> context = newProcessingContext();
        FragmentBuilder<N> builder = context.newFragmentBuilder();
        N doc = createComplexTestDocument(builder);
        
        assertNotNull(doc);
        Model<N> model = context.getModel();
        assertNotNull(model);
        
        ArrayList<N> sheep = new ArrayList<N>();
        // TODO
        // note: no attributes, no namespaces, and no descendants.
        // all descendants of following siblings and of ancestor's following siblings
    }
    
    @Test
    public void preceding()
    {
        ProcessingContext<N> context = newProcessingContext();
        FragmentBuilder<N> builder = context.newFragmentBuilder();
        N doc = createComplexTestDocument(builder);
        
        assertNotNull(doc);
        Model<N> model = context.getModel();
        assertNotNull(model);
        
        ArrayList<N> cousins = new ArrayList<N>();
        // TODO
        // oh, how fucking weird.  *cannot* include ancestors, but includes
        // other descendants of the root of the tree that precede this node in doc order 
    }
    
    private void iterableToList(Iterable<N> iterable, ArrayList<N> list)
    {
        list.clear();
        for (N n : iterable)
            list.add(n);
    }
    
//    /**
//     * Returns the nodes along the ancestor axis using the specified node as the origin.
//     * 
//     * @param node
//     *            The origin node.
//     */
//    Iterable<N> getAncestorAxis(N node);
//
//    /**
//     * Returns the nodes along the ancestor-or-self axis using the specified node as the origin.
//     * 
//     * @param node
//     *            The origin node.
//     */
//    Iterable<N> getAncestorOrSelfAxis(N node);
//
//    /**
//     * Returns the nodes along the child axis using the specified node as the origin.
//     * 
//     * <br/>
//     * 
//     * Corresponds to the <a href="http://www.w3.org/TR/xpath-datamodel/#acc-summ-children">
//     * dm:children</a> accessor in the XDM.
//     * 
//     * @param node
//     *            The origin node.
//     * 
//     * @see http://www.w3.org/TR/xpath-datamodel/#acc-summ-children
//     */
//    Iterable<N> getChildAxis(N node);
//
//    /**
//     * Returns all the child element along the child axis.
//     * 
//     * @param node
//     *            The parent node that owns the child axis.
//     */
//    Iterable<N> getChildElements(N node);
//
//    /**
//     * Returns all the child element along the child axis whose names match the arguments supplied.
//     * 
//     * @param node
//     *            The parent node that owns the child axis.
//     * @param namespaceURI
//     *            The namespace-uri to be matched.
//     * @param localName
//     *            The local-name to be matched.
//     */
//    Iterable<N> getChildElementsByName(N node, String namespaceURI, String localName);
//
//    /**
//     * Returns the nodes along the descendant axis using the specified node as the origin.
//     * 
//     * @param node
//     *            The origin node.
//     */
//    Iterable<N> getDescendantAxis(N node);
//
//    /**
//     * Returns the nodes along the descendant-or-self axis using the specified node as the origin.
//     * 
//     * @param node
//     *            The origin node.
//     */
//    Iterable<N> getDescendantOrSelfAxis(N node);
//
//    /**
//     * Returns the nodes along the following axis using the specified node as the origin.
//     * 
//     * @param node
//     *            The origin node.
//     */
//    Iterable<N> getFollowingAxis(N node);
//
//    /**
//     * Returns the nodes along the following-sibling axis using the specified node as the origin.
//     * 
//     * @param node
//     *            The origin node.
//     */
//    Iterable<N> getFollowingSiblingAxis(N node);
//
//    /**
//     * Returns the nodes along the preceding axis using the specified node as the origin.
//     * 
//     * @param node
//     *            The origin node.
//     */
//    Iterable<N> getPrecedingAxis(N node);
//
//    /**
//     * Returns the nodes along the preceding-sibling axis using the specified node as the origin.
//     * 
//     * @param node
//     *            The origin node.
//     */
//    Iterable<N> getPrecedingSiblingAxis(N node);
}