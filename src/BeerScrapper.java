import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: wukelvin
 * Date: 13-5-9
 * Time: 上午1:37
 * To change this template use File | Settings | File Templates.
 */
public class BeerScrapper {
    public static void main(String[] args) throws Exception {
        File tempCsv = new File("beers.csv");
        if(tempCsv.exists()) {
            tempCsv.delete();
            tempCsv.createNewFile();
        }
//        CsvWriter csvWriter = new CsvWriter(new BufferedWriter(new OutputStreamWriter(
//                new FileOutputStream(tempCsv), "UTF-8")), ',');
        CsvWriter csvWriter = new CsvWriter(new FileOutputStream(tempCsv), ',', Charset.forName("UTF8"));
        for(int i = 'A'; i <= 'Z'; i++) {
//        for(int i = 'A'; i <= 'Z'; i++) {
            String url;

            if((char) i == 'D') {
                url = "http://shop.belgianshop.com/acatalog/copy_of__" + ((char) i) + "_.html";
            } else if((char) i == 'I') {
                continue;
            } else {
                url = "http://shop.belgianshop.com/acatalog/_" + ((char) i) + "_.html";
            }
            System.out.println(url);
            URL server = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) server.openConnection();
            connection.setConnectTimeout(60000);
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String currLine = "";
            while ((currLine = reader.readLine()) != null) {
                if(currLine.length() > 1000 && currLine.contains("<b>")) {
                    Pattern p = Pattern.compile("<b>(.*?)<\\/b>");
                    Matcher m = p.matcher(currLine);
                    StringBuffer sb = new StringBuffer();
                    boolean result = m.find();
                    int matchNo = 0;
                    while (result) {
                        if(matchNo%2 == 0) {// beer name
//                            System.out.println(decodeName(m.group().substring(3, m.group().length() - 4)));
                            csvWriter.write(decodeName(m.group().substring(3, m.group().length() - 4)));
                        } else { // price
                            csvWriter.write(decodePrice(m.group()));
                            csvWriter.endRecord();
                        }
                        result = m.find();
                        matchNo++;
                    }
                }
            }
            connection.disconnect();
            reader.close();
        }
        csvWriter.flush();
        csvWriter.close();

    }


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
