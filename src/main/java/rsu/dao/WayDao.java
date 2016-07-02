package rsu.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import rsu.dao.model.MapMatchingResult;
import rsu.dto.Position;

import java.util.List;

@Repository
public class WayDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public MapMatchingResult mapMatching(Position firstPosition, Position secondPosition) {
		String sql = "SELECT mapmatching(" + firstPosition.toString() + "," + secondPosition.toString() + ")";
		List<MapMatchingResult> mapMatchingResult = jdbcTemplate
				.query(sql, (rs, rowNum) -> {
					MapMatchingResult mapMatchingResult1 = new MapMatchingResult();
					String resultAsString = rs.getString(1);
					String[] split = resultAsString.replaceAll("\\(", "").replaceAll("\\)", "").split(",");
					mapMatchingResult1.setLatM(Float.parseFloat(split[0]));
					mapMatchingResult1.setLonM(Float.parseFloat(split[1]));
					mapMatchingResult1.setWayM(Long.parseLong(split[2]));
					mapMatchingResult1.setSuccess(split[3].equals("1"));
					return mapMatchingResult1;
				});
		return mapMatchingResult.get(0);
	}

	public float getDistanceFromA(Position position, String scenario) {
		String sql;
		if (scenario.equals("meteor")) {
			String firstScenarioA = "23.5852987, 46.7535537";
			sql = "select ST_Distance_Sphere(ST_SetSRID(ST_Point(" + firstScenarioA + "), 4326), ST_SetSRID(ST_Point(" + position.getLongitude() + "," + position.getLatitude() +
					"), 4326));";
		}
		else {
			String secondScenarioA = "23.5831504, 46.7518788";
			sql = "select ST_Distance_Sphere(ST_SetSRID(ST_Point(" + secondScenarioA + "), 4326), ST_SetSRID(ST_Point(" + position.getLongitude() + "," + position.getLatitude() +
					"), 4326));";
		}
		return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
			return rs.getFloat(1);
		});
	}

	public float getDistanceFromB(Position position, String scenario) {
		String sql;
		if (scenario.equals("meteor")) {
			String firstScenarioB = "23.5867874, 46.7529477";
			sql = "select ST_Distance_Sphere(ST_SetSRID(ST_Point(" + firstScenarioB + "), 4326), ST_SetSRID(ST_Point(" + position.getLongitude() + "," + position.getLatitude() +
					"), 4326));";
		}
		else {
			String secondScenarioB = "23.5813245, 46.7536431";
			sql = "select ST_Distance_Sphere(ST_SetSRID(ST_Point(" + secondScenarioB + "), 4326), ST_SetSRID(ST_Point(" + position.getLongitude() + "," + position.getLatitude() +
					"), 4326));";
		}
		return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
			return rs.getFloat(1);
		});
	}

	public float getHeading(Position firstPosition, Position secondPosition) {
		String sql = "SELECT ST_Azimuth(ST_GeomFromText('POINT(" + firstPosition.getLatitude() + " " + firstPosition.getLongitude() + ")', 4326), ST_GeomFromText('POINT(" +
				secondPosition.getLatitude() + " " + secondPosition.getLongitude() + ")', 4326));";
		return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
			return rs.getFloat(1);
		});
	}

}
