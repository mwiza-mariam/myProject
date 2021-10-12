package domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "courses")
public class Course implements Serializable {

    @Id
    private String courseCode;
    private String Chapter;
    @OneToMany
    private List<Lesson> listOfLesson;

    private int visit;

    @OneToOne
    private SubscriptionType level;

    public String getChapter() {
        return Chapter;
    }

    public void setChapter(String chapter) {
        Chapter = chapter;
    }

    public int getVisit() {
        return visit;
    }

    public void setVisit(int visit) {
        this.visit = visit;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    @Override
    public String toString() {
        return getChapter();
    }

    public List<Lesson> getListOfLesson() {
        return listOfLesson;
    }

    public void setListOfLesson(List<Lesson> listOfLesson) {
        this.listOfLesson = listOfLesson;
    }

    public SubscriptionType getLevel() {
        return level;
    }

    public void setLevel(SubscriptionType level) {
        this.level = level;
    }

}
