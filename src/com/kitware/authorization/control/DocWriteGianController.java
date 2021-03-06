package com.kitware.authorization.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kitware.A.control.Controller;
import com.kitware.authorization.service.DocManipulService;
import com.kitware.authorization.vo.DocDetailVO;
import com.kitware.authorization.vo.DocGiganVO;
import com.kitware.authorization.vo.DocVO;
import com.kitware.member.vo.Members;

public class DocWriteGianController implements Controller {
	DocManipulService service = DocManipulService.getInstance();

	public DocWriteGianController() {
	}

	public DocWriteGianController(DocManipulService service) {
		super();
		this.service = service;
	}

	public DocManipulService getService() {
		return service;
	}

	public void setService(DocManipulService service) {
		this.service = service;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<DocDetailVO> list = new ArrayList<>();

		HttpSession session = request.getSession();
		Members member = (Members) session.getAttribute("loginInfo");
		String kind = request.getParameter("kind");
		String doc_num = request.getParameter("doc_num").trim();
		String emp_num = member.getEmp_num();
		int doc_kind = Integer.parseInt(request.getParameter("kind"));
		
		System.out.println(kind);
		String date = request.getParameter("date").trim();
		String dept = request.getParameter("dept").trim();
		String title = request.getParameter("title").trim();
		String content = request.getParameter("content");
		String g1_grade = request.getParameter("g1_grade").trim();
		String g1 = request.getParameter("g1").trim();
		String g2_grade = request.getParameter("g2_grade").trim();
		String g2 = request.getParameter("g2").trim();
		String g3_grade = request.getParameter("g3_grade").trim();
		String g3 = request.getParameter("g3");
		String replace = request.getParameter("replace");
		String start_date = request.getParameter("start_date");
		String end_date = request.getParameter("end_date");
		DocGiganVO doc_gigan = new DocGiganVO();
		doc_gigan.setDoc_num(doc_num);
		doc_gigan.setStart_date("");
		doc_gigan.setEnd_date("");
		if (start_date != null) {
			System.out.println(start_date);
			doc_gigan.setStart_date(start_date);
			doc_gigan.setEnd_date(end_date);

		}

		try {
			if (g1_grade.length() > 0) {
				DocDetailVO docdetail = new DocDetailVO();
				docdetail.setDoc_num(doc_num);
				docdetail.setConf_num(service.getEmpNum(g1, g1_grade));
				list.add(docdetail);
				if (g2_grade.length() > 0) {
					docdetail = new DocDetailVO();
					docdetail.setDoc_num(doc_num);
					docdetail.setConf_num(service.getEmpNum(g2, g2_grade));
					list.add(docdetail);
					if (g3_grade.length() > 0) {
						docdetail = new DocDetailVO();
						docdetail.setDoc_num(doc_num);
						docdetail.setConf_num(service.getEmpNum(g3, g3_grade));
						list.add(docdetail);
					}
				}
			}

			DocVO giandoc = new DocVO();
			giandoc.setDoc_num(doc_num);
			giandoc.setEmp_num(emp_num);
			giandoc.setDoc_kind(doc_kind);
			giandoc.setStart_date(date);
			giandoc.setRcv_dept(dept);
			giandoc.setDoc_title(title);
			giandoc.setDoc_content(content);
			giandoc.setRefer(replace);
			giandoc.setDoc_detail(list);
			giandoc.setDoc_gigan(doc_gigan);

			service.insertgian(giandoc);
			request.setAttribute("result", 1);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("result", -1);
		}

		String forwardURL = "/docresult.jsp";
		return forwardURL;
	}

}