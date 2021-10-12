/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LanguageDao;

import domain.Course;
import domain.User;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Mwiza
 */
public class CourseDao extends GenericDao<Course> {

    public List<Course> findBySubscriptionType(String subscriptionType) {
        Session ss = null;

        try {
            ss = NewHibernateUtil.getSessionFactory().openSession();
            List<Course> l = ss.createQuery("FROM Course WHERE level.id = :subscriptionId").setString("subscriptionId", subscriptionType).list();
            ss.close();
            return l;
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
            throw e;
        }
    }
    
    public int incrementVisit(String courseId){
        Session ss = null;

        try {
            ss = NewHibernateUtil.getSessionFactory().openSession();
            Transaction tx = ss.beginTransaction();
            int l = ss.createQuery("UPDATE Course SET visit = visit + 1 WHERE courseCode = :courseId").setString("courseId", courseId).executeUpdate();
            tx.commit();
            ss.close();
            return l;
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
            throw e;
        }
    }
}
