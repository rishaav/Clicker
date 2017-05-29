import java.io.*;
import java.sql.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.Image;


public class DBConnect {
	private Connection con; 
	private Statement st; 
	private ResultSet rs;
	ArrayList<String> cour = new ArrayList<String>(); 
	ArrayList<String> ids = new ArrayList<String>(); 
	ArrayList<String> ans = new ArrayList<String>(); 
	ArrayList<String> qname = new ArrayList<String>(); 
	ArrayList<String> dura = new ArrayList<String>();
	
	


	public DBConnect(){
		
		try{ 
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/clicker", "root", "");
			st = con.createStatement();

		}
		
		catch(Exception ex){
			System.out.println("Error: " + ex); 
		}
	} 


	public ArrayList<String> getcoursename(){ 
		
		try{ 
			String query = "SELECT course_name FROM courses"; 

			rs = st.executeQuery(query);
			while(rs.next()){ 
				String coursename = rs.getString("course_name"); 
				File file = new File("d:\\" + coursename);
			
				System.out.println("The courses are: " + coursename);
				cour.add(coursename);

			}


		}
		
		catch(Exception ex){ 
			System.out.println(ex);
		}
		
		return cour; 
	}
	
	public String getcourseID(String courseName){ 
		courseName = "\""+ courseName +"\"";
		
		String courseid = "";
		try{ 
			String query = "SELECT id FROM courses WHERE course_name = " + courseName; 

			rs = st.executeQuery(query);
			while(rs.next()){ 
				 courseid = rs.getString("course_name"); 
				

			}


		}
		
		catch(Exception ex){ 
			System.out.println(ex);
		}
		
		return courseid; 
	}
		
	
	



	public ArrayList<String> getsession(String courID){ 
		
		try{ 
			
			String query = "SELECT id FROM sessions WHERE course_id = " + courID; 
			rs = st.executeQuery(query);
			
			while(rs.next()){ 
				String id = rs.getString("id");
				
				ans.add(id);
			}

		}
		
		catch(Exception ex){ 
			System.out.println(ex);
		}
		
		return ans; 
	}
	
	
public ArrayList<String> getQuestionName(String sessId){ 
		
		try{ 
			
			String query = "SELECT name FROM questions WHERE session_id = " + sessId;  
			rs = st.executeQuery(query);
			
			while(rs.next()){ 
				String id = rs.getString("name");
				
				qname.add(id);
			}

		}
		
		catch(Exception ex){ 
			System.out.println(ex);
		}
		
		return qname; 
	}
	
	





	public Image getImage2(String session, String question){

		question = "\""+ question+"\"";
		
		InputStream is = null;
		
		Image image = null;
		
		try{ 
			String query = "SELECT picture FROM questions WHERE session_id = " + session 
					+ " AND name = " + question;
			//System.out.println(query);

			rs = st.executeQuery(query);
			
			while(rs.next()){ 
				is=rs.getBinaryStream(1);				
				image = ImageIO.read(is);
			}
		}
		
		catch(Exception ex){ 
			System.out.println(ex);
		}
		
		return image;
	}
	
	public Image getResponse2(String session, String question){

		question = "\""+ question+"\"";
		
		InputStream is = null; 
		
		Image image = null;
		
		try{ 
			String query = "SELECT response FROM questions WHERE session_id = " + session 
					+ " AND name = " + question;
			
			

			rs = st.executeQuery(query);
			
			while(rs.next()){ 
				is=rs.getBinaryStream(1);				
				image = ImageIO.read(is);
			}
		}
		
		catch(Exception ex){ 
			System.out.println(ex);
		}
		
		return image;
	}

	public String getImage(String session){

		String counter = null;
		InputStream is = null; 
		
		try{ 
			int count = 0; 
			String query = "SELECT picture FROM questions WHERE session_id = " + session;

			rs = st.executeQuery(query);
			while(rs.next()){ 
				count++;
				counter = Integer.toString(count); 
				is=rs.getBinaryStream(1);
				
				
				/*
				FileOutputStream fos=new FileOutputStream("d:\\CS 141 FALL 2016\\"+session+"\\"+counter+".jpg");
				
				int k;
				while((k=is.read())!=-1){
					fos.write(k);
				}
				
				fos.close();
				 */
			}
		}
		
		catch(Exception ex){ 
			System.out.println(ex);
		}
		
		return "d:\\"+counter+".jpg";
	}


	



		
	public void addAnswer(String question, String answer, String session){
		
		question = "\"Question "+ question+"\"";
		answer = "\""+answer+"\"";
		
		
		try{ 
			String query = "UPDATE questions SET answer = " + answer + " WHERE name = " + question + " AND session_id = " + session;
			 st.executeUpdate(query); 
			

		}catch(Exception ex){ 
			System.out.println(ex);
		}

		
	}
	
	

	
	
	
	
	



	
	
	
	
	
	
}