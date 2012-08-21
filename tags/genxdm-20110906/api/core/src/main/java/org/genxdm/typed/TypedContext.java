/*
 * Copyright (c) 2009-2011 TIBCO Software Inc.
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
package org.genxdm.typed;

import java.net.URI;

import org.genxdm.ProcessingContext;
import org.genxdm.typed.io.SequenceBuilder;
import org.genxdm.typed.io.TypedDocumentHandlerFactory;
import org.genxdm.typed.types.AtomBridge;
import org.genxdm.typed.types.TypesBridge;
import org.genxdm.typed.variant.VariantBridge;
import org.genxdm.xs.Schema;

/**
 * A context for schema-related processing and state.
 * 
 * TypedContext extends Schema so that it can be initialized with the schemas
 * necessary for validating target instances.  It also extends
 * TypedDocumentHandlerFactory in order to permit validation on parse.
 * 
 * @param <N>
 *            The node handle.
 * @param <A>
 *            The atom handle.
 */
public interface TypedContext<N, A> 
    extends Schema, TypedDocumentHandlerFactory<N, A>
{
    /**
     * Returns the bridge used for atom interaction.  Atoms are typed values.
     * 
     * @return the AtomBridge associated with this typed context; never null.
     */
    AtomBridge<A> getAtomBridge();

    /**
     * Returns the bridge used for type interaction.  Types means both
     * type names and the org.genxdm.xs abstractions used to model types.
     * 
     * @return the TypesBridge associated with this typed context; never null.
     */
    TypesBridge getTypesBridge();

    /**
     * Returns the {@link TypedModel} for navigating the document model.
     * 
     * @return the TypedModel associated with this typed context; never null.
     */
    TypedModel<N, A> getModel();
    
    /**
     * Returns the associated ProcessingContext.  The ProcessingContext
     * is the 'parent' of this typed context.
     * 
     * @return the ProcessingContext from which this typed context was originally
     * obtained; never null.
     */
    ProcessingContext<N> getProcessingContext();
    
    /**
     * Returns the associated VariantBridge.  A 'variant' is a means of packaging
     * co-variant returns in a way that won't make Java choke.
     * 
     * @return the VariantBridge associated with this typed context; never null.
     */
    VariantBridge<N, A> getVariantBridge();

    /**
     * Determines whether this processing context is currently in a locked state.
     * 
     * @return true if lock() has been called; false otherwise.
     */
    boolean isLocked();

    /** Lock this processing context, preventing registry of additional
     * schema components.
     * 
     * The processing context is in an unlocked state after it has been created. 
     * While in this state an implementation is not required to provide safe 
     * multi-threaded access. Locking the processing context guarantees that 
     * the processing context is safe for access by different threads. 
     */
    void lock();

    /**
     * Returns a cursor for navigating the data model.
     * 
     * @param node the context node over which the cursor is initially
     * positioned.  If null, an exception will be thrown.
     * 
     * @return a new TypedCursor positioned over the supplied node.
     */
    TypedCursor<N, A> newCursor(N node);
    
    /** Create a new SequenceBuilder for constructing type-annotated trees
     * in memory, with typed values.
     * 
     * @return a new SequenceBuilder capable of generating typed and type-annotated
     * trees compatible with this typed context, never null.
     */
    SequenceBuilder<N, A> newSequenceBuilder();
    
    /** Validate or revalidate a tree in memory.
     * 
     * @param source the starting point for validation; must not be null.
     * @param validator the (partially-initialized) validation handler to be
     * used; must not be null.
     * @param schemaNamespace the URI of the schema namespace to be used for
     * validation; may not be null, but may be the empty string (global namespace).
     * @return a node representing the equivalent to the supplied source node,
     * as the base of a validated (type-annotated and type-valued) tree; never
     * null.  If validation has failed, the tree may not be typed.  The returned
     * node <em>may</em>, but is not required to be the same node supplied as an
     * argument, suitably modified with type annotations and typed values, if
     * the bridge supports this form of mutation; otherwise, the bridge may
     * create and return a tree comparable to the supplied tree, with added
     * type annotations and typed values.
     */
    N validate(N source, ValidationHandler<A> validator, URI schemaNamespace);

}