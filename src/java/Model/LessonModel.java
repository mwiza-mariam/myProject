package Model;

import LanguageDao.AuthenticationService;
import LanguageDao.CourseDao;
import LanguageDao.LessonDao;
import domain.Course;
import domain.Lesson;
import domain.Student;
import domain.User;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

@ManagedBean(name = "lesson")
@SessionScoped
public class LessonModel {

    private Course course = new Course();
    private Lesson lesson = new Lesson();
    private LessonDao service = new LessonDao();

    
    private CourseDao courseService = new CourseDao();
    private List<Lesson> listLessons = service.findAll(Lesson.class);
    private String coursecode;
    private List<Lesson> listofContentlesson = service.findAll(Lesson.class);
    private List<Course> listOfCourses = courseService.findAll(Course.class);

    private List<Lesson> listOfContentsInChapter;

    private List<Lesson> listOfingombajwi;

    private String filePath;
    private Part uploadedFile;
    private boolean fileuploaded = false;

    public void upload() {

        if (null != uploadedFile) {
            try {
                InputStream is = uploadedFile.getInputStream();
                filePath = new Scanner(is).useDelimiter("\\A").next();
            } catch (IOException ex) {
            }
        }
    }

    public void updateLesson() {
        try {
            service.update(lesson);
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Ivugururwa ryagenze neza","Ivugururwa ryagenze neza!!"));

        } catch (Exception e) {
            // TODO: handle exception
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"kuvugurwa ntibyagenze neza!","kuvugurwa ntibyagenze neza!"));
            e.getMessage();
            e.printStackTrace();
        }
    }

    public void loadLesson(String id) {

        lesson = service.findById(Lesson.class, id);
    }

    public void saveLesson() throws Exception {
        try {

            if (null != uploadedFile) {
                try {
                    Course c = courseService.findById(Course.class, coursecode);
                    System.out.println("coursecode:" + coursecode + "course:" + c);
                    lesson.setCourse(c);
                    lesson.setFileUpload(UUID.randomUUID().toString() + uploadedFile.getSubmittedFileName());
                    System.out.println("DD:" + FacesContext.getCurrentInstance().getExternalContext().getRealPath("uploadedFile"));
                    copy(uploadedFile.getInputStream(), new FileOutputStream(FacesContext.getCurrentInstance().getExternalContext().getRealPath("uploadedFile")+"/"+lesson.getFileUpload()));
                    
                    service.create(lesson);
                    FacesContext fc = FacesContext.getCurrentInstance();
                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"byoherejwe neza!!", "byoherejwe neza!!"));

                } catch (Exception ex) { 
                    ex.printStackTrace();
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            e.getMessage();
        }
    }

    void copy(InputStream source, OutputStream target) throws IOException {
        byte[] buf = new byte[8192];
        int length;
        while ((length = source.read(buf)) > 0) {
            target.write(buf, 0, length);
        }
       
    }
    public void download(String filename) throws IOException {
        
        try{
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.responseReset();
            ec.setResponseContentType("application/data");
            ec.setResponseHeader("Content-Disposition", "attachment;filename="+filename );
           FileInputStream input=new FileInputStream(FacesContext.getCurrentInstance().getExternalContext().getRealPath("uploadedFile")+"/"+filename);
            
            OutputStream output=ec.getResponseOutputStream();
            copy(input, output);
              fc.responseComplete();

        }catch(Exception e){
            e.printStackTrace();
            e.getMessage();
        }
    
   
       
    }
    public void deleteContent() {
        try {

            service.delete(lesson);
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO,"byasibitse!!", "byasibitse!!"));

        } catch (Exception e) {
            // TODO: handle exception //
            e.printStackTrace();
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO," ntibyagenze neza!"," ntibyagenze neza!" + e.getMessage()));
        }

    }

    public LessonDao getService() {
        return service;
    }

    public void setService(LessonDao service) {
        this.service = service;
    }

    
    public CourseDao getCourseService() {
        return courseService;
    }

    public void setCourseService(CourseDao courseService) {
        this.courseService = courseService;
    }

    public List<Lesson> getListLessons() {
        String courseId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("courseId");
        if (courseId!=null) {
            Course c = courseService.findById(Course.class, courseId);
            String subscriptionId = c.getLevel().getSubscriptionTypeId();
            User loggedInUser = (User) AuthenticationService.getSession().getAttribute("s_user");
            if (loggedInUser==null||((loggedInUser instanceof Student && loggedInUser.getActiveSubscription(subscriptionId)==null))){
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("PaymentForm.xhtml");
            } catch (IOException ex) {
                ex.printStackTrace();
                Logger.getLogger(CourseModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            List<String> coursesVisit = (List)AuthenticationService.getSession().getAttribute("coursesVisit");
            if (coursesVisit==null){
                coursesVisit = new ArrayList<>();
                AuthenticationService.getSession().setAttribute("coursesVisit", coursesVisit);
            }
            if(coursesVisit.indexOf(courseId)<0){
                coursesVisit.add(courseId);
                courseService.incrementVisit(courseId);
            }
            List<Lesson> l = service.findByCourse(courseId);
            System.out.println("Size: "+l.size());
            return l;
        }
        return listLessons;
    }

    public void setListLessons(List<Lesson> listLessons) {
        this.listLessons = listLessons;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public List<Lesson> getListofContentlesson() {
        return listofContentlesson;
    }

    public void setListofContentlesson(List<Lesson> listofContentlesson) {
        this.listofContentlesson = listofContentlesson;
    }

    public List<Course> getListOfCourses() {
        return listOfCourses;
    }

    public void setListOfCourses(List<Course> listOfCourses) {
        this.listOfCourses = listOfCourses;
    }

    public Part getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(Part uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public List<Lesson> getListOfContentsInChapter() {
        return listOfContentsInChapter;
    }

    public void setListOfContentsInChapter(List<Lesson> listOfContentsInChapter) {
        this.listOfContentsInChapter = listOfContentsInChapter;
    }

    public List<Lesson> getListOfingombajwi() {
        return listOfingombajwi;
    }

    public void setListOfingombajwi(List<Lesson> listOfingombajwi) {
        this.listOfingombajwi = listOfingombajwi;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isFileuploaded() {
        return fileuploaded;
    }

    public void setFileuploaded(boolean fileuploaded) {
        this.fileuploaded = fileuploaded;
    }

    public String getCoursecode() {
        return coursecode;
    }

    public void setCoursecode(String coursecode) {
        this.coursecode = coursecode;
    }

}
