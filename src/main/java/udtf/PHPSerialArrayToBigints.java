package udtf;

import com.aliyun.odps.udf.ExecutionContext;
import com.aliyun.odps.udf.UDFException;
import com.aliyun.odps.udf.UDTF;
import com.aliyun.odps.udf.annotation.Resolve;
import com.aliyun.odps.utils.StringUtils;
import org.phprpc.util.AssocArray;
import org.phprpc.util.Cast;
import org.phprpc.util.PHPSerializer;
import udf.MyHashCode;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wyg
 * @time 2020/4/1 10:54:00
 * php_deserial(主键id(无实际意义),关系id1(角色id+系统标识+租户标识),关系id2数组(php序列化数组),系统标识)
 */
// TODO define input and output types, e.g. "string,string->string,bigint".
@Resolve({"string,string,string,string->bigint,bigint,bigint"})
public class PHPSerialArrayToBigints extends UDTF {

    private MyHashCode hashCode = new MyHashCode();

    @Override
    public void setup(ExecutionContext ctx) throws UDFException {

    }

    /**
     * @return (id,id1,id2)
     * @param args
     * @throws UDFException
     */
    @Override
    public void process(Object[] args) throws UDFException {

        String arg0= (String) args[0];
        String arg1 = (String) args[1];
        String content= (String) args[2];
        String sysCode= (String) args[3];

        List<String> list=new ArrayList<String>(16);
        try {
             list = getDeserializeFromArray(content);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (!list.isEmpty()){
            for (String a:list){
                forward(Long.valueOf(arg0),hashCode.evaluate(arg1),hashCode.evaluate(a+sysCode));
            }
        }
    }

    @Override
    public void close() throws UDFException {

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