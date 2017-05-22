import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/* Takes the search inputs in string form and makes the results available via get methods.
 * If current implementation is too slow, may need to gather and store all data at time of 
 * construction instead of doing a query for each single string get method.
 */
public class SearchResults {
	private String[] questionIDs;
	private Connection con; 
	private Statement st; 
	private ResultSet rs;
	ArrayList<String> cour = new ArrayList<String>(); 
	ArrayList<String> ids = new ArrayList<String>(); 
	ArrayList<String> perc = new ArrayList<String>(); 
	ArrayList<String> name = new ArrayList<String>(); 
	TreeMap<String, String>IDnfans = new TreeMap<String, String>(); 
	ArrayList<String> dura = new ArrayList<String>();
	ArrayList<String> resultarr = new ArrayList<String>(); 
	Map<String, String> temp = new LinkedHashMap<String, String>(); 

	
	/* Takes the same input as QueryBuilder
	 * Makes QueryBuilder, gets query, connects, runs query, stores questionID array from resultSet
	 */
	public SearchResults(String course, String session, String filter, String x, 
						String orderBy, String order, String ignore) {
		
		try{ 
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/clicker?verifyServerCertificate=false&useSSL=true", "root", "");
			st = con.createStatement();

		}
		
		catch(Exception ex){
			System.out.println("Failing there " + ex); 
		}
		
		
		
		QueryBuilder qb = new QueryBuilder(course, session, filter, x, orderBy, order, ignore);
		
		getqIDs(qb.getQuery()); 
		
		
		
		
		
	}
	
	// return a list of all the question ids in the search results
	public ArrayList<String> getQuestionIDs() {
			return ids;
	
	}
	
	
	
	
public ArrayList<String> getqIDs(String query){ 
			
		try{ 
			
			
			rs = st.executeQuery(query);
			while(rs.next()){ 
				String qIDs = rs.getString("id");
				ids.add(qIDs);
				
				
				
			}
		


		}
		
		catch(Exception ex){ 
			System.out.println(ex);
		}
		
		return ids; 
		
		
	}


	
	
	
	
	// Helper method used by the single string get methods below
	// Connect to the database, run query, get resultSet, return string in resultSet
	public ArrayList<String> singleResultHelper (String query) {
		String resultval = ""; 
		
		try{ 
			rs = st.executeQuery(query);
			rs.next(); 
			resultval =  rs.getString(1);
			System.out.println("Result val is " + resultval);
			resultarr.add(resultval); 
		}
		
		catch (SQLException e){
			System.out.println("Error here " + e );
		}

		return resultarr; 
	}
	
	/* All single string get methods will take a questionID or sessionID and insert it into a 
	 * query which it will pass to the previous helper method. 
	 * Method names have Q for question info, S for session info.
	 */
	
	// return the duration of question voting
	
	

	public ArrayList<String> getQDuration(String questionID) {
			
	try{ 
		String query = "SELECT duration\n" +
				"FROM questions\n" +
				"WHERE questions.id = " + questionID;
			
			
			rs = st.executeQuery(query);
			while(rs.next()){ 
				String durat = rs.getString("duration");
				dura.add(durat);
				
				
				
			}
		


		}
		
		catch(Exception ex){ 
			System.out.println(ex);
		}
		
		return dura; 
		
		
		
		
		
	}

	// return the percentage of correct final responses
	public ArrayList<String> getQPercentCorrect(String questionID) {
		
		try{ 
			String query = "SELECT (correct.cnt/total.cnt * 100) as percent\n" +
					"FROM (\n" +
					"SELECT count(votes.id) as cnt\n" +
					"from votes INNER JOIN questions\n" +
					"ON (votes.question_id = " + questionID + " AND questions.id = " + questionID + ")\n" +
					") AS total\n" +
					"INNER JOIN (\n" +
					"SELECT count(votes.id) as cnt\n" +
					"from votes INNER JOIN questions\n" +
					"ON (votes.question_id = " + questionID + " AND questions.id = " + questionID + 
						" AND votes.fans = questions.answer)\n" +
					") AS correct\n";
				//System.out.println("Query is " + query);
				
				rs = st.executeQuery(query);
				while(rs.next()){ 
					String correct = rs.getString("percent");
					perc.add(correct);
					
					
					
				}
			


			}
			
			catch(Exception ex){ 
				System.out.println(ex);
			}
			
			return perc; 
		
		
		
	}
	
	public String getQTime(String questionID) {
		String query = "SELECT start_time\n" +
				"FROM questions\n" +
				"WHERE questions.id = " + questionID;
		ArrayList<String> array = singleResultHelper(query);
		String result = array.get(0);
		// result format example: 11:35:01
		return result;
	}
	
