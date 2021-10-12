package Model;

import LanguageDao.AuthenticationService;
import LanguageDao.CourseDao;
import LanguageDao.LessonDao;
import LanguageDao.PdfBuilder;
import LanguageDao.SubscriptionTypeDao;
import domain.Course;
import domain.Student;
import domain.SubscriptionType;
import domain.User;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class CourseModel {

    private CourseDao service = new CourseDao();
    private LessonDao lessonService = new LessonDao();
    private Course course = new Course();
    private SubscriptionTypeDao subservice = new SubscriptionTypeDao();
    private SubscriptionType subscription = new SubscriptionType();
    private List<SubscriptionType> listOfSubscriptionType = subservice.findAll(SubscriptionType.class);
    private List<Course>listOfCourses=service.findAll(Course.class);

    private boolean isUpdateMode = false;

    
    private String SubId;
    public void saveCourse() {

        if (!isUpdateMode) {
            SubscriptionType type = subservice.findById(SubscriptionType.class, SubId);
            System.out.println("Subscription: " + type + " Id:" + SubId);
            course.setLevel(type);
            service.create(course);
            course = new Course();
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Kongeramo byagenze neza","byagenze neza!"));

        } else {
            service.update(course);
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Guhindura isomo byagenze neza","Byahinduwe neza"));

            course = new Course();
            isUpdateMode = false;
        }
    }

    public void updateCourse() {
        try {
            service.update(course);
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"","Byavuguruwe neza!!"));

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Mwihangane","ntibyavuguruwe neza" + e.getMessage()));

        }
    }

    public void deleteContent() {
        try {

            service.delete(course);
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Isomo ryabitswe neza"," ryasibitse neza!"));

        } catch (Exception e) {
            // TODO: handle exception // 
            e.printStackTrace(); 
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Mwihangane ","Ntishoboye kubika isomo mugereze mukanya!"));
        }

    }
    
    
    public void printReport() {
		try {
			List<Course> l = service.findAll(Course.class);
			PdfBuilder pdf = new PdfBuilder();
			pdf.addTitle("Lisiti y'uko amasomo yasuwe");
			pdf.init(3);
			pdf.addTableHeader(new String[] { "code y'isomo", "izina ry'isomo", "inshuro ryasuwe" });
			for (Course cor : l) {
				pdf.addTableCell(cor.getCourseCode() );
				// pdf.addTableCell(user.getLname());
				pdf.addTableCell(cor.getChapter());
				pdf.addTableCell(cor.getVisit()+"");
				
			}
			pdf.download("Uko amasomo yasuwe.pdf");
                        System.out.println("AA");
			FacesContext f=FacesContext.getCurrentInstance();
                        f.addMessage(null, new FacesMessage("byagenze neza"));
                        
		} catch (Exception e) {
			FacesContext f=FacesContext.getCurrentInstance();
                        f.addMessage(null, new FacesMessage(" ntibyagenze neza"));
                        e.printStackTrace();
		}
	}
    
    
    
    
    

    public CourseDao getService() {
        return service;
    }

    public void setService(CourseDao service) {
        this.service = service;
    }

    public LessonDao getLessonService() {
        return lessonService;
    }

    public void setLessonService(LessonDao lessonService) {
        this.lessonService = lessonService;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }


    public SubscriptionType getSubscription() {
        return subscription;
    }

    public void setSubscription(SubscriptionType subscription) {
        this.subscription = subscription;
    }

    public List<SubscriptionType> getListOfSubscriptionType() {
        return listOfSubscriptionType;
    }

    public void setListOfSubscriptionType(List<SubscriptionType> listOfSubscriptionType) {
        this.listOfSubscriptionType = listOfSubscriptionType;
    }

    public SubscriptionTypeDao getSubservice() {
        return subservice;
    }

    public void setSubservice(SubscriptionTypeDao subservice) {
        this.subservice = subservice;
    }

    public boolean isIsUpdateMode() {
        return isUpdateMode;
    }

    public void setIsUpdateMode(boolean isUpdateMode) {
        this.isUpdateMode = isUpdateMode;
    }

    public List<Course> getListOfCourses() {
        User loggedInUser = (User) AuthenticationService.getSession().getAttribute("s_user");
        String subscriptionId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("subscriptionId");
        System.out.println("Subscription ID Changed: "+subscriptionId);
//        if (loggedInUser==null||((loggedInUser instanceof Student && loggedInUser.getActiveSubscription(subscriptionId)==null))){
//            try {
//                FacesContext.getCurrentInstance().getExternalContext().redirect("PaymentForm.xhtml");
//            } catch (IOException ex) {
//                ex.printStackTrace();
//                Logger.getLogger(CourseModel.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        if(subscriptionId!=null){
            return service.findBySubscriptionType(subscriptionId);
        }
        return listOfCourses;
    }
    
    

    public void setListOfCourses(List<Course> listOfCourses) {
        this.listOfCourses = listOfCourses;
    }

    

    public boolean isUpdateMode() {
        return isUpdateMode;
    }

    public void setUpdateMode(boolean isUpdateMode) {
        this.isUpdateMode = isUpdateMode;
    }

    public String getSubId() {
        return SubId;
    }

    public void setSubId(String SubId) {
        this.SubId = SubId;
    }

}
