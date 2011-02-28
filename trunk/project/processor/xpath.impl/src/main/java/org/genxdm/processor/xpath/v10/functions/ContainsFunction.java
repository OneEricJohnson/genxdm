/**
 * Portions copyright (c) 1998-1999, James Clark : see copyingjc.txt for
 * license details
 * Portions copyright (c) 2002, Bill Lindsey : see copying.txt for license
 * details
 * 
 * Portions copyright (c) 2009-2010 TIBCO Software Inc.
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
package org.genxdm.processor.xpath.v10.functions;

import org.genxdm.Model;
import org.genxdm.xpath.v10.expressions.ConvertibleBooleanExpr;
import org.genxdm.xpath.v10.expressions.ConvertibleExpr;
import org.genxdm.xpath.v10.expressions.ExprContextDynamic;
import org.genxdm.xpath.v10.expressions.ExprContextStatic;
import org.genxdm.xpath.v10.expressions.ExprException;
import org.genxdm.xpath.v10.expressions.StringExpr;

/**
 * Represents the XPath Function: boolean contains(string, string)
 * 
 * The contains function returns true if the first argument string contains the second argument string, and otherwise returns false.
 */
public final class ContainsFunction 
    extends Function2
{

	ConvertibleExpr makeCallExpr(final ConvertibleExpr e1, final ConvertibleExpr e2, final ExprContextStatic statEnv)
	{
		final StringExpr se1 = e1.makeStringExpr(statEnv);
		final StringExpr se2 = e2.makeStringExpr(statEnv);

		return new ConvertibleBooleanExpr()
		{
			public <N> boolean booleanFunction(Model<N> model, final N node, final ExprContextDynamic<N> dynEnv) throws ExprException
			{
				return se1.stringFunction(model, node, dynEnv).indexOf(se2.stringFunction(model, node, dynEnv)) >= 0;
			}
		};
	}
}