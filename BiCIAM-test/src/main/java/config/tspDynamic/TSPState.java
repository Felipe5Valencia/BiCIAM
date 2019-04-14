package config.tspDynamic;

public class TSPState {
	
	private int value;
	private int idCity;
	private boolean pool;
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public boolean isPool() {
		return pool;
	}
	public void setPool(boolean pool) {
		this.pool = pool;
	}
	public int getIdCity() {
		return idCity;
	}
	public void setIdCity(int idCity) {
		this.idCity = idCity;
	}

}
