////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code for adherence to a set of rules.
// Copyright (C) 2001-2015 the original author or authors.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
////////////////////////////////////////////////////////////////////////////////
package checks.coding;

//import gov.nasa.jpf.annotation.Conditional;
import api.Check;
import api.DetailAST;
import api.TokenTypes;
import specifications.Configuration;

/**
 * <p>
 * Check that the <code>default</code> is after all the <code>case</code>s
 * in a <code>switch</code> statement.
 * </p>
 * <p>
 * Rationale: Java allows <code>default</code> anywhere within the
 * <code>switch</code> statement. But if it comes after the last
 * <code>case</code> then it is more readable.
 * </p>
 * <p>
 * An example of how to configure the check is:
 * </p>
 * <pre>
 * &lt;module name="DefaultComesLast"/&gt;
 * </pre>
 * @author o_sukhodolsky
 */
public class DefaultComesLastCheck extends Check
{
//	@Conditional
//	private static boolean DefaultComesLast = true;
	public boolean isEnabled() {
		return Configuration.DefaultComesLast;
	}
    /**
     * A key is pointing to the warning message text in "messages.properties"
     * file.
     */
    public static final String MSG_KEY = "default.comes.last";

    /** Creates new instance of the check. */
    public DefaultComesLastCheck()
    {
        // do nothing
    }

    @Override
    public int[] getDefaultTokens()
    {
        return new int[] {
            TokenTypes.LITERAL_DEFAULT,
        };
    }

    @Override
    public int[] getAcceptableTokens()
    {
        return getDefaultTokens();
    }

    @Override
    public void visitToken(DetailAST ast)
    {
        final DetailAST defaultGroupAST = ast.getParent();
        //default keywords used in annotations too - not what we're
        //interested in
        if (defaultGroupAST.getType() != TokenTypes.ANNOTATION_FIELD_DEF
                && defaultGroupAST.getType() != TokenTypes.MODIFIERS)
        {
            final DetailAST switchAST = defaultGroupAST.getParent();
            final DetailAST lastGroupAST =
                switchAST.getLastChild().getPreviousSibling();

            if ((defaultGroupAST.getLineNo() != lastGroupAST.getLineNo())
                || (defaultGroupAST.getColumnNo()
                    != lastGroupAST.getColumnNo()))
            {
                log(ast, MSG_KEY);
            }
        }
    }
}
