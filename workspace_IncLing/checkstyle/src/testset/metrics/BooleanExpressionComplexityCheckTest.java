////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code for adherence to a set of rules.
// Copyright (C) 2001-2018 the original author or authors.
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

package testset.metrics;

import static checks.metrics.BooleanExpressionComplexityCheck.MSG_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import antlr.CommonHiddenStreamToken;
import api.DetailAST;
import api.TokenTypes;
import checks.metrics.BooleanExpressionComplexityCheck;
import checkstyle.DefaultConfiguration;
//import com.puppycrawl.tools.checkstyle.AbstractModuleTestSupport;
//import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
//import com.puppycrawl.tools.checkstyle.api.DetailAST;
//import com.puppycrawl.tools.checkstyle.api.TokenTypes;
//import com.puppycrawl.tools.checkstyle.utils.CommonUtil;
import testset.checkstyle.AbstractModuleTestSupport;
import utils.CommonUtil;

public class BooleanExpressionComplexityCheckTest extends AbstractModuleTestSupport {

    @Override
    protected String getPackageLocation() {
        return "com/puppycrawl/tools/checkstyle/checks/metrics/booleanexpressioncomplexity";
    }

    @Test
    public void test() throws Exception {
        final DefaultConfiguration checkConfig =
            createModuleConfig(BooleanExpressionComplexityCheck.class);

        final String[] expected = {
            "13:9: " + getCheckMessage(MSG_KEY, 4, 3),
            "29:87: " + getCheckMessage(MSG_KEY, 4, 3),
            "39:9: " + getCheckMessage(MSG_KEY, 6, 3),
            "45:34: " + getCheckMessage(MSG_KEY, 4, 3),
            "47:34: " + getCheckMessage(MSG_KEY, 4, 3),
        };

        verify(checkConfig, getPath("InputBooleanExpressionComplexity.java"), expected);
    }

    @Test
    public void testNoBitwise() throws Exception {
        final DefaultConfiguration checkConfig =
            createModuleConfig(BooleanExpressionComplexityCheck.class);
        checkConfig.addAttribute("max", "5");
        checkConfig.addAttribute("tokens", "BXOR,LAND,LOR");

        final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;

        verify(checkConfig, getPath("InputBooleanExpressionComplexity.java"), expected);
    }

    @Test
    public void testNullPointerException() throws Exception {
        final DefaultConfiguration checkConfig =
            createModuleConfig(BooleanExpressionComplexityCheck.class);

        final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;

        verify(checkConfig, getPath("InputBooleanExpressionComplexityNPE.java"), expected);
    }

//    @Test
//    public void testWrongToken() {
//        final BooleanExpressionComplexityCheck booleanExpressionComplexityCheckObj =
//            new BooleanExpressionComplexityCheck();
//        final DetailAST ast = new DetailAST();
//        ast.initialize(new CommonHiddenStreamToken(TokenTypes.INTERFACE_DEF, "interface"));
//        try {
//            booleanExpressionComplexityCheckObj.visitToken(ast);
//            fail("exception expected");
//        }
//        catch (IllegalArgumentException ex) {
//            assertEquals("Invalid exception message",
//                "Unknown type: interface[0x-1]", ex.getMessage());
//        }
//    }

}
