import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.iot.gateway.exceptions.MessageParseException;
import org.junit.Test;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * TODO:
 *
 * @author likain
 * @since 2023/5/29 11:03
 */
public class TestApp {

    @Test
    public void test() {
        // JSONObject obj = new JSONObject();
        // obj.put("A0", "119.45.119.119");
        // System.out.println("01 01\n" + obj);

        // System.out.println(UUID.randomUUID().toString().substring(0, 8));
        // String a = "hello";
        // byte[] bytes = a.getBytes(StandardCharsets.UTF_8);
        // System.out.println(bytes);

        // String test = "hhh";
        // System.out.println(test.split(",").length);

        // String info = "01 01\nhello";
        // String start = info.split("\n")[0];
        // info = info.substring(start.length() + 1);
        // System.out.println(info);

        // String info =
        //         "{\n" +
        //         "\"A0\":\"hA0\",\n" +
        //         "\"A1\":\"hA1\", }";
        // AA aa = JSONObject.parseObject(info, AA.class);
        // System.out.println(aa.getA0() + "\n" + aa.getA1());

        // parse();

        // String test = null;
        // test = test+"ss";
        // System.out.println(test);

        // Class<TestA> clazz = TestA.class;
        // Field[] fields = clazz.getDeclaredFields();
        // for (Field field : fields) {
        //     System.out.println(field.getName());
        // }

        // JSONObject obj = new JSONObject();
        // obj.put("serialNum", "89860469092190096244");
        // String str = JSON.toJSONString(obj);
        // System.out.println(str);
        //
        // String l = "{\"serialNum\":\"89860469092190096244\"}";
        // List<String> list = new ArrayList<>();
        // for (String s : list) {
        //     System.out.println(s);
        // }

        StringBuilder builder = new StringBuilder();
        builder.append("1,");
        builder.append("2,");
        builder.append("3,");
        System.out.println(builder.toString());
    }

    public void parse() throws MessageParseException {
        int i = 0;
        try {
            i = Integer.parseInt("h");
        } catch (NumberFormatException e) {
            throw new MessageParseException("hhh", e);
        }
        System.out.println(i);
    }

    static class AA {
        private String A0;
        private String A1;

        public String getA0() {
            return A0;
        }

        public String getA1() {
            return A1;
        }

        public void setA0(String a0) {
            A0 = a0;
        }

        public void setA1(String a1) {
            A1 = a1;
        }
    }
}
