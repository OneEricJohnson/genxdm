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

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import org.gxml.exceptions.PreCondition;
import org.gxml.typed.types.AtomBridge;
import org.gxml.xs.components.SmEnumeration;
import org.gxml.xs.enums.SmDerivationMethod;
import org.gxml.xs.enums.SmScopeExtent;
import org.gxml.xs.enums.SmWhiteSpacePolicy;
import org.gxml.xs.exceptions.SmDatatypeException;
import org.gxml.xs.facets.SmFacet;
import org.gxml.xs.facets.SmFacetKind;
import org.gxml.xs.facets.SmPattern;
import org.gxml.xs.resolve.SmPrefixResolver;
import org.gxml.xs.types.SmNativeType;
import org.gxml.xs.types.SmSequenceTypeVisitor;
import org.gxml.xs.types.SmSimpleType;

public class IntegerDerivedType<A> extends AbstractAtomType<A>
{
	private static final BigInteger UNSIGNED_LONG_MAX_INCLUSIVE = BigInteger.valueOf(Long.MAX_VALUE).subtract(BigInteger.valueOf(Long.MIN_VALUE));
	private static final BigInteger UNSIGNED_INT_MAX_INCLUSIVE = BigInteger.valueOf(Integer.MAX_VALUE).subtract(BigInteger.valueOf(Integer.MIN_VALUE));
	private static final BigInteger UNSIGNED_SHORT_MAX_INCLUSIVE = BigInteger.valueOf(Short.MAX_VALUE).subtract(BigInteger.valueOf(Short.MIN_VALUE));
	private static final BigInteger UNSIGNED_BYTE_MAX_INCLUSIVE = BigInteger.valueOf(Byte.MAX_VALUE).subtract(BigInteger.valueOf(Byte.MIN_VALUE));
	private final SmNativeType nativeType;

	public IntegerDerivedType(final SmNativeType nativeType, final QName name, final SmSimpleType<A> baseType, final AtomBridge<A> atomBridge)
	{
		super(name, baseType, atomBridge);
		this.nativeType = PreCondition.assertArgumentNotNull(nativeType, "nativeType");
	}

	public void accept(SmSequenceTypeVisitor<A> visitor)
	{
		// TODO Auto-generated method stub
		throw new AssertionError("TODO");
	}

	public boolean derivedFrom(String namespace, String name, Set<SmDerivationMethod> derivationMethods)
	{
		// TODO Auto-generated method stub
		throw new AssertionError("TODO");
	}

	public Iterable<SmEnumeration<A>> getEnumerations()
	{
		// TODO Auto-generated method stub
		throw new AssertionError("TODO");
	}

	public SmFacet<A> getFacetOfKind(final SmFacetKind facetKind)
	{
		return null;
	}

	public Iterable<SmFacet<A>> getFacets()
	{
		return Collections.emptyList();
	}

	public Set<SmDerivationMethod> getFinal()
	{
		return Collections.emptySet();
	}

	public SmNativeType getNativeType()
	{
		return nativeType;
	}

	public Iterable<SmPattern> getPatterns()
	{
		// TODO Auto-generated method stub
		throw new AssertionError("TODO");
	}

	public SmScopeExtent getScopeExtent()
	{
		// TODO Auto-generated method stub
		throw new AssertionError("TODO");
	}

	public SmWhiteSpacePolicy getWhiteSpacePolicy()
	{
		return SmWhiteSpacePolicy.COLLAPSE;
	}

	public boolean hasEnumerations()
	{
		return false;
	}

	public boolean hasFacetOfKind(final SmFacetKind facetKind)
	{
		return false;
	}

	public boolean hasFacets()
	{
		return false;
	}

	public boolean hasPatterns()
	{
		return false;
	}

	public boolean isAbstract()
	{
		return false;
	}

	public boolean isID()
	{
		return false;
	}

	public boolean isIDREF()
	{
		return false;
	}

	public List<A> validate(final String initialValue) throws SmDatatypeException
	{
		try
		{
			// Note that trimming eliminates a leading plus-sign, but leaves leading minus-sign.
			final String trimmed = trim(initialValue);
			final BigInteger integerValue = new BigInteger(trimmed);
			switch (nativeType)
			{
				case UNSIGNED_LONG:
				{
					if (integerValue.signum() >= 0 && integerValue.compareTo(UNSIGNED_LONG_MAX_INCLUSIVE) <= 0)
					{
						return atomBridge.wrapAtom(atomBridge.createIntegerDerived(integerValue, nativeType));
					}
					else
					{
						throw new SmDatatypeException(trimmed, this);
					}
				}
				case UNSIGNED_INT:
				{
					if (integerValue.signum() >= 0 && integerValue.compareTo(UNSIGNED_INT_MAX_INCLUSIVE) <= 0)
					{
						return atomBridge.wrapAtom(atomBridge.createIntegerDerived(integerValue.longValue(), nativeType));
					}
					else
					{
						throw new SmDatatypeException(trimmed, this);
					}
				}
				case UNSIGNED_SHORT:
				{
					if (integerValue.signum() >= 0 && integerValue.compareTo(UNSIGNED_SHORT_MAX_INCLUSIVE) <= 0)
					{
						return atomBridge.wrapAtom(atomBridge.createIntegerDerived(integerValue.intValue(), nativeType));
					}
					else
					{
						throw new SmDatatypeException(trimmed, this);
					}
				}
				case UNSIGNED_BYTE:
				{
					if (integerValue.signum() >= 0 && integerValue.compareTo(UNSIGNED_BYTE_MAX_INCLUSIVE) <= 0)
					{
						return atomBridge.wrapAtom(atomBridge.createIntegerDerived(integerValue.shortValue(), nativeType));
					}
					else
					{
						throw new SmDatatypeException(trimmed, this);
					}
				}
				case NEGATIVE_INTEGER:
				{
					if (integerValue.signum() < 0)
					{
						return atomBridge.wrapAtom(atomBridge.createIntegerDerived(integerValue, nativeType));
					}
					else
					{
						throw new SmDatatypeException(trimmed, this);
					}
				}
				case POSITIVE_INTEGER:
				{
					if (integerValue.signum() > 0)
					{
						return atomBridge.wrapAtom(atomBridge.createIntegerDerived(integerValue, SmNativeType.POSITIVE_INTEGER));
					}
					else
					{
						throw new SmDatatypeException(trimmed, this);
					}
				}
				case NON_POSITIVE_INTEGER:
				{
					if (integerValue.signum() <= 0)
					{
						return atomBridge.wrapAtom(atomBridge.createIntegerDerived(integerValue, nativeType));
					}
					else
					{
						throw new SmDatatypeException(trimmed, this);
					}
				}
				case NON_NEGATIVE_INTEGER:
				{
					if (integerValue.signum() >= 0)
					{
						return atomBridge.wrapAtom(atomBridge.createIntegerDerived(integerValue, nativeType));
					}
					else
					{
						throw new SmDatatypeException(trimmed, this);
					}
				}
				default:
				{
					throw new AssertionError(nativeType);
				}
			}
		}
		catch (final NumberFormatException e)
		{
			throw new SmDatatypeException(initialValue, this);
		}
	}

	public List<A> validate(String initialValue, SmPrefixResolver resolver) throws SmDatatypeException
	{
		// TODO Auto-generated method stub
		throw new AssertionError("TODO");
	}
}
