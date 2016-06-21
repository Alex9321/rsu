package rsu.dao.model;

import javax.persistence.*;
//
//@NamedStoredProcedureQueries({
//		@NamedStoredProcedureQuery(
//				name = "mapMatching",
//				procedureName = "mapMatching",
//				parameters = {
//						@StoredProcedureParameter(mode = ParameterMode.IN, name = "latPi", type = Float.class),
//						@StoredProcedureParameter(mode = ParameterMode.IN, name = "longPi", type = Float.class),
//						@StoredProcedureParameter(mode = ParameterMode.IN, name = "latPj", type = Float.class),
//						@StoredProcedureParameter(mode = ParameterMode.IN, name = "longPj", type = Float.class),
//						@StoredProcedureParameter(mode = ParameterMode.OUT, name = "latM", type = Float.class),
//						@StoredProcedureParameter(mode = ParameterMode.OUT, name = "lonM", type = Float.class),
//						@StoredProcedureParameter(mode = ParameterMode.OUT, name = "wayM", type = Long.class),
//						@StoredProcedureParameter(mode = ParameterMode.OUT, name = "success", type = Integer.class)
//				},
//				resultClasses = MapMatchingResult.class) })
public class MapMatchingResult {

	private long id;

	private float latM;

	private float lonM;

	private long wayM;

	private boolean success;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public long getWayM() {
		return wayM;
	}

	public void setWayM(long wayM) {
		this.wayM = wayM;
	}

	public float getLonM() {
		return lonM;
	}

	public void setLonM(float lonM) {
		this.lonM = lonM;
	}

	public float getLatM() {
		return latM;
	}

	public void setLatM(float latM) {
		this.latM = latM;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
