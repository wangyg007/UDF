package base;

import com.aliyun.odps.utils.StringUtils;
import org.phprpc.util.AssocArray;
import org.phprpc.util.Cast;
import org.phprpc.util.PHPSerializer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyg
 * @time 16:00
 * @note
 **/
public class PHPDeserialize {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {

        String source="a:23:{i:0;s:1:\"2\";i:1;s:2:\"18\";i:2;s:3:\"106\";i:3;s:3:\"107\";i:4;s:3:\"108\";i:5;" +
                "s:3:\"109\";i:6;s:3:\"110\";i:7;s:1:\"4\";i:8;s:2:\"27\";i:9;s:3:\"116\";i:10;s:3:\"181\";i:11;s:1:\"6\";" +
                "i:12;s:2:\"28\";i:13;s:2:\"34\";i:14;s:2:\"35\";i:15;s:2:\"36\";i:16;s:2:\"37\";i:17;s:2:\"31\";i:18;s:2:\"32\";" +
                "i:19;s:2:\"33\";i:20;s:2:\"38\";i:21;s:2:\"39\";i:22;s:2:\"40\";}";
        System.out.println(getDeserializeFromArray(source));

    }


    public static List<String> getDeserializeFromArray(String content) throws InvocationTargetException, IllegalAccessException {
        ArrayList<String> list = new ArrayList<String>();
        PHPSerializer phpSerializer = new PHPSerializer();
        if (StringUtils.isEmpty(content)){
            return list;
        }
        AssocArray array = (AssocArray) phpSerializer.unserialize(content.getBytes());
        for (int i=0;i<array.size();i++){
            String s = (String) Cast.cast(array.get(i), String.class);
            list.add(s);
        }
        return list;
    }


}
