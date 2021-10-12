package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
@Entity
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int transactionId;
	
	@ManyToOne
private User user;
	@ManyToOne
	private Subscription subscription;
	@OneToOne
	private SubscriptionType subscriptiontype;
	private Double price;
	
@Enumerated(EnumType.STRING)
private TransactionStage stage;
@Column(columnDefinition ="TEXT" )
	private String payload;
	@Enumerated(EnumType.STRING)
	private PaymentChannel channel;
	private String externalTransactionId;
public int getTransactionId() {
	return transactionId;
}
public void setTransactionId(int transactionId) {
	this.transactionId = transactionId;
}

public User getUser() {
	return user;
}
public void setUser(User user) {
	this.user = user;
}
public TransactionStage getStage() {
	return stage;
}
public void setStage(TransactionStage stage) {
	this.stage = stage;
}
public Subscription getSubsription() {
	return subscription;
}
public void setSubsription(Subscription subsription) {
	this.subscription = subsription;
}
public Subscription getSubscription() {
	return subscription;
}
public void setSubscription(Subscription subscription) {
	this.subscription = subscription;
}
public Double getPrice() {
	return price;
}
public void setPrice(Double price) {
	this.price = price;
}
public SubscriptionType getSubscriptiontype() {
	return subscriptiontype;
}
public void setSubscriptiontype(SubscriptionType subscriptiontype) {
	this.subscriptiontype = subscriptiontype;
}
public String getPayload() {
	return payload;
}
public void setPayload(String payload) {
	this.payload = payload;
}

public PaymentChannel getChannel() {
	return channel;
}
public void setChannel(PaymentChannel channel) {
	this.channel = channel;
}
public String getExternalTransactionId() {
	return externalTransactionId;
}
public void setExternalTransactionId(String externalTransactionId) {
	this.externalTransactionId = externalTransactionId;
}


}
