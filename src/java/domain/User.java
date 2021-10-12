package domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "USER_TABLE",
        uniqueConstraints = @UniqueConstraint(columnNames = "email")
)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id

    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;
    private String Nid;
    private String email;
    private String fname;
    private String lname;
    private String phone;

    @OneToMany(mappedBy = "user")
    private List<SubscriptionType> subscriptionType;

    @ManyToOne
    @JoinColumn(name = "code")
    private Address address;
    @OneToMany(mappedBy = "user")
    private List<Subscription> subscription;
    private String password;
    private String cpassword;
    @Temporal(TemporalType.DATE)
    private Date expiryDate;

    @OneToMany(mappedBy = "user")
    private List<Comment> comment;
//@OneToMany(mappedBy = "user")
//private List<QuizSession> quizSession;

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getNid() {
        return Nid;
    }

    public void setNid(String nid) {
        Nid = nid;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCpassword() {
        return cpassword;
    }

    public void setCpassword(String cpassword) {
        this.cpassword = cpassword;
    }

    public String getAccountType() {
        if (this instanceof Student) {
            return "Student";
        } else if (this instanceof Teacher) {
            return "Teacher";
        } else if (this instanceof Admin) {
            return " Admin";
        }
        return null;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "  " + fname + " " + lname + "";
    }

    public List<Subscription> getSubscription() {
        return subscription;
    }

    public void setSubscription(List<Subscription> subscription) {
        this.subscription = subscription;
    }

    public List<SubscriptionType> getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(List<SubscriptionType> subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Subscription getActiveSubscription() {
        for (Subscription sub : subscription) {
            System.out.println("Check sub Id:" + sub.getSubscriptionId() + " start" + sub.getStartDate().before(new Date()) + " e" + sub.getEndDate().after(new Date()));
            if (sub.getStartDate().before(new Date()) && sub.getEndDate().after(new Date())) {
                return sub;
            }
        }
        return null;
    }

    public Subscription getActiveSubscription(String subscriptionType) {
        System.out.println("Subscription Type Changed: "+subscriptionType);
        List<Subscription> subs = getSubscription();
        if(subs==null){
            return null;
        }
        System.out.println(subs+"");
        for (Subscription sub : subs) {
            System.out.println("Check sub id: "+ sub.getSubscriptionId() + " " + sub.getStartDate().before(new Date()) + " e" + sub.getEndDate().after(new Date()));
            if (sub.getStartDate().before(new Date()) && sub.getEndDate().after(new Date()) && sub.getSubscription().getSubscriptionTypeId().equals(subscriptionType)) {
                return sub;
            }
        }
        return null;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
