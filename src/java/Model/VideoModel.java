/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import LanguageDao.VideoDao;
import domain.Video;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Mwiza
 */@ManagedBean(name="video")
 @SessionScoped
public class VideoModel {
   private Video video=new Video();
    private VideoDao vdao=new VideoDao();
    private List<Video>listOfVideo=vdao.findAll(Video.class);
    
    public void createLink(){
        try {
            if(vdao.getByUrl(video.getUrl())==null){
                vdao.create(video);
        FacesContext fc=FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"umuyoboro wabitswe neza","umuyoboro wabitswe neza"));
            } else{
               FacesContext fc=FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"umuyoboro Usanzwe ubamo shyiramo undi","umuyoboro wabitswe neza"));
             
            }
            
        } catch (Exception e) {
          
        FacesContext fc=FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Ntibyakunze mwongere ","umuyoboro wabitswe neza"));
       e.printStackTrace();
       e.getMessage();
        }
       
    }
    public void deleteLink(Video video){
        this.vdao.delete(video);
        FacesContext fc=FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage("umuyoboro wasibitse neza"));
    }
public String updateLink(Video video){
        this.video=video;
      return "UpdateLink";  
    }
public void updateLink(){
        this.vdao.update(video);
        FacesContext fc=FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage("umuyoboro wasibitse neza"));
    }
    
    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public VideoDao getVdao() {
        return vdao;
    }

    public void setVdao(VideoDao vdao) {
        this.vdao = vdao;
    }

    public List<Video> getListOfVideo() {
        return listOfVideo;
    }

    public void setListOfVideo(List<Video> listOfVideo) {
        this.listOfVideo = listOfVideo;
    }
    
}
