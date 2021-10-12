/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LanguageDao;

import cultureLearning.cultureLearning.exception.InvalidMailException;
import domain.Lesson;
import domain.SubscriptionType;
import domain.Transaction;
import domain.User;
import domain.Video;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

/**
 *
 * @author Mwiza
 * @param <X>
 */
public class GenericDao<X> {

    public void create(X d) {
        Session ss = null;
        X obj;
        try {
            ss = NewHibernateUtil.getSessionFactory().openSession();
            ss.beginTransaction();
            ss.save(d);
            ss.getTransaction().commit();
            ss.close();
        } catch (Exception e) {
            if (ss != null) {
                ss.close();
            }
            throw e;
        }

    }

    public void update(X d) {
        Session ss = null;
        X obj;
        try {
            ss = NewHibernateUtil.getSessionFactory().openSession();
            ss.beginTransaction();
            ss.update(d);
            ss.getTransaction().commit();
            ss.close();
        } catch (Exception e) {
            if (ss != null) {
                ss.close();
            }
            throw e;
        }

    }

    public void delete(X d) {
        Session ss = null;
        X obj;
        try {
            ss = NewHibernateUtil.getSessionFactory().openSession();
            ss.beginTransaction();
            ss.delete(d);
            ss.getTransaction().commit();
            ss.close();
        } catch (Exception e) {
            if (ss != null) {
                ss.close();
            }
            throw e;
        }

    }

    public List<X> findAll(Class kls) {
        Session ss = null;
        X obj;
        try {
            ss = NewHibernateUtil.getSessionFactory().openSession();
            Query q = ss.createQuery("from " + kls.getName());
            List<X> list = q.list();

            ss.close();
            return list;
        } catch (Exception e) {
            if (ss != null) {
                ss.close();
            }
            throw e;
        }

    }

    public X findById(Class kls, Serializable id) {

        Session ss = null;
        X obj;
        try {
            ss = NewHibernateUtil.getSessionFactory().openSession();
            obj = (X) ss.get(kls, id);
            ss.close();
            return obj;
        } catch (Exception e) {
            if (ss != null) {
                ss.close();
            }
            throw e;
        }
    }

    public String uploadFile(Part uploadedFile) throws Exception {
        if (!(new File(
                FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "uploads"
        )).exists()) {
            (new File(
                    FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "uploads"
            )).mkdirs();
        }
        String fileName = UUID.randomUUID() + "-"
                + uploadedFile.getSubmittedFileName();
        InputStream is = uploadedFile.getInputStream();
        File f = new File(
                FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "uploads", fileName);

        System.out.println(f.getAbsolutePath());
        f.createNewFile();
        FileOutputStream file = new FileOutputStream(f);
        byte[] data = new byte[1024];
        int counter;
        while ((counter
                = is.read(data)) > 0) {

            file.write(data, 0, counter);
            file.flush();
        }
        file.close();
        is.close();
        return fileName;
    }

    
    public List getVisits() {

        Session ss = null;
        try {
            ss = NewHibernateUtil.getSessionFactory().openSession();
            SQLQuery q = ss.createSQLQuery("SELECT chapter, visit FROM  course ORDER BY visit DESC as tempcourses ");
            //"(SELECT title, visit FROM courses WHERE TYPE='QuestionBased') UNION (SELECT title, visit FROM lesson WHERE basetype = 'ContentBased') ORDER BY visit DESC");
            q.addScalar("chapter", StringType.INSTANCE);
            q.addScalar("visit", IntegerType.INSTANCE);
            //return q.list();
            return new ArrayList<>();
        } catch (Exception e) {
            if (ss != null) {
                ss.close();
            }
            throw e;
        }
    }

    public List<SubscriptionType> getSubscription() {
        Session ss = null;
        try {

            ss = NewHibernateUtil.getSessionFactory().openSession();
            // TODO Auto-generated method stub
            return ss.createQuery("from SubscriptionType where user='user_userid' && course='course_coursecode' && startdate<=today && enddate<=today").list();

        } catch (Exception e) {

            if (ss != null) {
                ss.close();
            }
            throw e;

        }
    }

    public List<Transaction> listofTransaction() {
        // TODO Auto-generated method stub
        Session ss = null;

        try {
            ss = NewHibernateUtil.getSessionFactory().openSession();
            return ss.createQuery("from Transaction").list();
        } catch (Exception e) {

            if (ss != null) {
                ss.close();
            }
            throw e;

        }

    }

    public List<Transaction> listofPaidTransaction() {
        // TODO Auto-generated method stub
        Session ss = null;

        try {
            ss = NewHibernateUtil.getSessionFactory().openSession();
            return ss.createQuery("from Transaction where stage='PAID'").list();
        } catch (Exception e) {

            if (ss != null) {
                ss.close();
            }
            throw e;

        }

    }

    public User userExist(String email, String password) {
        Session ss = null;
        try {
            ss = NewHibernateUtil.getSessionFactory().openSession();
            User user = (User) ss.createCriteria(User.class
            ).add(Restrictions.eq("email", email)).uniqueResult();
              ss.close();
            if (user
                    == null) {
                throw new InvalidMailException("Imeri yawe ntiyanditse neza");
            }

            if (PasswordProtection.verifyPassword(password, user.getPassword())) {
                return user;
            } else {
                throw new InvalidMailException("Wanditse nabi ijambo ry'ibanga");
            }
          
        } catch (Exception e) {
            if (ss != null&&ss.isOpen()) {
                ss.close();
            }
            throw e;
        }
    }

