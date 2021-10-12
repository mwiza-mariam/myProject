/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import LanguageDao.AuthenticationService;
import LanguageDao.PaymentHelper;
import LanguageDao.PdfBuilder;
import LanguageDao.SubscriptionDao;
import LanguageDao.SubscriptionTypeDao;
import LanguageDao.TransactionDao;
import LanguageDao.UserDao;
import domain.PaymentChannel;
import domain.Subscription;
import domain.SubscriptionType;
import domain.Transaction;
import domain.TransactionStage;
import domain.User;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Mwiza
 */
@ManagedBean(name = "transaction")
public class TransactionModel {

    private Transaction transaction = new Transaction();
    private TransactionDao tdao = new TransactionDao();
    private List<Transaction> listOfAllTransaction = tdao.findAll(Transaction.class);
    private SubscriptionType subtype = new SubscriptionType();
    private User user = new User();
    private UserDao udao = new UserDao();
    private String subId;
    private List<Transaction> paidTransactionList = tdao.listofPaidTransaction();
    private SubscriptionTypeDao sub = new SubscriptionTypeDao();
    private List<SubscriptionType> listOfSubType = sub.findAll(SubscriptionType.class);
    private Subscription subscription = new Subscription();
    private SubscriptionDao subdao = new SubscriptionDao();

    // private List<SubscriptionType> listOfAllSubscriptionCategory=subdao.findAll(SubscriptionType.class);
    @PostConstruct
    private void init() {

        String transactionId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("transaction_id");
        String status = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("status");
        String txRef = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("tx_ref");

        System.out.println("T: " + transactionId + " S: " + status + " TX: " + txRef);

        if (status != null) {
            if (status.equals("successful") && transactionId != null & txRef != null) {
                // Checking ko yishyuye nyabyo
                PaymentHelper.PaymentData paymentData = null;
                try {
                    paymentData = PaymentHelper.getPaymentData(transactionId);
                } catch (Exception e) {
                    e.printStackTrace();
                    paymentFailed(e.getMessage());
                    return;
                }
                TransactionDao transactionService = new TransactionDao();
                // No gu chekcing transaction
                Transaction transaction = transactionService.findById(Transaction.class, Integer.parseInt(txRef.substring(3)));
                if (transaction == null) {
                    paymentFailed("Transaction is null ID: " + Integer.parseInt(txRef.substring(3)));
                    return;
                }
                User user = transaction.getUser();
                if (user == null) {
                    paymentFailed("User is null");
                    return;
                }
                if (transaction.getStage() != null && transaction.getStage().equals(TransactionStage.PAID)) {
                    paymentFailed("Transaction already paid");
                    return;
                }
                Subscription subscription = new Subscription();
                subscription.setStartDate(new Date());
                subscription.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusMonths(6)));
                subscription.setUser(user);
                subscription.setSubscription(transaction.getSubscriptiontype());
                subdao.create(subscription);
                transaction.setSubscription(subscription);
                transaction.setStage(TransactionStage.PAID);
                transaction.setChannel(PaymentChannel.FLUTTERWAVE);
                transaction.setExternalTransactionId(paymentData.getTransactionId());
                transaction.setPayload(paymentData.getPayload());
                tdao.update(transaction);
                User loggedUser = (User) AuthenticationService.getSession().getAttribute("s_user");
                if (loggedUser != null) {
                    loggedUser = sub.getByEmail(loggedUser.getEmail());
                    AuthenticationService.getSession().setAttribute("s_user", loggedUser);
                }

            } else {
                paymentFailed("From flutterwave");
            }
        }

    }

    private String paymentFailed(String comment) {
        System.out.println(comment);

        return "PaymentFail.xhtml";
    }

    public void savePayment() {
        User sessionUser = (User) AuthenticationService.getSession().getAttribute("s_user");
        System.out.println("session user who is going to pay:" + sessionUser);
        if (subtype == null || subId == null) {
            System.out.println("Subtype is null");
            return;
        }
        SubscriptionType subscriptionType = sub.findById(SubscriptionType.class, subId);
        if (subscriptionType == null || sessionUser == null) {
            System.out.println("User or Subscription is null User:" + sessionUser + " Subscrpition:" + subscription);
            return;
        }
        user = sessionUser;
        Double amount = subscriptionType.getPrice();
        Transaction transaction = new Transaction();
        transaction.setPrice(amount);
        transaction.setUser(user);
        transaction.setSubscriptiontype(subscriptionType);
        tdao.create(transaction);
        try {
            //if(transaction!=null) {
            HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String url = req.getRequestURL().toString();
            url = url.substring(0, url.length() - req.getRequestURI().length()) + req.getContextPath() + "/";
            System.out.println(transaction.getTransactionId());
            url = PaymentHelper.payWithMoMo("TX_" + transaction.getTransactionId() + "", null, amount, user.getEmail(), user.getFname(), url + "/faces/student/PaymentSucces.xhtml");
            //Executions.sendRedirect(url);
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
            // Respons
            //Executions.sendRedirect("/Succespayment.zul");
            //Clients.showNotification("You have succesful paid");
            //}

        } catch (Exception e) {
            e.printStackTrace();
            //Clients.showNotification("Internal error", "error", null, "code", 0, true);
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ntibyagenze neza mwongere mugerageze", null));

        }
    }

