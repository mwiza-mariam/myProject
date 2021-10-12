package Model;



import LanguageDao.AddressDao;
import domain.Address;
import java.util.List;
import javax.faces.application.FacesMessage;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class AddressModel {
	private  Address add = new Address();
	private AddressDao adao=new AddressDao();
	
	private int id;
	private List<Address>listofCountries=adao.findAll(Address.class);
      
	
	public void addcountry() {
		try {
			
			adao.create(add);
                          FacesContext fc=FacesContext.getCurrentInstance();
			fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Igihugu cyabitswe neza","Igihugu cyabitswe neza!!"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
                          FacesContext fc=FacesContext.getCurrentInstance();
                        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Igihugu nticyabitswe, Igihugu gisanzwe kirimo!!","Igihugu gisanzwe kirimo!!" + e.getMessage()));
			
		}
		
	}

	public String Updatecountry(Address add) {
		try {
			this.add=add;
			  FacesContext fc=FacesContext.getCurrentInstance();
                        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Igihugu cyavuguruwe neza","Igihugu cyavuguruwe neza !!"));
		} catch (Exception e) {
			// TODO: handle exception
                      FacesContext fc=FacesContext.getCurrentInstance();
                    fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Ivugurura ntiryagenze neza","Ivugurura ntiryagenze neza!!" +e.getMessage()));
			
		}
		return "UpdateLocation";
	}
		public String updatecountry() {
		try {
			this.adao.update(add);
			FacesContext fc=FacesContext.getCurrentInstance();
                        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Igihugu cyavuguruwe neza","Igihugu cyavuguruwe neza !!"));
		} catch (Exception e) {
			// TODO: handle exception
                      FacesContext fc=FacesContext.getCurrentInstance();
                   fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Ivugurura ntiryagenze neza","Ivugurura ntiryagenze neza!!" +e.getMessage()));
		e.printStackTrace();
                e.getMessage();
		}
		return "DisplayLocation";
	}
	
	
//	@Command("delete")
//	@NotifyChange("listofCountries")
	public void deleteAddress(Address add) {
		try {
			this.adao.delete(add);
                          FacesContext fc=FacesContext.getCurrentInstance();
			fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Igihugu cyasibitse!!","Igihugu cyasibitse!!"));
		} catch (Exception e) {
			  FacesContext fc=FacesContext.getCurrentInstance();
			fc.addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Igihugu nticyasibitse ,mwongere mugerageze","Igihugu nticyasibitse ,mwongere mugerageze !"+e.getMessage()));
			e.printStackTrace();
		}
	}
	
	public Address getAdd() {
		return add;
	}
	public void setAdd(Address add) {
		this.add = add;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

    public AddressDao getAdao() {
        return adao;
    }

    public void setAdao(AddressDao adao) {
        this.adao = adao;
    }

    public List<Address> getListofCountries() {
        return listofCountries;
    }

    public void setListofCountries(List<Address> listofCountries) {
        this.listofCountries = listofCountries;
    }


    

   

    
	
}
