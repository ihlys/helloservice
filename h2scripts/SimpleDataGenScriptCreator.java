import java.io.PrintWriter;
import java.util.Random;
import java.util.Arrays;

/**
*
* This class generates sql script for creating large set of simple data.
* It runs as a standalone program and requires arguments from command line.
*
* Arguments: {SQL statement, row count, column value template}, where 'column value template'
* is a sequence of arguments that represent fixed and generated fragments of value.
* Argument for fixed fragment is a text literal and argument for generated fragment is a length
* of generated by program fragment. SQL statement must contain value placeholder for generated column values.
*
*/

public class SimpleDataGenScriptCreator
{
	
	// A-Z a-z 0-9
	static final String alphabet="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
	static final Random rand = new Random();
	
	
	public static void main(String[] args)
	{	
		String[] sqlExprLeftRightParts = args[0].split("value");
		sqlExprLeftRightParts[0] = sqlExprLeftRightParts[0] + "'";
		sqlExprLeftRightParts[1] = "'" + sqlExprLeftRightParts[1];
		
		int count = Integer.parseInt(args[1]);
		
		String[] template = Arrays.copyOfRange(args, 2, args.length);
		
		System.out.println(Arrays.toString(template));
		
		generateSQLScript(sqlExprLeftRightParts[0], sqlExprLeftRightParts[1], count, template);
	}
	
	
	private static void generateSQLScript(String sqlExprLeft, String sqlExprRight, int count, String[] template)
	{
		try
		{
			int order = isInteger(template[0]);
			
			PrintWriter writer = new PrintWriter("pop_schema_large.sql", "UTF-8");
			
			for (int i = 0; i < count; i++)
			{
				writer.println(sqlExprLeft + generateName(template, order) + sqlExprRight);
			}
			writer.close();
		} catch (Exception e) {
		   e.printStackTrace();
		}
	}
	
	private static String generateName(String[] template, int order)
	{
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < template.length; i++)
		{
			if (i % 2 == order) {
				sb.append(template[i]);
			} else {
				generateFragment(sb, Integer.parseInt(template[i]));
			}
		}
		
		return sb.toString();
	}
	
	private static void generateFragment(StringBuilder sb, int length)
	{
		for (int i = 0; i < length; i++)
		{
			sb.append(alphabet.charAt(rand.nextInt(alphabet.length())));
		}
	}
	
	
	private static int isInteger(String str)
	{
		for (char c : str.toCharArray())
		{
			if (Character.isDigit(c) == false)
			{
				return 0;
			}
		}
		return 1;
	}
}
