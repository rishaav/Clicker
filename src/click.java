import java.util.Scanner;

public class click {

	public static void main(String[] main){
		 
		
		GUI d = new GUI(); 
		//DBConnect connect = new DBConnect();
		boolean go = false; 
		//ConnectTest connector = new ConnectTest(); 
		// DBConnect connect = new DBConnect(); 
		 QueryBuilderMini m = new QueryBuilderMini(); 
			
		
		
		
		
		

		while(!go){
			
				
			Scanner input = new Scanner(System.in);  //to read from standard input
			System.out.println("What do you want to get?");
			System.out.println("1) Course name");
			System.out.println("2) Get Session ");
			System.out.println("3) Questions");
			System.out.println("4) Returner");
			System.out.println("0) Exit");
			int response = input.nextInt(); 

			

			if(response == 1){ 
			//	connect.getcoursename();
			}else if(response == 2){ 
				//connect.getImage();
			}else if(response == 3){ 
				System.out.println("Session Name is ");
			}	else if(response == 0){  
				System.out.println("Goodbye!");
				go = true; 
				break; 
			}else{ 
				throw new IllegalArgumentException(); 
			}
		}


	}

}
