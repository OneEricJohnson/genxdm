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
package org.genxdm.xs.exceptions;

import org.genxdm.exceptions.PreCondition;
import org.genxdm.xs.enums.ValidationOutcome;
import org.genxdm.xs.types.SimpleType;

@SuppressWarnings("serial")
public final class SimpleTypeException extends SchemaException
{
    private final String initialValue;
    private final SimpleType type;

    public SimpleTypeException(final String initialValue, final SimpleType type, final SchemaException cause)
    {
        super(ValidationOutcome.CVC_Simple_Type, "?", cause);
        this.initialValue = PreCondition.assertArgumentNotNull(initialValue, "initialValue");
        this.type = type;
    }

    @Override
    public String getMessage()
    {
        final String name;
        if (null != type)
        {
            name = type.isAnonymous() ? "{anonymous}" : type.getName().toString();
        }
        else
        {
            name = "{unknown}";
        }
        final String localMessage = "The initial value '" + initialValue + "' is not valid with respect to the simple type definition '" + name + "'.";

        final StringBuilder message = new StringBuilder();
        // message.append(getOutcome().getSection());
        // message.append(".");
        // message.append(getPartNumber());
        // message.append(": ");
        message.append(localMessage);
        return message.toString();
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj instanceof SimpleTypeException)
        {
            final SimpleTypeException other = (SimpleTypeException)obj;
            return initialValue.equals(other.initialValue) && type.getName().equals(other.type.getName());
        }
        else
        {
            return false;
        }
    }
}
