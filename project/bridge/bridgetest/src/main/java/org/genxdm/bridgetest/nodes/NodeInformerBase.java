package org.genxdm.bridgetest.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.genxdm.NodeKind;
import org.genxdm.base.Model;
import org.genxdm.base.ProcessingContext;
import org.genxdm.base.io.FragmentBuilder;
import org.genxdm.bridgetest.TestBase;
import org.genxdm.names.NamespaceBinding;
import org.junit.Test;

public abstract class NodeInformerBase<N>
    extends TestBase<N>
{

    @Test
    public void nodeKinds()
    {
        ProcessingContext<N> context = newProcessingContext();
        FragmentBuilder<N> builder = context.newFragmentBuilder();
        N doc = createSimpleAllKindsDocument(builder);
        
        assertNotNull(doc);
        Model<N> model = context.getModel();
        assertNotNull(model);
        
        // we're going to assume that navigation works; that's tested
        // in nodenavigator.
        assertTrue(model.getNodeKind(doc) == NodeKind.DOCUMENT);
        
        N docElement = model.getFirstChildElement(doc);
        assertTrue(model.getNodeKind(docElement) == NodeKind.ELEMENT);
        assertTrue(model.isElement(docElement));
        
        N node = model.getAttribute(docElement, "", "att");
        assertNotNull(node);
        assertTrue(model.getNodeKind(node) == NodeKind.ATTRIBUTE);
        assertTrue(model.isAttribute(node));
  
        node = getNamespaceNode(model, docElement, "ns");
        assertNotNull(node);
        assertTrue(model.getNodeKind(node) == NodeKind.NAMESPACE);
        assertTrue(model.isNamespace(node));
        
        node = model.getFirstChild(docElement);
        assertNotNull(node); // it should be a comment
        assertTrue(model.getNodeKind(node) == NodeKind.COMMENT);
        
        node = model.getNextSibling(node);
        assertNotNull(node); // it should be text
        assertTrue(model.getNodeKind(node) == NodeKind.TEXT);
        assertTrue(model.isText(node));
        
        node = model.getNextSibling(node);
        assertNotNull(node); // it should be a pi
        assertTrue(model.getNodeKind(node) == NodeKind.PROCESSING_INSTRUCTION);
    }
    
    @Test
    public void nodeIdentity()
    {
        ProcessingContext<N> context = newProcessingContext();
        FragmentBuilder<N> builder = context.newFragmentBuilder();
        N doc = createSimpleAllKindsDocument(builder);
        
        assertNotNull(doc);
        Model<N> model = context.getModel();
        assertNotNull(model);
        
        // TODO: see issue 41.  We need to test based on the content of
        // that issue, after adjusting the API javadoc (and implementations,
        // if necessary) to match.
        Object id = model.getNodeId(doc);
        N docElement = model.getFirstChildElement(doc);
        assertNotNull(docElement);
        assertEquals(id, model.getNodeId(model.getParent(docElement)));
        
        id = model.getNodeId(docElement);
        assertEquals(id, model.getNodeId(model.getFirstChildElement(doc)));
    }
    
    @Test
    public void idsAndRefs()
    {
        ProcessingContext<N> context = newProcessingContext();
        FragmentBuilder<N> builder = context.newFragmentBuilder();
        N doc = createIdsAndRefsTestDocument(builder);
        
        assertNotNull(doc);
        Model<N> model = context.getModel();
        assertNotNull(model);
        
        N node = model.getFirstChildElement(doc); // doc
        node = model.getFirstChildElement(node); // e1
        node = model.getFirstChildElement(node); // e2
        
        N idNode = model.getFirstChildElement(node); // e3
        assertTrue(model.isId(idNode));
        N attr = model.getAttribute(idNode, XMLConstants.NULL_NS_URI, "id");
        assertTrue(model.isId(attr));
        
        N idRefNode = model.getNextSibling(idNode); // e4
        assertTrue(model.isIdRefs(idRefNode));
        attr = model.getAttribute(idRefNode, XMLConstants.NULL_NS_URI, "ref");
        assertTrue(model.isIdRefs(attr));

        idNode = model.getNextSibling(idRefNode); // e5
        assertTrue(model.isId(idNode));
        attr = model.getAttribute(idNode, XMLConstants.XML_NS_URI, "id");
        assertTrue(model.isId(attr));
    }
    
    @Test
    public void attributes()
    {
        ProcessingContext<N> context = newProcessingContext();
        FragmentBuilder<N> builder = context.newFragmentBuilder();
        N doc = createSimpleAllKindsDocument(builder);
        
        assertNotNull(doc);
        Model<N> model = context.getModel();
        assertNotNull(model);
        
        // contract seems less than well-documented.
        // the rule is complex: on an element, getAttributeNames never
        // returns null, but may return an empty iterable.  on any other
        // node type, it always returns null.
        // getAttributeStringValue may return null.
        // it should not return anything for a "namespace attribute",
        // when a tree model provides such things.
        
        Iterable<QName> attributes = model.getAttributeNames(doc, false);
        assertNull(attributes);
        assertNull(model.getAttributeStringValue(doc, "", "xyzzy"));
        N node = model.getFirstChildElement(doc);
        assertNotNull(node);
        
        attributes = model.getAttributeNames(node, false);
        assertNotNull(attributes);
        // TODO: we really need a test that has an element with no attributes
        int count = 0;
        for (QName name : attributes)
        {
            assertNotNull(name.getLocalPart());
            assertNotNull(name.getNamespaceURI());
            assertNotNull(name.getPrefix());
            count++;
        }
        assertEquals(1, count);
        assertEquals("value", model.getAttributeStringValue(node, "", "att"));
        
        node = model.getFirstChild(node); // comment
        attributes = model.getAttributeNames(node, false);
        assertNull(attributes);
        assertNull(model.getAttributeStringValue(node, "", "att"));
        
        node = model.getNextSibling(node); // text
        attributes = model.getAttributeNames(node, false);
        assertNull(attributes);
        assertNull(model.getAttributeStringValue(node, "", "att"));
        
        node = model.getNextSibling(node); // pi
        attributes = model.getAttributeNames(node, false);
        assertNull(attributes);
        assertNull(model.getAttributeStringValue(node, "", "att"));
    }
    
    @Test
    public void namespaces()
    {
        ProcessingContext<N> context = newProcessingContext();
        FragmentBuilder<N> builder = context.newFragmentBuilder();
        N doc = createSimpleAllKindsDocument(builder);
        
        assertNotNull(doc);
        Model<N> model = context.getModel();
        assertNotNull(model);
        
        N docEl = model.getFirstChildElement(doc);
        // there's only one namespace declared; we shouldn't have a problem.
        assertEquals("ns", model.getNamespaceForPrefix(docEl, "ns"));
        for (String nsName : model.getNamespaceNames(docEl, false))
        {
            assertEquals("ns", nsName);
        }
        for (NamespaceBinding binding : model.getNamespaceBindings(docEl))
        {
            assertEquals("ns", binding.getPrefix());
            assertEquals("ns", binding.getNamespaceURI());
        }
    }
    
    @Test
    public void relationships()
    {
        ProcessingContext<N> context = newProcessingContext();
        FragmentBuilder<N> builder = context.newFragmentBuilder();
        N doc = createSimpleAllKindsDocument(builder);
        
        assertNotNull(doc);
        Model<N> model = context.getModel();
        assertNotNull(model);
        
        assertFalse(model.hasAttributes(doc));
        assertFalse(model.hasNamespaces(doc));
        assertTrue(model.hasChildren(doc));
        assertFalse(model.hasParent(doc));
        assertFalse(model.hasPreviousSibling(doc));
        assertFalse(model.hasNextSibling(doc));

        N elem = model.getFirstChildElement(doc);
        assertNotNull(elem);
        assertTrue(model.hasAttributes(elem));
        assertTrue(model.hasNamespaces(elem));
        assertTrue(model.hasChildren(elem));
        assertTrue(model.hasParent(elem));
        assertFalse(model.hasPreviousSibling(elem));
        assertFalse(model.hasNextSibling(elem));
        
        N node = model.getAttribute(elem, "", "att");
        assertNotNull(node);
        assertFalse(model.hasAttributes(node));
        assertFalse(model.hasNamespaces(node));
        assertFalse(model.hasChildren(node));
        assertTrue(model.hasParent(node));
        assertFalse(model.hasPreviousSibling(node));
        assertFalse(model.hasNextSibling(node));
        
        node = getNamespaceNode(model, elem, "ns");
        assertNotNull(node);
        assertFalse(model.hasAttributes(node));
        assertFalse(model.hasNamespaces(node));
        assertFalse(model.hasChildren(node));
        assertTrue(model.hasParent(node));
        assertFalse(model.hasPreviousSibling(node));
        assertFalse(model.hasNextSibling(node));
        
        node = model.getFirstChild(elem);
        assertNotNull(node); // comment node
        assertFalse(model.hasAttributes(node));
        assertFalse(model.hasNamespaces(node));
        assertFalse(model.hasChildren(node));
        assertTrue(model.hasParent(node));
        assertFalse(model.hasPreviousSibling(node));
        assertTrue(model.hasNextSibling(node));
        
        node = model.getNextSibling(node);
        assertNotNull(node); // text
        assertFalse(model.hasAttributes(node));
        assertFalse(model.hasNamespaces(node));
        assertFalse(model.hasChildren(node));
        assertTrue(model.hasParent(node));
        assertTrue(model.hasPreviousSibling(node));
        assertTrue(model.hasNextSibling(node));
        
        node = model.getNextSibling(node);
        assertNotNull(node); // pi
        assertFalse(model.hasAttributes(node));
        assertFalse(model.hasNamespaces(node));
        assertFalse(model.hasChildren(node));
        assertTrue(model.hasParent(node));
        assertTrue(model.hasPreviousSibling(node));
        assertFalse(model.hasNextSibling(node));
    }
    
    @Test
    public void names()
    {
        ProcessingContext<N> context = newProcessingContext();
        FragmentBuilder<N> builder = context.newFragmentBuilder();
        N doc = createSimpleAllKindsDocument(builder);
        
        assertNotNull(doc);
        Model<N> model = context.getModel();
        assertNotNull(model);
        
        // docs have no name
        assertNull(model.getNamespaceURI(doc));
        assertNull(model.getLocalName(doc));
        assertNull(model.getPrefix(doc));
        
        N el = model.getFirstChildElement(doc);
        assertNotNull(el);
        assertEquals("", model.getNamespaceURI(el));
        assertEquals("doc", model.getLocalName(el));
        assertEquals("", model.getPrefix(el));
        
        N n = model.getAttribute(el, "", "att");
        assertNotNull(n);
        assertEquals("", model.getNamespaceURI(n));
        assertEquals("att", model.getLocalName(n));
        assertEquals("", model.getPrefix(n));
        
        // this looks weird, but it's all according to spec.
        // alas.
        n = getNamespaceNode(model, el, "ns");
        assertNotNull(n);
        assertEquals(XMLConstants.NULL_NS_URI, model.getNamespaceURI(n));
        assertEquals("ns", model.getLocalName(n));
        assertEquals(XMLConstants.DEFAULT_NS_PREFIX, model.getPrefix(n));
        
        // comments have no name
        n = model.getFirstChild(el);
        assertNotNull(n);
        assertNull(model.getNamespaceURI(n));
        assertNull(model.getLocalName(n));
        assertNull(model.getPrefix(n));
        
        // text nodes have no name
        n = model.getNextSibling(n);
        assertNotNull(n);
        assertNull(model.getNamespaceURI(n));
        assertNull(model.getLocalName(n));
        assertNull(model.getPrefix(n));
        
        n = model.getNextSibling(n);
        assertNotNull(n);
        assertEquals("", model.getNamespaceURI(n));
        assertEquals("target", model.getLocalName(n));
        assertEquals("", model.getPrefix(n));
    }
    
    @Test
    public void values()
    {
        ProcessingContext<N> context = newProcessingContext();
        FragmentBuilder<N> builder = context.newFragmentBuilder();
        N doc = createSimpleAllKindsDocument(builder);
        
        assertNotNull(doc);
        Model<N> model = context.getModel();
        assertNotNull(model);
        
        final String text = "text";
        
        assertEquals(text, model.getStringValue(doc));
        N e = model.getFirstChildElement(doc);
        assertEquals(text, model.getStringValue(e));
        
        N n = model.getAttribute(e, "", "att");
        assertEquals("value", model.getStringValue(n));
        
        // binding a prefix to itself is kinda bogus for testing
        n = getNamespaceNode(model, e, "ns");
        assertEquals("ns", model.getStringValue(n));
        
        n = model.getFirstChild(e);
        assertEquals("comment", model.getStringValue(n));
        
        n = model.getNextSibling(n);
        assertEquals(text, model.getStringValue(n));
        
        n = model.getNextSibling(n);
        assertEquals("data", model.getStringValue(n));
    }
    
    @Test
    public void uris()
    {
        // TODO: this is a terrible solution.  We have methods that
        // optionally return information or not; there's no telling
        // whether null is returned because the bridge is incapable,
        // or because the information never existed.  See issue 40.
        // the implication is that some nodes might return information,
        // while other nodes do not.
        ProcessingContext<N> context = newProcessingContext();
        FragmentBuilder<N> builder = context.newFragmentBuilder();
        N doc = createSimpleAllKindsDocument(builder);
        
        assertNotNull(doc);
        Model<N> model = context.getModel();
        assertNotNull(model);
        URI docURI = null;
        try { docURI = new URI(URI_PREFIX + SIMPLE_DOC); }
        catch (URISyntaxException u) { /* do nothing */ } 
        
        URI uri = model.getDocumentURI(doc);
        if (uri != null)
        {
            assertEquals(docURI, uri);
            if (model.getBaseURI(doc) != null)
            {
                assertEquals(uri, model.getBaseURI(doc)); // which means that all three are equal
                
                N e = model.getFirstChildElement(doc);
                assertNull(model.getDocumentURI(e));
                assertEquals(uri, model.getBaseURI(e));
                
                N n = model.getAttribute(e, "", "att");
                assertNull(model.getDocumentURI(n));
                assertEquals(uri, model.getBaseURI(n));
                
                n = getNamespaceNode(model, e, "ns");
                assertNull(model.getDocumentURI(n));
                assertEquals(uri, model.getBaseURI(n));
                
                n = model.getFirstChild(e);
                assertNull(model.getDocumentURI(n));
                assertEquals(uri, model.getBaseURI(n));
                
                n = model.getNextSibling(n);
                assertNull(model.getDocumentURI(n));
                assertEquals(uri, model.getBaseURI(n));
                
                n = model.getNextSibling(n);
                assertNull(model.getDocumentURI(n));
                assertEquals(uri, model.getBaseURI(n));
            }
        }
    }
    
    @Test
    public void matching()
    {
        // TODO
    }
    
//    /**
//     * Returns the namespace bindings associated with the node as a set or prefix/URI pairs.
//     * Corresponds to the <a href="http://www.w3.org/TR/xpath-datamodel/#acc-summ-namespace-bindings">
//     * dm:namespace-bindings</a> accessor.
//     * 
//     * Only includes prefix mappings which are explicit and local to the node.
//     * 
//     * @param node
//     *            The node under consideration.
//     * 
//     * @see http://www.w3.org/TR/xpath-datamodel/#acc-summ-namespace-bindings
//     */
//    Iterable<NamespaceBinding> getNamespaceBindings(N node);
//
//    /** Only reports on namespace declarations for the target node,
//     * not namespaces in scope for that node.
//     * 
//     * @param node the target node on which the namespace is declared.
//     * @param prefix the prefix (namespace name) for which the URI is desired.
//     * 
//     * @return the namespace URI declared for this prefix, or null if no such prefix
//     * mapping is declared on this node.
//     */
//    String getNamespaceForPrefix(N node, String prefix);
//
//    /**
//     * Returns the set of namespace names (prefixes) for a given node.
//     * 
//     * <br/>
//     * 
//     * This refers to the prefix mappings which are explicit and local to the node.
//     * 
//     * @param orderCanonical
//     *            Determines whether the names will be returned in canonical order (lexicographically by local name).
//     */
//    Iterable<String> getNamespaceNames(N node, boolean orderCanonical);
//
//    /**
//     * Deterimines whether the specified node matches the arguments.
//     * 
//     * @param node
//     *            The XML node.
//     * @param nodeKind
//     *            The node kind to match.
//     * @param namespaceURI
//     *            The namespace-uri to match.
//     * @param localName
//     *            The local-name to match.
//     */
//    boolean matches(N node, NodeKind nodeKind, String namespaceURI, String localName);
//
//    /**
//     * Determines whether the specified node matches in name.
//     * 
//     * @param node
//     *            The node being tested.
//     * @param namespaceURI
//     *            The namespace-uri part of the name.
//     * @param localName
//     *            The local-name part of the name.
//     * @return <code>true</code> if the node matches the arguments specified, otherwise <code>false</code>.
//     */
//    boolean matches(N node, String namespaceURI, String localName);
}
