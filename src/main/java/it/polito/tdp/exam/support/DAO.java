package it.polito.tdp.exam.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DAO {

	
	private static final String jdbcURL = "jdbc:mariadb://localhost/lahmansbaseballdb";
	private static HikariDataSource ds;

	public static Connection getConnection() {

		if (ds == null) {
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(jdbcURL);
			config.setUsername("root");
			config.setPassword("MDB.TdP.ip");
			
			// configurazione MySQL
			config.addDataSourceProperty("cachePrepStmts", "true");
			config.addDataSourceProperty("prepStmtCacheSize", "250");
			config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
			
			ds = new HikariDataSource(config);
		}

		try {
			return ds.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public List<GeneralObject> getAllObjects() {
		
		String sql = "SELECT id, customers_affected, date_event_began, date_event_finished "
				+ "FROM PowerOutages "
				+ "WHERE nerc_id = ? "
				+ "ORDER BY date_event_began ASC";
		
		List<GeneralObject> objList = new ArrayList<>();

		try {
			Connection conn = this.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, 55);					//l'ordinamento dei parametri viene fatto contando da 1, in ordine di comparizione del simbolo ?
			ResultSet rs = st.executeQuery();	//Restituisce un pool di risultati, su cui è possibile iterare; all'inizio è posizionato su una riga prima della prima letta dal DB; alla fine si posiziona su una riga fittizia, dopo l'ultima letta dal DB

			while (rs.next()) {					//leggo fintanto che c'è una riga successiva: se l'iteratore trova una riga da leggere, restituisce true, se finisce sulla riga fittizia dopo l'ultima, restituisce false, dunque esce dal ciclo
				
				GeneralObject obj = new GeneralObject(rs.getInt("id"), rs.getString("name"),
													new LatLng(rs.getDouble("coordx"), rs.getDouble("coordy")),
													rs.getDate("build_Date").toLocalDate(), rs.getTimestamp("last_use").toLocalDateTime(),
													rs.getTime("time").toLocalTime());
				objList.add(obj);
			}

			st.close();
			rs.close();
			conn.close();
			
			return objList;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		
	}

	public List<Edge> getAllArchi(Map<Integer, GeneralObject> oggettiIdMap) {
		// TODO Auto-generated method stub
		return null;
	}
}
