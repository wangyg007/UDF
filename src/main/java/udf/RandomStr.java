package udf;

import com.aliyun.odps.udf.UDF;

import java.util.Random;

public class RandomStr extends UDF {
    // TODO define parameters and return type, e.g:  public String evaluate(String a, String b)
    public String evaluate(Long s) {
        if (null==s || s<=0){
            return null;
        }
        return getRandomString(s.intValue());
    }


    public String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyz0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(36);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        RandomStr randomStr = new RandomStr();
        System.out.println(randomStr.evaluate(10L));;
    }
}