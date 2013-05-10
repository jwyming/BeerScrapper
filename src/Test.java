import javax.swing.text.html.HTML;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: wukelvin
 * Date: 13-5-9
 * Time: 下午2:43
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args) {
//        for(int i = 'A'; i <= 'Z'; i++) {
//
//            System.out.println("asdfa====" + i + "======asdfasdf");
//        }
        String name = "Abbaye Bonne Esp&#233;rance 8&#176;&#45;1&#47;3L &#45;V";
        String price ="<b><Actinic:PRICES PROD_REF=\"1!BB20018\" RETAIL_PRICE_PROMPT=\"Price:\"> <br /> Price: (11 or fewer items) $6.00<br /> Price: (12 or more items) $5.00</Actinic:PRICES></b>";
        System.out.println(decodeName(name));
        System.out.println(decodePrice(price));
    }

//    public static String decode(String str){
//        String[] tmp = str.split(";&#|&#|;");
//        StringBuffer sb = new StringBuffer("");
//        for (int i=0; i<tmp.length; i++){
//            if (tmp[i].matches("\\d{5}")){
//                sb.append((char)Integer.parseInt(tmp[i]));
//            } else {
//                sb.append(tmp[i]);
//            }
//        }
//        return sb.toString();
//    }

    private static String decodeName(String originalStr) {
        Pattern p = Pattern.compile("&#(\\d+);");
        Matcher m = p.matcher(originalStr);
        StringBuffer sb = new StringBuffer();
        boolean result = m.find();
        while (result) {
            m.appendReplacement(sb, String.valueOf((char)Integer.parseInt(m.group().substring(2, m.group().length() -1))));
            result = m.find();
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private static String decodePrice(String originalStr) {
        Pattern p = Pattern.compile("Price:([^>]*?)<");
        Matcher m = p.matcher(originalStr);
        StringBuffer sb = new StringBuffer();
        boolean result = m.find();
        while (result) {
//            System.out.println(m.group());
            sb.append(m.group().substring(0, m.group().length() -1) + " ");
//            m.appendReplacement(sb, String.valueOf((char)Integer.parseInt(m.group().substring(2, m.group().length() -1))));
            result = m.find();
        }
//        m.appendTail(sb);
        return sb.toString();
//        return null;
    }
}