    public List<User> findAllUser() {
        // TODO Auto-generated method stub
        Session ss = null;
        try {
            ss = NewHibernateUtil.getSessionFactory().openSession();
            Query q=ss.createQuery(" from User");
            List<User>lstUser=q.list();
            ss.close();
            return lstUser;
        } catch (Exception e) {

            if (ss != null) {
                ss.close();
            }
            throw e;

        }

    }

    public void forgotPassword(User user, String npassword, String email) {
        Session ss = null;
        try {

            if (getByEmail(user.getEmail()) != null) {

                String sql = "update User set password=:npassword where email=:email";
                ss = NewHibernateUtil.getSessionFactory().openSession();
                Query query = ss.createQuery(sql);
                query.setParameter("npassword", PasswordProtection.hashPassword(npassword));
                query.setParameter("email", email);

                System.out.println(email);
                System.out.println(npassword);

                query.executeUpdate();
                ss.close();
            } else {
                throw new InvalidMailException("email doesn't exist");
            }
        } catch (Exception e) {
            e.getMessage();
            if (ss != null) {
                ss.close();
            }
            throw e;
        }

    }

    public String search(User user) {

        if (getByEmail(user.getEmail()) != null) {

            //Execution.sendRedirect("/pages/users/ForgetPassword.zul");
            return "ForgetPassword.xhtml";
        } else {
            throw new InvalidMailException("Imeri ntibaho!Shyiramo iyindi");
        }
    }

    public User updatePassword(String email, String oldPassword, String password, String cpassword) {
        Session ss = null;

        try {

            User user = getByEmail(email);
            System.out.println("NNNN: " + oldPassword + " " + password + " " + cpassword);
            if (user != null) {
                if (PasswordProtection.verifyPassword(oldPassword, user.getPassword())) {

                    if (password.equals(cpassword)) {
                        System.out.println(password);
                        user.setPassword(PasswordProtection.hashPassword(password));
                        ss = NewHibernateUtil.getSessionFactory().openSession();
                        org.hibernate.Transaction tx = ss.beginTransaction();
                        ss.update(user);
                        tx.commit();
                        ss.close();
                    } else {
                        throw new InvalidMailException("Amagambo y'ibanga ntabwo asa!");
                    }

                } else {
                    throw new InvalidMailException("Ijambo ryawe ry'inbanga ntabwo ari ryo");
                }
            } else {
                throw new InvalidMailException("iyi konti ntibaho");
            }
            return user;
        } catch (Exception e) {
            if (ss != null) {
                ss.close();
            }
            throw e;
        }
    }

    public User getByEmail(String email) {
        Session ss = null;
        User user;
        try {
            
            ss = NewHibernateUtil.getSessionFactory().openSession();
            user =  (User) ss.createCriteria(User.class).add(Restrictions.eq("email", email)).uniqueResult();
            
            ss.close();
            return user;
            
        } catch (Exception e) {
            if (ss != null) { 
                ss.close();
            }
            throw e;
        }

    }

    public Video getByUrl(String url) {
        Session ss = null;
        Video video;
        try {
            
            ss = NewHibernateUtil.getSessionFactory().openSession();
            video =  (Video) ss.createCriteria(Video.class).add(Restrictions.eq("url", url)).uniqueResult();
            
            ss.close();
            return video;
            
        } catch (Exception e) {
            if (ss != null) { 
                ss.close();
            }
            throw e;
        }

    }

    @SuppressWarnings("unchecked")
    public List<User> listOfLearner() {
        Session ss = null;
        try {
            ss = NewHibernateUtil.getSessionFactory().openSession();
            return ss.createQuery("from Student").list();

        } catch (Exception e) {
            if (ss != null) {
                ss.close();
            }
            throw e;
        }

    }

    public List<User> listOfTeacher() {
        // TODO Auto-generated method stub
        Session ss = null;
        try {
            ss = NewHibernateUtil.getSessionFactory().openSession();
            //return ss.createQuery(" from Teacher").list();
            Query q=ss.createQuery(" from Teacher");
            List<User> listofTacher=q.list();
            ss.close();
            return listofTacher;
        } catch (Exception e) {
            if (ss != null) {
                ss.close();
            }
            throw e;
        }

    }

    public List<Lesson> ViewCourse() {
        
        Session ss = null;
        try {
            ss = NewHibernateUtil.getSessionFactory().openSession();
         
            Query q=ss.createQuery("FROM Course l ORDER BY l.visit DESC");
            List<Lesson>listViewCourse=q.list();
            ss.close();
            return listViewCourse;
        } catch (Exception e) {
            if (ss != null) {
                ss.close();
            }
            throw e;
        }

    }

    public List<Lesson> ingombajwi() {
        // TODO Auto-generated method stub
        Session ss = null;
        try {
            ss = NewHibernateUtil.getSessionFactory().openSession();
            String id = null;
            //id = Executions.getCurrent().getParameter("id");
            System.out.println("Id: " + id + "from Lesson  where course_coursecode='" + id + "' ");
           
            Query q=ss.createQuery("from Lesson where course_coursecode=:coursecode ").setParameter("coursecode", id + "");
            List<Lesson>ingombajwiList=q.list();
            return  ingombajwiList;
        } catch (Exception e) {
            if (ss != null) {
                ss.close();
            }
            throw e;
        }
    }

//    public List<Course> ViewCourses() {
//        // TODO Auto-generated method stub
//        Session ss = null;
//        try {
//            ss = NewHibernateUtil.getSessionFactory().openSession();
//            return ss.createQuery("FROM Course l ORDER BY l.visit DESC").list();
//        } catch (Exception e) {
//            if (ss != null) {
//                ss.close();
//            }
//            throw e;
//        }
//
//    }
}
