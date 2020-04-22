package udf;

import com.aliyun.odps.udf.UDF;

import java.util.Random;

public class RandomNum extends UDF {
    // TODO define parameters and return type, e.g:  public String evaluate(String a, String b)

    public Long evaluate(Long s) {

        if (null==s || s<=0){
            return null;
        }
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<s;i++){
            if (i==0){
                int a=0;
                a=random.nextInt(10);
                if (a==0){
                    a=a+1;
                }
                sb.append(a+"");
            }else {
                sb.append(""+random.nextInt(10));
            }

        }
        return Long.valueOf(sb.toString());

    }

    public static void main(String[] args) throws InterruptedException {
        RandomNum randomNum = new RandomNum();
        while (true){
            System.out.println(randomNum.evaluate(4L));
            Thread.sleep(500);
        }

    }
}