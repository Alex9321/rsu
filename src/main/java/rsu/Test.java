package rsu;

import org.postgresql.jdbc.PgConnection;
import org.postgresql.util.PGobject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Test {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://localhost:5432/zorilor";
		PgConnection conn = (PgConnection) DriverManager.getConnection(url, "alex", "");

		ServerSocket serverSocket = new ServerSocket(9999);

		conn.addDataType("geometry", "org.postgis.PGgeometry");
		Socket socket = serverSocket.accept();
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String streetName = "";
		String firstPosition = null;
		String secondPosition = null;
		while (true) {
			String position = inFromClient.readLine();
			if (position == null) {
				continue;
			}
			else {
				if (firstPosition == null) {
					firstPosition = position;
					continue;
				}
				secondPosition = firstPosition;
				firstPosition = position;
			}

			Statement s = conn.createStatement();
			ResultSet r = s.executeQuery("select mapmatching(" + firstPosition + "," + secondPosition + ")");

			r.next();
			PGobject geom = (PGobject) r.getObject(1);
			r.close();
			if (geom.getValue().getBytes().length < 5) {
				continue;
			}
			String success = geom.getValue().replace(")", "").split(",")[3];
			String wayId = geom.getValue().replace(")", "").split(",")[2];
			if (success.equals("1")) {
				ResultSet way = s.executeQuery("select v from way_tags where k='name' and way_id='" + wayId + "'");

				way.next();
				try {
					String newStreet = way.getString(1);
					if (!newStreet.equals(streetName)) {
						streetName = newStreet;
						System.out.println(streetName + " ----- at position:   ");
						System.out.println(position);
						System.out.println();
					}
				}
				catch (Exception e) {
					way.close();
				}
			}

			s.close();
		}
//        conn.close();
	}

}
