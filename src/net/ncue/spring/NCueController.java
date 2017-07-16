package net.ncue.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;

public class NCueController extends MultiActionController {
	protected final Log logger = LogFactory.getLog (getClass ());
	protected static final String DEFAULT_CURRENT_PAGE = "1";
	protected static final String DELIMETER = ",";
	private MessageSourceAccessor msAccessor;

	/**
	 * 
	 * @param msAccessor
	 */
	public void setMessageSourceAccessor (MessageSourceAccessor msAccessor) {
		this.msAccessor = msAccessor;
	}

	/**
	 * 
	 * @return
	 */
	protected final MessageSourceAccessor getMsAccessor () {
		return msAccessor;
	}

	/**
	 * 
	 */
	protected void bind (HttpServletRequest request, Object command) throws Exception {
		try {
			ServletRequestDataBinder binder = createBinder (request, command);
			binder.bind (request);
			binder.closeNoCatch ();
		} catch (Exception e) {
		}
	}

	/**
	 * 
	 */
	protected ServletRequestDataBinder createBinder (HttpServletRequest request, Object command) throws Exception {
		ServletRequestDataBinder binder = new ServletRequestDataBinder (command, getCommandName (command));
		initBinder (request, binder);
		return binder;
	}

	/**
	 * 
	 */
	protected String getCommandName (Object command) {
		return DEFAULT_COMMAND_NAME;
	}

	/**
	 * 
	 */
	protected void initBinder (HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder (request, binder);
		binder.registerCustomEditor (java.lang.String.class, new NCuePropertyEditor ());
	}

	/**
	 * 
	 */
	protected ModelAndView handleRequestInternal (HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.handleRequestInternal (request, response);
	}
	
	protected ModelAndView redirectMain (HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView (new RedirectView ("/index.map"));
	}
}
