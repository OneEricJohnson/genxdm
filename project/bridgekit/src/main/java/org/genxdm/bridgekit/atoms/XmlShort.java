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
package org.genxdm.bridgekit.atoms;

import org.genxdm.xs.types.NativeType;

/**
 * Corresponds to the W3C XML Schema <a href="http://www.w3.org/TR/xmlschema-2/#short">short</a>.
 */
public final class XmlShort 
    extends XmlAbstractAtom
{
    public XmlShort(final short value)
    {
        this.shortValue = value;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj instanceof XmlShort)
            return shortValue == ((XmlShort)obj).shortValue;
        return false;
    }

    public String getC14NForm()
    {
        return Short.toString(shortValue);
    }

    public NativeType getNativeType()
    {
        return NativeType.SHORT;
    }

    public short getShortValue()
    {
        return shortValue;
    }

    @Override
    public int hashCode()
    {
        return shortValue;
    }

    public boolean isWhiteSpace()
    {
        return false;
    }

    private final short shortValue;
}
