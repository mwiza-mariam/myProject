/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import LanguageDao.SubscriptionTypeDao;

import domain.SubscriptionType;
import java.util.List;
import javax.faces.application.FacesMessage;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Mwiza
 */

@ManagedBean(name="cat")
@SessionScoped
public class SubscriptionTypeModel {
    
    private SubscriptionType stype=new SubscriptionType();
    private SubscriptionTypeDao subDao=new SubscriptionTypeDao();
  
    private List<SubscriptionType>SubscriptionList=subDao.findAll(SubscriptionType.class);
    
    public void createSubscription(){
        try {
             subDao.create(stype);
             FacesContext fc=FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Ikiciro cy'isomo cyongewemo neza"," cyongewemo neza neza"));
        } catch (Exception e) {
            FacesContext fc=FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Mwihangane Mugerageze mukanya"," Ntibishoboye kuboneka"));
        }
       
      
    }
    
    public String updateSubscription( SubscriptionType stype){
        this.stype=stype;
       
        return "UpdateSubscription";
    }
public String updateSubscription( ){
    try {
         this.subDao.update(stype);
        FacesContext fc=FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Ikiciro cy'isomo cyavuguruwe"," Cyavuguruwe neza"));
    } catch (Exception e) {
      FacesContext fc=FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR," Mwongere mugerageze Ikiciro cy'isomo"," nticyavuguruwe "));
       e.getMessage();
        e.printStackTrace();
        
    }
     return "SubscriptionList";
    }
    
    public void deleteSubscription(SubscriptionType stype) { 
        
        this.subDao.delete(stype);
            }

    public SubscriptionType getStype() {
        return stype;
    }

    public void setStype(SubscriptionType stype) {
        this.stype = stype;
    }

    public SubscriptionTypeDao getSubDao() {
        return subDao;
    }

    public void setSubDao(SubscriptionTypeDao subDao) {
        this.subDao = subDao;
    }

    public List<SubscriptionType> getSubscriptionList() {
        return SubscriptionList;
    }

    public void setSubscriptionList(List<SubscriptionType> SubscriptionList) {
        this.SubscriptionList = SubscriptionList;
    }

   

    

    
    
}
