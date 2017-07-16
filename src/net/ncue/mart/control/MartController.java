package net.ncue.mart.control;

/**
 * @author: DosangYoon
 * @version: 0.1
 * @date    01/01/2017
 * @brief:
 */

import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.xmlrpc.XmlRpcServer;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import net.ncue.mart.dao.MartDao;
import net.ncue.mart.dao.OnLineMallDao;
import net.ncue.mart.dao.TodaySaleDao;
import net.ncue.mart.util.MartParameter;
import net.ncue.spring.NCueController;
import net.ncue.conf.Configure;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/** Simple command-line based search demo. */
public class MartController extends NCueController {
	MartDao martDao = new MartDao();
	TodaySaleDao todaySaleDao = new TodaySaleDao();
	OnLineMallDao onLineMallDao = new OnLineMallDao();
    XmlRpcServer xmlrpc = null;

	public ModelAndView info (HttpServletRequest request, HttpServletResponse response) throws Exception {
		Configure.setAttribute(request);
		MartParameter parameter = new MartParameter();
		parameter.parse(request);
		
		return new ModelAndView("mart/background");
	}
	
	public ModelAndView getMartsByRoundSearch (HttpServletRequest request, HttpServletResponse response) throws Exception {
		Configure.setAttribute(request);
		MartParameter parameter = new MartParameter();
		parameter.parse(request);
		
		JSONObject object = martDao.getMartsByRoundSearch(parameter);
		request.setAttribute("msg", object);
		
		return new ModelAndView("mart/msg");
	}
	public ModelAndView searchMartsByRoundSearch (HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);
		JSONObject object = martDao.getMartsByRoundSearch(parameter);
				
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + object.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	public ModelAndView getMartsByBBoxSearch (HttpServletRequest request, HttpServletResponse response) throws Exception {
		Configure.setAttribute(request);
		MartParameter parameter = new MartParameter();
		parameter.parse(request);
		
		JSONObject object = martDao.getMartsByBBoxSearch(parameter);
		request.setAttribute("msg", object);
		
		return new ModelAndView("home/msg");
	}	
	public ModelAndView searchMartsByBBoxSearch (HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONObject object = martDao.getMartsByBBoxSearch(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + object.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	public ModelAndView getGroupCountByRegion (HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONObject jsonObject = martDao.getGroupCountByRegion(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	public ModelAndView signup (HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONObject jsonObject = martDao.signup(request, parameter);
		if (jsonObject.get("msg").toString().equals("SUCCESS")) {
			HttpSession session = request.getSession();
			session.setAttribute("name", parameter.getName());
			session.setAttribute("email", parameter.getEmail());
		}
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		MartParameter parameter = new MartParameter();
		parameter.parse(request);
		JSONObject jsonObject = martDao.login(request, parameter);

		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		MartParameter parameter = new MartParameter();
		parameter.parse(request);
		JSONObject jsonObject = martDao.logout(request, parameter);

		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	public ModelAndView changePassword (HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONObject jsonObject = martDao.changePassword(request, parameter);
		if (jsonObject.get("msg").toString().equals("SUCCESS")) {
			HttpSession session = request.getSession();
			session.setAttribute("name", parameter.getName());
			session.setAttribute("email", parameter.getEmail());
			JSONObject tmp  = martDao.logout(request, parameter);
		} else if (jsonObject.get("msg").toString().equals("FAIL")) {
			JSONObject tmp  = martDao.logout(request, parameter);
		}
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	public ModelAndView checkStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		MartParameter parameter = new MartParameter();
		parameter.parse(request);
		JSONObject jsonObject = martDao.checkStatus(request, parameter);
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	
	
	
	
	
	public ModelAndView searchProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONObject jsonObject = martDao.searchProduct(parameter);
		jsonObject.put("code", "200");
		jsonObject.put("msg", "success");
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	public ModelAndView searchSaleProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONObject jsonObject = martDao.searchSaleProduct(parameter);
		jsonObject.put("code", "200");
		jsonObject.put("msg", "success");
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
		
	
	
	
	

	public ModelAndView getOnLineMall(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONObject jsonObject = onLineMallDao.getOnLineMall(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	
	public ModelAndView uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

    	MartParameter parameter = new MartParameter();
		parameter.parse(request);		
    	String mid = martDao.getMyMid(parameter);
    	final MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
    	Iterator<String> it =  multiRequest.getFileNames();

    	while (it.hasNext()) {
            String key = it.next();
            CommonsMultipartFile file = (CommonsMultipartFile) multiRequest.getFile(key);
            
            if (file != null) {
            	String dir = Configure.MART_SALE_FIlE_DIR+mid;
            	if (!(new File(dir).exists())) (new File(dir)).mkdir();
            	String fullFileName = dir+"/"+file.getOriginalFilename();
            	file.transferTo(new File(fullFileName));
            	martDao.insertFileInfo(mid, file.getOriginalFilename());
            	martDao.insertSalePrice(mid, fullFileName);
            } else {
            	request.setAttribute("msg", "WRONG FILE");
            	return new ModelAndView ("mart/msg");    	
            }
            break;
        }
    	request.setAttribute("msg", martDao.uploadFileInfo(mid));
    	return new ModelAndView ("mart/msg");
    }
	
	
	
	
	
	
	public ModelAndView setViewCount(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONObject jsonObject = martDao.setViewCount(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	public ModelAndView setPbClickCount(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONObject jsonObject = martDao.setPbClickCount(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}

	
	public ModelAndView getProductDesc(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONArray jsonArray = martDao.getProductDescription(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonArray.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	
	
	public ModelAndView getFileList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		MartParameter parameter = new MartParameter();
		parameter.parse(request);
		
       	JSONArray jSONArray = martDao.uploadFileInfo(parameter);
       	request.setAttribute("msg", jSONArray);

       	return new ModelAndView ("mart/msg");
    }
	
	public ModelAndView getMID(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONArray jsonArray = martDao.getMID(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonArray.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}

	public ModelAndView getSaleData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONArray jsonArray = martDao.getSaleData(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonArray.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}	
	
	
	public ModelAndView changeDisplayStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONArray jsonArray = martDao.changeDisplayStatus(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonArray.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	public ModelAndView registProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONArray jsonArray = martDao.registProduct(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonArray.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	public ModelAndView modifyItem(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONArray jsonArray = martDao.modifyItem(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonArray.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	public ModelAndView getPosition(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONObject jSONObject = martDao.getPosition(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jSONObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	
	
	///////// cart (장바구니) 에 대한 처리 ///////////
	public ModelAndView getCartedCount (HttpServletRequest request, HttpServletResponse response) throws Exception {	
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONObject jSONObject = martDao.getCartedCount(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jSONObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	public ModelAndView insertCart (HttpServletRequest request, HttpServletResponse response) throws Exception {	
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONObject jSONObject = martDao.insertCart(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jSONObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	public ModelAndView selectCart (HttpServletRequest request, HttpServletResponse response) throws Exception {	
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONObject jSONObject = martDao.selectCart(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jSONObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	public ModelAndView removeCart (HttpServletRequest request, HttpServletResponse response) throws Exception {	
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONObject jSONObject = martDao.removeCart(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jSONObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	public ModelAndView updateSelectCount (HttpServletRequest request, HttpServletResponse response) throws Exception {	
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONObject jSONObject = martDao.updateSelectCount(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jSONObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	public ModelAndView orderCart (HttpServletRequest request, HttpServletResponse response) throws Exception {	
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONObject jSONObject = martDao.orderCart(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jSONObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	public ModelAndView cancelCart (HttpServletRequest request, HttpServletResponse response) throws Exception {	
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONObject jSONObject = martDao.cancelCart(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jSONObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	public ModelAndView getConfirmCart (HttpServletRequest request, HttpServletResponse response) throws Exception {	
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONObject jSONObject = martDao.getConfirmCart(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jSONObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	public ModelAndView setInitInfo (HttpServletRequest request, HttpServletResponse response) throws Exception {	
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);		
		JSONObject jSONObject = martDao.setInitInfo(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jSONObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	
	// mid와 pid를 이용해서 상품 정보를 가져온다.
	public ModelAndView getProductInfo (HttpServletRequest request, HttpServletResponse response) throws Exception {	
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);
		JSONArray jsonArray = martDao.getProductInfo(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonArray.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	// 상품을 수정한다.
	public ModelAndView modifyProduct (HttpServletRequest request, HttpServletResponse response) throws Exception {	
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);
		JSONObject jsonObject = martDao.modifyProduct(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	
	// 상품을 수정한다.
	public ModelAndView checkGCM (HttpServletRequest request, HttpServletResponse response) throws Exception {	
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);
		JSONObject jsonObject = martDao.checkGCM(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}

	// 오늘의 세일 상품을 등록한다.
	public ModelAndView registerTodaySale (HttpServletRequest request, HttpServletResponse response) throws Exception {	
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);
		JSONObject jsonObject = martDao.registerTodaySale(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	
	
	// push 메시지를 받아서 db에 등록하고 사용자에게 날린다. 
	public ModelAndView pushMsg (HttpServletRequest request, HttpServletResponse response) throws Exception {	
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);
		JSONObject jsonObject = todaySaleDao.push(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
	
	// 주어진 날짜에 해당하는 push 메시지를 가져옴
	public ModelAndView getTodaySale (HttpServletRequest request, HttpServletResponse response) throws Exception {	
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String callBack = request.getParameter("callback");
		
		MartParameter parameter = new MartParameter();
		parameter.parse(request);
		JSONObject jsonObject = todaySaleDao.getTodaySale(parameter);
		
		PrintWriter out = response.getWriter();
		out.write(callBack + "(" + jsonObject.toString() + ")");
		 
		out.flush();
		out.close();
		
		return null;
	}
}
