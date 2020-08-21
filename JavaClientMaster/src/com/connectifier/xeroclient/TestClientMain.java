package com.connectifier.xeroclient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Iterator;

import com.connectifier.xeroclient.models.BankTransaction;
import com.connectifier.xeroclient.models.BrandingTheme;
import com.connectifier.xeroclient.models.Contact;
import com.connectifier.xeroclient.models.ContactPerson;
import com.connectifier.xeroclient.models.CreditNote;
import com.connectifier.xeroclient.models.Currency;
import com.connectifier.xeroclient.models.Employee;
import com.connectifier.xeroclient.models.ExpenseClaim;
import com.connectifier.xeroclient.models.Invoice;
import com.connectifier.xeroclient.models.InvoiceType;
import com.connectifier.xeroclient.models.Item;
import com.connectifier.xeroclient.models.Journal;
import com.connectifier.xeroclient.models.JournalLine;
import com.connectifier.xeroclient.models.LineItem;
import com.connectifier.xeroclient.models.Account;
import com.connectifier.xeroclient.models.ManualJournal;
import com.connectifier.xeroclient.models.ManualJournalLine;
import com.connectifier.xeroclient.models.Payment;
import com.connectifier.xeroclient.models.Receipt;
import com.connectifier.xeroclient.models.RepeatingInvoice;
import com.connectifier.xeroclient.models.TaxRate;
import com.connectifier.xeroclient.models.TrackingCategory;
import com.connectifier.xeroclient.models.TrackingCategoryOption;
import com.connectifier.xeroclient.models.User;
import com.connectifier.xeroclient.models.BankTransfer;
import com.google.common.collect.ImmutableList;

public class TestClientMain {

	XeroClient client = null;
	private static Connection connection;
	private Statement stmt;
	private ResultSet resultSet;
	public static void main(String[] args) {

		TestClientMain clientMain = new TestClientMain();
		clientMain.createXeroClient();
		System.out.println(" Organisation " + clientMain.client.getOrganisation().getName());
		clientMain.insertInvoicesValues();
		clientMain.insertEmployeesValues();
		clientMain.insertReceiptsValues();
		clientMain.insertItemsValues();
		clientMain.insertExpenseClaimsValues();
		clientMain.insertTaxRatesValues();
		clientMain.insertBrandingThemesValues();
		clientMain.insertAccountsValues();
		clientMain.insertContactsValues();
		clientMain.insertCurrenciesValues();
		clientMain.insertBankTransfersValues();
		clientMain.insertJournalsValues();
		clientMain.insertBankTransactionsValues();
		clientMain.insertUsersValues();
		clientMain.insertCreditNotesValues();
		clientMain.insertPaymentsValues();
		clientMain.insertTrackingCategoriesValues();
		clientMain.insertManualJournalsValues();
		clientMain.insertRepeatingInvoicesValues();
		List<Invoice> invoce = clientMain.client.getInvoices();
		System.out.println(" number of invoices " + clientMain.client.getInvoices().size());
		int numOfInvoices = clientMain.client.getInvoices().size();
		for (int i=0; i < numOfInvoices; i++) {
			System.out.println(invoce.get(i).getInvoiceNumber());
			System.out.println(invoce.get(i).getUrl());
			System.out.println(invoce.get(i).getStatus());
		}
		// Do iteration and print

		Invoice invoice = new Invoice();
		invoice.setType(InvoiceType.ACCREC);
		System.out.println(" The Client contact " + clientMain.client.getContacts().get(0).getBankAccountDetails());
		System.out.println(" The Client contact " + clientMain.client.toString());
		invoice.setContact(clientMain.client.getContacts().get(0));
		LineItem item = new LineItem();
		item.setDescription("MVS Invoice");
		invoice.setLineItems(ImmutableList.of(item));
		clientMain.client.createInvoice(invoice);
	}

