package models.order;

import java.util.ArrayList;

import controllers.order.ViewBackOrderController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.schemas.OrderedProduct;
import models.schemas.StockRequired;
import models.schemas.Transaction;

public class ViewBackOrderModel {
	private ViewBackOrderController controller;
	private ObservableList<Transaction> backorder = FXCollections.observableArrayList();

	public ViewBackOrderModel(ViewBackOrderController controller) {
		this.controller = controller;
	}
	
	
	public ObservableList<Transaction> getBackorder() {
		return backorder;
	}


	public void setBackorder(ObservableList<Transaction> backorder) {
		this.backorder = backorder;
	}


	public ObservableList<Transaction> getBackOrder(){
		ObservableList<OrderedProduct> orderedProducts= controller.getDBManager().query.getOrder();
		ObservableList<Transaction> transactions = controller.getDBManager().query.getTransactions(null);
		ObservableList<Transaction> backorders = FXCollections.observableArrayList();
		System.out.println(orderedProducts.toString()+" order Size "+ backorders.size());

		ArrayList<Integer> transactionIds = new ArrayList<>();
			for(OrderedProduct ordered : orderedProducts) {
				if(ordered.getCurrent_quantity() < ordered.getRequired_quantity()) {
					transactionIds.add(ordered.getTransaction_id());
				}

			}
		
		for(int id : transactionIds) {
			for(Transaction transaction : transactions) {
				if(id == transaction.getId()) {
					backorders.add(transaction);
				}
			}
		}
		setBackorder(backorders);
		System.out.println(backorders.toString()+" Size "+ backorders.size());
		return backorders;
	}
	public ObservableList<Transaction> getTransactionsByName(String name){
		ObservableList<Transaction> transactions= controller.getDBManager().query.getTransactions(name);
		ObservableList<Transaction> backorderFiltered = FXCollections.observableArrayList();
		for(Transaction transaction : transactions) {
			for(Transaction backorderT : backorder) {
				if(backorderT.getId() == transaction.getId()) {
					backorderFiltered.add(backorderT);
				}
			}
		}
		return backorderFiltered;
	}
	

}
