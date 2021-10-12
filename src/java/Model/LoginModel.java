/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import LanguageDao.AuthenticationService;
import LanguageDao.NotificationDao;
import LanguageDao.PasswordProtection;
import LanguageDao.UserDao;
import cultureLearning.cultureLearning.exception.InvalidMailException;
import domain.Admin;
import domain.Student;
import domain.Teacher;
import domain.User;
import encryptionClass.Bcrypt;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Mwiza
 */
@ManagedBean(name = "login")
@SessionScoped
public class LoginModel {

    private User user = new User();
    private UserDao udao = new UserDao();
    private Bcrypt b = new Bcrypt();
    private String email;
    private Teacher teacher = new Teacher();
    private Student student = new Student();
    private PasswordProtection psw = new PasswordProtection();
    private boolean isUsernameValid;
    private boolean isPasswordValid;
    private boolean validationComplete = false;
    private String password;
    private NotificationDao notifier = new NotificationDao();
    private User sessionUser;
//    private List<User>listOffAllUser=udao.findAllUser();

    public String loginfunction() {
        try {
            //boolean valid=udao.userExist(email, password);
            if (user.getEmail().length() > 0 && user.getPassword().length() > 0) {

                sessionUser = udao.userExist(user.getEmail(), user.getPassword());
                System.out.println("Logged in user is:" + sessionUser);
                if (null != sessionUser) {
                    HttpSession session = AuthenticationService.getSession();
                    session.setAttribute("s_user", sessionUser);
                    if (sessionUser instanceof Admin) {
                        FacesContext fc = FacesContext.getCurrentInstance();
                        // fc.addMessage(null, new FacesMessage("Murakaza neza "+sessionUser.getLname()));
                        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Murakaza neza"  , "Murakaza neza"));
                        return "admin/AdminDashboard.xhtml" + "?faces-redirect=true";

                    } else if (sessionUser instanceof Student) {
                        FacesContext fc = FacesContext.getCurrentInstance();
                        fc.addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO,"Murakaza neza","Murakaza neza " ));
                        return "student/StudentDashboard.xhtml" + "?faces-redirect=true";
                    } else if (sessionUser instanceof Teacher) {
                        FacesContext fc = FacesContext.getCurrentInstance();
                        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Murakaza neza","Murakaza neza " ));
                        return "teacher/TeacherDashboard.xhtml" + "?faces-redirect=true"; 
                    } else {
                        FacesContext fc = FacesContext.getCurrentInstance();
                        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Mwihangane ntabwo tubashije kumenya uwo uriwe!","Mwihangane ntabwo tubashije kumenya uwo uriwe! "));
                        return "student/StudentDashboard.xhtml " + "?faces-redirect=true";

                    }

                } else {
                    FacesContext fc = FacesContext.getCurrentInstance();
                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"imeri cyangwa ijambobanga byanditse nabi!","imeri cyangwa ijambobanga byanditse nabi!"));

                }
            } else {
                FacesContext fc = FacesContext.getCurrentInstance();
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Andika email ni'ijambo banga ryawe!","Andika email ni'ijambo banga ryawe!"));

            }
        } catch (InvalidMailException e) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"email cyangwa ijambobangabyawe byanditse nabi","email cyangwa ijambobangabyawe byanditse nabi"));
            e.getMessage();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            FacesContext fc = FacesContext.getCurrentInstance();
             

            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Internal Error", "Internal Error"));

        }
        return "";
    }

    public String logout() {
        HttpSession session = AuthenticationService.getSession();
        session.invalidate();
        return "login";
    }

    public String checkValidity() {
        if (user.getEmail() == null || user.getEmail().equals("")) {
            isUsernameValid = false;
        } else {
            isUsernameValid = true;
        }
        if (user.getPassword() == null || user.getPassword().equals("")) {
            isPasswordValid = false;
        } else {
            isPasswordValid = true;
        }
        validationComplete = true;
        return "success";
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

    public Bcrypt getB() {
        return b;
    }

    public void setB(Bcrypt b) {
        this.b = b;
    }

    public PasswordProtection getPsw() {
        return psw;
    }

    public void setPsw(PasswordProtection psw) {
        this.psw = psw;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public NotificationDao getNotifier() {
        return notifier;
    }

    public void setNotifier(NotificationDao notifier) {
        this.notifier = notifier;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public boolean isIsUsernameValid() {
        return isUsernameValid;
    }

    public void setIsUsernameValid(boolean isUsernameValid) {
        this.isUsernameValid = isUsernameValid;
    }

    public boolean isIsPasswordValid() {
        return isPasswordValid;
    }

    public void setIsPasswordValid(boolean isPasswordValid) {
        this.isPasswordValid = isPasswordValid;
    }

    public boolean isValidationComplete() {
        return validationComplete;
    }

    public void setValidationComplete(boolean validationComplete) {
        this.validationComplete = validationComplete;
    }

    public User getSessionUser() {
        return sessionUser;
    }

}
