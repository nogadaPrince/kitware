package com.kitware.authorization.control;

import java.io.IOException;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kitware.A.control.Controller;
import com.kitware.authorization.service.DocSelectService;
import com.kitware.authorization.vo.DocVO;
import com.kitware.board.vo.PageBean;
import com.kitware.member.vo.Members;

public class DocGJWaitController implements Controller {
	private DocSelectService service;
	
	public DocGJWaitController() {
		super();
	}
	
	public DocGJWaitController(DocSelectService service) {
		super();
		this.service = service;
	}

	public DocSelectService getService() {
		return service;
	}

	public void setService(DocSelectService service) {
		this.service = service;
	}
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Members loginInfo = (Members)session.getAttribute("loginInfo");	
		String emp_num = loginInfo.getEmp_num();
		List<DocVO> list= null;
		String forwardURL = null;
		String page = request.getParameter("page");
		System.out.println("로그인번호"+emp_num);
		String mode = request.getParameter("mode");
		System.out.println("mode"+mode);
		
		int intPage = 1;
		if(page != null) {
			intPage = Integer.parseInt(page);
		}try {
			
			/*if(chart.equals("ok")) {
				forwardURL = "/home/chartresult.jsp";
			}*/
			
			if(mode.equals("ing")) {
				list = service.findIng(emp_num);
			}else if(mode.equals("ok")) {
				list = service.selectOK(emp_num);
			}else if(mode.equals("cancel")) {
				list = service.selectCancle(emp_num);
			}else if(mode.equals("all")){
				list = service.selectmyAll(emp_num);
				System.out.println(list.size());
			}
			int totalCount = list.size();
			//총페이지수계산
			int cntPerPage = 5;// 1페이지별 5건씩 보여준다
			int endRow = (cntPerPage * intPage)-1;
			int startRow = (endRow+1) - cntPerPage;
			int totalPage = (int)Math.ceil((double)totalCount/ cntPerPage);
			//페이지그룹에서 쓰일 시작페이지값, 끝페이지값계산
			int cntPerPageGroup=5; //페이지그룹별 5페이지씩 보여준다
			int startPage = (int)Math.floor((double)(intPage)/(cntPerPageGroup+1))*cntPerPageGroup+1;
			int endPage = startPage+cntPerPageGroup-1;
			if(endPage > totalPage)
				endPage = totalPage;
			
			PageBean<DocVO> pb = new PageBean<>();
			pb.setCurrentPage(intPage);//현재페이지
			pb.setTotalPage(totalPage); //총페이지
			pb.setList(list); //목록
			pb.setStartPage(startPage); //시작페이지
			pb.setEndPage(endPage); //끝페이지
			
			request.setAttribute("pagebean", pb);
			request.setAttribute("totalCount", totalCount);
			request.setAttribute("mode", mode);
			request.setAttribute("startRow", startRow);
			request.setAttribute("endRow", endRow);
		
			
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("result", e.getMessage());
		}
		forwardURL = "/authorization/gj_wait_all.jsp";
		return forwardURL;
	}
}
