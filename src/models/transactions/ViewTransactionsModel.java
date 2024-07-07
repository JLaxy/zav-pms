package models.transactions;

import controllers.transactions.ViewTransactionsController;
import javafx.collections.ObservableList;
import models.schemas.Transaction;

public class ViewTransactionsModel {
	private ViewTransactionsController controller;

	public ViewTransactionsModel(ViewTransactionsController controller) {
		this.controller = controller;
	}
	
	public ObservableList<Transaction> getTransactions(){
		System.out.println(controller.getDBManager());
		return controller.getDBManager().query.getTransactions(null);
	}
	
	public ObservableList<Transaction> getTransactionsByName(String name){
		System.out.println(controller.getDBManager());
		return controller.getDBManager().query.getTransactions(name);
	}

}
