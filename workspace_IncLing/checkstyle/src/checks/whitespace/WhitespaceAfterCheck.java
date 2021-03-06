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
package checks.whitespace;

//import gov.nasa.jpf.annotation.Conditional;
import api.Check;
import api.DetailAST;
import api.TokenTypes;
import specifications.Configuration;

/**
 * <p>
 * Checks that a token is followed by whitespace, with the exception that it
 * does not check for whitespace after the semicolon of an empty for iterator.
 * Use Check {@link EmptyForIteratorPadCheck EmptyForIteratorPad} to validate
 * empty for iterators.
 * </p>
 * <p> By default the check will check the following tokens:
 *  {@link TokenTypes#COMMA COMMA},
 *  {@link TokenTypes#SEMI SEMI},
 *  {@link TokenTypes#TYPECAST TYPECAST}.
 * </p>
 * <p>
 * An example of how to configure the check is:
 * </p>
 * <pre>
 * &lt;module name="WhitespaceAfter"/&gt;
 * </pre>
 * <p> An example of how to configure the check for whitespace only after
 * {@link TokenTypes#COMMA COMMA} and {@link TokenTypes#SEMI SEMI} tokens is:
 * </p>
 * <pre>
 * &lt;module name="WhitespaceAfter"&gt;
 *     &lt;property name="tokens" value="COMMA, SEMI"/&gt;
 * &lt;/module&gt;
 * </pre>
 * @author Oliver Burn
 * @author Rick Giles
 * @version 1.0
 */
public class WhitespaceAfterCheck
    extends Check
{
//	@Conditional
//	private static boolean WhitespaceAfter = true;
	
	@Override
	public boolean isEnabled() {
		return Configuration.WhitespaceAfter;
	}
    /**
     * A key is pointing to the warning message text in "messages.properties"
     * file.
     */
    public static final String WS_NOT_FOLLOWED = "ws.notFollowed";

    @Override
    public int[] getDefaultTokens()
    {
        return new int[] {
            TokenTypes.COMMA,
            TokenTypes.SEMI,
            TokenTypes.TYPECAST,
        };
    }

    @Override
    public int[] getAcceptableTokens()
    {
        return new int[] {
            TokenTypes.COMMA,
            TokenTypes.SEMI,
            TokenTypes.TYPECAST,
        };
    }

    @Override
    public void visitToken(DetailAST ast)
    {
        final Object[] message;
        final DetailAST targetAST;
        if (ast.getType() == TokenTypes.TYPECAST) {
            targetAST = ast.findFirstToken(TokenTypes.RPAREN);
            // TODO: i18n
            message = new Object[]{"cast"};
        }
        else {
            targetAST = ast;
            message = new Object[]{ast.getText()};
        }
        final String line = getLine(targetAST.getLineNo() - 1);
        final int after =
            targetAST.getColumnNo() + targetAST.getText().length();

        if (after < line.length()) {

            final char charAfter = line.charAt(after);
            if ((targetAST.getType() == TokenTypes.SEMI)
                && ((charAfter == ';') || (charAfter == ')')))
            {
                return;
            }
            if (!Character.isWhitespace(charAfter)) {
                //empty FOR_ITERATOR?
                if (targetAST.getType() == TokenTypes.SEMI) {
                    final DetailAST sibling =
                        targetAST.getNextSibling();
                    if ((sibling != null)
                        && (sibling.getType() == TokenTypes.FOR_ITERATOR)
                        && (sibling.getChildCount() == 0))
                    {
                        return;
                    }
                }
                log(targetAST.getLineNo(),
                    targetAST.getColumnNo() + targetAST.getText().length(),
                    WS_NOT_FOLLOWED,
                    message);
            }
        }
    }
}