	public ArrayList<String> getQNum(String questionID) {
		
				
		try{ 
			String query = "SELECT name\n" +
					"FROM questions\n" +
					"WHERE questions.id = " + questionID;
				
				
				rs = st.executeQuery(query);
				while(rs.next()){ 
					String namez = rs.getString("name");
					name.add(namez);
					
					
					
				}
			


			}
			
			catch(Exception ex){ 
				System.out.println(ex);
			}
			
			return name; 
			
			
		
		
		
		
	}
	
	public String getQCorrectAnswer(String questionID) {
		String query = "SELECT answer\n" +
				"FROM questions\n" +
				"WHERE questions.id = " + questionID;
		ArrayList<String> array = singleResultHelper(query);
		String result = array.get(0);
		// format example: A
		return result;
	}
	
	// TODO pair and sister stuff
	
	public String getSID(String questionID) {
		String query = "SELECT sessions.id\n" +
				"FROM questions INNER JOIN sessions\n" +
				"ON questions.session_id = sessions.id\n" +
				"WHERE questions.id = " + questionID;
		ArrayList<String> array = singleResultHelper(query);
		String result = array.get(0);
		// result format example: 1
		return result;
	}
	
	public String getSMinDuration(String sessionID) {
		String query = "SELECT MIN(duration)\n" +
				"FROM questions INNER JOIN sessions\n" +
				"ON questions.session_id = " + sessionID;
		ArrayList<String> array = singleResultHelper(query);
		String result = array.get(0);
		// result format example: 00:00:36
		return result.substring(3);
	}
	
	public String getSAvgDuration(String sessionID) {
		String query = "SELECT AVG(duration)\n" +
				"FROM questions INNER JOIN sessions\n" +
				"ON questions.session_id = " + sessionID;
		ArrayList<String> array = singleResultHelper(query);
		String result = array.get(0);		// result format example: 00:00:36
		return result.substring(3);
	}
	
	public String getSMaxDuration(String sessionID) {
		String query = "SELECT MAX(duration)\n" +
				"FROM questions INNER JOIN sessions\n" +
				"ON questions.session_id = " + sessionID;
		ArrayList<String> array = singleResultHelper(query);
		String result = array.get(0);
		// result format example: 00:00:36
		return result.substring(3);
	}
	
	public String getSNumQuestions(String sessionID) {
		String query = "SELECT COUNT(DISTINCT id)\n" +
				"FROM questions\n" +
				"WHERE session_id = " + sessionID;
		ArrayList<String> array = singleResultHelper(query);
		String result = array.get(0);
		// result format example: 2
		return result;
	}
	
	public String getSTime(String sessionID) {
		String query = "SELECT time\n" +
				"FROM sessions\n" +
				"WHERE sessions.id = " + sessionID;
		ArrayList<String> array = singleResultHelper(query);
		String result = array.get(0);
		// result format example: 1316
		return result.substring(0,2) + ":" + result.substring(2);
	}
	
	public String getSCourse(String sessionID) {
		String query = "SELECT course_name\n" +
				"FROM courses INNER JOIN sessions\n" +
				"ON courses.id = sessions.course_id\n" +
				"WHERE sessions.id = " + sessionID;
		ArrayList<String> array = singleResultHelper(query);
		String result = array.get(0);
		// format example: CS 141 FALL 2016
		return result;
	}
	
	public String getSDate(String sessionID) {
		String query = "SELECT date\n" +
				"FROM sessions\n" +
				"WHERE sessions.id = " + sessionID;
		ArrayList<String> array = singleResultHelper(query);
		String result = array.get(0);
		// format example: 160908
		return result.substring(2, 4)+"/"+result.substring(4,6)+"/"+result.substring(0,2);
	}
	
	public String getSPercentCorrect(String sessionID) {
		String query = "SELECT AVG(percent)\n" +
				"FROM questions INNER JOIN sessions ON (sessions.id = 1)\n" +
				"INNER JOIN (\n" +
				"SELECT ((correct.cnt/total.cnt)*100) as percent\n" +
				"FROM (\n" +
				"SELECT question_id, count(votes.id) as cnt \n" +
				"from votes join questions on questions.id=votes.question_id \n" +
				"WHERE session_id = " + sessionID + "\n" +
				"GROUP BY question_id\n" +
				") AS total\n" +
				"INNER JOIN (\n" +
				"SELECT question_id, count(votes.id) as cnt\n" +
				"from votes join questions on questions.id=votes.question_id\n" +
				"WHERE session_id = " + sessionID + " AND votes.fans = questions.answer\n" +
				"GROUP BY question_id\n" +
				") AS correct\n" +
				"ON total.question_id = correct.question_id)  AS sub\n";
		//String result = singleResultHelper(query);
		
		// format example: 76.6667 
		
		ArrayList<String> array = singleResultHelper(query);
		String result = array.get(0);
		return result;
	}
	
	
}
