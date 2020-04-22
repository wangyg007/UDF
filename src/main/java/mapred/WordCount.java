package mapred;

import com.aliyun.odps.OdpsException;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.ReducerBase;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;
import com.aliyun.odps.mapred.utils.SchemaUtils;

import java.io.IOException;
import java.util.Iterator;

/**
 * word-cout demo
 *
 * @author wyg
 */
public class WordCount {



    public static class MainMapper extends MapperBase{

        private Record word;
        private Record one;

        @Override
        public void setup(TaskContext context) throws IOException {

            word = context.createMapOutputKeyRecord();
            one = context.createMapOutputKeyRecord();
            one.set(new Object[]{1L});
            System.out.println("taskId:"+context.getTaskID().toString());

        }

        @Override
        public void map(long recordNum, Record record, TaskContext context) throws IOException {
            //TODO
            for(int i=0;i<record.getColumnCount();i++){
                word.set(new Object[]{record.get(i).toString()});
                context.write(word,one);
            }
        }
    }



    public static class SumCombiner extends ReducerBase {

        private Record count;

        @Override
        public void setup(TaskContext context) throws IOException {
            count=context.createMapOutputValueRecord();
        }

        @Override
        public void reduce(Record key, Iterator<Record> values, TaskContext context) throws IOException {
            long c=0;
            while (values.hasNext()){
                Record val=values.next();
                c+=(Long) val.get(0);
            }

            count.set(0,c);
            context.write(key,count);
        }

    }

    public static class SumReducer extends ReducerBase{

        private Record result;

        @Override
        public void setup(TaskContext context) throws IOException {
            result=context.createOutputRecord();
        }

        @Override
        public void reduce(Record key, Iterator<Record> values, TaskContext context) throws IOException {
            long count=0;
            while (values.hasNext()){
                Record val=values.next();
                count+=(Long) val.get(0);
            }
            result.set(0,key.get(0));
            result.set(1,count);
        }
    }

    public static void main(String[] args) throws OdpsException {
        if(args.length!=2){
            System.err.println("Usage: WordCount <in_table> <out_table>");
            System.exit(2);
        }
        JobConf jobConf = new JobConf();
        jobConf.setMapperClass(MainMapper.class);
        jobConf.setCombinerClass(SumCombiner.class);
        jobConf.setReducerClass(SumReducer.class);

        jobConf.setMapOutputKeySchema(SchemaUtils.fromString("word:string"));
        jobConf.setMapOutputValueSchema(SchemaUtils.fromString("count:bigint"));

        InputUtils.addTable(TableInfo.builder().tableName(args[0]).build(),jobConf);
        OutputUtils.addTable(TableInfo.builder().tableName(args[1]).build(),jobConf);
        JobClient.runJob(jobConf);
    }

}