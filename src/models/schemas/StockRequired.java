package models.schemas;

public class StockRequired {
	private int foodProductId;
	private int stockId;
	private int quantity;
	public StockRequired(int foodProductId, int stockId, int quantity) {
		this.foodProductId = foodProductId;
		this.stockId = stockId;
		this.quantity = quantity;
	}
	public int getFoodProductId() {
		return foodProductId;
	}
	public void setFoodProductId(int foodProductId) {
		this.foodProductId = foodProductId;
	}
	public int getStockId() {
		return stockId;
	}
	public void setStockId(int stockId) {
		this.stockId = stockId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
