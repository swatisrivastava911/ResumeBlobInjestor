package words_count;

import java.io.IOException;

import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.Path;

import org.apache.hadoop.io.IntWritable;

import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.Mapper;

import org.apache.hadoop.mapreduce.Reducer;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class WordsCount 

{
  

public static class Map extends Mapper<Object , Text , Text , IntWritable>
  
{
	
private final static IntWritable one = new IntWritable(1);
	
private Text word = new Text();
    
public void map(Object key , Text value , Context context) throws IOException , InterruptedException
      
{
		
String str =value.toString();
	    
StringTokenizer st=new StringTokenizer(str);
	    
while(st.hasMoreTokens())
	    
{
		  
word.set(st.nextToken());
		  
context.write(word , one);
	    
}
      
}
  
}
 
public static class Reduce extends Reducer<Text , IntWritable , Text , IntWritable>
 
{
     
	private IntWritable res= new IntWritable();
	
        public void reduce(Text key , Iterable<IntWritable> values, Context context) throws IOException, InterruptedException
	 
     {
		
int sum=0;
		
for(IntWritable value : values)
		
{
			
sum = sum+value.get();
		
}
		
res.set(sum);
		
context.write(key , res);
	
}

}
 
public static  void main(String[] args) throws Exception
  
{
	
Configuration con=new Configuration();
	
Job job=new Job(con ,"wordcount");
	
job.setJarByClass(WordsCount.class);
	
job.setOutputKeyClass(Text.class);
	
job.setOutputValueClass(IntWritable.class);
	
job.setMapperClass(Map.class);
	
job.setReducerClass(Reduce.class);
	
FileInputFormat.addInputPath(job, new Path(args[0]));
	
FileOutputFormat.setOutputPath(job, new Path(args[1]));
	
System.exit(job.waitForCompletion(true) ? 0 : 1);
  
}

}
