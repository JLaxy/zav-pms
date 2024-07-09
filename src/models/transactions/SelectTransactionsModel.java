package models.transactions;

import controllers.transactions.SelectTransactionsController;
import javafx.collections.ObservableList;
import models.schemas.Transaction;

public class SelectTransactionsModel {
	private SelectTransactionsController selectTransactionsController;

	public SelectTransactionsModel(SelectTransactionsController selectTransactionsController) {
		super();
		this.selectTransactionsController = selectTransactionsController;
	}
	
	public ObservableList<Transaction> retrieveTransactions(String userQuery){
		return this.selectTransactionsController.getDBManager().query.getTransactions(userQuery);
	}
	
	public ObservableList<Transaction> retrieveTransactionsById(int id){
		return this.selectTransactionsController.getDBManager().query.getTransactionsById(id);
	}

}
