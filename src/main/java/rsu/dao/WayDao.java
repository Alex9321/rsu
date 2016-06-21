package rsu.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import rsu.dao.model.MapMatchingResult;
import rsu.dto.Position;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class WayDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public MapMatchingResult mapMatching(Position firstPosition, Position secondPosition) {
		String sql = "SELECT mapmatching(" + firstPosition.toString() + "," + secondPosition.toString() + ")";
		List<MapMatchingResult> mapMatchingResult = jdbcTemplate
				.query(sql, new RowMapper<MapMatchingResult>() {
					@Override
					public MapMatchingResult mapRow(ResultSet rs, int rowNum) throws SQLException {
						MapMatchingResult mapMatchingResult = new MapMatchingResult();
						String resultAsString = rs.getString(1);
						String[] split = resultAsString.replaceAll("\\(", "").replaceAll("\\)", "").split(",");
						mapMatchingResult.setLatM(Float.parseFloat(split[0]));
						mapMatchingResult.setLonM(Float.parseFloat(split[1]));
						mapMatchingResult.setWayM(Long.parseLong(split[2]));
						mapMatchingResult.setSuccess(split[3].equals("1"));
						return mapMatchingResult;
					}
				});
		return mapMatchingResult.get(0);
	}

	public float getDistanceFromStart(Position position) {
		String sql = "select ST_Distance_Sphere(ST_SetSRID(ST_Point(23.5852987, 46.7535537), 4326), ST_SetSRID(ST_Point(" + position.getLongitude() + "," + position.getLatitude() +
				"), 4326));";
		return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
			return rs.getFloat(1);
		});
	}

}
