/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LanguageDao;

import domain.Lesson;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Mwiza
 */
public class LessonDao  extends  GenericDao<Lesson>{
    public List<Lesson> findByCourse(String courseId){
        Session ss = null;

        try {
            ss = NewHibernateUtil.getSessionFactory().openSession();
            List<Lesson> l = ss.createQuery("FROM Lesson WHERE course.courseCode = :courseId").setString("courseId", courseId).list();
            ss.close();
            return l;
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            if (ss != null && ss.isOpen()) {
                ss.close();
            }
        }
        return null;
    }
}
