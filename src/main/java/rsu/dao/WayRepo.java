package rsu.dao;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import rsu.dao.model.MapMatchingResult;

public interface WayRepo extends CrudRepository<MapMatchingResult, Long> {

	@Procedure("mapMatching")
	MapMatchingResult mapMatching(float latPi, float longPi, float latPj, float longPj);

}
