/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LanguageDao;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Mwiza
 */
@FacesValidator("phoneValidator")
public class PhoneValidator implements Validator{
     @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
       /// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    String model = (String) value;

		if (model.length() <= 13 ) {
			FacesMessage msg = new FacesMessage("telepfone yawe ntiyuzuye","Uzuzamo byibura imibare 10");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);

			throw new ValidatorException(msg);
		}

    }
  
}
