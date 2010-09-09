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
package org.gxml.xs.exceptions;

import org.gxml.exceptions.PreCondition;
import org.gxml.xs.enums.SmOutcome;
import org.gxml.xs.facets.SmLengthFacetUOM;
import org.gxml.xs.facets.SmMinLength;


/**
 * xs:minLength
 */
@SuppressWarnings("serial")
public final class SmFacetMinLengthException extends SmFacetException
{
	private final SmMinLength<?> minLength;
	private final int actualLength;
	private final SmLengthFacetUOM uom;

	public SmFacetMinLengthException(final SmMinLength<?> minLength, final int actualLength, final SmLengthFacetUOM uom)
	{
		super(SmOutcome.CVC_MinLength);
		this.minLength = minLength;
		this.actualLength = actualLength;
		this.uom = PreCondition.assertArgumentNotNull(uom, "uom");
	}

	@Override
	public String getMessage()
	{
		final String localMessage;
		switch (uom)
		{
			case Characters:
			{
				localMessage = "The length of the value (" + actualLength + "), as measured in characters must be greater than or equal to " + minLength.getMinLength() + ".";
			}
			break;
			case Octets:
			{
				localMessage = "The length of the value (" + actualLength + "), as measured in octets of the binary data, must be greater than or equal to " + minLength.getMinLength() + ".";
			}
			break;
			case ListItems:
			{
				localMessage = "The length of the value (" + actualLength + "), as measured in list items, must be greater than or equal to " + minLength.getMinLength() + ".";
			}
			break;
			default:
			{
				throw new RuntimeException(uom.name());
			}
		}

		final StringBuilder message = new StringBuilder();
		message.append(getOutcome().getSection());
		message.append(".");
		message.append(getPartNumber());
		message.append(": ");
		message.append(localMessage);
		return message.toString();
	}
}