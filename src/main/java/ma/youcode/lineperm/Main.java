package ma.youcode.lineperm;

import ma.youcode.lineperm.ui.ConsoleApp;
import ma.youcode.lineperm.db.DBConnection;
import java.sql.Connection;

	public class Main {

		public static void main(String[] args) {

			// ConsoleApp appObj = new ConsoleApp();
			// appObj.start();

			Connection connTest = DBConnection.getConnection();
			if(connTest != null) {
				System.out.println("Conn success");
			} else {
				System.out.println("Errour");
			}

    		}
}