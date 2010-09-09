/**
 * Copyright (c) 2009-2010 TIBCO Software Inc.
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
package org.gxml.bridgekit.xs;

import org.gxml.xs.enums.SmNodeKind;
import org.gxml.xs.types.SmNodeType;

abstract class AbstractLeafNodeType<A> extends AbstractPrimeExcludingNoneType<A> implements SmNodeType<A>
{
	protected final SmCache<A> cache;
	private final SmNodeKind nodeKind;

	public AbstractLeafNodeType(final SmNodeKind nodeKind, final SmCache<A> cache)
	{
		switch (nodeKind)
		{
			case TEXT:
			case COMMENT:
			case PROCESSING_INSTRUCTION:
			case NAMESPACE:
			{
				this.nodeKind = nodeKind;
			}
			break;
			default:
			{
				throw new AssertionError(nodeKind);
			}
		}
		this.cache = cache;
	}

	public final SmNodeKind getNodeKind()
	{
		return nodeKind;
	}

	public boolean isNative()
	{
		return false;
	}

	public final boolean isChoice()
	{
		return false;
	}

	@Override
	public final String toString()
	{
		switch (nodeKind)
		{
			case TEXT:
			{
				return "text()";
			}
			case COMMENT:
			{
				return "comment()";
			}
			case PROCESSING_INSTRUCTION:
			{
				return "processing-instruction()";
			}
			case NAMESPACE:
			{
				return "namespace()";
			}
			default:
			{
				throw new AssertionError(nodeKind.toString());
			}
		}
	}
}