//	
    public void printReport() {
        try {
            List<Transaction> c = tdao.listofPaidTransaction();
            PdfBuilder pdf = new PdfBuilder();
            pdf.addTitle("raporo y'imari");
            pdf.init(9);
            pdf.addTableHeader(new String[]{"transaction Id", "Stage", "Amazina y'Uwishyuye", "Ikiciro y'ishyuriye", " Amafaranga yishyuye", "Umuyoboro wakoreshejwe", "Italiki yishyuriyeho", "Italiki azarangiriza", "external transactionId"});
            for (Transaction trans : c) {
                pdf.addTableCell(trans.getTransactionId() + "");
                pdf.addTableCell(trans.getStage() + "");
                pdf.addTableCell(trans.getUser().getFname() + " " + trans.getUser().getLname());
                pdf.addTableCell(trans.getSubscriptiontype() + "");
                pdf.addTableCell(trans.getPrice() + "");
                pdf.addTableCell(trans.getChannel() + "");
                pdf.addTableCell(trans.getSubscription().getStartDate() + "");
                pdf.addTableCell(trans.getSubscription().getEndDate() + "");
                pdf.addTableCell(trans.getExternalTransactionId() + "");

            }
            pdf.download("Raporo y'imari.pdf");
            System.out.println("AA");
            FacesContext f = FacesContext.getCurrentInstance();
            f.addMessage(null, new FacesMessage("byagenze neza"));

        } catch (Exception e) {
            FacesContext f = FacesContext.getCurrentInstance();
            f.addMessage(null, new FacesMessage(" ntibyagenze neza"));
            e.printStackTrace();
        }
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public TransactionDao getTdao() {
        return tdao;
    }

    public void setTdao(TransactionDao tdao) {
        this.tdao = tdao;
    }

    public List<Transaction> getListOfAllTransaction() {
        return listOfAllTransaction;
    }

    public void setListOfAllTransaction(List<Transaction> listOfAllTransaction) {
        this.listOfAllTransaction = listOfAllTransaction;
    }

    public SubscriptionType getSubtype() {
        return subtype;
    }

    public void setSubtype(SubscriptionType subtype) {
        this.subtype = subtype;
    }

    public SubscriptionTypeDao getSub() {
        return sub;
    }

    public void setSub(SubscriptionTypeDao sub) {
        this.sub = sub;
    }

    public List<SubscriptionType> getListOfSubType() {
        return listOfSubType;
    }

    public void setListOfSubType(List<SubscriptionType> listOfSubType) {
        this.listOfSubType = listOfSubType;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public SubscriptionDao getSubdao() {
        return subdao;
    }

    public void setSubdao(SubscriptionDao subdao) {
        this.subdao = subdao;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserDao getUdao() {
        return udao;
    }

    public void setUdao(UserDao udao) {
        this.udao = udao;
    }

    public String getSubId() {
        return subId;
    }

    public Double getAmount() {
        if (subId != null) {
            SubscriptionType type = sub.findById(SubscriptionType.class, subId);
            if (type != null) {
                return type.getPrice();
            }
        }
        return null;
    }

    public void setAmount(Double amount) {
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public List<Transaction> getPaidTransactionList() {
        return paidTransactionList;
    }

    public void setPaidTransactionList(List<Transaction> paidTransactionList) {
        this.paidTransactionList = paidTransactionList;
    }

}
