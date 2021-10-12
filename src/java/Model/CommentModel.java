package Model;

import LanguageDao.AuthenticationService;
import LanguageDao.CommentDao;
import LanguageDao.UserDao;
import domain.Comment;
import domain.User;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class CommentModel {

    private Comment comment = new Comment();

    private User user = new User();

    private CommentDao service = new CommentDao();
    private List<User> listOfUsers = new UserDao().findAllUser();
    private List<Comment> listOfComment = service.findAll(Comment.class);

    private UserDao userService;

    

    public void initialization() {
        comment = new Comment();
        listOfComment = service.findAll(Comment.class);
        listOfUsers = userService.findAll(User.class);
        user = new User();

    }

//@Command("save")
//@NotifyChange("listOfComment")
    public void createComment() {
        try {
            System.out.println("Here");
           
            User loggedUser = (User) AuthenticationService.getSession().getAttribute("s_user");
            comment.setUser(loggedUser);
            service.create(comment);
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Ibitekerezo byoherejwe neza ","Ibitekerezo byoherejwe neza neza!!"));

            System.out.println(System.getenv("DB_PASSWORD"));

        } catch (Exception e) {
            // TODO: handle exception
           FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Ibitekerezo byoherejwe neza neza","Ibitekerezo byoherejwe neza neza!!"));

            e.printStackTrace();
        }
    }

    public void deleteCommen(Comment comment) {
        try {
            this.service.delete(comment);

        } catch (Exception e) {
            // TODO: handle exception
         FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Ntibyakunze"," mwongere mugerageze" + e.getMessage()));
            e.printStackTrace();
        }
    }

    public String updateCommen(Comment comment) {
        this.comment = comment;
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"kuvugurura Byagenze neza",""));

        return "UpdateComment";
    }

    public String updateCommen() {
        this.service.update(this.comment);
        
        return "DisplayComment";
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CommentDao getService() {
        return service;
    }

    public void setService(CommentDao service) {
        this.service = service;
    }

    public List<User> getListOfUsers() {
        return listOfUsers;
    }

    public void setListOfUsers(List<User> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }

    public List<Comment> getListOfComment() {
        return listOfComment;
    }

    public void setListOfComment(List<Comment> listOfComment) {
        this.listOfComment = listOfComment;
    }

    public UserDao getUserService() {
        return userService;
    }

    public void setUserService(UserDao userService) {
        this.userService = userService;
    }

    
}