	public void createXeroClient() {
		Reader pemReader = null;
		try {
			pemReader = new FileReader(new File(".\\privateKey.pem"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client = new XeroClient(pemReader, "6B7U6M2GUOHGRPOV3M8AJZURHCUIXK", "YYDYKZHZQLIQ7PGNSTWLIHKLAFEXGM");

	}
	public void connectionToMDB() {
		try
		{
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			System.out.println("Driver successfully loaded");
		} catch (ClassNotFoundException c) {
			System.out.println("Unable to load driver\n" + c);
		}
		//connect to database
		try
		{
			connection = DriverManager.getConnection("jdbc:ucanaccess://" + "DiamondAM1.accdb"+ ";showschema=true");
			System.out.println("Connection successful");
			stmt = connection.createStatement();
		} catch (SQLException e) {
			System.out.println("Unable to connect\n" + e);
		}
	}


	public void insertRepeatingInvoicesValues() {

		// TODO Auto-generated method stub
		connectionToMDB();
		// After connections the corresponding values are to be inserted. while coming out close.
		try {

			String sqlQuery = "";
			List<RepeatingInvoice> repeatingInvoiceList = client.getRepeatingInvoices();
			int numOfRepeatingInvoice = repeatingInvoiceList.size();
			int cntForRepeatingInvoice = 0;
			int cntForRepeatingInvoiceLineCnt = 0;
			for (int i=0; i < cntForRepeatingInvoice; i++) {
				RepeatingInvoice repeatingInvoiceData = repeatingInvoiceList.get(i);
				List<LineItem>  lineData = repeatingInvoiceData.getLineItems();
				int lineDataSize = lineData.size();
				for (int j=0; j <  lineDataSize; j++) {
					cntForRepeatingInvoiceLineCnt++;
					sqlQuery = " Insert into RepeatingInvoices(ID,RepeatingInvoiceId,Type,ContactId,ContactName," +
							" Schedule_Period,Schedule_Unit,Schedule_DueDate,Schedule_DueDateType, " +
							" Schedule_StartDate,Schedule_NextScheduledDate,Schedule_EndDate,LineItem_Description," +
							"LineItem_Quantity,LineItem_UnitAmount,LineItem_ItemCode,LineItem_AccountCode," +
							"LineItem_TaxType,LineItem_TaxAmount,LineItem_LineAmount,LineItem_TrackingCategory1_Id," + 
							"LineItem_TrackingCategory2_Id,LineItem_DiscountRate,LineAmountTypes,Reference," +
							"BrandingThemeId,CurrencyCode,Status,SubTotal,TotalTax,Total,HasAttachments) values (" +
							j + ",'" + repeatingInvoiceData.getRepeatingInvoiceID() + 
							repeatingInvoiceData.getType() + "','" + 
							repeatingInvoiceData.getContact().getContactID() + "','" + 
							repeatingInvoiceData.getContact().getName() + "','" + repeatingInvoiceData.getSchedule().getPeriod() +
							repeatingInvoiceData.getSchedule().getUnit() + "','" +
							repeatingInvoiceData.getSchedule().getDueDate() + "','" +
							repeatingInvoiceData.getSchedule().getDueDateType() + "','" +
							repeatingInvoiceData.getSchedule().getStartDate() + "','" +
							repeatingInvoiceData.getSchedule().getNextScheduledDate() + "','" +
							repeatingInvoiceData.getSchedule().getEndDate() + "','" + 
							lineData.get(j).getDescription() + "','" + lineData.get(j).getQuantity() + "','" + 
							lineData.get(j).getUnitAmount() + "','"  + lineData.get(j).getItemCode() + "','" +
							lineData.get(j).getAccountCode() + "','" + lineData.get(j).getTaxType() + "','" +
							lineData.get(j).getTaxAmount().doubleValue() + "','" + lineData.get(j).getLineAmount().doubleValue() + "','" +
							lineData.get(j).getTracking().get(0).getTrackingCategoryID() + "','" +
							lineData.get(j).getTracking().get(1).getTrackingCategoryID() + "','" +
							lineData.get(j).getDiscountRate().doubleValue() + "','" + 
							repeatingInvoiceData.getLineAmountTypes().toArray().toString() + "','" + 
							repeatingInvoiceData.getReference() + "','" + repeatingInvoiceData.getBrandingThemeID() +
							"','" + repeatingInvoiceData.getCurrencyCode().toString() +"','" +
							repeatingInvoiceData.getStatus().toString() + "','" + 
							repeatingInvoiceData.getSubTotal().toString() + "','" +
							repeatingInvoiceData.getTotalTax().doubleValue() + "','" +
							repeatingInvoiceData.getTotal().doubleValue() + "','" +
							repeatingInvoiceData.isHasAttachments() +")";


					ResultSet rs = stmt.executeQuery(sqlQuery);
				}

			}			
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public  void insertManualJournalsValues() {

		// TODO Auto-generated method stub
		connectionToMDB();
		// After connections the corresponding values are to be inserted. while coming out close.
		try {

			String sqlQuery = "";
			List<ManualJournal> manualJournalList = client.getManualJournals();
			int numOfManualJournal = manualJournalList.size();
			int cntForManualJournal = 0;
			int cntForManualJournalLineCnt = 0;
			for (int i=0; i < cntForManualJournal; i++) {
				ManualJournal manualJournalData = manualJournalList.get(i);
				List<ManualJournalLine>  lineData = manualJournalData.getJournalLines();
				int manualJournalLineDataSize = lineData.size();
				for (int j=0; j <  manualJournalLineDataSize; j++) {
					cntForManualJournalLineCnt++;
					sqlQuery = "Insert into ManualJournals(ID,ManualJournalId,Narration,JournalLine_LineAmount," + 
							"JournalLine_AccountCode,JournalLine_Description,JournalLine_TaxType," +
							"JournalLine_TrackingCategory1_Id,JournalLine_TrackingCategory2_Id,Date," + 
							" Status,URL,ShowOnCashBasisReports,LineAmountTypes,UpdatedDateUTC) values (" +
							cntForManualJournalLineCnt + ",'" + 
							manualJournalData.getManualJournalID() + "','"+ 
							manualJournalData.getNarration() + "','" + "','" + 
							manualJournalData.getJournalLines().get(j).getLineAmount() + "','" +
							manualJournalData.getJournalLines().get(j).getAccountCode() + "','" +
							manualJournalData.getJournalLines().get(j).getDescription() + "','" +
							manualJournalData.getJournalLines().get(j).getTaxType() + "','" + 
							manualJournalData.getJournalLines().get(j).getTracking().getTrackingCategoryID() + "','" +
							manualJournalData.getJournalLines().get(j+1).getTracking().getTrackingCategoryID() + "','" +
							manualJournalData.getDate() + manualJournalData.getStatus() + 
							manualJournalData.getUrl() + manualJournalData.isShowOnCashBasisReports() + 
							manualJournalData.getLineAmountTypes().toArray().toString() +
							manualJournalData.getUpdatedDateUTC() + ")";
					ResultSet rs = stmt.executeQuery(sqlQuery);
				}

			}			
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




	}

	public void insertTrackingCategoriesValues() {
		// TODO Auto-generated method stub
		connectionToMDB();
		// After connections the corresponding values are to be inserted. while coming out close.
		try {

			String sqlQuery = "";
			List<TrackingCategory> trackingCategoryList = client.getTrackingCategories();
			int numOfTrackingCategory = trackingCategoryList.size();
			int cntForTrackingCategory = 0;
			int cntForCategory = 0;
			for (int i=0; i < cntForTrackingCategory; i++) {
				TrackingCategory trackingCategoryData = trackingCategoryList.get(i);
				List<TrackingCategoryOption>  optionsData = trackingCategoryData.getOptions();
				int optionsDataSize = optionsData.size();
				for (int j=0; j < optionsDataSize; j++) {
					cntForCategory++;
					sqlQuery = " Insert into TrackingCategories( " +
							" ID, TrackingCategoryId,TrackingOptionId,Name,Status,OptionName) " +
							" values (" + cntForCategory + ",'" + 
							trackingCategoryData.getTrackingCategoryID() + "','" + 
							optionsData.get(j).getTrackingOptionID() + 
							trackingCategoryData.getName() + "','" +
							trackingCategoryData.getStatus() + "','" +
							optionsData.get(j).getName() + "')";

					ResultSet rs = stmt.executeQuery(sqlQuery);
				}

			}			
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public  void insertPaymentsValues() {

		connectionToMDB();
		// After connections the corresponding values are to be inserted. while coming out close.
		try {

			String sqlQuery = "";
			List<Payment> paymentList = client.getPayments();
			int numOfPayments = paymentList.size();
			int cntForPayment = 0;
			for (int i=0; i < cntForPayment; i++) {
				Payment paymentData = paymentList.get(i);
				sqlQuery = "Insert into Payments(ID,PaymentId,Date,Amount,CurrencyRate,Reference,PaymentType,Status,UpdatedDateUTC,InvoiceId,InvoiceNumber,CreditNoteId,CreditNoteNumber," +
						" AccountId,AccountCode) values ( " + i + ",'" + paymentData.getPaymentID() + "','" +
						paymentData.getDate() + "'," + paymentData.getAmount() + "," +
						paymentData.getCurrencyRate() + ",'" + paymentData.getReference() + "','" +
						paymentData.getPaymentType() + "','" + paymentData.getStatus() + "','" +
						paymentData.getUpdatedDateUTC() + "','" + paymentData.getInvoice().getInvoiceID() +
						"','" + paymentData.getInvoice().getInvoiceNumber() + 
						"','" + paymentData.getInvoice().getCreditNotes().get(i).getCreditNoteID() + "','" +
						paymentData.getInvoice().getCreditNotes().get(i).getCreditNoteNumber() + 
						paymentData.getAccount().getAccountID() + "','" + paymentData.getAccount().getCode() + "')";
				ResultSet rs = stmt.executeQuery(sqlQuery);

			}			
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

	public void insertCreditNotesValues() {

		connectionToMDB();
		// After connections the corresponding values are to be inserted. while coming out close.
		try {

			String sqlQuery = "";
			List<CreditNote> creditNoteList = client.getCreditNotes();
			int numOfCreditNotes = creditNoteList.size();
			int cntForCreditNotes = 0;
			for (int i=0; i < cntForCreditNotes; i++) {
				CreditNote creditNoteData = creditNoteList.get(i);
				List<LineItem> lineItems = creditNoteData.getLineItems();
				int numOfLineItems = lineItems.size();
				for (int j = 0; j < numOfLineItems; j++) {

					sqlQuery = " Insert into CreditNotes( ID,creditNoteId,creditNoteNumber,Reference,Type," +
							" Contact_ContactId,Contact_Name,Date,Status,LineAmountTypes," +
							" LineItem_Description,LineItem_Quantity,LineItem_UnitAmount,LineItem_ItemCode," + 
							" LineItem_AccountCode,LineItem_TaxType,LineItem_TaxAmount,LineItem_LineAmount, " +
							" LineItem_TrackingCategory1_Id,LineItem_TrackingCategory2_Id,LineItem_DiscountRate," +
							" SubTotal,TotalTax,Total,UpdatedDateUTC,CurrencyCode,FullyPaidOnDate,SentToContact," +
							" BrandingThemeId,CurrencyRate,RemainingCredit ) values ( " + i + ",'" + 
							creditNoteData.getCreditNoteID() + "','" + creditNoteData.getCreditNoteNumber() + "','" + 
							creditNoteData.getReference() + "','" + creditNoteData.getType() + "','" + 
							creditNoteData.getContact().getContactID() + "','" + creditNoteData.getContact().getName() + "','" +
							creditNoteData.getDate() + "','" + creditNoteData.getStatus() + "','" + 
							creditNoteData.getLineAmountTypes().toArray().toString() + "','" + 
							creditNoteData.getLineItems().get(j).getDescription() + "','" + 
							creditNoteData.getLineItems().get(j).getQuantity().doubleValue() + "','" +
							creditNoteData.getLineItems().get(j).getUnitAmount().doubleValue() +  "','" +
							creditNoteData.getLineItems().get(j).getItemCode() + "','" +
							creditNoteData.getLineItems().get(j).getAccountCode() + "','" +
							creditNoteData.getLineItems().get(j).getTaxType() + "','"+
							creditNoteData.getLineItems().get(j).getTaxAmount() + "','" +
							creditNoteData.getLineItems().get(j).getLineAmount() + "','" +
							creditNoteData.getLineItems().get(j).getTracking().get(0).getTrackingCategoryID() + "','" +
							creditNoteData.getLineItems().get(j).getTracking().get(1).getTrackingCategoryID() + "','" +
							creditNoteData.getLineItems().get(j).getDiscountRate() + "','" +
							creditNoteData.getSubTotal().doubleValue() + "','" + creditNoteData.getTotalTax().doubleValue() + "','" +	
							creditNoteData.getTotal().doubleValue() + "','" + creditNoteData.getUpdatedDateUTC() + "','" + 
							creditNoteData.getCurrencyCode() + "','" + creditNoteData.getFullyPaidOnDate() + "','" + 
							creditNoteData.getSentToContact() + "','" + creditNoteData.getBrandingThemeID() + "','" +
							creditNoteData.getCurrencyRate().doubleValue() + ");";		

					ResultSet rs = stmt.executeQuery(sqlQuery);
				}
			}			
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

	public void insertUsersValues() {

		connectionToMDB();
		// After connections the corresponding values are to be inserted. while coming out close.
		try {

			String sqlQuery = "";
			List<User> userList = client.getUsers();
			int numOfUsers = userList.size();
			int cntForUser = 0;
			for (int i=0; i < cntForUser; i++) {
				User userData = userList.get(i);
				sqlQuery = "Insert into Users(ID,userId,FirstName,LastName,UpdatedDateUTC,IsSubscriber,OrganisationRole) values (" + 
						i + ",'" + userData.getUserID() + "','"
						+ userData.getFirstName() + "','" +
						userData.getLastName() + "','" + 
						userData.getUpdatedDateUTC() + "','" +
						userData.getIsSubscriber() + "','" +
						userData.getOrganisationRole() + "')";

				ResultSet rs = stmt.executeQuery(sqlQuery);

			}			
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void insertBankTransactionsValues() {
		connectionToMDB();
		// After connections the corresponding values are to be inserted. while coming out close.
		try {

			String sqlQuery = "";
			List<BankTransaction> bankTransactionList = client.getBankTransactions();
			int numOfBankTransaction = bankTransactionList.size();
			int cntForBankTransaction = 0;
			for (int i=0; i < cntForBankTransaction; i++) {
				BankTransaction bankTransactionData = bankTransactionList.get(i);
				List<LineItem> lnItem = bankTransactionList.get(i).getLineItems();
				int numOfLnItems = lnItem.size();
				for (int j = 0; j < numOfLnItems; j++ ) {
					sqlQuery = " Insert into BankTransactions( ID,	BankTransactionId,	" +
							" Type,	Contact_ContactName,	Contact_ContactId,	BankAccount_AccountId,	" +
							" BankAccount_Code,	LineItem_Description,	LineItem_Quantity,	LineItem_UnitAmount," +
							" LineItem_AccountCode,	LineItem_TaxType,	LineItem_TaxAmount,	LineItem_LineAmount, " +
							" LineItem_TrackingCategory1_Id,	LineItem_TrackingCategory2_Id,	IsReconciled,	Date," + 
							" Reference,	CurrencyRate,		Status,	SubTotal,	TotalTax,	Total,	" +
							" UpdatedDateUTC) values (" +
							i + ",'"+ bankTransactionData.getBankTransactionID() + "','" +
							bankTransactionData.getType() + "','" + 
							bankTransactionData.getContact().getName() + "','" +
							bankTransactionData.getContact().getContactID() + "','" +
							bankTransactionData.getBankAccount().getAccountID() + "','" +
							bankTransactionData.getBankAccount().getCode() + "','" +
							bankTransactionData.getLineItems().get(j).getDescription() + "','" +
							bankTransactionData.getLineItems().get(j).getQuantity() + "','" +
							bankTransactionData.getLineItems().get(j).getLineAmount().doubleValue() + "','" + 
							bankTransactionData.getLineItems().get(j).getAccountCode() + "','" +
							bankTransactionData.getLineItems().get(j).getTaxType() + "','" + 
							bankTransactionData.getLineItems().get(j).getTaxAmount().doubleValue() + "','" +
							bankTransactionData.getLineItems().get(j).getLineAmount().doubleValue() + "','" +
							bankTransactionData.getLineItems().get(j).getTracking().get(0).getTrackingCategoryID() + "','" +
							bankTransactionData.getLineItems().get(j).getTracking().get(1).getTrackingCategoryID() + "','" +
							bankTransactionData.getIsReconciled() + "','" + bankTransactionData.getDate() + "','" +
							bankTransactionData.getReference() + "','" + bankTransactionData.getCurrencyRate().doubleValue() + "','" +
							bankTransactionData.getStatus().toString() + "','" +
							bankTransactionData.getSubTotal().doubleValue() + "','" +
							bankTransactionData.getTotalTax().doubleValue() + "','" +
							bankTransactionData.getTotal().doubleValue() + "','" +
							bankTransactionData.getUpdatedDateUTC() + "')" ;


					ResultSet rs = stmt.executeQuery(sqlQuery);
				}
			}			
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

	public void insertJournalsValues() {
		connectionToMDB();
		// After connections the corresponding values are to be inserted. while coming out close.
		try {

			String sqlQuery = "";
			List<Journal> journalList = client.getJournals();
			int numOfJournals = journalList.size();
			int cntForJournal = 0;
			for (int i=0; i < cntForJournal; i++) {
				Journal journalData = journalList.get(i);
				List<JournalLine> jrnlLnList = journalData.getJournalLines();
				int jrnlLnListSize = jrnlLnList.size();
				for (int j = 0; j < jrnlLnListSize; j++) {
					JournalLine jnlLine =  jrnlLnList.get(j);
					sqlQuery = "Insert into Journals(ID,JournalId,JournalDate,JournalNumber,CreatedDateUTC," + 
							"Reference,JournalLine_JournalLineId,JournalLine_AccountId,JournalLine_AccountCode," +
							"JournalLine_AccountType,JournalLine_AccountName,JournalLine_NetAmount,JournalLine_GrossAmount," +
							"JournalLine_TaxAmount,JournalLine_TaxType,JournalLine_TaxName,JournalLine_TrackingCategory1_Id," +
							"JournalLine_TrackingCategory1_Name,JournalLine_TrackingCategory1_Option," + 
							"JournalLine_TrackingCategory1_OptionId,JournalLine_TrackingCategory2_Id," + 
							"JournalLine_TrackingCategory2_Name,JournalLine_TrackingCategory2_Option," +
							"JournalLine_TrackingCategory2_OptionId) values (" + i + ",'" +
							journalData.getJournalID() + "','" + journalData.getJournalNumber() + "','" +
							journalData.getCreatedDateUTC() + "','" + journalData.getReference() + "','" +
							jnlLine.getJournalLineID() + "','" + jnlLine.getAccountID() + "','" +
							jnlLine.getAccountCode() + "','" + 
							jnlLine.getAccountType() + "','" +
							jnlLine.getAccountName() + "','" + 
							jnlLine.getNetAmount().doubleValue() + "','" +
							jnlLine.getGrossAmount().doubleValue() + "','" +
							jnlLine.getTaxAmount().doubleValue() + "','" +
							jnlLine.getTaxType() + "','" +
							jnlLine.getTaxName() + "','" +
							jnlLine.getTrackingCategories().get(0).getTrackingCategoryID() + "','" +
							jnlLine.getTrackingCategories().get(0).getName() + "','" +
							jnlLine.getTrackingCategories().get(0).getOption() + "','" +
							jnlLine.getTrackingCategories().get(0).getOptions().get(0).getTrackingOptionID() + "','" +
							jnlLine.getTrackingCategories().get(1).getTrackingCategoryID() + "','" + 
							jnlLine.getTrackingCategories().get(1).getName() + "','" + 
							jnlLine.getTrackingCategories().get(1).getOption() + "','" +
							jnlLine.getTrackingCategories().get(1).getOptions().get(1).getTrackingOptionID() + "')";

					ResultSet rs = stmt.executeQuery(sqlQuery);
				}
			}			
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

	public void insertBankTransfersValues() {

		connectionToMDB();
		// After connections the corresponding values are to be inserted. while coming out close.
		try {

			String sqlQuery = "";
			List<BankTransfer> bankTransferList = client.getBankTransfers();
			int numOfBankTransfer = bankTransferList.size();
			int cntForBankTransfer = 0;
			for (int i=0; i < cntForBankTransfer; i++) {
				BankTransfer bankTransferData = bankTransferList.get(i);
				sqlQuery = "Insert into BankTransfers(BankTransferId,FromBankAccount_Code,FromBankAccount_AccountId," +
						"FromBankAccount_Name,ToBankAccount_Code,ToBankAccount_AccountId,ToBankAccount_Name, " + 
						" Amount,Date,CurrencyRate,FromBankTransactionID,ToBankTransactionID,HasAttachments ) values (" +
						i + ",'" + bankTransferData.getBankTransferID() + "','" +
						bankTransferData.getFromBankAccount().getCode() + "','" + 
						bankTransferData.getFromBankAccount().getAccountID() + "','" +
						bankTransferData.getFromBankAccount().getName() + "','" +
						bankTransferData.getToBankAccount().getCode() +"','" +
						bankTransferData.getToBankAccount().getAccountID() + "','" +
						bankTransferData.getToBankAccount().getName()  +"','" +
						bankTransferData.getAmount().doubleValue() + "','" +
						bankTransferData.getDate().toString() + "','" +
						bankTransferData.getCurrencyRate().doubleValue() +"','" +
						bankTransferData.getFromBankTransactionID().toString() + "','" +
						bankTransferData.getToBankTransactionID().toString() + "','" +
						bankTransferData.isHasAttachments() +"')";
				ResultSet rs = stmt.executeQuery(sqlQuery);

			}			
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

	public void insertCurrenciesValues() {
		connectionToMDB();
		// After connections the corresponding values are to be inserted. while coming out close.
		try {

			String sqlQuery = "";
			List<Currency> currencyList = client.getCurrencies();
			int numOfCurrency = currencyList.size();
			int cntForCurrency = 0;
			for (int i=0; i < cntForCurrency; i++) {
				Currency currencyData = currencyList.get(i);
				sqlQuery = "Insert into Currencies(ID,Code,Description) values (" + 
						i + ",'" + currencyData.getCode().toString() + "','"
						+ currencyData.getDescription() + "')";
				ResultSet rs = stmt.executeQuery(sqlQuery);

			}			
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public  void insertContactsValues() {
		connectionToMDB();
		// After connections the corresponding values are to be inserted. while coming out close.
		try {

			String sqlQuery = "";
			List<Contact> contactList = client.getContacts();
			int numOfContact = contactList.size();
			int cntForContact = 0;
			for (int i=0; i < cntForContact; i++) {
				Contact contactData = contactList.get(i);
				sqlQuery = " INSERT INTO Contact(ID,ContactId,ContactNumber,ContactStatus,Name,FirstName," +
						"LastName,EmailAddress,SkypeUserName,BankAccountDetails,TaxNumber,AccountsReceivableTaxType," +
						"AccountsPayableTaxType,AddressType,AddressLine1,AddressLine2,AddressLine3,AddressLine4,City," +
						"Region,PostalCode,Country,AttentionTo,PhoneType,PhoneNumber,PhoneAreaCode,PhoneCountryCode," +
						"UpdatedDateUTC,ContactGroup,IsSupplier,IsCustomer,BrandingTheme,DefaultCurrency,Balances," +
						"Website,ContactPersons,HasAttachments) values (" + i + "','" + contactData.getContactID() +
						"','" + contactData.getContactNumber() + "','" + contactData.getContactStatus() + "','" +
						contactData.getName() + "','" + contactData.getFirstName() + "','" + contactData.getLastName() + "','" +
						contactData.getEmailAddress() + "','" + contactData.getSkypeUserName() + "','" + 
						contactData.getBankAccountDetails().toString() + "','" + contactData.getTaxNumber() + "','" +
						contactData.getAccountsReceivableTaxType() + "','" + contactData.getAccountsPayableTaxType() + "','" +
						"','" + contactData.getAddresses().get(0).getAddressLine1() + "','" + contactData.getAddresses().get(0).getAddressLine1() 
						+ "','" + contactData.getAddresses().get(0).getAddressLine3() + "','" + contactData.getAddresses().get(0).getAddressLine4() + "','" +
						contactData.getAddresses().get(0).getCity() + "','" + contactData.getAddresses().get(0).getRegion() + "','" +
						contactData.getAddresses().get(0).getPostalCode() + "','" + contactData.getAddresses().get(0).getCountry() + "','" +
						contactData.getAddresses().get(0).getAttentionTo() + "','" + contactData.getPhones().get(0).getPhoneType().toString() + "','" +
						contactData.getPhones().get(0).getPhoneNumber() + "','" + contactData.getPhones().get(0).getPhoneAreaCode() + "','" +
						contactData.getPhones().get(0).getPhoneCountryCode() + "','" + contactData.getUpdatedDateUTC() +
						"','" + contactData.getContactGroups().toString() + "','" + contactData.isIsSupplier() + "','" +
						contactData.isIsCustomer() + "','" + contactData.getBrandingTheme().toString() + "','" +
						contactData.getDefaultCurrency().toString() + contactData.getBalances().toString() +
						contactData.getWebsite() + "','" + contactData.getContactPersons().get(0).toString() +
						contactData.isHasAttachments() + ")";


				ResultSet rs = stmt.executeQuery(sqlQuery);

			}			
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public  void insertAccountsValues() {

		connectionToMDB();
		// After connections the corresponding values are to be inserted. while coming out close.
		try {

			String sqlQuery = "";
			List<Account> accountList = client.getAccounts();
			int numOfAccount = accountList.size();
			int cntForAccount = 0;
			for (int i=0; i < cntForAccount; i++) {
				Account accountData = accountList.get(i);
				sqlQuery = 	" INSERT INTO Accounts( accountId,	Code,	Name,	Type,	Description,	TaxType,	" + 
						" EnablePaymentsToAccount,	ShowInExpenseClaims,	Class,	Status,	SystemAccount,	" +
						" BankAccountNumber,	CurrencyCode,	ReportingCode,	ReportingCodeName) " +
						" values ( '" +
						accountData.getAccountID() + "','" + accountData.getCode() + "','"	 + accountData.getName() 
						+ "','" + accountData.getType() + "','" + accountData.getDescription() + "','" + accountData.getTaxType() + "','"	
						+ accountData.isEnablePaymentsToAccount() + "','" + accountData.isShowInExpenseClaims() + "','" + accountData.getClass().getName() +
						"','" + accountData.getStatus() + "','" + accountData.getSystemAccount().name() + "','" +
						accountData.getBankAccountNumber() + "','"	+ accountData.getCurrencyCode().toString() + "','" +
						accountData.getReportingCode() + "','" + accountData.getReportingCodeName() + "')" ;

				ResultSet rs = stmt.executeQuery(sqlQuery);

			}			
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

	public void insertBrandingThemesValues() {

		connectionToMDB();
		// After connections the corresponding values are to be inserted. while coming out close.
		try {

			String sqlQuery = "";
			List<BrandingTheme> brandingThemeList = client.getBrandingThemes();
			int numOfBrandingTheme = brandingThemeList.size();
			int cntForBrandingTheme = 0;
			for (int i=0; i < cntForBrandingTheme; i++) {
				BrandingTheme brandingThemeData = brandingThemeList.get(i);
				sqlQuery = "Insert into BrandingThemes(BrandingThemeId,Name,SortOrder,CreatedDateUTC) " +
						" values ( " +
						brandingThemeData.getBrandingThemeID() + ",'" + 
						brandingThemeData.getName() + "','" + 
						brandingThemeData.getSortOrder() + "','" + 
						brandingThemeData.getCreatedDateUTC() + "')" ;

				ResultSet rs = stmt.executeQuery(sqlQuery);

			}			
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

	public  void insertTaxRatesValues() {
		connectionToMDB();
		// After connections the corresponding values are to be inserted. while coming out close.
		try {

			String sqlQuery = "";
			List<TaxRate> taxRateList = client.getTaxRates();
			int numOfTaxRate = taxRateList.size();
			int cntForTaxRate = 0;
			for (int i=0; i < cntForTaxRate; i++) {
				TaxRate taxRateData = taxRateList.get(i);
				sqlQuery = "Insert into TaxRates(ID,TaxRatesId,TaxType,Name,Status,ReportTaxType,CanApplyToAssets,CanApplyToEquity,CanApplyToExpenses,CanApplyToLiabilities,CanApplyToRevenue,DisplayTaxRate, " +
						"EffectiveRate ) values (" + 
						i + ",'" + 
						i + "','" + taxRateData.getTaxType() + "','" +
						taxRateData.getTaxType() + "','" + taxRateData.getName() + "','" +
						taxRateData.getStatus() + "','" + 
						taxRateData.getTaxType() + "','" +
						taxRateData.isCanApplyToAssets() + "','" +
						taxRateData.isCanApplyToEquity() + "','" +
						taxRateData.isCanApplyToExpenses() + "','" +
						taxRateData.isCanApplyToLiabilities() + "','" +
						taxRateData.isCanApplyToRevenue() + "','" +
						taxRateData.getDisplayTaxRate() + "'," + 
						taxRateData.getEffectiveRate().doubleValue() + ")";
				ResultSet rs = stmt.executeQuery(sqlQuery);

			}			
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public void insertExpenseClaimsValues() {

		connectionToMDB();
		// After connections the corresponding values are to be inserted. while coming out close.
		try {

			String sqlQuery = "";
			List<ExpenseClaim> expenseClaimList = client.getExpenseClaims();
			int numOfExpenseClaim = expenseClaimList.size();
			int cntForExpenseClaim = 0;
			for (int i=0; i < cntForExpenseClaim; i++) {
				ExpenseClaim expenseClaimData = expenseClaimList.get(i);
				List<Receipt> receiptList = expenseClaimData.getReceipts();
				int numOfReceipts = receiptList.size();
				for (int j = 0; j < numOfReceipts; j++) {
					Receipt receipt = receiptList.get(j);
					sqlQuery = " Insert into ExpenseClaims(ID,ExpenseClaimId,UserId,ReceiptId,Status,UpdatedDateUTC," +
							" Total) values (" + i + "','" + 
							expenseClaimData.getExpenseClaimID() + "','" + expenseClaimData.getUser().getUserID() + 
							"','" + expenseClaimData.getReceipts().get(j).getReceiptID() +"','"+
							expenseClaimData.getStatus() + "','" + expenseClaimData.getUpdatedDateUTC() + "','" + 
							expenseClaimData.getTotal() + "','" + expenseClaimData.getTotal().doubleValue() + "')";
					ResultSet rs = stmt.executeQuery(sqlQuery);
				}
			}			
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




	}

	public void insertItemsValues() {
		connectionToMDB();
		// After connections the corresponding values are to be inserted. while coming out close.
		try {

			String sqlQuery = "";
			List<Item> itemsList = client.getItems();
			int numOfItems = client.getItems().size();
			int cntForItem = 0;
			for (int i=0; i < cntForItem; i++) {
				Item itemData = itemsList.get(i);

				sqlQuery = "Insert into Items(ID,ItemId,Code,Description,PurchaseDetails_UnitPrice," +
						"PurchaseDetails_AccountCode,PurchaseDetails_TaxType,SalesDetails_UnitPrice," +
						"SalesDetails_AccountCode,SalesDetails_TaxType,UpdatedDateUTC) values (" + i + ",'"
						+ itemData.getItemID() + "','" + 
						itemData.getDescription() + "','" + itemData.getPurchaseDetails().getUnitPrice() + "','" +
						itemData.getPurchaseDetails().getAccountCode() + "','" +
						itemData.getPurchaseDetails().getTaxType() + "','" +
						itemData.getSalesDetails().getUnitPrice() + "','" + 
						itemData.getSalesDetails().getAccountCode() +  "','" + 
						itemData.getSalesDetails().getTaxType() +  "','" + 
						itemData.getUpdatedDateUTC() + "')";
				ResultSet rs = stmt.executeQuery(sqlQuery);

			}			
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void insertReceiptsValues() {

		connectionToMDB();
		// After connections the corresponding values are to be inserted. while coming out close.
		try {

			String sqlQuery = "";
			List<Receipt> receiptsList = client.getReceipts();
			int numOfReceipts = receiptsList.size();
			int cntForReceipt = 0;
			for (int i=0; i < cntForReceipt; i++) {
				Receipt receiptData = receiptsList.get(i);
				List<LineItem> lnItems = receiptData.getLineItems();
				int numOfLineItems = lnItems.size();
				for (int j = 0; j <  numOfLineItems; j++ ) {
					LineItem lnItem = lnItems.get(j);
					sqlQuery = "Insert into Receipts(ID,ReceiptId,Date,Contact_ContactId,Contact_Name," + 
							"LineItem_Description,LineItem_UnitAmount,LineItem_AccountCode,LineItem_Quantity," + 
							"LineItem_TaxType,LineItem_LineAmount,LineItem_TrackingCategory1_Id," +
							"LineItem_TrackingCategory2_Id,LineItem_DiscountRate,User_UserId,LineAmountTypes," +
							" SubTotal,TotalTax,Total,Status,ReceiptNumber,UpdatedDateUTC,HasAttachments,URL )  values (" + 
							i + ",'" + receiptData.getReceiptID() + "','" + 
							receiptData.getDate() + "','" +  receiptData.getContact().getContactID() + "','" +
							receiptData.getContact().getName() + "','" +
							lnItem.getDescription() + "','" + 
							lnItem.getLineAmount() + "','" + lnItem.getAccountCode() + "','" +
							lnItem.getQuantity().doubleValue() + "','" + 
							lnItem.getTaxType() + "','" + 
							lnItem.getLineAmount().doubleValue() + "','" + 
							lnItem.getTracking().get(0).getTrackingCategoryID() + "','" +
							lnItem.getTracking().get(1).getTrackingCategoryID() + "','" +
							lnItem.getDiscountRate().doubleValue() + "','" +
							receiptData.getUser().getUserID() + "','" +
							receiptData.getLineAmountTypes().toString() + "','" +
							receiptData.getSubTotal().doubleValue() + "','" + receiptData.getTotalTax().doubleValue() + "','" + 
							receiptData.getTotal().doubleValue() + "','" + receiptData.getStatus() + "','" +
							receiptData.getReceiptNumber().toString() + "','" +
							receiptData.getUpdatedDateUTC() + "','" + receiptData.isHasAttachments() + "','" +
							receiptData.getUrl() + "')";

					ResultSet rs = stmt.executeQuery(sqlQuery);
				}

			}			
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

	private  void insertEmployeesValues() {
		connectionToMDB();
		// After connections the corresponding values are to be inserted. while coming out close.
		try {

			String sqlQuery = "";
			List<Employee> employeesList = client.getEmployees();
			int numOfEmployees = client.getEmployees().size();
			int cntForEmployee = 0;
			for (int i=0; i < cntForEmployee; i++) {
				Employee employeeData = employeesList.get(i);
				sqlQuery = "Insert into Employees(ID,EmployeeId,Status,FirstName,LastName,ExternalLink_URL,xternalLink_Description,UpdatedDateUTC) " + 
						"values ( " +  i + ",'" + employeeData.getEmployeeID() + "','" + employeeData.getStatus() + 
						"','" + employeeData.getFirstName() + "','" + employeeData.getLastName() + "','" +
						employeeData.getExternalLink().getUrl() + "','" + 
						employeeData.getExternalLink().getDescription() + "','" +
						employeeData.getUpdatedDateUTC() + "')";
				ResultSet rs = stmt.executeQuery(sqlQuery);

			}			
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void insertInvoicesValues() {

		connectionToMDB();
		// After connections the corresponding values are to be inserted. while coming out close.
		try {

			String sqlQuery = "";
			List<Invoice> invoiceList = client.getInvoices();
			int numOfInvoices = client.getInvoices().size();
			int cntForInvoice = 0;
			for (int i=0; i < numOfInvoices; i++) {
				Invoice invoiceData = invoiceList.get(i);
				int lineItemSize = invoiceData.getLineItems().size();
				List<LineItem>  lineItemsData = invoiceData.getLineItems();
				for (int j=0; j < lineItemSize; j++) {
					cntForInvoice++;
					LineItem lineIt = lineItemsData.get(j);
					sqlQuery = "Insert into Invoices(ID,InvoiceId,Type,ContactId,ContactName,LineItem_Description,LineItem_Quantity,LineItem_UnitAmount,LineItem_ItemCode,LineItem_AccountCode,LineItem_TaxType,LineItem_TaxAmount,LineItem_LineAmount,LineItem_TrackingCategory1_Id,LineItem_TrackingCategory1_Name,LineItem_TrackingCategory1_Option,LineItem_TrackingCategory1_Option_Stat,LineItem_TrackingCategory2_Id,LineItem_TrackingCategory2_Name,LineItem_TrackingCategory2_Option,LineItem_TrackingCategory2_Option_Stat,LineItem_DiscountRate,Date,DueDate,LineAmountTypes,InvoiceNumber,Reference,BrandingThemeId,URL,CurrencyCode,Status,SentToContact,SubTotal,TotalTax,Total,HasAttachments,PaymentIds,AmountDue,AmountPaid,AmountCredited,UpdatedDateUTC,CreditNoteIds,ExpectedPaymentDate,PlannedPaymentDate) " +
							" values ( " + cntForInvoice + ",'" + 
							invoiceData.getInvoiceID() + "','" + 
							invoiceData.getType() + "','" + 
							invoiceData.getContact().getContactID() + "','" +
							invoiceData.getContact().getName() + "','" + 
							lineIt.getDescription() + "','" +
							lineIt.getQuantity() + "','" +
							lineIt.getUnitAmount().doubleValue() +  "','" + 
							lineIt.getItemCode() + "','" +
							lineIt.getAccountCode() + "','" + 
							lineIt.getTaxType() + "','" +
							lineIt.getTaxAmount() + "','" + 
							lineIt.getLineAmount() + "','" +
							lineIt.getTracking().get(0).getTrackingCategoryID() + "','" + 
							lineIt.getTracking().get(0).getName() + "','" +
							lineIt.getTracking().get(0).getOption() +"','" + 
							lineIt.getTracking().get(0).getStatus() + "','" + 
							lineIt.getTracking().get(1).getTrackingCategoryID() + "','" +
							lineIt.getTracking().get(1).getName() + "','" +
							lineIt.getTracking().get(1).getOption() + "','" +
							lineIt.getTracking().get(0).getStatus() + "','" + 
							lineIt.getDiscountRate().doubleValue() + "','" +
							invoiceData.getDate() + "','" + 
							invoiceData.getDueDate() + "','" + 
							invoiceData.getLineAmountTypes().toArray().toString() + "','" +
							invoiceData.getInvoiceNumber() + "','" +
							invoiceData.getReference() + "','" +
							invoiceData.getBrandingThemeID() + "','" +
							invoiceData.getUrl() + "','" +
							invoiceData.getCurrencyCode().name() + "','" +
							invoiceData.getStatus() + "','" + 
							invoiceData.isSentToContact() + "','" + 
							invoiceData.getSubTotal().doubleValue() + "','" +
							invoiceData.getTotalTax().doubleValue() + "','" +
							invoiceData.getTotal().doubleValue() + "','" +
							invoiceData.isHasAttachments() + "','" + 
							invoiceData.getPayments().toArray().toString() + "','" + 
							invoiceData.getAmountDue().doubleValue() + "','" +
							invoiceData.getAmountPaid().doubleValue() + "','" +
							invoiceData.getAmountCredited().doubleValue() + "','" + 
							invoiceData.getUpdatedDateUTC() + "','" + 
							invoiceData.getCreditNotes().toArray().toString() + "','" +
							invoiceData.getExpectedPaymentDate() + "','" +
							invoiceData.getPayments().get(0).getDate() +  "')";
					ResultSet rs = stmt.executeQuery(sqlQuery);
				}
			}			
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
