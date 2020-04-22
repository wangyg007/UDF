package udf;

import com.aliyun.odps.udf.UDF;
import org.apache.commons.io.FileUtils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class MyHashCode extends UDF {
    // TODO define parameters and return type, e.g:  public String evaluate(String a, String b)
    public Long evaluate(String s) {
        if (null==s){
            return null;
        }
//        if ("".equals(s)){
//            return 0L;
//        }
//        String aaa=s;
//        Long res=0L;
//        for (int i=0;i<aaa.length();i++){
//            res+=res*11+aaa.charAt(i);
//        }
        CRC32 crc32 = new CRC32();
        crc32.update(s.getBytes());


        return crc32.getValue();
    }

    public static void main(String[] args) {
//        System.out.println(("UN020H11".hashCode()>>16) ^ ("UN020H11".hashCode()));
//        System.out.println(("UN020H0P".hashCode()>>16) ^ ("UN020H0P".hashCode()));

//        String aaa="UN020H11";
//        StringBuilder sb = new StringBuilder();
//        sb.append(aaa.charAt(0)+0);
//        sb.append(aaa.charAt(aaa.length()%2)+0);
//        sb.append(aaa.charAt(aaa.length()-1)+0);
//        sb.append(""+aaa.length());
//        System.out.println(sb.toString());
//        System.out.println(Long.valueOf(sb.toString()));
//        MyHashCode myHashCode = new MyHashCode();
//        System.out.println(myHashCode.evaluate("020H11"));

//        CRC32 crc32 = new CRC32();
//        crc32.update("".getBytes());
//        System.out.println(crc32.getValue());
        FileOutputStream outputStream=null;
        try {

            outputStream = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\crc32.data",true);


            MyHashCode myHashCode = new MyHashCode();
            RandomStr randomStr = new RandomStr();
            int i=0;
            while (i<10000000){
                outputStream.write((myHashCode.evaluate(i+"zhenjia")+"\n").getBytes());
                i++;
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (null!=outputStream){
                try {
                    outputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
//        while (true){
//            //System.out.println(myHashCode.evaluate(randomStr.evaluate((32L))) % 10000);
//            System.out.println(randomStr.evaluate((32L)).hashCode() % 10000);
//            Thread.sleep(300);
//        }
        //System.out.println(8328533 % 10000);
    }
}