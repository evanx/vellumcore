package vellum.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static vellum.util.Numbers.formatMoney;
import static vellum.util.Numbers.formatMoneyAlt;
import static vellum.util.Numbers.newBigDecimal;

public class NumbersTest {

    static Logger logger = LoggerFactory.getLogger(NumbersTest.class);

    @Test
    public void test() throws Exception {
        System.out.println(formatMoneyAlt((double) 514329.455000001));
        System.out.println(formatMoneyAlt((double) 514329.455));
        System.out.println(formatMoney(514329.455000001));
        System.out.println(formatMoney(514329.455));
        System.out.println(newBigDecimal(514329.455000001, 2).toString());
        System.out.println(newBigDecimal(514329.455, 2).toString());
    }
}
